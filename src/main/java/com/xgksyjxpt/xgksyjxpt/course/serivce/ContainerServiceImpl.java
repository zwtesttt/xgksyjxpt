package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Container;
import com.xgksyjxpt.xgksyjxpt.course.mapper.ContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerServiceImpl implements ContainerService {
    //注入mapper对象
    @Autowired
    private ContainerMapper containerMapper;
    //添加运行容器记录
    @Override
    public int createContainer(Container container) {
        return containerMapper.createContainer(container);
    }

    @Override
    public String queryPasswd(String cid) {
        return containerMapper.queryPasswd(cid);
    }
}
