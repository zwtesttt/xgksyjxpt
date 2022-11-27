package com.xgksyjxpt.xgksyjxpt.login.serivce;

import com.xgksyjxpt.xgksyjxpt.login.domain.Admin;
import com.xgksyjxpt.xgksyjxpt.login.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 根据用户名查询账号信息
     * @param id
     * @return
     */
    @Override
    public Admin selectAdmin(String id) {
        return adminMapper.selectAdmin(id);
    }
}
