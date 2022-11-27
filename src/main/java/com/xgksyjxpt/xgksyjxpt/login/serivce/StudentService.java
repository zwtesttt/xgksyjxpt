package com.xgksyjxpt.xgksyjxpt.login.serivce;

import com.xgksyjxpt.xgksyjxpt.login.domain.Student;
import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Student selectStudent(String id);
    List<Student> queryStudents();

    int updateStudent(Student student);
}
