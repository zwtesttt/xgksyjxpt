package com.xgksyjxpt.xgksyjxpt.course.domain.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "课程章节类")
public class CourseChapter extends CourseChapterKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_chapter_t.chapter_name
     *
     * @mbggenerated Sun Dec 18 15:21:05 CST 2022
     */
    @ApiModelProperty(value = "章节名称",dataType = "String",required = true)
    private String chapter_name;
    @ApiModelProperty(value = "课程id",dataType = "String",required = true)
    private String cid;


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_chapter_t.chapter_id
     *
     * @mbggenerated Sun Dec 18 15:21:05 CST 2022
     */
    @ApiModelProperty(value = "章节id",dataType = "int",required = true)
    private Integer chapter_id;

    @Override
    public String getCid() {
        return cid;
    }

    @Override
    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public Integer getChapter_id() {
        return chapter_id;
    }

    @Override
    public void setChapter_id(Integer chapter_id) {
        this.chapter_id = chapter_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_chapter_t.chapter_name
     *
     * @return the value of course_chapter_t.chapter_name
     *
     * @mbggenerated Sun Dec 18 15:21:05 CST 2022
     */
    public String getChapter_name() {
        return chapter_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_chapter_t.chapter_name
     *
     * @param chapter_name the value for course_chapter_t.chapter_name
     *
     * @mbggenerated Sun Dec 18 15:21:05 CST 2022
     */
    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }
}