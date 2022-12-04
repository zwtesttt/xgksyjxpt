package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Student;
import com.xgksyjxpt.xgksyjxpt.course.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 批量添加学生
     * @param students
     * @return
     */
    @Override
    @Transactional//开启事务
    public int insertStudent(Student[] students) {
        return studentMapper.insertStudent(students);
    }

    /**
     * 添加学生
     * @param student
     * @return
     */
    @Override
    public int insertStudentOne(Student student) {
        return studentMapper.insertStudentOne(student);
    }

    /**
     * 修改学生密码
     * @param sid
     * @param passwd
     * @return
     */
    @Override
    public int updateStuPass(String sid, String passwd) {
        return studentMapper.updateStuPass(sid,passwd);
    }

    /**
     * 查询学生密码
     * @param sid
     * @return
     */
    @Override
    public String selectStuPass(String sid) {
        return studentMapper.selectStuPass(sid);
    }
}
