package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseChapter;

import java.util.List;

public interface CourseChapterService {
    /**
     * 课程章节接口
     */
    int deleteCourseChapterByCid(String cid);
    int deleteCourseChapterByCidAndChapterId(String cid,Integer chapterId);
    int insertCourseChapter(CourseChapter courseChapter);
    List<CourseChapter> selectCourseChapter(String cid);
    Integer queryCourseChapterMaxId(String cid);
    int updateCourseChapterName(String cid,Integer chapterId,String chapterName);
}
