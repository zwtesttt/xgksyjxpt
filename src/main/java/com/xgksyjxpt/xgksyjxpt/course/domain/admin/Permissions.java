package com.xgksyjxpt.xgksyjxpt.course.domain.admin;

public class Permissions {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column permissions_t.id
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column permissions_t.permissions
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    private String permissions;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column permissions_t.id
     *
     * @return the value of permissions_t.id
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column permissions_t.id
     *
     * @param id the value for permissions_t.id
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column permissions_t.permissions
     *
     * @return the value of permissions_t.permissions
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column permissions_t.permissions
     *
     * @param permissions the value for permissions_t.permissions
     *
     * @mbggenerated Sat Feb 11 20:34:54 CST 2023
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}