package com.xgksyjxpt.xgksyjxpt.course.serivce.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;
import com.xgksyjxpt.xgksyjxpt.course.mapper.teacher.TeacherHeadMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.teacher.TeacherMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
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
    public List<Teacher> queryAllTeacher(Integer start_flg,Integer pageflg) {
        return teacherMapper.queryAllTeacher(start_flg,pageflg);
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
            //删除数据库中老师头像记录
            deleteTeaHeadByTid(tid);
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
}
