package com.mmall.utils;

public class ImportDTO {

    private String account;
    private String phone;
    private Integer age;
    private SexEnum sex;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "ImportDTO{" +
                "name='" + account + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
