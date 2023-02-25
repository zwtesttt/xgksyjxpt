package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminHead;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.AdminHeadMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.AdminMapper;
import com.xgksyjxpt.xgksyjxpt.domain.HeadUrl;
import com.xgksyjxpt.xgksyjxpt.util.FastdfsUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminHeadMapper adminHeadMapper;

    @Autowired
    private FastdfsUtil fastdfsUtil;

    /**
     * 根据用户名查询正常账号信息
     * @param id
     * @return
     */
    @Override
    public Admin selectNotDelAdmin(String id) {
        return adminMapper.selectNotDelAdmin(id);
    }

    /**
     * 查询所有账号信息
     * @param id
     * @return
     */
    @Override
    public Admin selectAdmin(String id) {
        return adminMapper.selectAdmin(id);
    }

    /**
     * 查询头像链接
     * @param rid
     * @return
     */
    @Override
    public String selectAdminHeadUrl(String rid) {
        return adminHeadMapper.selectAdminHeadUrl(rid);
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @Override
    @Transactional
    public int insertAdmin(Admin admin) {
        //设置管理员默认头像
        AdminHead head=new AdminHead();
        head.setRid(admin.getRid());
        head.setHead_link(HeadUrl.DEFAULT_ADM_HEAD);
        adminHeadMapper.insertAdminHead(head);
        return adminMapper.insertAdmin(admin);
    }

    /**
     * 更新管理员信息
     * @param admin
     * @return
     */
    @Override
    public int updateAdmin(Admin admin) {
        return adminMapper.updateAdmin(admin);
    }

    /**
     * 添加管理员头像
     * @param adminHead
     * @return
     */
    @Override
    public int insertAdminHead(AdminHead adminHead) {
        return adminHeadMapper.insertAdminHead(adminHead);
    }

    /**
     * 修改管理员头像
     * @param adminHead
     * @return
     */
    @Override
    public int updateAdminHead(AdminHead adminHead) {
        return adminHeadMapper.updateAdminHead(adminHead);
    }

    /**
     * 修改管理员密码
     * @param pass
     * @param rid
     * @return
     */
    @Override
    public int updateAdminPass(String rid, String pass) {
        return adminMapper.updateAdminPass(rid,pass);
    }

    /**
     * 删除管理员
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public int deleteAdmins(String[] rids) {
        for (String rid:rids
             ) {
            //查询头像url
            String url=adminHeadMapper.selectAdminHeadUrl(rid);
//        排除默认头像
            if (!HeadUrl.DEFAULT_ADM_HEAD.equals(url)){
                fastdfsUtil.deleteFile(url);
            }
            //删除头像
            adminHeadMapper.deleteAdminHead(rid);
        }
        //删除管理员
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.in("rid",rids);
        return adminMapper.delete(wrapper);
//        return adminMapper.deleteAdmins(rids);
    }

    /**
     * 查询所有普通管理员信息
     * @return
     */
    @Override
    public List<Admin> selectCommAdmin(Admin admin,Integer pageNum,Integer pageSize) {
        return adminMapper.selectCommonAdmin(admin,pageNum,pageSize);
    }

    /**
     * 查询普通管理员总数
     * @return
     */
    @Override
    public int queryAdmincCount(Admin admin) {
        return adminMapper.queryAdminCount(admin);
    }

    /**
     * 查询管理员id
     * @return
     */
    @Override
    public List<String> selectAdminRid() {
        return adminMapper.selectAdminRid();
    }

}
