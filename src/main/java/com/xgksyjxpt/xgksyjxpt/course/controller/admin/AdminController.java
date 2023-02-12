package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminIdentity;
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
     * 修改管理员信息
     */
    @PostMapping("updateAdmin")
    @ApiOperation("修改管理员信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))

    @ApiImplicitParams({
            @ApiImplicitParam(name="identity",value="身份(不用带)",dataType="string",required = false),
            @ApiImplicitParam(name="passwd",value="密码(不用带)",dataType="string",required = false)
    })
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
                            //截取url
                            re.setData(url.substring(7));
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
            String pass= adminService.selectNotDelAdmin(rid).getPasswd();
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
     * 查询管理员个人信息
     */
    @GetMapping("/getAdminInfo")
    @ApiOperation("查询管理员个人信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="rid",value="管理员id",dataType="string",required = true)
    public Object getAdminInfo(String rid){
        ReturnObject re=new ReturnObject();
        if (rid!=null){
            //验证管理员id
            Admin admin=adminService.selectNotDelAdmin(rid);
            if (admin!=null){
                //封装
                Map<String,Object> remap=new HashMap<>();
                remap.put("rid",admin.getRid());
                //名字
                remap.put("name",admin.getName());
                //账号身份
                remap.put("identity",admin.getIdentity());
                //头像url
                remap.put("headUrl",adminService.selectAdminHeadUrl(rid).substring(7));
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                re.setData(remap);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("管理员不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("管理员id不能为空");
        }
        return re;
    }

}
