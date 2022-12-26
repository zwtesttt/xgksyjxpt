package com.xgksyjxpt.xgksyjxpt.course.serivce.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentCourseMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentHeadMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentCourseMapper studentCourseMapper;



    @Autowired
    private StudentHeadMapper studentHeadMapper;

    @Autowired
    private StudentTestMapper studentTestMapper;

    @Autowired
    private ContainerService containerService;


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
    public List<Student> queryStudents(Integer pageNum,Integer pageSize) {
        return studentMapper.queryAllStudent(pageNum,pageSize);
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

    /**
     * 批量删除学生
     * @param stuIds
     * @return
     */
    @Override
    @Transactional//开启事务
    public int deleteStudents(String[] stuIds) {
        //删除学生容器记录
        containerService.deleteStuContainer(stuIds);
        //删除学生实验记录
        studentTestMapper.deleteStuTest(stuIds);
        //删除学生头像记录
        studentHeadMapper.deleteStuHead(stuIds);
        //删除学生选课记录
        studentCourseMapper.deleteStuCourse(stuIds);
        //删除学生表记录
        return studentMapper.deleteStudents(stuIds);
    }

    /**
     * 批量删除学生学生选课记录
     * @param stuIds
     * @return
     */
    @Override
    public int deleteStuCourse(String[] stuIds) {
        return studentCourseMapper.deleteStuCourse(stuIds);
    }
    /**
     * 批量删除学生实验记录
     */

    @Override
    public int deleteStuTest(String[] stuIds) {
        return studentTestMapper.deleteStuTest(stuIds);
    }

    /**
     * 根据实验id删除学生实验记录
     * @param testId
     * @return
     */
    @Override
    public int deleteStuTestByTestId(String testId) {
        return studentTestMapper.deleteStuTestByTestId(testId);
    }

    /**
     * 根据课程号删除学生实验记录
     * @param cid
     * @return
     */
    @Override
    public int deleteStuTestByCid(String cid) {
        return studentTestMapper.deleteStuTestByCid(cid);
    }

    /**
     * 批量删除学生头像
     * @param stuIds
     * @return
     */
    @Override
    public int deleteStuHead(String[] stuIds) {
        return studentHeadMapper.deleteStuHead(stuIds);
    }

    /**
     * 上传头像
     * @param studentHead
     * @return
     */
    @Override
    public int uploadStuHead(StudentHead studentHead) {
        return studentHeadMapper.uploadStuHead(studentHead);
    }

    /**
     * 根据课程号删除学生选课记录
     * @param cid
     * @return
     */
    @Override
    public int deleteStuCourseByCid(String cid) {
        return studentCourseMapper.deleteStuCourseByCid(cid);
    }

    /**
     * 根据课程号查询学生选课人数
     * @param cid
     * @return
     */
    @Override
    public int queryStuCourseByCid(String cid) {
        return studentCourseMapper.queryStuCourseCourseByCid(cid);
    }

    /**
     * 查询学生头像链接
     * @param sid
     * @return
     */
    @Override
    public String selectStuHeadUrl(String sid) {
        return studentHeadMapper.selectStuHeadUrl(sid);
    }

    /**
     * 修改学生头像
     * @param studentHead
     * @return
     */
    @Override
    public int updateStuHead(StudentHead studentHead) {
        return studentHeadMapper.updateStuHead(studentHead);
    }


}
