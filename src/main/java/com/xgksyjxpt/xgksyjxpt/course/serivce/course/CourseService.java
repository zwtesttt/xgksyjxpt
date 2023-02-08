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
    List<Course> queryAllCourse(Course course,Integer pageNum,Integer pageSize);
    int createEvent(String date,String cid,String eventName,String courseStatus);
    List<Course> selectCourseByTid(String tid,Integer pageNum,Integer pageSize);
    int updateCourseInfo(Course course);
    int queryCourseCount(Course course);

    /**
     * 课程封面接口
     * @param courseHead
     * @return
     */
    int uploadCourseHead(CourseHead courseHead);
    int deleteCourseHead(String cid);
    String selectCourseHeadUrlByCid(String cid);
    int updateCourseHead(CourseHead courseHead);

    /**
     * 课程实验镜像接口
     */
    List<CourseTestImages> selectAllImagesName();
    int insertCourseTestImages(CourseTestImages courseTestImages);
    int deleteCourseTestImages(String[] imagesNames);


}
