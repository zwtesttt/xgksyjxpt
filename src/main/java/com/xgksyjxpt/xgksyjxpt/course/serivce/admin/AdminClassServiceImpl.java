package com.xgksyjxpt.xgksyjxpt.course.serivce.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xgksyjxpt.xgksyjxpt.course.domain.admin.ClassName;
import com.xgksyjxpt.xgksyjxpt.course.domain.student.Student;
import com.xgksyjxpt.xgksyjxpt.course.mapper.admin.AdminClassMapper;
import com.xgksyjxpt.xgksyjxpt.course.serivce.student.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminClassServiceImpl implements AdminClassService {
    @Resource
    private StudentService studentService;
    @Resource
    private AdminClassMapper adminClassMapper;

    //添加班级
    @Override
    public int insertClass(String className) {
        //实例化
        ClassName class1=new ClassName();
        class1.setClassName(className);
        return adminClassMapper.insert(class1);
    }

    /**
     * 查询所有班级
     * @return
     */
    @Override
    public Object selectAllClass(Integer pageSize,Integer pageNum) {
        Page<ClassName> page=new Page<>(pageNum, pageSize);
        QueryWrapper<ClassName> wrapper=new QueryWrapper<>();
        wrapper.select("class_name");
        Object o = adminClassMapper.selectPage(page,wrapper);
        return o;
    }
    /**
     * 修改班级名称
     */
    @Override
    public int updateClassName(String className, String newClassName) {
        QueryWrapper<ClassName> wrapper=new QueryWrapper<>();
        wrapper.select("id").eq("class_name",className);
        ClassName oj=adminClassMapper.selectOne(wrapper);
        oj.setClassName(newClassName);
        return adminClassMapper.updateById(oj);
    }

    /**
     * 删除班级
     * @param className
     * @return
     */
    @Override
    public int deleteClassName(String className) {
        //查询该班级学生学号
        List<String> list= studentService.selectStudentIdByClassName(new String[]{className});
        //删除学生
        studentService.deleteStudents(list.toArray(new String[list.size()]));
        return adminClassMapper.deleteById(adminClassMapper.selectOne(new QueryWrapper<ClassName>().eq("class_name",className)));
    }

    /**
     * 根据班级名称查询
     * @param name
     * @return
     */
    @Override
    public ClassName selectClassNameByClassName(String name) {
        return adminClassMapper.selectOne(new QueryWrapper<ClassName>().eq("class_name",name));
    }


}
