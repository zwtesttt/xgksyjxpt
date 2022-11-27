package com.xgksyjxpt.xgksyjxpt.login.serivce;

import com.xgksyjxpt.xgksyjxpt.login.domain.Student;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface StudentService {
    Student selectStudent(String id);
    List<Student> queryStudents();
}
