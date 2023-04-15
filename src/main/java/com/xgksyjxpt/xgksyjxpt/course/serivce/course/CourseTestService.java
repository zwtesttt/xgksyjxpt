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
    List<CourseTest> queryCourseTestByTidOrSid(String tid,String sid,CourseTest courseTest,Integer pageNum,Integer pageSize);
    int insertCourseTest(CourseTest courseTest);

    int updateCourseTest(CourseTest courseTest);
    int deleteCourseTestByTestId(String testId);
    CourseTest selectCourseTestByTestId(String testId);
    int queryCourseTestCountByTidOrSid(String tid,String sid,CourseTest courseTest);
    int deleteCourseTestsByTestIds(String[] ids);

    List<String> selectCourseTestClass(String testId);

    int updateTestClassByTestIdAndClassNames(String testId,String[] newclassNames);

    int updayeCourseTestDocByTestId(String testId,String doc);
}
