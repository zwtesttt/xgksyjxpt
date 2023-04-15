package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseStatus;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentTest;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.CourseTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentTestMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CourseTestServiceImpl implements CourseTestService {

    @Resource
    private CourseTestMapper courseTestMapper;

    @Resource
    private ContainerService containerService;
    @Resource
    private DockerService dockerService;

    @Resource
    private StudentService studentService;

    @Resource
    private StudentTestMapper studentTestMapper;
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
                List<String> containerIds=containerService.selectContainerIdByTestIds(new String[]{s});
                //删除运行容器记录
                if (containerIds.size()!=0){
                    int stu=containerService.deleteContainerByTestIds(new String[]{s});
                    if (stu==containerIds.size()){
                        //删除该实验下的容器
                        dockerService.removeContainers(containerIds);
                    }
                }
                //删除实验记录
                studentService.deleteStuTestByTestIds(new String[]{s});
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
    public List<CourseTest> queryAllCourseTest(Integer pageNum,Integer pageSize) {
        return courseTestMapper.queryAllCourseTest(pageNum,pageSize);
    }

    /**
     * 根据课程号查询课程实验
     * @param
     * @return
     */
    @Override
    public List<CourseTest> queryCourseTestByTidOrSid(String tid,String sid,CourseTest courseTest,Integer pageNum,Integer pageSize) {
        return courseTestMapper.queryAllCourseTestByTidOrSid(tid,sid,courseTest,pageNum,pageSize);
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
            start= DateUtil.getDate(courseTest.getTest_start_time());
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
                courseTestMapper.createCourseTestEvent(courseTest.getTest_start_time(),courseTest.getTest_id(),"courseevent"+courseTest.getTest_id()+"start", CourseStatus.COURSE_START);
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
     * 修改课程实验
     * @param courseTest
     * @return
     */
    @Override
    @Transactional
    public int updateCourseTest(CourseTest courseTest) {
        //删除之前创建的定时任务
        courseTestMapper.deleteCourseTestEvent("courseevent"+courseTest.getTest_id()+"start");
        courseTestMapper.deleteCourseTestEvent("courseevent"+courseTest.getTest_id()+"end");
        //设置实验状态
        //比较课程开始时间和当前时间
        //现在时间
        Date nowdate=new Date();
        Date start=null;
        Date end=null;
        //开始时间
        try {
            start= DateUtil.getDate(courseTest.getTest_start_time());
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
                courseTestMapper.createCourseTestEvent(courseTest.getTest_start_time(),courseTest.getTest_id(),"courseevent"+courseTest.getTest_id()+"start", CourseStatus.COURSE_START);
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
        return courseTestMapper.updateCourseTest(courseTest);
    }

    /**
     * 删除课程实验
     * @param testId
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseTestByTestId(String testId) {
        //删除之前创建的定时任务
        courseTestMapper.deleteCourseTestEvent("courseevent"+testId+"start");
        courseTestMapper.deleteCourseTestEvent("courseevent"+testId+"end");
        //查询实验下所有容器id
        List<String> containerIds=containerService.selectContainerIdByTestIds(new String[]{testId});
        //删除运行容器记录
        if (containerIds.size()!=0){
            //删除容器记录
            int stu=containerService.deleteContainerByTestIds(new String[]{testId});
            if (stu==containerIds.size()){
                //删除该实验下的容器
                dockerService.removeContainers(containerIds);
            }
        }
        //删除学生实验记录
        studentService.deleteStuTestByTestIds(new String[]{testId});
        return courseTestMapper.deleteCourseTestByTestId(testId);
    }

    /**
     * 根据实验id查询实验信息
     * @param testId
     * @return
     */
    @Override
    public CourseTest selectCourseTestByTestId(String testId) {
        return courseTestMapper.selectCourseTestByTestId(testId);
    }

    /**
     * 根据课程号查询实验总数
     * @param courseTest
     * @param
     * @return
     */
    @Override
    public int queryCourseTestCountByTidOrSid(String tid,String sid,CourseTest courseTest) {
        return courseTestMapper.queryCourseCountByTidOrSid(tid,sid,courseTest);
    }

    /**
     * 批量删除实验
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int deleteCourseTestsByTestIds(String[] ids) {
        for (String testId:ids
             ) {
            //删除之前创建的定时任务
            courseTestMapper.deleteCourseTestEvent("courseevent"+testId+"start");
            courseTestMapper.deleteCourseTestEvent("courseevent"+testId+"end");
        }
        //查询实验下所有容器id
        List<String> containerIds=containerService.selectContainerIdByTestIds(ids);
        //删除运行容器记录
        if (containerIds.size()!=0){
            //删除容器记录
            int stu=containerService.deleteContainerByTestIds(ids);
            if (stu==containerIds.size()){
                //删除该实验下的容器
                dockerService.removeContainers(containerIds);
            }
        }
        //批量删除学生实验记录
        studentService.deleteStuTestByTestIds(ids);
        return courseTestMapper.deleteCourseTestsByTestIds(ids);
    }

    /**
     * 根据实验id查询拥有实验的班级
     * @param testId
     * @return
     */
    @Override
    public List<String> selectCourseTestClass(String testId) {
        return courseTestMapper.selectCourseTestClass(testId);
    }

    /**
     * 修改实验班级
     * @param testId
     * @param newclassNames
     * @return
     */
    @Override
    public int updateTestClassByTestIdAndClassNames(String testId, String[] newclassNames) {
        int res=0;
//        删除原来的
        if(studentService.deleteStuTestByTestIds(new String[]{testId})!=0){
            //查询实验所属课程
            CourseTest courseTest=courseTestMapper.selectCourseTestByTestId(testId);
            //拿到要修改的学号
            List<String> sidList=studentService.selectStudentIdByClassName(newclassNames);
            StudentTest[] stlist=new StudentTest[sidList.size()];
            int i=0;
            for (String sid:sidList
                 ) {
                StudentTest st=new StudentTest();
                st.setTest_id(testId);
                st.setSid(sid);
                st.setCid(courseTest.getCid());
                stlist[i++]=st;
            }
            if(i==sidList.size()){
                //添加学生实验
                studentService.insertStudentTest(stlist);
                res=1;
            }
        }else{
            return res;
        }

        return res;
    }

    /**
     * 修改实验文档
     * @param testId
     * @param doc
     * @return
     */
    @Override
    public int updayeCourseTestDocByTestId(String testId, String doc) {
        return courseTestMapper.updateCourseTestDocByTestId(testId,doc);
    }

}
