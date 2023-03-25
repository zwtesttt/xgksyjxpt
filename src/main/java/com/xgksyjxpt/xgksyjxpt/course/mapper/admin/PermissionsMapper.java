package com.xgksyjxpt.xgksyjxpt.course.mapper.admin;

import com.xgksyjxpt.xgksyjxpt.course.domain.admin.Permissions;

import java.util.List;

public interface PermissionsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    int insert(Permissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    int insertSelective(Permissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    Permissions selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    int updateByPrimaryKeySelective(Permissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions_t
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    int updateByPrimaryKey(Permissions record);
//    查询所有权限
    List<String> selectAllPermissions();
}