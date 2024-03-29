package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.*;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.*;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.UuidUtil;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseHeadMapper courseHeadMapper;

    @Resource
    private StudentService studentService;

    @Resource
    private CourseChapterService courseChapterService;
    @Resource
    private CourseTestService courseTestService;
    @Resource
    private FastdfsUtil fastdfsUtil;

    @Resource
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
            start=DateUtil.getDate(course.getCourseStart());
            //结束时间
            end=DateUtil.getDate(course.getCourseEnd());
        }catch (Exception e){
            e.printStackTrace();
        }
        //结束时间在当前时间之后则添加定时任务
        if(end.compareTo(nowdate)>0){
            //开课时间在当前时间之后
            if (start.compareTo(nowdate)>0){
                //创建数据库定时事件
                createEvent(course.getCourseStart(),course.getCid(),"event"+course.getCid()+"start",CourseStatus.COURSE_START);
                course.setCourse_status(CourseStatus.COURSE_NOT_START);
                //开课时间在当前时间之前或者等于当前时间
            }else{
                course.setCourse_status(CourseStatus.COURSE_START);
            }
            //创建数据库定时事件
            createEvent(course.getCourseEnd(),course.getCid(),"event"+course.getCid()+"end",CourseStatus.COURSE_END);
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
        courseChapterService.deleteCourseChapterByCid(cid);
        //删除课程实验
        courseTestService.deleteCourseTestByCid(cid);
        //保存课程封面url
        String headurl=selectCourseHeadUrlByCid(cid);
        //删除课程封面记录
        deleteCourseHead(cid);
        //删除学生选课记录
        studentService.deleteStuCourseByCid(cid);
        //       开始删除服务器中的封面
        fastdfsUtil.deleteFile(headurl);
        //删除课程定时任务
        courseMapper.deleteCourseEvent("event"+cid+"start");
        courseMapper.deleteCourseEvent("event"+cid+"end");
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
            courseChapterService.deleteCourseChapterByCid(cid);
            //删除课程实验
            courseTestService.deleteCourseTestByCid(cid);
            //保存课程封面url
            String headurl=selectCourseHeadUrlByCid(cid);
            //删除课程封面记录
            deleteCourseHead(cid);
            //删除学生选课记录
            studentService.deleteStuCourseByCid(cid);
            //       开始删除服务器中的封面
            fastdfsUtil.deleteFile(headurl);
            //删除课程定时任务
            courseMapper.deleteCourseEvent("event"+cid+"start");
            courseMapper.deleteCourseEvent("event"+cid+"end");
        }
        return courseMapper.deleteCoursesByCids(cids);
    }

    /**
     * 查询所有课程信息
     * @return
     */
    @Override
    public List<Course> queryAllCourse(Course course,Integer pageNum,Integer pageSize) {
        return courseMapper.queryAllCourse(course,pageNum,pageSize);
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
     * 根据老师id查询课程信息
     */
    @Override
    public List<Course> selectCourseByTid(String tid,String courseName) {
        return courseMapper.selectCourseByTid(tid,courseName);
    }

    /**
     * 修改课程信息
     * @param course
     * @return
     */
    @Override
    @Transactional
    public int updateCourseInfo(Course course) {
        return courseMapper.updateCourseInfo(updateCourseStatus(course));
    }
    /**
     * 更新课程状态
     */
    public Course updateCourseStatus(Course course){
        //现在时间
        Date nowdate=new Date();
        Date start=null;
        Date end=null;
        //同时修改开始和结束时间
        if (course.getCourseStart()!=null && course.getCourseEnd()!=null){
            //开始时间
            try {
                start= DateUtil.getDate(course.getCourseStart());
                //结束时间
                end=DateUtil.getDate(course.getCourseEnd());
            }catch (Exception e){
                e.printStackTrace();
            }
            //不需要修改全部时间的情况,使用旧的时间
        }else{
            Course course1=courseMapper.selectCourseByCid(course.getCid());
            if (course.getCourseStart()==null&&course.getCourseEnd()!=null){
                //开始时间
                try {
                    start= DateUtil.getDate(course1.getCourseStart());
                    //结束时间
                    end=DateUtil.getDate(course.getCourseEnd());
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (course.getCourseStart()!=null&&course.getCourseEnd()==null) {
                try {
                    start= DateUtil.getDate(course.getCourseStart());
                    //结束时间
                    end=DateUtil.getDate(course1.getCourseEnd());
                }catch (Exception e){
                    e.printStackTrace();
                }
                //不需要修改时间的话直接退出
            }else{
                return course;
            }
        }
        //删除之前创建的定时任务
        courseMapper.deleteCourseEvent("event"+course.getCid()+"start");
        courseMapper.deleteCourseEvent("event"+course.getCid()+"end");
        //设置实验状态
        //比较课程开始时间和当前时间
        //结束时间在当前时间之后则添加定时任务
        if(end.compareTo(nowdate)>0){
            if (start.compareTo(nowdate)>0){
                //创建数据库定时事件
                createEvent(course.getCourseStart(),course.getCid(),"event"+course.getCid()+"start",CourseStatus.COURSE_START);
                course.setCourse_status(CourseStatus.COURSE_NOT_START);
                //开课时间在当前时间之前或者等于当前时间
            }else{
                course.setCourse_status(CourseStatus.COURSE_START);
            }
            //创建数据库定时事件
            createEvent(course.getCourseEnd(),course.getCid(),"event"+course.getCid()+"end",CourseStatus.COURSE_END);
            //结束时间在当前之间之前或者相等则把课程状态设置成已结束
        }else{
            course.setCourse_status(CourseStatus.COURSE_END);
        }
        return course;
    }

    /**
     * 查询所有课程总数
     * @param course
     * @return
     */
    @Override
    public int queryCourseCount(Course course) {
        return courseMapper.queryTeacherCount(course);
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
     * 修改课程封面
     * @param courseHead
     * @return
     */
    @Override
    public int updateCourseHead(CourseHead courseHead) {
        return courseHeadMapper.updateCourseHead(courseHead);
    }

    /**
     * 查询所有可用镜像名
     * @return
     */
    @Override
    public List<CourseTestImages> selectAllImagesName(String imageName,Integer pageNum,Integer pageSize) {
        return courseTestImagesMapper.selectAllImagesName(imageName,pageNum,pageSize);
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
     * 查询镜像总条数
     * @param imageName
     * @return
     */
    @Override
    public int queryImageCount(String imageName) {
        return courseTestImagesMapper.queryImageCount(imageName);
    }


}
