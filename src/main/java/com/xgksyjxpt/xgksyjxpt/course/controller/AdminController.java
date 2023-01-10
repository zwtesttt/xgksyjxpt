package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.util.DockerUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员通用接口")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 访问管理员首页
     */
    @GetMapping("/toIndex")
    @ApiOperation(value = "跳转管理员首页",hidden = true)
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }
    /**
     * 查询宿主机当前所有镜像名
     */
    @GetMapping("/queryImages")
    @ApiOperation("查询宿主机当前所有镜像名")
    public List<String> queryImages(){
        List<String> list=new ArrayList<>();
        //实例化dockerclient对象
        DockerClient dockerClient=DockerUtil.queryDockerClient(DockerConfig.DOCKER_API_URL);
//        查询所有
        List<Image> ll=DockerUtil.imageList(dockerClient);
        //遍历镜像
        for (Image im:ll) {
            //跳过none的镜像,使用split切割字符串
            if(!im.getRepoTags()[0].split(":")[0].equals("<none>")){
                list.add(im.getRepoTags()[0].split(":")[0]);
            }
        }
        return list;
    }
    /**
     * 添加管理员
     */
    @PostMapping("/addAdmin")
    @ApiOperation("添加管理员")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addAdmin(Admin admin){
        ReturnObject re=new ReturnObject();
        try{
            if (admin!=null){
                //在所有账号内查询是否存在账号
                if(adminService.selectAdmin(admin.getRid())!=null){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("管理员id已存在");
                }else{
                    //不存在则开始添加管理员
                    //密码加密
                    String enpass=passwordEncoder.encode(admin.getPasswd());
                    admin.setPasswd(enpass);
                    int stu = adminService.insertAdmin(admin);
                    if (stu!=0){
                        //添加记录成功则响应成功
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 更新管理员信息
     */
    @PostMapping("updateAdmin")
    @ApiOperation("修改管理员信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="passwd",value="密码(不用填)",dataType="string",required = false)
    public Object updateAdmin(Admin admin){
        ReturnObject re=new ReturnObject();
        try{
            if (admin!=null){
//                修改信息
                int stu = adminService.updateAdmin(admin);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("修改成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 修改管理员头像
     */
    @PostMapping("/updateAdminHead")
    @ApiOperation("修改管理员头像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="头像文件流",dataType="multipartFile",required = true),
            @ApiImplicitParam(name="rid",value="管理员id",dataType="string",required = true)
    })
    public Object updateStuHead(MultipartFile file, String rid) {
        ReturnObject re =new ReturnObject();
        try {
            if (file.isEmpty()==false){
                String fileType=file.getContentType();
                if (fileType.equals("image/png")||fileType.equals("image/jpg")||fileType.equals("image/jpeg")){
                    //查询原来头像url
                    String fileurl= adminService.selectAdminHeadUrl(rid);
                    //如果用的是默认头像就不删除文件系统中的文件
                    if (!HeadUrl.DEFAULT_ADM_HEAD.equals(fileurl)){
                        //不是默认头像就删除
                        fastdfsUtil.deleteFile(fileurl);
                    }
//                上传新头像
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
//                    上传成功则更新数据库信息
                        AdminHead adminHead=new AdminHead();
                        adminHead.setHead_link(url);
                        adminHead.setRid(rid);
                        int stu=adminService.updateAdminHead(adminHead);
                        if (stu!=0){
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                            re.setMessage("修改头像成功");
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("修改头像失败");
                        }

                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改头像失败");
                    }
                }else {
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("头像格式错误");
                }


            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改头像失败");
        }
        return re;
    }
    /**
     * 修改管理员密码
     */
    @PostMapping("/updateAdmPass")
    @ApiOperation("修改管理员密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="rid",value="管理员id",dataType="string",required = true),
            @ApiImplicitParam(name="oldPass",value="旧密码",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updatePass(String rid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (rid!=null){
            //核对管理员旧密码
            //对新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
            //查询旧密码
            String pass= adminService.selectAdmin(rid).getPasswd();
            if(passwordEncoder.matches(oldPass,pass)){
                try {
                    //开始修改密码
                    int stu=adminService.updateAdminPass(rid,newpasswd);
//                   修改是否成功
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改失败");
                    }
                }catch (Exception e){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                    e.printStackTrace();
                }

            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("原密码不匹配");
            }
        }
        return re;
    }
    /**
     * 删除管理员
     */
    @DeleteMapping("/deleteAdmin")
    @ApiOperation("删除管理员")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object deleteAdmin(String rid){
        ReturnObject re=new ReturnObject();
        try {
            if (rid!=null){
                //校验管理员id
                if (adminService.selectNotDelAdmin(rid)==null){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("管理员不存在");
                }else{
//                    删除管理员
                    int stu=adminService.deleteAdmin(rid);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("管理员id不能为空");
            }
        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }

}
