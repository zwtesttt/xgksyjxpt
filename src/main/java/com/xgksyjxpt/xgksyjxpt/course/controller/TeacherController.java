package com.xgksyjxpt.xgksyjxpt.course.controller;

import com.xgksyjxpt.xgksyjxpt.domain.ResturnStuatus;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.course.serivce.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    /**
     * 访问教师首页
     * @return
     */
    @GetMapping("/toIndex")
    public Object toIndex(){
        ReturnObject re=new ReturnObject();
        re.setCode(ResturnStuatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("请求成功");
        return re;
    }
}
