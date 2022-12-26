package com.xgksyjxpt.xgksyjxpt.course.serivce.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;

import java.util.List;

public interface TeacherService {
    Teacher selectTeacher(String id);

    int addTeachers(Teacher[] teachers);

    int updateTeacher(Teacher teacher);

    int updateTeaPasswd(String tid,String passwd);
    String selectTeaPaawd(String tid);

    List<Teacher> queryAllTeacher(Integer start_flg,Integer pageflg);

    int uploadTeaHead(TeacherHead teacherHead);
    int deleteTeaHeadByTid(String tid);
    int deleteTeachers(String[] tids);
    String selectTeaHeadUrl(String tid);
}
