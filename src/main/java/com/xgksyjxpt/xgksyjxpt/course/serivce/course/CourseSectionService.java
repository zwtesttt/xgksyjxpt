package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;

import java.util.List;

public interface CourseSectionService {
    /**
     * 课程小节接口
     */
    int deleteCourseSectionByCidAndChapterId(String cid,Integer chapterId);
    int deleteCourseSectionByCid(String cid);
    int deleteCourseSectionByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    int insertCourseSection(CourseSection courseSection);
    List<CourseSection> selectCourseSectionName(String cid, Integer chapterId);
    Integer queryCourseSectionMaxId(String cid,Integer chapterId);
    String queryCourseSectionText(String cid,Integer chapterId,Integer sectionId);
    int updayeCourseSectionText(String cid,Integer chapterId,Integer sectionId,String text);


    /**
     * 管理小节图片的接口
     */
    int uploadSectionImage(CourseSectionImage courseSectionImage);
    List<String> selectImageUrlByCid(String cid);
    List<String> selectImageUrlByCidAndChapterId(String cid,Integer chapterId);
    int deleteImageByCidAndChapterId(String cid,Integer chapterId);
    int deleteImageByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    List<String> selectImageUrlByCidAndChapterIdAndSectionId(String cid,Integer chapterId,Integer sectionId);
    int deleteImageByCid(String cid);
}
