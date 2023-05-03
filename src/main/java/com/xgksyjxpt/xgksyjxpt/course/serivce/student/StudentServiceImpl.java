package com.xgksyjxpt.xgksyjxpt.course.serivce.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentCourse;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentCourseMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentHeadMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * 修改学生选课
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int updateStudentCourseByCid(String cid,String[]newClassArray) {
        int re = 0;
        //删除学生选课
        int s=deleteStuCourseByCid(cid);
        if (s != 0) {
            //给学生绑定选课
            //查询学号
            List<String> stuIds=studentMapper.selectStudentIdByClassName(newClassArray);
            String[] stulist=stuIds.toArray(new String[stuIds.size()]);
            //添加选课
            int stu=studentCourseMapper.insertStudentCourseByCid(stulist,cid);
            if (stu!=0){
                re=1;
            }else{
                re=0;
            }
        }
        return re;
    }

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
     * 只查询正常状态的学生
     * @param id
     * @return
     */
    @Override
    public Student selectNotDelStudent(String id) {
        return studentMapper.selectNotDelStudent(id);
    }

    /**
     * 查询所有学生
     * @return
     */
    @Override
    public List<Student> queryStudents(Student student,Integer pageNum,Integer pageSize) {
        return studentMapper.queryAllStudent(student,pageNum,pageSize);
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
        StudentHead head=null;
//        添加默认头像
        for (Student s:students
             ) {
            head=new StudentHead();
            head.setSid(s.getSid());
            head.setHead_link(HeadUrl.DEFAULT_STU_HEAD);
            studentHeadMapper.uploadStuHead(head);
        }

        return studentMapper.insertStudent(students);
    }

    /**
     * 添加学生
     * @param student
     * @return
     */
    @Override
    @Transactional
    public int insertStudentOne(Student student) {
//        学生设置默认头像
        StudentHead head=new StudentHead();
        head.setSid(student.getSid());
        head.setHead_link(HeadUrl.DEFAULT_STU_HEAD);
        studentHeadMapper.uploadStuHead(head);

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
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.in("sid",stuIds);
        return studentMapper.delete(wrapper);
//        return studentMapper.deleteStudents(stuIds);
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
    public int deleteStuTestByTestIds(String[] testId) {
        return studentTestMapper.deleteStuTestByTestIds(testId);
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
     * 查询所有学生班级名称
     * @return
     */
    @Override
    public List<String> selectStudentClassName() {
        return studentMapper.selectStudentClassName();
    }

    /**
     * 根据班级名查询学号
     * @param classNames
     * @return
     */
    @Override
    public List<String> selectStudentIdByClassName(String[] classNames) {
        return studentMapper.selectStudentIdByClassName(classNames);
    }

    /**
     * 添加学生选课记录
     * @param stuIds
     * @param courseId
     * @return
     */
    @Override
    public int insertStudentCourseByCourseId(String[] stuIds, String courseId) {
        return studentCourseMapper.insertStudentCourseByCid(stuIds,courseId);
    }

    /**
     * 批量添加学生实验
     * @param studentTests
     * @return
     */
    @Override
    public int insertStudentTest(StudentTest[] studentTests) {
        return studentTestMapper.insertStudentTest(studentTests);
    }

    /**
     * 查询选课的学号
     * @param cid
     * @return
     */
    @Override
    public List<String> selectStudentCourseSidByCid(String cid) {
        return studentCourseMapper.selectStudentCourseSidByCId(cid);
    }

    /**
     * 根据课程号查询选课班级名
     * @param cid
     * @return
     */
    @Override
    public List<String> selectStudentCourseClassNameByCid(String cid) {
        return studentCourseMapper.selectStudentCourseClassNameByCid(cid);
    }

    /**
     * 查询学生选课信息
     * @param sid
     * @return
     */
    @Override
    public List<Map<String, Object>> selectStudentCourseInfo(String sid,Integer pageNum,Integer pageSize) {
        return studentCourseMapper.selectStudentCourseInfoBySId(sid,pageNum,pageSize);
    }

    /**
     * 根据学号和课程号查询实验
     * @param sid
     * @param  courseTest
     * @return
     */
    @Override
    public List<Map<String, Object>> selectStudentTestInfo(String sid, CourseTest courseTest, Integer pageNum, Integer pageSize) {
        return studentTestMapper.selectStudentTestInfo(sid,courseTest,pageNum,pageSize);
    }

    /**
     * 根据学号和实验id查询实验
     * @param sid
     * @param testId
     * @return
     */
    @Override
    public StudentTest selectStudentTestBySidAndTestId(String sid, String testId) {
        return studentTestMapper.selectStudentTestBySidAndTestId(sid,testId);
    }

    /**
     * 查询学生总数
     * @return
     */
    @Override
    public int queryStudentCount(Student student) {
        return studentMapper.queryStudentCount(student);
    }

    /**
     * 查询指定身份id
     * @param identity
     * @return
     */
    @Override
    public List<String> selectIdentitySid(String identity) {
        return studentMapper.selectIdentitySid(identity);
    }

    /**
     * 查询指定学生总选课数
     * @param sid
     * @return
     */
    @Override
    public int queryStudentCourseCountBySid(String sid) {
        QueryWrapper<StudentCourse> wrapper=new QueryWrapper<>();
        wrapper.eq("sid",sid);
        return Math.toIntExact(studentCourseMapper.selectCount(wrapper));
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
