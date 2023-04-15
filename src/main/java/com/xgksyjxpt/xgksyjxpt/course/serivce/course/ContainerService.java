package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Container;

import java.util.List;

public interface ContainerService {
    int createContainer(Container container);
    String queryPasswd(String cid);

    int deleteStuContainer(String[] stuIds);

    List<String> selectStuContainerId(String stuId);

    int deleteContainerByTestIds(String[] testId);
    List<String> selectContainerIdByTestIds(String[] testId);

    List<Container> selectContainerByTestId(Container container,String testId,Integer pageNum,Integer pageSize);
    int queryContainerCountByTestId(Container container,String testId);

    int deleteContainerByContainerIds(String[] cids);
}
