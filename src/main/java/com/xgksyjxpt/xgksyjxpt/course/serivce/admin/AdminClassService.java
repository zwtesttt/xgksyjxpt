package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.ClassName;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminClassService {
    int insertClass(String className);
    Object selectAllClass(Integer pageSize,Integer pageNum);
    int updateClassName(String className,String newClassName);

    int deleteClassName(String className);

    ClassName selectClassNameByClassName(String name);
}
