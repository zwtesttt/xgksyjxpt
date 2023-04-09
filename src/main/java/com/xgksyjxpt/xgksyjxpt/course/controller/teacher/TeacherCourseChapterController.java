package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Course;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseChapter;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseChapterService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseSectionService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师业务(章节)")
public class TeacherCourseChapterController {

    @Autowired
    private CourseChapterService courseChapterService;

    @Autowired
    private CourseService courseService;

    @Resource
    private CourseSectionService courseSectionService;
    /**
     * 添加章节
     */
    @PostMapping("/addCourseChapter")
    @ApiOperation("添加章节")
    @ApiImplicitParam(name="chapter_id",value="章节id(不用带)",dataType="int",required = false)
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addCourseChapter(CourseChapter courseChapter){
        ReturnObject re=new ReturnObject();
        try{
            if (courseChapter!=null){
                //验证课程号
                Course course=courseService.selectCourseByCid(courseChapter.getCid());
                if (course!=null){
                    Integer chapter_id=courseChapterService.queryCourseChapterMaxId(courseChapter.getCid());
                    //如果没有找到最大的，说明是第一个章节
                    if (chapter_id==null){
                        chapter_id=1;
                    }else{
                        //否则在最大基础上+1
                        chapter_id+=1;
                    }
                    //设置章节id
                    courseChapter.setChapter_id(chapter_id);
                    //添加章节
                    int stu=courseChapterService.insertCourseChapter(courseChapter);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("章节信息不能为空");
            }
        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }

    /**
     * 根据章节id删除课程章节
     */
    @DeleteMapping("/delCourseChapter")
    @ApiOperation("根据章节id删除章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节id",dataType="string",required = true)
    })
    public Object delCourseChapterByChapterId(String cid,Integer chapterId){
        ReturnObject re=new ReturnObject();
        try {
            if (cid!=null && chapterId!=null){
//                删除章节
                int stu=courseChapterService.deleteCourseChapterByCidAndChapterId(cid,chapterId);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号或者章节号不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }

    /**
     * 查询章节
     */
    @GetMapping("/getCourseChapter")
    @ApiOperation("查询章节")
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
     * 修改课程章节标题
     */
    @PostMapping("/modifyCourseChapter")
    @ApiOperation("修改章节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
            @ApiImplicitParam(name="String",value="章节id",dataType="string",required = true),
            @ApiImplicitParam(name="chapterName",value="章节名字",dataType="string",required = true)
    })
    public Object modifyCourseChapter(String cid,Integer chapterId,String chapterName){
        ReturnObject re=new ReturnObject();
        try {
            //参数验证
            if (cid!=null&&chapterId!=null&&chapterName!=null){
                if (courseService.selectCourseByCid(cid)!=null){
                    int stu=courseChapterService.updateCourseChapterName(cid,chapterId,chapterName);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号、章节id、修改后的名字不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }

}
