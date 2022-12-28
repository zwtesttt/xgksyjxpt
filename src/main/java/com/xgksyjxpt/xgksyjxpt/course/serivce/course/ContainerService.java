package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Container;

import java.util.List;

public interface ContainerService {
    int createContainer(Container container);
    String queryPasswd(String cid);

    int deleteStuContainer(String[] stuIds);

    List<String> selectStuContainerId(String stuId);

    int deleteContainerByTestId(String testId);
    List<String> selectContainerIdByTestId(String testId);

    List<Container> selectContainerByTestId(String testId,Integer pageNum,Integer pageSize);
}
