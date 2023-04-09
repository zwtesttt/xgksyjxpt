package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.CourseSectionImageMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.CourseSectionMapper;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseSectionServiceImpl implements CourseSectionService {

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private CourseSectionImageMapper courseSectionImageMapper;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    /**
     * 根据课程号和章节号删除小节
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseSectionByCidAndChapterId(String cid, Integer chapterId) {
        //删除该章节下的小节上传的图片
        //保存即将要删除的图片url
        List<String> urls=courseSectionImageMapper.selectImageUrlByCidAndChapterId(cid,chapterId);
        //开始删除记录
        courseSectionImageMapper.deleteImageByCidAndChapterId(cid,chapterId);
        //删除数据库中的图片
        if (urls.size()!=0){
            for (String s:urls
            ) {
                fastdfsUtil.deleteFile(s);
            }
        }

        return courseSectionMapper.deleteCourseSectionByCidAndChapterId(cid,chapterId);
    }

    /**
     * 根据课程号删除小节
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseSectionByCid(String cid) {
        //删除该课程下的小节上传的图片
        //保存即将要删除的图片url
        List<String> urls=courseSectionImageMapper.selectImageUrlByCid(cid);
        //开始删除记录
        courseSectionImageMapper.deleteImageByCid(cid);
        //删除数据库中的图片
        if (urls.size()!=0){
            for (String s:urls
            ) {
                fastdfsUtil.deleteFile(s);
            }
        }
        return courseSectionMapper.deleteCourseSectionByCid(cid);
    }

    /**
     * 根据课程号、章节号、小节号删除小节
     * @param cid
     * @param chapterId
     * @param sectionId
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseSectionByCidAndChapterIdAndSectionId(String cid, Integer chapterId, Integer sectionId) {
        //删除该小节下上传的图片
        //保存即将要删除的图片url
        List<String> urls=courseSectionImageMapper.selectImageUrlByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
        //开始删除记录
        courseSectionImageMapper.deleteImageByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
        //删除数据库中的图片
        if (urls.size()!=0){
            for (String s:urls
            ) {
                fastdfsUtil.deleteFile(s);
            }
        }
        return courseSectionMapper.deleteCourseSectionByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
    }
    /**
     * 添加课程小节
     * @param courseSection
     * @return
     */
    @Override
    public int insertCourseSection(CourseSection courseSection) {
        return courseSectionMapper.insertCourseSectionByCidAndChapterId(courseSection);
    }

    /**
     * 查询章节下的小节标题
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    public List<CourseSection> selectCourseSectionName(String cid, Integer chapterId) {
        return courseSectionMapper.selectCourseSectionName(cid,chapterId);
    }

    /**
     * 查找章节下最大小节id
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    public Integer queryCourseSectionMaxId(String cid, Integer chapterId) {
        return courseSectionMapper.queryCourseSectionMaxId(cid,chapterId);
    }

    /**
     * 查询小节文本内容
     * @param cid
     * @param chapterId
     * @param sectionId
     * @return
     */
    @Override
    public String queryCourseSectionText(String cid, Integer chapterId, Integer sectionId) {
        return courseSectionMapper.queryCourseSectionText(cid,chapterId,sectionId);
    }

    /**
     * 修改小节内容
     * @param cid
     * @param chapterId
     * @param sectionId
     * @param text
     * @return
     */
    @Override
    public int updayeCourseSectionText(String cid, Integer chapterId, Integer sectionId, String text) {
        return courseSectionMapper.updateCourseSectionText(cid,chapterId,sectionId,text);
    }

    /**
     * 查询小节
     * @param cid
     * @param chapterId
     * @param sectionId
     * @return
     */
    @Override
    public CourseSection selectCourseSectionByCidAndChapterIdAndSectionId(String cid, Integer chapterId, Integer sectionId) {
        QueryWrapper<CourseSection> wq=new QueryWrapper<>();
        wq.eq("cid",cid).eq("chapter_id",chapterId).eq("section_id",sectionId);
        return courseSectionMapper.selectOne(wq);
    }

    /**
     * 根据cid删除图片
     * @param cid
     * @return
     */
    @Override
    public int deleteImageByCid(String cid) {
        return courseSectionImageMapper.deleteImageByCid(cid);
    }

    /**
     * 根据课程号查询图片url
     * @param cid
     * @return
     */
    @Override
    public List<String> selectImageUrlByCid(String cid) {
        return courseSectionImageMapper.selectImageUrlByCid(cid);
    }

    /**
     * 根据课程号和章节号查询图片url
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    public List<String> selectImageUrlByCidAndChapterId(String cid, Integer chapterId) {
        return courseSectionImageMapper.selectImageUrlByCidAndChapterId(cid,chapterId);
    }

    /**
     * 根据课程号和章节号删除图片记录
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    public int deleteImageByCidAndChapterId(String cid, Integer chapterId) {
        return courseSectionImageMapper.deleteImageByCidAndChapterId(cid,chapterId);
    }

    /**
     * 根据课程号和章节号、小节号删除图片记录
     * @param cid
     * @param chapterId
     * @param sectionId
     * @return
     */
    @Override
    public int deleteImageByCidAndChapterIdAndSectionId(String cid, Integer chapterId, Integer sectionId) {
        return courseSectionImageMapper.deleteImageByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
    }

    /**
     * 根据课程号和章节号、小节号查询图片url
     * @param cid
     * @param chapterId
     * @param sectionId
     * @return
     */
    @Override
    public List<String> selectImageUrlByCidAndChapterIdAndSectionId(String cid, Integer chapterId, Integer sectionId) {
        return courseSectionImageMapper.selectImageUrlByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
    }

    /**
     * 添加上传图片记录
     * @param courseSectionImage
     * @return
     */
    @Override
    public int uploadSectionImage(CourseSectionImage courseSectionImage) {
        return courseSectionImageMapper.uploadchapterImage(courseSectionImage);
    }
}
