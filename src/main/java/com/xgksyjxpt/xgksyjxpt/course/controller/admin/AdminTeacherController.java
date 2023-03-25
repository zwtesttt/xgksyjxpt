package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.Teacher;
import com.xgksyjxpt.xgksyjxpt.course.domain.teacher.TeacherHead;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.GetCellValue;
import io.swagger.annotations.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacherManage")
@Api(tags = "老师管理")
public class AdminTeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 查询所有老师信息
     * @return
     */
    @GetMapping("/getTeachers")
    @ApiOperation("查询所有老师信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)})
    public Object getTeachers(Teacher teacher,Integer pageNum, Integer pageSize){
        ReturnObject re=new ReturnObject();
        if(pageNum!=null&&pageSize!=null){
            Map<String,Object> remap=new HashMap<>();
            List<Map<String,String>> list=new ArrayList();
            //获取所有老师列表
            List<Teacher> ss=teacherService.queryAllTeacher(teacher,(pageNum-1)*pageSize,pageSize);
            for (Teacher s:ss) {
                //封装老师信息
                Map<String,String> map=new HashMap<>();
                //教师号
                map.put("tid",s.getTid());
                //名字
                map.put("name",s.getName());
                //性别
                map.put("sex",s.getSex());
                //年龄
                map.put("age",s.getAge()+"");
                //身份
                map.put("identity",s.getIdentity());
//            对密码进行加密后发送到前端
//            map.put("passwd", Base64Converter.encode(s.getPasswd()));
                list.add(map);
            }
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            re.setMessage("查询成功");
//            封装响应对象
            remap.put("teaList",list);
            remap.put("total",teacherService.queryTeacherCount(teacher));
            re.setData(remap);
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("分页参数不能为空");
        }

        return re;
    }
    /**
     * 添加老师
     */
    @PostMapping("/addTeacher")
    @ApiOperation("添加老师")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addTeacher(Teacher teacher){
        ReturnObject re =new ReturnObject();
        try{
            if (teacher!=null){
                if(teacherService.selectNotDelTeacher(teacher.getTid())!=null){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("教师号已存在");
                }else{
                    //加密密码
                    //密码加密
                    String passwd = passwordEncoder.encode(teacher.getPasswd());
                    teacher.setPasswd(passwd);
                    int stu=teacherService.insertTeacher(teacher);
                    if(stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }

            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("老师信息不能为空");
            }

        }catch(Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re ;
    }

    /**
     * 批量添加老师
     */
    @PostMapping("/addTeachers")
    @ApiOperation(value = "批量添加老师(弃用)",hidden = true)
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addStudents(@RequestBody Teacher[] teachers){
        ReturnObject re =new ReturnObject();
        int flag=0;
        try {
            if (teachers.length!=0){
                //验证教师号
                for (Teacher s:teachers
                ) {
                    //判断教师号第一个字符
                    if(!"t".equals(s.getTid().substring(0,1))){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("教师号格式错误失败");
                        break;
                    }
                    //判断教师号是否存在
                    if(teacherService.selectNotDelTeacher(s.getTid())!=null){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("教师号"+s.getTid()+"已存在");
                        break;
                    }
                }
                if(flag==0){
                    for (Teacher s:teachers) {
                        //密码加密
                        String passwd = passwordEncoder.encode(s.getPasswd());
                        s.setPasswd(passwd);
                    }
//                添加记录
                    int stu=teacherService.addTeachers(teachers);
                    if (stu==teachers.length){
                        //添加教师后设置默认头像
                        //注意，要先上传头像到文件系统，初始化的时候就要操作
                        for (Teacher s:teachers
                        ) {
                            TeacherHead sh=new TeacherHead();
                            sh.setTid(s.getTid());
                            sh.setHead_link(HeadUrl.DEFAULT_TEA_HEAD);
                            teacherService.uploadTeaHead(sh);
                        }
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 批量导入老师
     */
    @PostMapping("/uploadTeachers")
    @ApiOperation(value = "导入老师")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="file",value=".xlsx文件",required = true)
    })
    public Object uploadStudents(MultipartFile file){
        ReturnObject re=new ReturnObject();
        try {
            //获取文件输入流
            InputStream in=file.getInputStream();
            //创建excel页对象
            XSSFWorkbook wb = new XSSFWorkbook(in);
//            HSSFWorkbook wb = new HSSFWorkbook(in);
            //获取第一页
//            HSSFSheet sheet = wb.getSheetAt(0);
            XSSFSheet sheet=wb.getSheetAt(0);
//            HSSFRow row = null;
//            HSSFCell cell = null;
            XSSFRow row=null;
            XSSFCell cell=null;
            Teacher tea=null;
            //存放学生信息
            List<Teacher> tealist=new ArrayList<>();
            //从第一行开始循环，到最后一行数据
            for (int i=1;i<=sheet.getLastRowNum();i++){
                tea=new Teacher();
//                每次获取行
                row=sheet.getRow(i);
                //获取列信息
                for (int k=0;k<row.getLastCellNum();k++){
                    cell=row.getCell(k);
                    //获取列中的值
//                    String cellvalue= GetCellValue.getCellValue(cell);
                    String cellvalue= GetCellValue.getXSSFCellValue(cell);
                    //第一列为学号
                    if (k==0){
                        if(teacherService.selectNotDelTeacher(cellvalue)==null){
                            tea.setTid(cellvalue);
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("教师号已存在");
                            return re;
                        }
                        //第二列为名字
                    }else if (k==1){
                        tea.setName(cellvalue);
                        //第三列为年龄
                    }else if (k==2){
                        //把年龄转成整形
                        Integer ag=(int)Double.parseDouble(cellvalue);
                        tea.setAge(Integer.valueOf(ag));
                        //第四列为性别
                    }else if(k==3){
                        tea.setSex(cellvalue);
//                    第五列为密码
                    } else if (k==4) {
                        //加密密码
                        String enpass= passwordEncoder.encode(cellvalue);
                        tea.setPasswd(enpass);
                    }

                }
                tealist.add(tea);
            }
            if (tealist.size()!=0){
                Teacher[] teas=new Teacher[tealist.size()];
                int i=0;
                for (Teacher s:tealist
                ) {
                    teas[i++]=s;
                }
                int res=teacherService.insertTeachers(teas);
                if (res!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("添加成功");
                }else {
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("添加失败");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 更新老师信息
     */
    @PostMapping("/updateTeacher")
    @ApiOperation("更新老师信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParam(name="passwd",value="密码(不用带)",required = false)
    public Object updateStudent(Teacher teacher){
        ReturnObject re=new ReturnObject();
        try {
//            //密码加密
//            String passwd = passwordEncoder.encode(teacher.getPasswd());
//            teacher.setPasswd(passwd);
            int stu= teacherService.updateTeacher(teacher);
            if (stu!=0){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                re.setMessage("修改成功");
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 修改老师密码
     */
    @PostMapping("/updateTeaPass")
    @ApiOperation("修改老师密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="tid",value="学生id",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updateTeaPass(String tid,String newPass){
        ReturnObject re=new ReturnObject();
        if (tid!=null&&newPass!=null){
            //对新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
            try {
                //开始修改密码
                int stu=teacherService.updateTeaPasswd(tid,newpasswd);
//                   修改是否成功
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("修改成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                }
            }catch (Exception e){
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("修改失败");
                e.printStackTrace();
            }
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("教师号和新密码不能为空");
        }
        return re;
    }

    /**
     * 批量删除老师
     */
    @DeleteMapping("/deleteTeachers")
    @ApiOperation("批量删除老师")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="tids",value="老师id列表",dataType="array",required = true)
    })
    public Object deleteTeachers(@RequestBody String[] tids){
        ReturnObject re=new ReturnObject();
        try {
            if (tids.length!=0){
                int teacount=0;
                //验证老师是否存在
                for (String tid:tids
                ) {
                    Teacher ss=teacherService.selectNotDelTeacher(tid);
                    if (ss!=null) {teacount++;}
                }
                //如果列表内所有老师都存在才删除
                if (teacount==tids.length){
                    //                提前保存老师头像url
                    List<String> headurl=new ArrayList<>();
                    for (String tid:tids
                    ) {
                        //查询老师头像url
                        String url=teacherService.selectTeaHeadUrl(tid);
                        //排除默认头像
                        if (!HeadUrl.DEFAULT_TEA_HEAD.equals(url)){
                            headurl.add(url);
                        }
                    }
//                开始删除老师
                    int stu=teacherService.deleteTeachers(tids);
                    if (stu!=0){
                        //删除文件系统中的老师头像
                        for (String url:headurl
                        ) {
                            fastdfsUtil.deleteFile(url);
                        }
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("删除成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("一个或多个教师不存在,请检查教师号是否正确");
                }
            }else{
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("删除列表为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("删除失败");
        }
        return re;
    }
}
