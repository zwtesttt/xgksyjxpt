package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Student;

import java.util.List;

public interface StudentService {
    Student selectStudent(String id);
    List<Student> queryStudents();

    int updateStudent(Student student);

    int insertStudent(Student[] students);

    int insertStudentOne(Student student);
}
