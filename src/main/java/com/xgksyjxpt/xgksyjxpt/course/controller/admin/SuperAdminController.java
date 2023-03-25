package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminIdentity;
import com.xgksyjxpt.xgksyjxpt.course.serivce.admin.AdminService;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminManage")
@Api(tags = "管理员管理")
public class SuperAdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 添加管理员
     */
    @PostMapping("/addAdmin")
    @ApiOperation("添加管理员")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="identity",value="身份(不用带)",dataType="string",required = false)
    public Object addAdmin(Admin admin){
        ReturnObject re=new ReturnObject();
        try{
            if (admin!=null){
                //在所有账号内查询是否存在账号
                if(adminService.selectNotDelAdmin(admin.getRid())!=null){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("管理员id已存在");
                }else{
                    //不存在则开始添加管理员
                    //密码加密
                    String enpass=passwordEncoder.encode(admin.getPasswd());
                    admin.setPasswd(enpass);
                    //设置身份为普通管理员
                    admin.setIdentity(AdminIdentity.ADMIN);
                    int stu = adminService.insertAdmin(admin);
                    if (stu!=0){
                        //添加记录成功则响应成功
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }

    /**
     * 批量删除管理员
     */
    @DeleteMapping("/deleteAdmins")
    @ApiOperation("批量删除管理员")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="rids",value="管理员id列表",dataType="array",required = true)
    public Object deleteAdmin(@RequestBody String[] rids){
        ReturnObject re=new ReturnObject();
        try {
            if (rids!=null && rids.length!=0){
                int i=0;
                for (String rid:rids
                     ) {
                    //校验管理员id
                    if (adminService.selectNotDelAdmin(rid)==null){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("一个或多个管理员不存在");
                        break;
                    }else{
                        i++;
                    }
                }
                if (i==rids.length){
                    //                    删除管理员
                    int stu=adminService.deleteAdmins(rids);
                    if (stu==i){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("管理员id不能为空");
            }
        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
    /**
     * 查询所有普通管理员信息
     */
    @GetMapping("/getAdmins")
    @ApiOperation("查询所有普通管理员信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getAdmins(Admin admin,Integer pageNum, Integer pageSize){
        ReturnObject re=new ReturnObject();
        Map<String,Object> map=new HashMap<>();
        List<Admin> list=adminService.selectCommAdmin(admin,(pageNum-1)*pageSize,pageSize);
        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
        re.setMessage("查询成功");
        map.put("admList",list);
        map.put("total",adminService.queryAdmincCount(admin));
        re.setData(map);
        return re;
    }
    /**
     * 修改普通管理员密码
     */
    @PostMapping("/modifyAdminPaasswd")
    @ApiOperation("修改普通管理员密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="rid",value="管理员id",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object modifyAdminPaasswd(String rid,String newPass){
        ReturnObject re=new ReturnObject();
        try {
            if (rid!=null&&newPass!=null){
//                验证管理员id
                if (adminService.selectNotDelAdmin(rid)!=null){
//                    加密密码
                    String enPass=passwordEncoder.encode(newPass);
//                    修改密码
                    int stu=adminService.updateAdminPass(rid,enPass);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("修改成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("修改失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("管理员不存在");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("管理员id和新密码不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }

}
