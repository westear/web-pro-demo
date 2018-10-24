package com.mmall.common;

/**
 * @author Qinyunchan
 * @date Created in 下午10:23 2018/6/25
 * @Modified By
 */

public class CommonDemo {

    public Long id;
    public String name;
    public Integer age;
    public Boolean marray;
    public Double weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getMarray() {
        return marray;
    }

    public void setMarray(Boolean marray) {
        this.marray = marray;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public CommonDemo(){

    }

    public CommonDemo(Long id, String name, Integer age, Boolean marry, Double weight) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.marray = marry;
        this.weight = weight;
    }
}
