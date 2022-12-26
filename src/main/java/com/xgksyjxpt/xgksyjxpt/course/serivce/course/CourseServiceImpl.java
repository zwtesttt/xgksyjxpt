package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.*;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseSectionImageMapper courseSectionImageMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseHeadMapper courseHeadMapper;

    @Autowired
    private CourseChapterMapper courseChapterMapper;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private CourseTestMapper courseTestMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private DockerService dockerService;

    /**
     * 添加新课程
     * @param course
     * @return
     */
    @Override
    public int insertCourse(Course course) {
        return courseMapper.insertCourse(course);
    }

    /**
     * 删除课程
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int deleteCourse(String cid) {
        //删除课程章节
        deleteCourseChapterByCid(cid);
        //删除课程实验
        deleteCourseTestByCid(cid);
        //保存课程封面url
        String headurl=selectCourseHeadUrlByCid(cid);
        //删除课程封面记录
        deleteCourseHead(cid);
        //删除学生选课记录
        studentService.deleteStuCourseByCid(cid);
        //       开始删除服务器中的封面
        fastdfsUtil.deleteFile(headurl);
        return courseMapper.deleteCourse(cid);
    }

    /**
     * 根据cid查询课程信息
     * @param cid
     * @return
     */
    @Override
    public Course selectCourseByCid(String cid) {
        return courseMapper.selectCourseByCid(cid);
    }

    /**
     * 根据教师号查询课程号
     * @param tid
     * @return
     */
    @Override
    public List<String> selectCourseIdByTid(String tid) {
        return courseMapper.selectCourseIdByTid(tid);
    }

    /**
     * 根据课程号批量删除课程
     * @param cids
     * @return
     */
    @Override
    @Transactional
    public int deleteCourses(String[] cids) {
        for (String cid:cids
             ) {
            //删除课程章节
            deleteCourseChapterByCid(cid);
            //删除课程实验
            deleteCourseTestByCid(cid);
            //保存课程封面url
            String headurl=selectCourseHeadUrlByCid(cid);
            //删除课程封面记录
            deleteCourseHead(cid);
            //删除学生选课记录
            studentService.deleteStuCourseByCid(cid);
            //       开始删除服务器中的封面
            fastdfsUtil.deleteFile(headurl);
        }
        return courseMapper.deleteCoursesByCids(cids);
    }

    /**
     * 查询所有课程信息
     * @return
     */
    @Override
    public List<Course> queryAllCourse(Integer pageNum,Integer pageSize) {
        return courseMapper.queryAllCourse(pageNum,pageSize);
    }

    /**
     * 删除课程章节
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseChapterByCid(String cid) {
        //删除该课程下的小节
        deleteCourseSectionByCid(cid);
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
    public int deleteCourseChapterByCidAndChapterId(String cid, String chapterId) {
        //删除该章节小的小节
        deleteCourseSectionByCidAndChapterId(cid,chapterId);
        return courseChapterMapper.deleteCourseChapterByCidAndChapterId(cid,chapterId);
    }

    /**
     * 根据课程号和章节号删除小节
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseSectionByCidAndChapterId(String cid, String chapterId) {
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
    public int deleteCourseSectionByCidAndChapterIdAndSectionId(String cid, String chapterId, String sectionId) {
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
     * 根据课程号删除课程实验
     * @param cid
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseTestByCid(String cid) {
        //保存将被删除的课程的所有实验id
        List<String> testids=selectCourseTestIdByCid(cid);
        //保存将要删除的容器id
        if (testids.size()!=0){
            for (String s:testids
            ) {
                List<String> containerIds=containerService.selectContainerIdByTestId(s);
                //删除运行容器记录
                if (containerIds.size()!=0){
                    int stu=containerService.deleteContainerByTestId(s);
                    if (stu==containerIds.size()){
                        //删除该实验下的容器
                        dockerService.removeContainers(containerIds);
                    }
                }
                //删除实验记录
                studentService.deleteStuTestByTestId(s);
            }
        }
        return courseTestMapper.deleteCourseTestByCid(cid);
    }

    /**
     * 根据课程号查询实验id
     * @param cid
     * @return
     */
    @Override
    public List<String> selectCourseTestIdByCid(String cid) {
        return courseTestMapper.selectCourseTestIdByCid(cid);
    }

    /**
     * 查询所有实验
     * @return
     */
    @Override
    public List<CourseTest> queryAllCourseTest() {
        return courseTestMapper.queryAllCourseTest();
    }

    /**
     * 根据课程号查询课程实验
     * @param cid
     * @return
     */
    @Override
    public List<CourseTest> queryCourseTestByCid(String cid,Integer pageNum,Integer pageSize) {
        return courseTestMapper.queryAllCourseTestByCid(cid,pageNum,pageSize);
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
    public List<String> selectImageUrlByCidAndChapterId(String cid, String chapterId) {
        return courseSectionImageMapper.selectImageUrlByCidAndChapterId(cid,chapterId);
    }

    /**
     * 根据课程号和章节号删除图片记录
     * @param cid
     * @param chapterId
     * @return
     */
    @Override
    public int deleteImageByCidAndChapterId(String cid, String chapterId) {
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
    public int deleteImageByCidAndChapterIdAndSectionId(String cid, String chapterId, String sectionId) {
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
    public List<String> selectImageUrlByCidAndChapterIdAndSectionId(String cid, String chapterId, String sectionId) {
        return courseSectionImageMapper.selectImageUrlByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
    }
    /**
     * 添加课程封面记录
     * @param courseHead
     * @return
     */
    @Override
    public int uploadCourseHead(CourseHead courseHead) {
        return courseHeadMapper.insertCourseHead(courseHead);
    }

    /**
     * 根据课程号删除封面记录
     * @param cid
     * @return
     */
    @Override
    public int deleteCourseHead(String cid) {
        return courseHeadMapper.deleteCourseHeadByCid(cid);
    }

    /**
     * 根据课程号查询封面url
     * @param cid
     * @return
     */
    @Override
    public String selectCourseHeadUrlByCid(String cid) {
        return courseHeadMapper.selectCourseHeadUrlByCid(cid);
    }

    /**
     * 添加上传图片记录
     * @param courseSectionImage
     * @return
     */
    @Override
    public int uploadChapterImage(CourseSectionImage courseSectionImage) {
        return courseSectionImageMapper.uploadchapterImage(courseSectionImage);
    }
}
