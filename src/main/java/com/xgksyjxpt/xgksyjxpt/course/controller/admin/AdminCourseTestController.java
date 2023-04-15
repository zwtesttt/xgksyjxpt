package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.CourseTest;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.CourseTestService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseManage")
@Api(tags = "课程管理(实验管理)")
public class AdminCourseTestController {

    @Autowired
    private CourseTestService courseTestService;

    @Autowired
    private ContainerService containerService;

    /**
     * 查询实验列表
     */
    @GetMapping("/getCourseTestsByCid")
    @ApiOperation("查询实验列表")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getCourseTestsByCid(CourseTest courseTest, Integer pageNum, Integer pageSize){
        ReturnObject re =new ReturnObject();
        Map<String,Object> remap=new HashMap<>();
        List<Map<String,Object>> relist=new ArrayList<>();
        if (courseTest.getCid()!=null){
            if (pageNum != null && pageSize !=null) {
                List<CourseTest> list=courseTestService.queryCourseTestByTidOrSid(null,null,courseTest,(pageNum-1)*pageSize,pageSize);
                for (CourseTest ct:list
                ) {
                    //封装实验信息对象
                    Map<String,Object> map=new HashMap<>();
                    //实验id
                    map.put("testId",ct.getTest_id());
                    //实验名
                    map.put("testName",ct.getTest_name());
                    //实验开始时间
                    map.put("testStartTime",ct.getTest_start_time());
                    //实验结束时间
                    map.put("testEndTime",ct.getTest_end_time());
                    //使用镜像名
                    map.put("testImageName",ct.getTest_image_name());
                    //容器数量
                    map.put("containerCount",containerService.selectContainerIdByTestIds(new String[]{ct.getTest_id()}).size());
                    //实验状态
                    map.put("testStatus",ct.getTest_status());
                    //章节名
                    map.put("chapterId",ct.getChapterId());
                    //班级列表
                    map.put("classList",courseTestService.selectCourseTestClass(ct.getTest_id()));
                    //描述
                    map.put("testDescription",ct.getTest_description());
                    relist.add(map);
                }
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("查询成功");
                //封装响应对象
                remap.put("testList",relist);
                remap.put("total",courseTestService.queryCourseTestCountByTidOrSid(null,null,courseTest));
                re.setData(remap);
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("查询失败，分页参数不能为空");
            }

        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("课程号不能为空");
        }

        return re;
    }

    /**
     * 批量删除课程实验
     */
    @DeleteMapping("/delCourseTests")
    @ApiOperation("批量删除课程实验")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="testIds",value="实验id",dataType="array",required = true)
    public Object delCourseTest(@RequestBody String[] testIds){
        ReturnObject re=new ReturnObject();
        try {
//            验证参数
            if (testIds!=null && testIds.length!=0){
                //删除实验
                int stu=courseTestService.deleteCourseTestsByTestIds(testIds);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("实验id不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
}
