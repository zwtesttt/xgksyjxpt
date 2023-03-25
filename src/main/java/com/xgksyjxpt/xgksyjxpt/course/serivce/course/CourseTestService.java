package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;

import java.util.List;

public interface CourseTestService {
    /**
     * 课程实验接口
     */
    int deleteCourseTestByCid(String cid);
    List<String> selectCourseTestIdByCid(String cid);
    List<CourseTest> queryAllCourseTest(Integer pageNum,Integer pageSize);
    List<CourseTest> queryCourseTestByCid(CourseTest courseTest,String cid,Integer pageNum,Integer pageSize);
    int insertCourseTest(CourseTest courseTest);

    int updateCourseTest(CourseTest courseTest);
    int deleteCourseTestByTestId(String testId);
    CourseTest selectCourseTestByTestId(String testId);
    int queryCourseTestCountByCid(CourseTest courseTest,String cid);
    int deleteCourseTestsByTestIds(String[] ids);
}
