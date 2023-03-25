package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.IdentityPermissions;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.IdentityPermissionsService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.IdentityService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.PermissionsService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/identityManage")
@Api(tags = "角色管理")
public class IdentityPermissionsController {
    @Resource
    private IdentityPermissionsService identityPermissionsService;
    @Resource
    private IdentityService identityService;
    @Resource
    private PermissionsService permissionsService;

    /**
     * 添加角色的权限
     */
    @PostMapping("/addIdentityPermissions")
    @ApiOperation("添加角色的权限")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="identity",value="角色",dataType="string",required = true),
            @ApiImplicitParam(name="permissions",value="添加的权限",dataType="string",required = true)
    })
    public Object addIdentityPermissions(String identity,String permissions){
        ReturnObject re=new ReturnObject();
        try {
            //参数校验
            if (identity != null && permissions != null){
                //封装实体类
                IdentityPermissions obj=new IdentityPermissions();
                obj.setIdentity(identity);
                obj.setPermissions(permissions);
                //判断权限是否重复
                List<String> list=identityPermissionsService.selectIdentityPermissions(identity);
                if (list.contains(permissions)){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("权限重复");
                    return re;
                }
                //添加权限
                int stu=identityPermissionsService.insertIdentityPermissions(obj);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("添加成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("添加失败");
                }
            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("角色和权限不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 查询指定角色的权限
     */
    @GetMapping("/getIdentityPermissions")
    @ApiOperation("查询角色的权限")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="identity",value="角色",dataType="string",required = true)
    public Object getIdentityPermissions(String identity){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(identityPermissionsService.selectIdentityPermissions(identity));
        return re;
    }
    /**
     * 删除角色权限
     */
    @DeleteMapping("/delIdentityPermissions")
    @ApiOperation("删除角色的权限")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="identity",value="角色",dataType="string",required = true),
            @ApiImplicitParam(name="permissions",value="移除的权限名",dataType="string",required = true)
    })
    public Object delIdentityPermissions(String identity,String permissions){
        ReturnObject re=new ReturnObject();
        try {
            //参数校验
            if (identity != null && permissions != null){
                //封装实体类
                IdentityPermissions obj=new IdentityPermissions();
                obj.setIdentity(identity);
                obj.setPermissions(permissions);
                //移除权限
                int stu=identityPermissionsService.deleteIdentityPermissions(obj);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("删除成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("删除失败");
                }
            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("角色和权限不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
    /**
     * 添加角色
     */
    @PostMapping("/addIdentity")
    @ApiOperation("添加角色")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="identity",value="角色名",dataType="string",required = true)
    })
    public Object addIdentity(String identity){
        ReturnObject re=new ReturnObject();
        try{
//            参数校验
            if (identity!=null){
                //判断角色是否存在
                List<String> list=identityService.selectAllIdentity();
                //判断是否同名
                if (list.contains(identity)){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("角色已存在");
                    return re;
                }
                //添加角色
                int stu=identityService.insertIdentity(identity);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("添加成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("添加失败");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("角色名不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }

        return re;
    }
    /**
     * 查询所有角色
     */
    @GetMapping("/getIdentitys")
    @ApiOperation("查询所有角色")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object getIdentitys(){
        ReturnObject re=new ReturnObject();
        List<Map<String,Object>> relist=new ArrayList<>();
        //查询所有角色名
        List<String> identityList=identityService.selectAllIdentity();
        for (String identity:identityList
             ) {
//            封装响应对象
            Map<String,Object> map=new HashMap<>();
//            角色名
            map.put("identity",identity);
//            角色权限
            map.put("permissions",identityPermissionsService.selectIdentityPermissions(identity));
            relist.add(map);
        }
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(relist);
        return re;
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delIdentity")
    @ApiOperation("删除角色")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="identity",value="角色",dataType="string",required = true)})
    public Object delIdentity(String identity){
        ReturnObject re=new ReturnObject();
        try {
//            参数校验
            if (identity!=null){
                if ("superadmin".equals(identity)){
                    re.setMessage("超级管理员不能被删除");
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                }else{
//                    删除角色
                    int stu=identityService.deleteIdentity(identity);
                    if (stu!=0){
                        re.setMessage("删除成功");
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    }else{
                        re.setMessage("删除失败");
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    }
                }
            }else{
                re.setMessage("角色名不能为空");
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setMessage("删除失败");
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
        }
        return re;
    }
    /**
     * 查询所有权限
     */
    @GetMapping("/getPermissions")
    @ApiOperation("查询所有权限")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object getPermissions(){
        ReturnObject re=new ReturnObject();
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        re.setData(permissionsService.selectAllPermissions());
        return re;
    }

}
