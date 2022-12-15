package com.xgksyjxpt.xgksyjxpt.course.serivce;

import com.xgksyjxpt.xgksyjxpt.course.domain.StudentHead;

public interface FastdfsService {
    int uploadHead(StudentHead studentHead);

    String selectStuHeadUrl(String sid);

    int updateStuHead(StudentHead studentHead);
}
