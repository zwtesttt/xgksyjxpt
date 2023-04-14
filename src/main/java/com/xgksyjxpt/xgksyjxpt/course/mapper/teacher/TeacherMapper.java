package com.xgksyjxpt.xgksyjxpt.course.mapper.teacher;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper extends BaseMapper<Teacher> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    int deleteByPrimaryKey(String tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    int insert(Teacher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    int insertSelective(Teacher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    Teacher selectByPrimaryKey(String tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    int updateByPrimaryKeySelective(Teacher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher_t
     *
     * @mbggenerated Sun Nov 27 14:17:20 CST 2022
     */
    int updateByPrimaryKey(Teacher record);
    //查询老师
    Teacher selectTeacher(String id);

    //只查询正常状态老师
    Teacher selectNotDelTeacher(String id);
    //添加老师账号
    int addTeachers(Teacher[] teachers);
    //修改老师信息
    int updateTeacher(Teacher teacher);
    //修改老师密码
    int updateTeaPasswd(@Param("tid")String tid, @Param("passwd")String passwd);
    //查询老师密码
    String selectTeaPasswd(String tid);
    //查询所有老师信息
    List<Teacher> queryAllTeacher(@Param("teacher") Teacher teacher,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
    /**
     * 批量删除老师
     */
    int deleteTeacherByTids(String[] tids);
    /**
     * 添加单个老师
     */
    int insertTeacher(Teacher teacher);
    /**
     * 批量添加老师
     */
    int insertTeachers(Teacher[] teachers);
    /**
     * 查询老师总数
     */
    int queryTeacherCount(Teacher teacher);
    /**
     * 查询角色为老师的id
     */
    List<String> selectTeacherTid();
}