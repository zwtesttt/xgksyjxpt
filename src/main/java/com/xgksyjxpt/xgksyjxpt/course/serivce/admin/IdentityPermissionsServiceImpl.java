package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.IdentityPermissions;
import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.IdentityMapper;
import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.IdentityPermissionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IdentityPermissionsServiceImpl implements IdentityPermissionsService {
    @Resource
    private IdentityPermissionsMapper identityPermissionsMapper;

    /**
     * 查询身份权限
     * @param identity
     * @return
     */
    @Override
    public List<String> selectIdentityPermissions(String identity) {
        return identityPermissionsMapper.selectIdentityPermissions(identity);
    }

    /**
     * 给指定身份添加权限
     * @param identityPermissions
     * @return
     */
    @Override
    public int insertIdentityPermissions(IdentityPermissions identityPermissions) {
        return identityPermissionsMapper.insertIdentityPermissions(identityPermissions);
    }

    /**
     * 移除身份权限
     * @param identityPermissions
     * @return
     */
    @Override
    public int deleteIdentityPermissions(IdentityPermissions identityPermissions) {
        return identityPermissionsMapper.deleteIdentityPermissions(identityPermissions);
    }
}
