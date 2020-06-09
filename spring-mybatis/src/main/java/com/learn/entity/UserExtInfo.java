package com.learn.entity;

public class UserExtInfo {

    private Integer userExtId;
    private Integer userId;
    private Integer sex;
    private Integer age;
    private String username;

    public Integer getUserExtId() {
        return userExtId;
    }

    public void setUserExtId(Integer userExtId) {
        this.userExtId = userExtId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserExtInfo{" +
                "userExtId=" + userExtId +
                ", userId=" + userId +
                ", sex=" + (sex == 0 ? "男" : "女") +
                ", age=" + age +
                ", username=" + username +
                '}';
    }
}
