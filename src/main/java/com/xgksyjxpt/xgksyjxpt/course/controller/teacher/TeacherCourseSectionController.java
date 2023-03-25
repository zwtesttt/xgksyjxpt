package com.xgksyjxpt.xgksyjxpt.course.controller.teacher;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSection;
import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseSectionImage;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseSectionService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@Api(tags = "老师业务(小节)")
public class TeacherCourseSectionController {

    @Autowired
    private CourseSectionService courseSectionService;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private CourseService courseService;
    /**
     * 上传图片
     */
    @PostMapping("/uploadImage")
    @ApiOperation("上传图片")
    @ApiResponses(@ApiResponse(code = 200,response = String.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value="图片流",dataType="multipartFile",required = true),
    })
    public String uploadImage(MultipartFile file){
        String url=null;
        try{
            //上传文件拿到url
            url=fastdfsUtil.uploadFile(file);
            url=url.substring(7);
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 保存上传图片记录
     */
    @PostMapping("/saveSectionImage")
    @ApiOperation("保存上传图片记录")
    @ApiResponses(@ApiResponse(code = 200,response = String.class,message = "成功"))
    @ApiImplicitParams({
//            @ApiImplicitParam(name="cid",value="课程号",dataType="string",required = true),
//            @ApiImplicitParam(name="chapter_id",value="章节号",dataType="string",required = true),
//            @ApiImplicitParam(name="section_id",value="小节号",dataType="string",required = true),
//            @ApiImplicitParam(name="urls",value="上传的图片列表",dataType="list",required = true),
//            @ApiImplicitParam(name="image_url",value="不用管",dataType="string",required = false)
    })
    public Object uploadCourseSectionImage(@RequestBody CourseSectionImage courseSectionImage) {
        ReturnObject re=new ReturnObject();
        try {
            if (courseSectionImage!=null){
                CourseSectionImage obj=null;
                //封装对象
                //获取章节id
                Integer chapterId= courseSectionImage.getChapter_id();
                //获取课程id
                String cid= courseSectionImage.getCid();
                //获取小节id
                Integer sectionId=courseSectionImage.getSection_id();
                //保存url
                List<String> urls=courseSectionImage.getUrls();
                int count=0;
                if (urls.size()!=0){
                    for (String url:urls
                    ) {
                        obj=new CourseSectionImage();
                        obj.setChapter_id(chapterId);
                        obj.setCid(cid);
                        obj.setImage_url(url);
                        obj.setSection_id(sectionId);
                        //添加数据库记录
                        int st=courseSectionService.uploadSectionImage(obj);
                        if (st!=0){
                            count++;
                        }
                    }
                }
                if (count==urls.size()){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("保存成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("保存失败");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("保存失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("保存失败");
        }
        return re;
    }

    /**
     * 添加课程小节
     */
    @PostMapping("/addCourseSection")
    @ApiOperation("添加课程小节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="section_id",value="小节号(不用带)",dataType="int",required = true)
    public Object addCourseSection(CourseSection courseSection){
        ReturnObject re=new ReturnObject();
        try {
            if (courseSection!=null){
                //校验课程号
                if (courseService.selectCourseByCid(courseSection.getCid())!=null){
                    //查询最大小节id
                    Integer section_id=courseSectionService.queryCourseSectionMaxId(courseSection.getCid(),courseSection.getChapter_id());
                    if (section_id==null){
                        section_id=1;
                    }else{
                        section_id+=1;
                    }
                    //设置小节id
                    courseSection.setSection_id(section_id);
                    int stu=courseSectionService.insertCourseSection(courseSection);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("课程不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("小节信息不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
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
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true),
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
     * 修改小节文本内容
     */
    @PostMapping("/updateCourseSectionText")
    @ApiOperation("修改小节文本内容")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节id",dataType="int",required = true),
            @ApiImplicitParam(name="sectionId",value="小节id",dataType="int",required = true),
            @ApiImplicitParam(name="text",value="修改后的文本",dataType="string",required = true)
    })
    public Object updateCourseSectionText(String cid,Integer chapterId,Integer sectionId,String text){
        ReturnObject re=new ReturnObject();
        try{
            if (cid!=null&&chapterId!=null&&sectionId!=null){
                if (text!=null){
//                    修改小节内容
                    int stu=courseSectionService.updayeCourseSectionText(cid,chapterId,sectionId,text);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("文本内容不能为空");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号和章节id、小节id不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 删除小节
     */
    @DeleteMapping("/delCourseSection")
    @ApiOperation("删除小节")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cid",value="课程id",dataType="string",required = true),
            @ApiImplicitParam(name="chapterId",value="章节id",dataType="int",required = true),
            @ApiImplicitParam(name="sectionId",value="小节id",dataType="int",required = true)
    })
    public Object delCourseSection(String cid,Integer chapterId,Integer sectionId){
        ReturnObject re=new ReturnObject();
        try {
            if (cid!=null && chapterId!=null&&sectionId!=null){
//                删除小节
                int stu=courseSectionService.deleteCourseSectionByCidAndChapterIdAndSectionId(cid,chapterId,sectionId);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("课程号、章节id、小节id不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
}
