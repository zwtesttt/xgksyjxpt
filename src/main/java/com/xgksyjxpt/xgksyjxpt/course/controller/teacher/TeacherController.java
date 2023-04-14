package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;


import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师业务")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private FastdfsUtil fastdfsUtil;


    /**
     * 访问教师首页
     * @return
     */
    @GetMapping("/toIndex")
    @ApiOperation(value = "访问教师首页",hidden = true)
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }


    /**
     * 查询老师个人信息
     */
    @GetMapping("/getTeacherInfo")
    @ApiOperation("查询老师个人信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="tid",value="老师id",dataType="string",required = true)
    public Object getAdminInfo(String tid){
        ReturnObject re=new ReturnObject();
        if (tid!=null){
            //验证老师id
            Teacher teacher=teacherService.selectNotDelTeacher(tid);
            if (teacher!=null){
                //封装
                Map<String,Object> remap=new HashMap<>();
                remap.put("tid",teacher.getTid());
                //名字
                remap.put("name",teacher.getName());
                //年龄
                remap.put("age",teacher.getAge());
                //性别
                remap.put("sex",teacher.getSex());
                //头像url
                remap.put("headUrl",teacherService.selectTeaHeadUrl(tid).substring(7));
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

    /**
     * 更新老师信息
     */
    @PostMapping("/updateTeacher")
    @ApiOperation("更新老师信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="passwd",value="密码(不用填)",dataType="string",required = false)
    public Object updateStudent(Teacher teacher){
        ReturnObject re=new ReturnObject();
        try {
            int stu= teacherService.updateTeacher(teacher);
            if (stu!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("修改成功");
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 修改老师密码
     */
    @PostMapping("/updatePass")
    @ApiOperation("修改老师密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="oldPass",value="旧密码",dataType="string",required = true),
            @ApiImplicitParam(name="tid",value="老师id",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updatePass(String tid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (tid!=null){
            //核对学生旧密码
            //新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
            //查询旧密码
            String pass= teacherService.selectTeaPaawd(tid);
            //核对密码
            if(passwordEncoder.matches(oldPass,pass)){
                try {
                    //开始修改密码
                    int stu=teacherService.updateTeaPasswd(tid,newpasswd);
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
     * 修改老师头像
     */
    @PostMapping("/updateStuHead")
    @ApiOperation("修改老师头像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="头像文件流",dataType="multipartFile",required = true),
            @ApiImplicitParam(name="tid",value="老师id",dataType="string",required = true)
    })
    public Object updateStuHead(MultipartFile file, String tid) {
        ReturnObject re =new ReturnObject();
        try {
            if (file.isEmpty()==false){
                String fileType=file.getContentType();
                if (fileType.equals("image/png")||fileType.equals("image/jpg")||fileType.equals("image/jpeg")){
                    //删除原来头像
                    String fileurl= teacherService.selectTeaHeadUrl(tid);
                    //排除默认头像
                    if(!HeadUrl.DEFAULT_STU_HEAD.equals(fileurl)){
                        fastdfsUtil.deleteFile(fileurl);
                    }
//                上传新头像
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
//                    上传成功则更新数据库信息
                        TeacherHead teacherHead=new TeacherHead();
                        teacherHead.setHead_link(url);
                        teacherHead.setTid(tid);
                        int stu=teacherService.updateTeacherHead(teacherHead);
                        if (stu!=0){
                            re.setData(url.substring(7));
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
}
