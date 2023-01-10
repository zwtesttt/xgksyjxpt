package com.xgksyjxpt.xgksyjxpt.course.serivce.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;

import java.util.List;

public interface StudentService {
    Student selectStudent(String id);
    Student selectNotDelStudent(String id);
    List<Student> queryStudents(Integer pageNum,Integer pageSize);

    int updateStudent(Student student);

    int insertStudent(Student[] students);

    int insertStudentOne(Student student);

    int updateStuPass(String sid,String passwd);

    String selectStuPass(String sid);

    int deleteStudents(String[] stuIds);

    int deleteStuCourse(String[] stuIds);

    int deleteStuTest(String[] stuIds);
    int deleteStuTestByTestId(String testId);
    int deleteStuTestByCid(String cid);

    int deleteStuHead(String[] stuIds);

    String selectStuHeadUrl(String sid);

    int updateStuHead(StudentHead studentHead);

    int uploadStuHead(StudentHead studentHead);

    int deleteStuCourseByCid(String cid);

    int queryStuCourseByCid(String cid);

    List<String> selectStudentClassName();
    List<String> selectStudentIdByClassName(String[] classNames);
    int insertStudentCourseByCourseId(String[] stuIds,String courseId);
}

