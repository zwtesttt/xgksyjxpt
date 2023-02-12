package com.xgksyjxpt.xgksyjxpt.course.serivce.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Student selectStudent(String id);
    Student selectNotDelStudent(String id);
    List<Student> queryStudents(Student student,Integer pageNum,Integer pageSize);

    int updateStudent(Student student);

    int insertStudent(Student[] students);

    int insertStudentOne(Student student);

    int updateStuPass(String sid,String passwd);

    String selectStuPass(String sid);

    int deleteStudents(String[] stuIds);

    int deleteStuCourse(String[] stuIds);

    int deleteStuTest(String[] stuIds);
    int deleteStuTestByTestIds(String[] testId);
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

    int insertStudentTest(StudentTest[] studentTests);

    List<String> selectStudentCourseSidByCid(String cid);
    List<String> selectStudentCourseClassNameByCid(String cid);
    List<Map<String,Object>> selectStudentCourseInfo(String sid,Integer pageNum,Integer pageSize);
    List<Map<String,Object>> selectStudentTestInfo(String sid,String cid, Integer pageNum,Integer pageSize);
    StudentTest selectStudentTestBySidAndTestId(String sid,String testId);
    int queryStudentCount(Student student);

    List<String> selectIdentitySid(String identity);

}

