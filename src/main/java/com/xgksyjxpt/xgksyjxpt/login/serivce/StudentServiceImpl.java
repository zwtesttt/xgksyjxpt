package com.xgksyjxpt.xgksyjxpt.login.serivce;

import com.xgksyjxpt.xgksyjxpt.login.domain.Student;
import com.xgksyjxpt.xgksyjxpt.login.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 根据学号查询学生信息
     * @param id
     * @return
     */
    @Override
    public Student selectStudent(String id) {
        return studentMapper.selectStudent(id);
    }

    /**
     * 查询所有学生
     * @return
     */
    @Override
    public List<Student> queryStudents() {
        return studentMapper.queryAllStudent();
    }

    /**
     * 修改学生信息
     * @param student
     * @return
     */
    @Override
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }
}
