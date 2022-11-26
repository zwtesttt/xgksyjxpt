package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.Container;

public interface ContainerService {
    int createContainer(Container container);
    String queryPasswd(String cid);
}
