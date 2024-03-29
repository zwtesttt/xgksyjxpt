package com.xgksyjxpt.xgksyjxpt.course.domain.student;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.crypto.Data;
import java.util.Date;

@ApiModel(value = "学生类")
@TableName("student_t")
public class Student {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student_t.stu_id
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    @ApiModelProperty(value = "学生id",dataType = "String",required = true)
    private String sid;

    @ApiModelProperty(value = "身份",dataType = "String",required = true)
    private String identity;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student_t.name
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    @ApiModelProperty(value = "学生名字",dataType = "String",required = true)
    private String name;

    @TableLogic(value = "null",delval = "now()")
    @ApiModelProperty(hidden = true)
    private Date delete_time;

    public Date getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Date delete_time) {
        this.delete_time = delete_time;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student_t.age
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    @ApiModelProperty(value = "学生年龄",dataType = "Integer",required = true)
    private Integer age;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student_t.sex
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    @ApiModelProperty(value = "学生性别",dataType = "String",required = true)
    private String sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student_t.passwd
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    @ApiModelProperty(value = "学生密码",dataType = "String",required = true)
    private String passwd;
    @ApiModelProperty(value = "学生班级",dataType = "String",required = true)
    private String className;

    public String getClass_name() {
        return className;
    }

    public void setClass_name(String className) {
        this.className = className;
    }



    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student_t.stu_id
     *
     * @return the value of student_t.stu_id
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public String getSid() {
        return sid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student_t.stu_id
     *
     * @param sid the value for student_t.stu_id
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student_t.name
     *
     * @return the value of student_t.name
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student_t.name
     *
     * @param name the value for student_t.name
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student_t.age
     *
     * @return the value of student_t.age
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student_t.age
     *
     * @param age the value for student_t.age
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student_t.sex
     *
     * @return the value of student_t.sex
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student_t.sex
     *
     * @param sex the value for student_t.sex
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student_t.passwd
     *
     * @return the value of student_t.passwd
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student_t.passwd
     *
     * @param passwd the value for student_t.passwd
     *
     * @mbggenerated Sun Nov 27 14:20:38 CST 2022
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}