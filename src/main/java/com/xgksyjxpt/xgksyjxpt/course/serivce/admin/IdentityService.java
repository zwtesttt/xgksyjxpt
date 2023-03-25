package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import java.util.List;

public interface IdentityService {
    List<String> selectAllIdentity();
    int insertIdentity(String identity);
    int deleteIdentity(String identity);
}
