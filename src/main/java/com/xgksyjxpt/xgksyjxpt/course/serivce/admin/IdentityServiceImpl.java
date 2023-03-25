package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.IdentityMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.student.StudentMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.teacher.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色service
 */
@Service
public class IdentityServiceImpl implements IdentityService {
    @Resource
    private IdentityMapper identityMapper;
    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<String> selectAllIdentity() {
        return identityMapper.selectAllIdentity();
    }

    /**
     * 添加新角色
     * @param identity
     * @return
     */
    @Override
    public int insertIdentity(String identity) {
        return identityMapper.insertIdentity(identity);
    }

    /**
     * 删除角色
     * @param identity
     * @return
     */
    @Override
    @Transactional
    public int deleteIdentity(String identity) {
        List<String> list=null;
        if("superadmin".equals(identity)){
            return 0;
        }else if("admin".equals(identity)){
            if (list.size()!=0) {
//            查询所有身份为admin的账号的id
                list = adminService.selectAdminRid();
                //删除账号
                adminService.deleteAdmins(list.toArray(new String[list.size()]));
            }
        } else if ("teacher".equals(identity)) {
            if (list.size()!=0) {
//            查询所有身份为teacher的账号id
                list = teacherService.selectTeacherTid();
                //删除账号
                teacherService.deleteTeachers(list.toArray(new String[list.size()]));
            }
        }else{
            list=studentService.selectIdentitySid(identity);
            if (list.size()!=0){
                studentService.deleteStudents(list.toArray(new String[list.size()]));
            }
        }
        return identityMapper.deleteIdentity(identity);
    }
}
