package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import io.swagger.models.auth.In;
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
    int deleteCourseChapterByCidAndChapterId(String cid,Integer chapterId);
    int insertCourseChapter(CourseChapter courseChapter);
    List<CourseChapter> selectCourseChapter(String cid);
    Integer queryCourseChapterMaxId(String cid);

    /**
     * 课程小节接口
     */
    int deleteCourseSectionByCidAndChapterId(String cid,Integer chapterId);
    int deleteCourseSectionByCid(String cid);
    int deleteCourseSectionByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    int insertCourseSection(CourseSection courseSection);
    List<CourseSection> selectCourseSectionName(String cid,Integer chapterId);
    Integer queryCourseSectionMaxId(String cid,Integer chapterId);
    String queryCourseSectionText(String cid,Integer chapterId,Integer sectionId);
    int updayeCourseSectionText(String cid,Integer chapterId,Integer sectionId,String text);

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
    List<String> selectImageUrlByCidAndChapterId(String cid,Integer chapterId);
    int deleteImageByCidAndChapterId(String cid,Integer chapterId);
    int deleteImageByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    List<String> selectImageUrlByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    int deleteImageByCid(String cid);


    /**
     * 课程封面接口
     * @param courseHead
     * @return
     */
    int uploadCourseHead(CourseHead courseHead);
    int deleteCourseHead(String cid);
    String selectCourseHeadUrlByCid(String cid);

    /**
     * 课程实验镜像接口
     */
    List<CourseTestImages> selectAllImagesName();
    int insertCourseTestImages(CourseTestImages courseTestImages);
    int deleteCourseTestImages(String[] imagesNames);


}
