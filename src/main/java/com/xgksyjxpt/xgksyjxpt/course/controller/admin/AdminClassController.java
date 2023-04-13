package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.ClassName;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminClassService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classManage")
@Api(tags = "班级管理")
public class AdminClassController {
    @Resource
    private AdminClassService adminClassService;

    /**
     * 添加班级
     * @param className
     * @return
     */

    @ApiOperation("添加班级")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="className",value="班级名称",dataType="string",required = true)
    })
    @PostMapping("/addClass")
    public Object addClass(String className){
        ReturnObject re=new ReturnObject();
        try {
            //添加班级
            int s=adminClassService.insertClass(className);
            if (s!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("添加成功");
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("添加失败");
            }
        }catch (Exception e){
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 查询班级
     */
    @ApiOperation("查询班级")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true),
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true)
    })
    @GetMapping("/queryClass")
    public Object queryClass(Integer pageSize,Integer pageNum){
        ReturnObject re=new ReturnObject();
        //查询所有班级名称
        //查询班级学生人数
        IPage<ClassName> o=(IPage<ClassName>) adminClassService.selectAllClass(pageSize,pageNum);
        Map<String,Object> map=new HashMap<>();
        //提取班级名
        List<String> list=new ArrayList<>();
        for (ClassName c :o.getRecords()) {
            list.add(c.getClassName());
        }
        map.put("classList",list);
        map.put("total",o.getTotal());
        re.setData(map);
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        return re;
    }
    /**
     * 修改班级名称
     */
    @ApiOperation("修改班级名称")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="className",value="班级名称",dataType="string",required = true),
            @ApiImplicitParam(name="newClassName",value="新班级名称",dataType="string",required = true)
    })
    @PostMapping("/updateClass")
    public Object updateClass(String className,String newClassName){
        ReturnObject re=new ReturnObject();
        try {
//            校验班级名称
            ClassName o=adminClassService.selectClassNameByClassName(className);
            if (o!=null){
                int s=adminClassService.updateClassName(className,newClassName);
                if (s!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("修改成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("班级不存在");
            }
        }catch (Exception e){
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 删除班级
     */
    @ApiOperation("删除班级")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="className",value="班级名称",dataType="string",required = true),
    })
    @DeleteMapping("/delClass")
    public Object delClass(String className){
        ReturnObject re=new ReturnObject();
        try {
//            校验班级名称
            ClassName o=adminClassService.selectClassNameByClassName(className);
            if (o!=null){
                int s=adminClassService.deleteClassName(className);
                if (s!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("班级不存在");
            }

        }catch (Exception e){
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
}
