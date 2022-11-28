package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 根据老师id查询账号信息
     * @param id
     * @return
     */
    @Override
    public Teacher selectTeacher(String id) {
        return teacherMapper.selectTeacher(id);
    }
}
