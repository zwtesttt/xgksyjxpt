package com.xgksyjxpt.xgksyjxpt.course.mapper.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;

public interface StudentHeadMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    int insert(StudentHead record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    int insertSelective(StudentHead record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    StudentHead selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    int updateByPrimaryKeySelective(StudentHead record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_head_t
     *
     * @mbggenerated Tue Dec 13 21:48:52 CST 2022
     */
    int updateByPrimaryKey(StudentHead record);

    int uploadStuHead(StudentHead studentHead);

    /**
     * 查询学生头像链接
     * @param sid
     * @return
     */
    String selectStuHeadUrl(String sid);

    int updateStuHead(StudentHead studentHead);
    /**
     * 批量删除学生头像记录
     */
    int deleteStuHead(String[] stuIds);
}