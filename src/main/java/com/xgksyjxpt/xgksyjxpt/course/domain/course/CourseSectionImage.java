package com.xgksyjxpt.xgksyjxpt.course.domain.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@ApiModel(value = "课程小节图片类")
public class CourseSectionImage {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_section_image_t.id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    @ApiModelProperty(value = "id",dataType = "String",hidden = true)
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_section_image_t.cid
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    @ApiModelProperty(value = "课程id",dataType = "String",required = true)
    private String cid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_section_image_t.chapter_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    @ApiModelProperty(value = "章节id",dataType = "int",required = true)
    private Integer chapter_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_section_image_t.section_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    @ApiModelProperty(value = "小节id",dataType = "int",required = true)
    private Integer section_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column course_section_image_t.image_url
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    @ApiModelProperty(value = "小节内容图片url",dataType = "String",hidden = true)
    private String image_url;
    @ApiModelProperty(value = "上传的图片列表",dataType = "list",required = true)
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_section_image_t.id
     *
     * @return the value of course_section_image_t.id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_section_image_t.id
     *
     * @param id the value for course_section_image_t.id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_section_image_t.cid
     *
     * @return the value of course_section_image_t.cid
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public String getCid() {
        return cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_section_image_t.cid
     *
     * @param cid the value for course_section_image_t.cid
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_section_image_t.chapter_id
     *
     * @return the value of course_section_image_t.chapter_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public Integer getChapter_id() {
        return chapter_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_section_image_t.chapter_id
     *
     * @param chapter_id the value for course_section_image_t.chapter_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public void setChapter_id(Integer chapter_id) {
        this.chapter_id = chapter_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_section_image_t.section_id
     *
     * @return the value of course_section_image_t.section_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public Integer getSection_id() {
        return section_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_section_image_t.section_id
     *
     * @param section_id the value for course_section_image_t.section_id
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public void setSection_id(Integer section_id) {
        this.section_id = section_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column course_section_image_t.image_url
     *
     * @return the value of course_section_image_t.image_url
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column course_section_image_t.image_url
     *
     * @param image_url the value for course_section_image_t.image_url
     *
     * @mbggenerated Sat Dec 17 20:08:49 CST 2022
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}