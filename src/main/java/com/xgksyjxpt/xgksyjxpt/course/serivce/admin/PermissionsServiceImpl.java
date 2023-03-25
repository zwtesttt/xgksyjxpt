package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.PermissionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionsServiceImpl implements PermissionsService {
    @Resource
    private PermissionsMapper permissionsMapper;

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<String> selectAllPermissions() {
        return permissionsMapper.selectAllPermissions();
    }
}
