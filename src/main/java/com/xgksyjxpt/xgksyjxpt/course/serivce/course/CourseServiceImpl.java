package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.*;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private CourseTestImagesMapper courseTestImagesMapper;

    /**
     * 添加新课程
     * @param course
     * @return
     */
    @Override
    @Transactional
    public int insertCourse(Course course) {
        //设置课程状态
        //比较课程开始时间和当前时间
        //现在时间
        Date nowdate=new Date();
        Date start=null;
        Date end=null;
        //开始时间
        try {
            start=DateUtil.getDate(course.getCourse_start());
            //结束时间
            end=DateUtil.getDate(course.getCourse_end());
        }catch (Exception e){
            e.printStackTrace();
        }
        //结束时间在当前时间之后则添加定时任务
        if(end.compareTo(nowdate)>0){
            //开课时间在当前时间之后
            if (start.compareTo(nowdate)>0){
                //创建数据库定时事件
                createEvent(course.getCourse_start(),course.getCid(),"event"+course.getCid()+"start",CourseStatus.COURSE_START);
                course.setCourse_status(CourseStatus.COURSE_NOT_START);
                //开课时间在当前时间之前或者等于当前时间
            }else{
                course.setCourse_status(CourseStatus.COURSE_START);
            }
            //创建数据库定时事件
            createEvent(course.getCourse_end(),course.getCid(),"event"+course.getCid()+"end",CourseStatus.COURSE_END);
            //结束时间在当前之间之前或者相等则把课程状态设置成已结束
        }else{
            course.setCourse_status(CourseStatus.COURSE_END);
        }


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
     * 定时修改课程状态
     * @param date
     * @param cid
     * @return
     */
    @Override
    public int createEvent(String date, String cid,String eventName,String courseStatus) {
        return courseMapper.createEvent(date,cid,eventName,courseStatus);
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
     * 添加课程实验
     * @param courseTest
     * @return
     */
    @Override
    public int insertCourseTest(CourseTest courseTest) {
        //设置实验状态
        //比较课程开始时间和当前时间
        //现在时间
        Date nowdate=new Date();
        Date start=null;
        Date end=null;
        //开始时间
        try {
            start=DateUtil.getDate(courseTest.getTest_start_time());
            //结束时间
            end=DateUtil.getDate(courseTest.getTest_end_time());
        }catch (Exception e){
            e.printStackTrace();
        }
        //结束时间在当前时间之后则添加定时任务
        if(end.compareTo(nowdate)>0){
            //实验开始时间在当前时间之后
            if (start.compareTo(nowdate)>0){
                //创建数据库定时事件
                courseTestMapper.createCourseTestEvent(courseTest.getTest_start_time(),courseTest.getTest_id(),"courseevent"+courseTest.getTest_id()+"start",CourseStatus.COURSE_START);
                courseTest.setTest_status(CourseStatus.COURSE_NOT_START);
                //开课时间在当前时间之前或者等于当前时间
            }else{
                courseTest.setTest_status(CourseStatus.COURSE_START);
            }
            //创建数据库定时事件
            courseTestMapper.createCourseTestEvent(courseTest.getTest_end_time(),courseTest.getTest_id(),"courseevent"+courseTest.getTest_id()+"end",CourseStatus.COURSE_END);
            //结束时间在当前之间之前或者相等则把课程状态设置成已结束
        }else{
            courseTest.setTest_status(CourseStatus.COURSE_END);
        }
        return courseTestMapper.insertCourseTest(courseTest);
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
     * 查询所有可用镜像名
     * @return
     */
    @Override
    public List<CourseTestImages> selectAllImagesName() {
        return courseTestImagesMapper.selectAllImagesName();
    }

    /**
     * 添加新实验可用镜像
     * @param courseTestImages
     * @return
     */
    @Override
    public int insertCourseTestImages(CourseTestImages courseTestImages) {
        return courseTestImagesMapper.insertCourseTestImages(courseTestImages);
    }

    /**
     * 批量删除可用镜像
     * @param imagesNames
     * @return
     */
    @Override
    public int deleteCourseTestImages(String[] imagesNames) {
        return courseTestImagesMapper.deleteCourseTestImages(imagesNames);
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
