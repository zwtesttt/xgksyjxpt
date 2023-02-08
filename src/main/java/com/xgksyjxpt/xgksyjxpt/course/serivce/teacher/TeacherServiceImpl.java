package com.xgksyjxpt.xgksyjxpt.course.serivce.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;
import com.xgksyjxpt.xgksyjxpt.course.mapper.teacher.TeacherHeadMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.teacher.TeacherMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherHeadMapper teacherHeadMapper;

    @Autowired
    private CourseService courseService;

    /**
     * 根据老师id查询账号信息
     * @param id
     * @return
     */
    @Override
    public Teacher selectTeacher(String id) {
        return teacherMapper.selectTeacher(id);
    }

    /**
     * 查询正常状态老师
     * @param id
     * @return
     */
    @Override
    public Teacher selectNotDelTeacher(String id) {
        return teacherMapper.selectNotDelTeacher(id);
    }

    /**
     * 批量添加老师
     * @param teachers
     * @return
     */
    @Override
    @Transactional//开启事务
    public int addTeachers(Teacher[] teachers) {
        return teacherMapper.addTeachers(teachers);
    }

    /**
     * 修改老师信息
     * @param teacher
     * @return
     */
    @Override
    public int updateTeacher(Teacher teacher) {
        return teacherMapper.updateTeacher(teacher);
    }

    /**
     * 修改老师密码
     * @param tid
     * @param passwd
     * @return
     */
    @Override
    public int updateTeaPasswd(String tid, String passwd) {
        return teacherMapper.updateTeaPasswd(tid,passwd);
    }

    /**
     * 查询老师密码
     * @param tid
     * @return
     */
    @Override
    public String selectTeaPaawd(String tid) {
        return teacherMapper.selectTeaPasswd(tid);
    }

    //查询所有老师信息
    @Override
    public List<Teacher> queryAllTeacher(Teacher teacher,Integer pageNum,Integer pageSize) {
        return teacherMapper.queryAllTeacher(teacher,pageNum,pageSize);
    }

    /**
     * 上传教师头像
     * @param teacherHead
     * @return
     */
    @Override
    public int uploadTeaHead(TeacherHead teacherHead) {
        return teacherHeadMapper.uploadTeaHead(teacherHead);
    }

    /**
     * 根据教师号删除教师头像记录
     * @param tid
     * @return
     */
    @Override
    public int deleteTeaHeadByTid(String tid) {
        return teacherHeadMapper.deleteTeaHeadByTid(tid);
    }


    /**
     * 批量删除老师头像记录
     */
    @Override
    public int deleteTeaHeadByTids(String[] tids) {
        return teacherHeadMapper.deleteTeaHeadByTids(tids);
    }

    /**
     * 根据教师号批量删除删除老师
     * @param tids
     * @return
     */
    @Override
    @Transactional//开启事务
    public int deleteTeachers(String[] tids) {
        //删除该老师名下的课程
        for (String tid:tids
             ) {
            //查询老师名下所有课程号
            List<String> cids=courseService.selectCourseIdByTid(tid);
            if (cids.size()!=0){
                for (String cid:cids
                     ) {
                    //开始删除老师课程
                    courseService.deleteCourse(cid);
                }
            }
        }
        //删除数据库中老师头像记录
        deleteTeaHeadByTids(tids);
        return teacherMapper.deleteTeacherByTids(tids);
    }

    /**
     * 根据教师号查询头像url
     * @param tid
     * @return
     */
    @Override
    public String selectTeaHeadUrl(String tid) {
        return teacherHeadMapper.selectTeaHeadUrlByTid(tid);
    }

    /**
     * 添加老师
     * @param teacher
     * @return
     */
    @Override
    public int insertTeacher(Teacher teacher) {
        //添加默认头像
        TeacherHead head=new TeacherHead();
        head.setTid(teacher.getTid());
        head.setHead_link(HeadUrl.DEFAULT_TEA_HEAD);
        teacherHeadMapper.uploadTeaHead(head);

        return teacherMapper.insertTeacher(teacher);
    }

    /**
     * 批量添加老师
     * @param teachers
     * @return
     */
    @Override
    @Transactional
    public int insertTeachers(Teacher[] teachers) {
        TeacherHead head=null;

        for (Teacher t:teachers
             ) {
            //给老师添加默认头像
            head=new TeacherHead();
            head.setTid(t.getTid());
            head.setHead_link(HeadUrl.DEFAULT_TEA_HEAD);
            teacherHeadMapper.uploadTeaHead(head);
        }

        return teacherMapper.insertTeachers(teachers);
    }

    /**
     * 查询老师总数
     * @return
     */
    @Override
    public int queryTeacherCount(Teacher teacher) {
        return teacherMapper.queryTeacherCount(teacher);
    }


}
