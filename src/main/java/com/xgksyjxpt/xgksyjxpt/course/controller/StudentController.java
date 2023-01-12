package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/student")//给该controller类所有接口加上/student的url前缀
@Api(tags = "学生接口")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    /**
     * 访问学生首页
     */
    @GetMapping("/toIndex")
    @ApiOperation(value = "访问学生首页",hidden = true)
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }

    /**
     * 更新学生信息
     */
    @PostMapping("/updateStudent")
    @ApiOperation("更新学生信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="passwd",value="密码(不用填)",dataType="string",required = false)
    public Object updateStudent(Student student){
        ReturnObject re=new ReturnObject();
        try {
            int stu= studentService.updateStudent(student);
            if (stu!=0){
                re.setCode(1);
                re.setMessage("修改成功");
            }else{
                re.setCode(0);
                re.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(0);
            re.setMessage("修改失败");
        }
        return re;
    }

    /**
     * 修改学生密码
     */
    @PostMapping("/updateStuPass")
    @ApiOperation("修改学生密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true),
            @ApiImplicitParam(name="oldPass",value="旧密码",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updatePass(String sid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (sid!=null){
            //核对学生旧密码
            //对新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
            //查询旧密码
           String pass= studentService.selectStuPass(sid);
           if(passwordEncoder.matches(oldPass,pass)){
               try {
                   //开始修改密码
                   int stu=studentService.updateStuPass(sid,newpasswd);
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
     * 修改学生头像
     */
    @PostMapping("/updateStuHead")
    @ApiOperation("修改学生头像")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="头像文件流",dataType="multipartFile",required = true),
            @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true)
    })
    public Object updateStuHead(MultipartFile file, String sid) {
        ReturnObject re =new ReturnObject();
        try {
            if (file.isEmpty()==false){
                String fileType=file.getContentType();
                if (fileType.equals("image/png")||fileType.equals("image/jpg")||fileType.equals("image/jpeg")){
                    //删除原来头像
                    String fileurl=studentService.selectStuHeadUrl(sid);
                    //排除默认头像
                    if(!HeadUrl.DEFAULT_STU_HEAD.equals(fileurl)){
                        fastdfsUtil.deleteFile(fileurl);
                    }
//                上传新头像
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
//                    上传成功则更新数据库信息
                        StudentHead studentHead=new StudentHead();
                        studentHead.setHead_link(url);
                        studentHead.setSid(sid);
                        int stu=studentService.updateStuHead(studentHead);
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
     * 上传学生头像，最大支持5m
     * @param file
     * @param sid
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("上传学生头像(最大支持5m)")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="头像文件流",dataType="multipartFile",required = true),
            @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true)
    })
    public Object toDetail(MultipartFile file,String sid) {
//        System.out.println(myfile.getBytes());//返回该文件的byte数组
//        System.out.println(myfile.getName());//返回表单参数名
//        System.out.println(myfile.getContentType());//返回文件类型
//        System.out.println(myfile.getInputStream());//返回输入流
//        System.out.println(myfile.getOriginalFilename());//返回文件名
//        System.out.println(myfile.getSize());//返回文件大小
//        System.out.println(myfile.isEmpty());//当文件size不为0或者不为null时返回false
        ReturnObject re =new ReturnObject();
        try {
            if (file.isEmpty()==false){
//            获取文件的二进制数组
                byte[] bytes=file.getBytes();
////            获取完整文件名
//                String fileName=myfile.getOriginalFilename();
////            提取文件扩展名
//                String fileExName=fileName.substring(fileName.lastIndexOf(".")+1);
////            获取文件大小
//                long fileSize=myfile.getSize();
////            获取文件类型
                //判断文件类型
                String fileType=file.getContentType();
                if (fileType.equals("image/png")||fileType.equals("image/jpg")||fileType.equals("image/jpeg")){
                    //                上传文件返回url
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
//                    上传成功则录入数据库
                        StudentHead studentHead=new StudentHead();
                        studentHead.setHead_link(url);
                        studentHead.setSid(sid);
                        //上传新头像
                        studentService.uploadStuHead(studentHead);
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("上传头像成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("上传头像失败");
                    }

                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("头像格式错误");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("上传头像失败");
        }
        return re;
    }
    /**
     * 查询学生个人信息
     */
    @GetMapping("/getStudentInfo")
    @ApiOperation("查询学生个人信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true)
    public Object getAdminInfo(String sid){
        ReturnObject re=new ReturnObject();
        if (sid!=null){
            //验证学生id
            Student student=studentService.selectNotDelStudent(sid);
            if (student!=null){
                //封装
                Map<String,Object> remap=new HashMap<>();
                remap.put("sid",student.getSid());
                //名字
                remap.put("name",student.getName());
                //性别
                remap.put("sex",student.getSex());
                //年龄
                remap.put("age",student.getAge());
                //班级
                remap.put("className",student.getClass_name());
                //头像url
                remap.put("headUrl",studentService.selectStuHeadUrl(sid).substring(7));
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                re.setData(remap);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学生不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("学生id不能为空");
        }
        return re;
    }

}
