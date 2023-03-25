package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Container;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courseManage")
@Api(tags = "课程管理(容器管理)")
public class AdminTestContainerController {

    @Autowired
    private ContainerService containerService;
    @Autowired
    private DockerService dockerService;
    /**
     * 查询实验下容器列表
     */
    @GetMapping("/getTestContainersByTestId")
    @ApiOperation("查询实验下容器列表")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="testId",value="实验id",dataType="string",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getTestContainer(Container container, String testId, Integer pageNum, Integer pageSize){
        ReturnObject re=new ReturnObject();
        List<Map<String,Object>> relist=new ArrayList<>();
        Map<String,Object> remap=new HashMap<>();
        if (testId!=null){
            //获取所有容器信息
            List<Container> clist=containerService.selectContainerByTestId(container,testId,(pageNum-1)*pageSize,pageSize);
            for (Container c:clist
            ) {
                //封装容器信息对象
                Map<String,Object> map=new HashMap<>();
                //封装容器id
                map.put("cid",c.getContainer_id());
                //封装容器名
                map.put("cname",dockerService.selectContainersName(c.getContainer_id()));
//                //封装使用容器学生名字
//                map.put("stuname",studentService.selectNotDelStudent(c.getSid()).getName());
                //封装学号
                map.put("stuid",c.getSid());
                relist.add(map);
            }
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("查询成功");
            //封装响应对象
            remap.put("conerList",relist);
            remap.put("total",containerService.queryContainerCountByTestId(container,testId));
            re.setData(remap);
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("实验id不能为空");
        }


        return re;
    }
    /**
     * 批量删除容器
     */
    @DeleteMapping("/deleteContainers")
    @ApiOperation("批量删除容器")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="cids",value="容器id列表",dataType="array",required = true)
    })
    public Object deleteContainers(String[] cids){
        ReturnObject re =new ReturnObject();
        try {
            if (cids.length!=0){
                List<String> list=new ArrayList<>();
                for (String c:cids
                ) {
                    list.add(c);
                }
                //移除容器
                int stu=dockerService.removeContainers(list);
                if (stu==cids.length){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("删除列表不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }

}
