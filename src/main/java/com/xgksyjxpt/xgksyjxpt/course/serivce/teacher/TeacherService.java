package com.xgksyjxpt.xgksyjxpt.course.serivce.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;

import java.util.List;

public interface TeacherService {
    Teacher selectTeacher(String id);
    Teacher selectNotDelTeacher(String id);

    int addTeachers(Teacher[] teachers);

    int updateTeacher(Teacher teacher);

    int updateTeaPasswd(String tid,String passwd);
    String selectTeaPaawd(String tid);

    List<Teacher> queryAllTeacher(Teacher teacher,Integer start_flg,Integer pageflg);

    int uploadTeaHead(TeacherHead teacherHead);
    int deleteTeaHeadByTid(String tid);
    int deleteTeaHeadByTids(String[] tids);
    int deleteTeachers(String[] tids);
    String selectTeaHeadUrl(String tid);

    int insertTeacher(Teacher teacher);

    int insertTeachers(Teacher[] teachers);
    int queryTeacherCount(Teacher teacher);
    List<String> selectTeacherTid();
}
