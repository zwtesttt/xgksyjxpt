package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Admin;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.AdminHead;

import java.util.List;

public interface AdminService {
    Admin selectNotDelAdmin(String id);
    Admin selectAdmin(String id);
    String selectAdminHeadUrl(String rid);
    int insertAdmin(Admin admin);
    int updateAdmin(Admin admin);
    int insertAdminHead(AdminHead adminHead);

    int updateAdminHead(AdminHead adminHead);
    int updateAdminPass(String rid,String pass);
    int deleteAdmin(String rid);
    List<Admin> selectCommAdmin(Integer pageNum,Integer pageSize);
}
