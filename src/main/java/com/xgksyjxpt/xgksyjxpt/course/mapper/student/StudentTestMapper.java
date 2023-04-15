package com.xgksyjxpt.xgksyjxpt.course.mapper.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentTestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    int deleteByPrimaryKey(String test_id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    int insert(StudentTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    int insertSelective(StudentTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    StudentTest selectByPrimaryKey(String test_id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    int updateByPrimaryKeySelective(StudentTest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_test_t
     *
     * @mbggenerated Sat Dec 17 13:46:24 CST 2022
     */
    int updateByPrimaryKey(StudentTest record);

    /**
     * 根据学号删除学生实验记录
     */
    int deleteStuTest(String[] stuIds);

    /**
     * 根据实验id批量删除学生实验记录
     */
    int deleteStuTestByTestIds(String[] testId);
    /**
     * 根据课程号删除实验
     */
    int deleteStuTestByCid(String cid);
    /**
     * 批量添加学生实验
     */
    int insertStudentTest(StudentTest[] studentTests);
    /**
     * 根据学号和课程号查询实验信息
     */
    List<Map<String,Object>> selectStudentTestInfo(@Param("sid") String sid,@Param("courseTest") CourseTest courseTest,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
    /**
     * 根据学号和实验号查询实验记录
     */
    StudentTest selectStudentTestBySidAndTestId(@Param("sid")String sid,@Param("testId")String testId);


}