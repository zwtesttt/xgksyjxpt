package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.Base64Converter;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")//给该controller类所有接口加上/student的url前缀
public class StudentController {
    @Autowired
    private StudentService studentService;



    @Autowired
    private FastdfsUtil fastdfsUtil;

    /**
     * 访问学生首页
     */
    @GetMapping("/toIndex")
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
     * 前端要先对密码使用base64加密算法加密
     */
    @PostMapping("/updatePass")
    public Object updatePass(String sid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (sid!=null){
            //核对学生旧密码
            //密码解密
            String oldpasswd=Base64Converter.decode(oldPass);
            String newpasswd=Base64Converter.decode(newPass);
            //查询旧密码
           String pass= studentService.selectStuPass(sid);
           if(oldpasswd.equals(pass)){
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
               re.setMessage("密码不匹配");
           }
        }
        return re;
    }

    /**
     * 修改学生头像
     */
    @PostMapping("/updateStuHead")
    public Object updateStuHead(MultipartFile file, String sid) {
        ReturnObject re =new ReturnObject();
        try {
            if (file.isEmpty()==false){
                String fileType=file.getContentType();
                if (fileType.equals("image/png")||fileType.equals("image/jpg")||fileType.equals("image/jpeg")){
                    //删除原来头像
                    String fileurl=studentService.selectStuHeadUrl(sid);
                    fastdfsUtil.deleteFile(fileurl);
//                上传新头像
                    String url=fastdfsUtil.uploadFile(file);
                    if (url!=null){
//                    上传成功则更新数据库信息
                        StudentHead studentHead=new StudentHead();
                        studentHead.setHead_link(url);
                        studentHead.setStu_id(sid);
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
                        studentHead.setStu_id(sid);
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

}
