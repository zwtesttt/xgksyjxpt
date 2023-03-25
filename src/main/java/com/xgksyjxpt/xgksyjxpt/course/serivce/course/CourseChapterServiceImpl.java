package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseChapter;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.CourseChapterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseChapterServiceImpl implements CourseChapterService {
    @Autowired
    private CourseChapterMapper courseChapterMapper;

    @Autowired
    private CourseSectionService courseSectionService;
    /**
     * 删除课程章节
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseChapterByCid(String cid) {
        //删除该课程下的小节
        courseSectionService.deleteCourseSectionByCid(cid);
        return courseChapterMapper.deleteCourseChapterByCid(cid);
    }

    /**
     * 根据课程号和章节号删除章节
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseChapterByCidAndChapterId(String cid, Integer chapterId) {
        //删除该章节的小节
        courseSectionService.deleteCourseSectionByCidAndChapterId(cid,chapterId);
        return courseChapterMapper.deleteCourseChapterByCidAndChapterId(cid,chapterId);
    }

    /**
     * 添加课程章节
     * @param courseChapter
     * @return
     */
    @Override
    public int insertCourseChapter(CourseChapter courseChapter) {
        return courseChapterMapper.insertCourseChapter(courseChapter);
    }

    /**
     * 查询课程章节
     * @param cid
     * @return
     */
    @Override
    public List<CourseChapter> selectCourseChapter(String cid) {
        return courseChapterMapper.selectCourseChapter(cid);
    }

    /**
     * 查找最大的章节id
     * @param cid
     * @return
     */
    @Override
    public Integer queryCourseChapterMaxId(String cid) {
        return courseChapterMapper.queryCourseChapterMaxId(cid);
    }

    /**
     * 修改章节名
     * @param cid
     * @param chapterId
     * @param chapterName
     * @return
     */
    @Override
    public int updateCourseChapterName(String cid, Integer chapterId, String chapterName) {
        return courseChapterMapper.updateCourseChapter(cid,chapterId,chapterName);
    }
}
