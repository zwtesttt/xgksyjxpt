package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Admin;
import com.xgksyjxpt.xgksyjxpt.course.mapper.AdminMapper;
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
