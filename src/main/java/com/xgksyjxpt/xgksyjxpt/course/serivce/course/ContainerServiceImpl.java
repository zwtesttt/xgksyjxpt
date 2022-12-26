package com.xgksyjxpt.xgksyjxpt.course.serivce.course;

import com.xgksyjxpt.xgksyjxpt.course.domain.course.Container;
import com.xgksyjxpt.xgksyjxpt.course.mapper.course.ContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 查询容器ssh密码
     * @param cid
     * @return
     */
    @Override
    public String queryPasswd(String cid) {
        return containerMapper.queryPasswd(cid);
    }

    /**
     * 批量删除运行容器记录
     * @param stuIds
     * @return
     */
    @Override
    public int deleteStuContainer(String[] stuIds) {
        return containerMapper.deleteStuContainer(stuIds);
    }

    /**
     * 根据学号查询容器id
     * @param stuId
     * @return
     */
    @Override
    public List<String> selectStuContainerId(String stuId) {
        return containerMapper.selectStuContainerId(stuId);
    }

    /**
     * 根据实验id删除运行记录
     * @param testId
     * @return
     */
    @Override
    public int deleteContainerByTestId(String testId) {
        return containerMapper.deleteContainerByTestId(testId);
    }

    @Override
    public List<String> selectContainerIdByTestId(String testId) {
        return containerMapper.selectContainerIdByTestId(testId);
    }
}
