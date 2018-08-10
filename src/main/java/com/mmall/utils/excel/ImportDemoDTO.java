package com.mmall.utils.excel;

import java.util.Date;

/**
 * @author Qinyunchan
 * @date Created in 上午9:46 2018/8/9
 * @Modified By
 */

public class ImportDemoDTO {

    @IsNeed(sort = 0)
    private String account;
    @IsNeed(sort = 1)
    private Integer age;
    @IsNeed(sort = 2)
    private Date birthday;
    @IsNeed(sort = 3)
    private Double weight;
    @IsNeed(sort = 4)
    private Long money;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "ImportDemoDTO{" +
                "account='" + account + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", weight=" + weight +
                ", money=" + money +
                '}';
    }
}
