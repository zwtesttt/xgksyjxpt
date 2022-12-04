package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.domain.Student;
import com.xgksyjxpt.xgksyjxpt.course.serivce.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")//给该controller类所有接口加上/student的url前缀
public class StudentController {
    @Autowired
    private StudentService studentService;


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
     * 查询所有学生
     * @return
     */
    @GetMapping("/getStudents")
    public List<Map<String,String>> getStudents(){
        List<Map<String,String>> list=new ArrayList();
        //获取所有学生列表
        List<Student> ss=studentService.queryStudents();
        for (Student s:ss) {
            //封装学生信息
            Map<String,String> map=new HashMap<>();
            map.put("stu_id",s.getStu_id());
            map.put("name",s.getName());
            map.put("sex",s.getSex());
            map.put("age",s.getAge()+"");
            list.add(map);
        }
        return list;
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
     */
    @PostMapping("/updatePass")
    public Object updatePass(String sid,String oldPass,String newPass){
        ReturnObject re=new ReturnObject();
        if (sid!=null){
            //核对学生旧密码
           String pass= studentService.selectStuPass(sid);
           if(oldPass.equals(pass)){
               try {
                   //开始修改密码
                   int stu=studentService.updateStuPass(sid,newPass);
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

}
