package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.StudentHead;
import com.xgksyjxpt.xgksyjxpt.course.mapper.StudentHeadMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FastdfsServiceImpl implements FastdfsService {
    @Resource
    private StudentHeadMapper studentHeadMapper;

    /**
     * 上传头像
     * @param studentHead
     * @return
     */
    @Override
    public int uploadHead(StudentHead studentHead) {
        return studentHeadMapper.uploadHead(studentHead);
    }

    /**
     * 查询学生头像链接
     * @param sid
     * @return
     */
    @Override
    public String selectStuHeadUrl(String sid) {
        return studentHeadMapper.selectStuHeadUrl(sid);
    }

    /**
     * 修改学生头像
     * @param studentHead
     * @return
     */
    @Override
    public int updateStuHead(StudentHead studentHead) {
        return studentHeadMapper.updateStuHead(studentHead);
    }
}
