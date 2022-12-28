package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    /**
     * 课程接口
     * @param course
     * @return
     */
    int insertCourse(Course course);
    int deleteCourse(String cid);
    Course selectCourseByCid(String cid);
    List<String> selectCourseIdByTid(String tid);
    int deleteCourses(String[] cids);
    List<Course> queryAllCourse(Integer pageNum,Integer pageSize);
    int createEvent(String date,String cid,String eventName,String courseStatus);


    /**
     * 课程章节接口
     */
    int deleteCourseChapterByCid(String cid);
    int deleteCourseChapterByCidAndChapterId(String cid,String chapterId);

    /**
     * 课程小节接口
     */
    int deleteCourseSectionByCidAndChapterId(String cid,String chapterId);
    int deleteCourseSectionByCid(String cid);
    int deleteCourseSectionByCidAndChapterIdAndSectionId(String cid,String chapterId,String sectionId);

    /**
     * 课程实验接口
     */
    int deleteCourseTestByCid(String cid);
    List<String> selectCourseTestIdByCid(String cid);
    List<CourseTest> queryAllCourseTest();
    List<CourseTest> queryCourseTestByCid(String cid,Integer pageNum,Integer pageSize);
    int insertCourseTest(CourseTest courseTest);

    /**
     * 操作章节内容图片表的接口
     */
    int uploadChapterImage(CourseSectionImage courseSectionImage);
    List<String> selectImageUrlByCid(String cid);
    List<String> selectImageUrlByCidAndChapterId(String cid,String chapterId);
    int deleteImageByCidAndChapterId(String cid,String chapterId);
    int deleteImageByCidAndChapterIdAndSectionId(String cid,String chapterId,String sectionId);
    List<String> selectImageUrlByCidAndChapterIdAndSectionId(String cid,String chapterId,String sectionId);
    int deleteImageByCid(String cid);


    /**
     * 课程封面接口
     * @param courseHead
     * @return
     */
    int uploadCourseHead(CourseHead courseHead);
    int deleteCourseHead(String cid);
    String selectCourseHeadUrlByCid(String cid);




}
