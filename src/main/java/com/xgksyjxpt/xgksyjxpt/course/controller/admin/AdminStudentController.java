package com.xgksyjxpt.xgksyjxpt.course.controller.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnObject;
import com.xgksyjxpt.xgksyjxpt.domain.ReturnStatus;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import com.xgksyjxpt.xgksyjxpt.util.GetCellValue;
import io.swagger.annotations.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
@RequestMapping("/studentManage")
@Api(tags = "学生管理")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private FastdfsUtil fastdfsUtil;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private DockerService dockerService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    /**
     * 添加学生
     */
    @PostMapping("/addStudent")
    @ApiOperation("添加学生")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addStudents(@RequestBody Student student){
        ReturnObject re =new ReturnObject();
        try {
            if (student!=null){
                //验证学号是否存在
                if(studentService.selectNotDelStudent(student.getSid())!=null){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("学号"+student.getSid()+"已存在");
                }else{
                    //                密码加密
                    String enpass= passwordEncoder.encode(student.getPasswd());
                    student.setPasswd(enpass);
                    int stu=studentService.insertStudentOne(student);
                    if (stu!=0){
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                        re.setMessage("添加成功");
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("添加失败");
                    }
                }
            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学生信息不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("添加失败");
        }
        return re;
    }
    /**
     * 批量添加学生
     */
    @PostMapping("/addStudents")
    @ApiOperation(value = "批量添加学生(弃用)",hidden = true)
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object addStudents(@RequestBody Student[] students){
        ReturnObject re =new ReturnObject();
        int flag=0;
        try {
            if (students.length!=0){
                //验证学号
                for (Student s:students
                ) {
                    if (!"s".equals(s.getSid().substring(0,1))){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("学号格式错误");
                        break;
                    }
                    if(studentService.selectNotDelStudent(s.getSid())!=null){
                        flag=1;
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("学号"+s.getSid()+"已存在");
                        break;
                    }
                }
                if (flag==0){
//                    for (Student s:students) {
//                        //密码解密
//                        String passwd = Base64Converter.decode(s.getPasswd());
//                        s.setPasswd(passwd);
//                    }
//                添加记录
                    int stu=studentService.insertStudent(students);
                    if (stu==students.length){
                        //添加学生后设置默认头像
                        //注意，要先上传头像到文件系统，初始化的时候就要操作
                        for (Student s:students
                        ) {
                            StudentHead sh=new StudentHead();
                            sh.setSid(s.getSid());
                            sh.setHead_link(HeadUrl.DEFAULT_STU_HEAD);
                            studentService.uploadStuHead(sh);
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
     * 批量导入学生
     */
    @PostMapping("/uploadStudents")
    @ApiOperation(value = "导入学生")
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
            Student stu=null;
            //存放学生信息
            List<Student> stulist=new ArrayList<>();
            //从第一行开始循环，到最后一行数据
            for (int i=1;i<=sheet.getLastRowNum();i++){
                stu=new Student();
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
                        if(studentService.selectNotDelStudent(cellvalue)==null){
                            stu.setSid(cellvalue);
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("学号已存在");
                            return re;
                        }
                    //第二列为名字
                    }else if (k==1){
                        stu.setName(cellvalue);
                    //第三列为年龄
                    }else if (k==2){
                        //把年龄转成整形
                        Integer ag=(int)Double.parseDouble(cellvalue);
                        stu.setAge(Integer.valueOf(ag));
                    //第四列为性别
                    }else if(k==3){
                        stu.setSex(cellvalue);
//                    第五列为班级
                    }else if (k==4){
                        stu.setClass_name(cellvalue);
                    //第六列为密码
                    } else if (k==5) {
                        String enpass= passwordEncoder.encode(cellvalue);
                        stu.setPasswd(enpass);
                    }

                }
                //保存学生
                stulist.add(stu);
            }
            if (stulist.size()!=0){
                Student[] stus=new Student[stulist.size()];
                int i=0;
                //把列表学生放到数组里
                for (Student s:stulist
                ) {
                    stus[i++]=s;
                }
                //添加学生
                int res=studentService.insertStudent(stus);
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
     * 查询所有学生信息
     * @return
     */
    @GetMapping("/getStudents")
    @ApiOperation("查询所有学生信息")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页数",dataType="int",required = true),
            @ApiImplicitParam(name="pageSize",value="每页数据条数",dataType="int",required = true)
    })
    public Object getStudents(Student student,Integer pageNum, Integer pageSize){
        ReturnObject re=new ReturnObject();
        if(pageNum!=null&&pageSize!=null){
            Map<String,Object> remap=new HashMap<>();
            List<Map<String,String>> list=new ArrayList();
            //获取所有学生列表
            List<Student> ss=studentService.queryStudents(student,(pageNum-1)*pageSize,pageSize);
            for (Student s:ss) {
                //封装学生信息
                Map<String,String> map=new HashMap<>();
                //学号
                map.put("sid",s.getSid());
                //名字
                map.put("name",s.getName());
                //性别
                map.put("sex",s.getSex());
                //年龄
                map.put("age",s.getAge()+"");
                //班别
                map.put("className",s.getClass_name());
                //身份
                map.put("identity",s.getIdentity());
//            对密码进行加密后发送到前端
//            map.put("passwd",Base64Converter.encode(s.getPasswd()));
                list.add(map);
            }
            re.setMessage("查询成功");
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
            remap.put("stuList",list);
            remap.put("total",studentService.queryStudentCount(student));
            re.setData(remap);
        }else{
            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
            re.setMessage("分页参数不能为空");
        }

        return re;
    }
    /**
     * 更新学生信息
     */
    @PostMapping("/updateStudent")
    @ApiOperation("更新学生信息")
    @ApiImplicitParam(name="passwd",value="密码(不用填)",dataType="student",required = false)
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    public Object updateStudent(Student student){
        ReturnObject re=new ReturnObject();
        try {
            if (student!=null){
//                //密码加密
//                String passwd = passwordEncoder.encode(student.getPasswd());
//                student.setPasswd(passwd);
                int stu= studentService.updateStudent(student);
                if (stu!=0){
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                    re.setMessage("修改成功");
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("修改失败");
                }
            }else {
                re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                re.setMessage("学生信息不能为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            re.setCode(0);
            re.setMessage("修改失败");
        }
        return re;
    }
    /**
     * 批量删除学生
     */
    @DeleteMapping("/deleteStudents")
    @ApiOperation("批量删除学生")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="sids",value="学生id列表",dataType="array",required = true)
    })
    public Object deleteStudents(@RequestBody String[] sids){
        ReturnObject re=new ReturnObject();
        try {
            if (sids.length!=0){
                int stucount=0;
                //验证学生是否存在
                for (String sid:sids
                ) {
                    Student ss=studentService.selectNotDelStudent(sid);
                    if (ss!=null) {stucount++;}
                }
                //如果列表内所有学生都存在才删除
                if (stucount==sids.length){
                    //                提前保存学生头像url和容器id
                    List<String> headurl=new ArrayList<>();
                    List<String> cids=new ArrayList<>();
                    for (String sid:sids
                    ) {
                        //查询学生头像url
                        String url=studentService.selectStuHeadUrl(sid);
                        //排除默认头像
                        if (!HeadUrl.DEFAULT_STU_HEAD.equals(url)){
                            headurl.add(url);
                        }
                        //查询学生容器id
                        List<String> cid=containerService.selectStuContainerId(sid);
                        if (cid.size()!=0){
                            for (String c:cid
                            ) {
                                cids.add(c);
                            }
                        }
                    }
//                开始删除学生
                    int stu=studentService.deleteStudents(sids);
                    if (stu!=0){
                        //删除文件系统中的学生头像
                        for (String url:headurl
                        ) {
                            fastdfsUtil.deleteFile(url);
                        }
                        //停止学生容器
                        int rs=dockerService.removeContainers(cids);
                        if (rs==cids.size()){
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_CG);
                            re.setMessage("删除成功");
                        }else{
                            re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                            re.setMessage("容器移除失败");
                        }
                    }else{
                        re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                        re.setMessage("删除失败");
                    }
                }else{
                    re.setCode(ReturnStatus.RETURN_STUTAS_CODE_SB);
                    re.setMessage("一个或多个学号不存在,请检查学号是否正确");
                }

            }else {
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

    /**
     * 修改学生密码
     */
    @PostMapping("/updateStuPass")
    @ApiOperation("修改学生密码")
    @ApiResponses(@ApiResponse(code = 200,response = ReturnObject.class,message = "成功"))
    @ApiImplicitParams({
            @ApiImplicitParam(name="sid",value="学生id",dataType="string",required = true),
            @ApiImplicitParam(name="newPass",value="新密码",dataType="string",required = true)
    })
    public Object updatePass(String sid,String newPass){
        ReturnObject re=new ReturnObject();
        if (sid!=null&&newPass!=null){
            //对新密码加密
            String newpasswd=passwordEncoder.encode(newPass);
                try {
                    //开始修改密码
                    int stu=studentService.updateStuPass(sid,newpasswd);
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
            re.setMessage("修改失败");
        }
        return re;
    }
}
