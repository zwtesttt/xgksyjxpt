package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.IdentityPermissions;

import java.util.List;

public interface IdentityPermissionsService {
    List<String> selectIdentityPermissions(String identity);
    int insertIdentityPermissions(IdentityPermissions identityPermissions);
    int deleteIdentityPermissions(IdentityPermissions identityPermissions);
}
