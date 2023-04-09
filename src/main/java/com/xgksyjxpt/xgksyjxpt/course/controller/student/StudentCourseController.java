package com.xgksyjxpt.xgksyjxpt.course.controller.student;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseChapter;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseChapterService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseSectionService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")//给该controller类所有接口加上/student的url前缀
@Api(tags = "学生业务(课程)")
public class StudentCourseController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseChapterService courseChapterService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseSectionService courseSectionService;
    /**
     * 查询学生选课信息
     */
    @GetMapping("/getStudentCourseInfo")
    @ApiOperation("查询学生选课信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="sid",value="学号",dataType="string",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getStudentCourseInfo(String sid,Integer pageNum,Integer pageSize){
        ReturnObject re=new ReturnObject();
        if (sid!=null&&pageNum!=null&&pageSize!=null){
            //验证学号
            if (studentService.selectNotDelStudent(sid)!=null){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                //查询选课信息，并设置到响应对象里
                List<Map<String,Object>> list=studentService.selectStudentCourseInfo(sid,(pageNum-1)*pageSize,pageSize);
                //查询总条数
                int total=studentService.queryStudentCourseCountBySid(sid);
                for (Map<String,Object> map:list
                ) {
                    String url= (String) map.get("course_link");
                    map.put("course_link",url.substring(7));
                }
                Map<String,Object> remap=new HashMap<>();
                remap.put("stucourseList",list);
                remap.put("total",total);
                re.setData(remap);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学号不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("学号和分页参数不能为空");
        }
        return re;
    }
    /**
     * 查询课程章节
     */
    @GetMapping("/getCourseChapter")
    @ApiOperation("查询课程章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true)
    public Object getCourseChapter(String cid){
        ReturnObject re=new ReturnObject();
        if (cid!=null){
            //课程验证
            if (courseService.selectCourseByCid(cid)!=null){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                List<Map<String,Object>> relist=new ArrayList<>();
                //查询章节列表
                List<CourseChapter> chapterList= courseChapterService.selectCourseChapter(cid);
                for (CourseChapter cc:chapterList
                ) {
                    Map<String,Object> remap=new HashMap<>();
                    List<Map<String,Object>> cslist=new ArrayList<>();
                    //查询章节下的小节
                    List<CourseSection> sections=courseSectionService.selectCourseSectionName(cid,cc.getChapter_id());
                    for (CourseSection sc:sections
                         ) {
                        Map<String,Object> csmap=new HashMap<>();
                        csmap.put("sectionId",sc.getSection_id());
                        csmap.put("sectionName",sc.getSection_name());
                        cslist.add(csmap);
                    }
                    remap.put("cid",cid);
                    remap.put("chapterId",cc.getChapter_id());
                    remap.put("chapterName",cc.getChapter_name());
                    remap.put("sectionList",cslist);
                    relist.add(remap);
                }
                re.setData(relist);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号不能为空");
        }
        return re;
    }
    /**
     * 查询指定章节下的小节标题
     */
    @GetMapping("/getCourseSectionNames")
    @ApiOperation("查询指定章节下的小节标题")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节id",dataType="int",required = true)
    })
    public Object getCourseSectionNames(String cid,Integer chapterId){
        ReturnObject re=new ReturnObject();
        if (cid!=null&&chapterId!=null){
            //封装响应对象
            List<Map<String,Object>> relist=new ArrayList<>();
            Map<String,Object> map=null;
            //查询小节信息
            List<CourseSection> list=courseSectionService.selectCourseSectionName(cid,chapterId);
            for (CourseSection cs:list
            ) {
                map=new HashMap<>();
                map.put("section_id",cs.getSection_id());
                map.put("section_name",cs.getSection_name());
                relist.add(map);
            }
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("查询成功");
            re.setData(relist);
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号和章节id不能为空");
        }
        return re;
    }
    /**
     * 查询小节文本内容
     */
    @GetMapping("/getCourseSectionText")
    @ApiOperation("查询小节文本内容")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节id",dataType="int",required = true),
            @ApiImplicitParam(name="sectionId",value="小节id",dataType="int",required = true)
    })
    public Object getCourseSectionText(String cid,Integer chapterId,Integer sectionId){
        ReturnObject re=new ReturnObject();
        if (cid!=null&&chapterId!=null&&sectionId!=null){
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("查询成功");
            re.setData(courseSectionService.queryCourseSectionText(cid,chapterId,sectionId));
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号和章节id、小节id不能为空");
        }
        return re;
    }
    /**
     * 查询课程实验状态为已开始的实验
     */
    @GetMapping("/getStudentStartTest")
    @ApiOperation("根据学号和课程号查询状态为已开始的实验")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="sid",value="学号",dataType="String",required = true),
            @ApiImplicitParam(name="pageNum",value="页号",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getStudentStartTest(String sid, String cid, Integer pageNum,Integer pageSize){
        ReturnObject re=new ReturnObject();
        if (sid!=null&&cid!=null&&pageNum!=null&&pageSize!=null){
            if (studentService.selectNotDelStudent(sid)!=null){
                if (courseService.selectCourseByCid(cid)!=null){
                    List<Map<String,Object>> slist=studentService.selectStudentTestInfo(sid,cid,(pageNum-1)*pageSize,pageSize);
                    re.setData(slist);
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("查询成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学号不存在");
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("学号、课程号、分页参数不能为空");
        }
        return re;
    }
}
