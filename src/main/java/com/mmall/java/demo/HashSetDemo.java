package com.mmall.java.demo;

import java.util.*;

/**
 * @author Qinyunchan
 * @date Created in 下午2:49 2018/11/13
 * @Modified By
 */

public class HashSetDemo {


    class Person implements Comparable<Person> {

        private String name;

        private Integer age;

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

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            /*
            if (o == null || getClass() != o.getClass())
                return false;
            Person person = (Person) o;
            return Objects.equals(name, person.name);
            */
            /** 《Think in java》override equals **/
            return
                    o instanceof Person
                    && (name.equals(((Person) o).name));
        }

        /**
         * 如果 不覆盖重写 key 的 hashCode值，无法实现HashSet的不重复性
         * 两个不相等(equals方法返回false)的对象，产生的hashCode**不必必须** 相同
         * @return hashCode
         */
        @Override
        public int hashCode() {

            return Objects.hash(name);
        }

        @Override
        public int compareTo(Person o) {
            if (this == o)
                return 0;
            if (o == null)
                return 0;
            Person person = o;
            return name.compareTo(person.name);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age + '\'' +
                    ", hashCode=" + this.hashCode() +
                    '}';
        }
    }

    public static void main(String[] args) {
        HashSetDemo hashSetDemo = new HashSetDemo();
        Set<Person> personSet = new HashSet<>();
        Person p1 = hashSetDemo.new Person("Ada",22);
        Person p2 = hashSetDemo.new Person("Bob",24);
        Person p3 = hashSetDemo.new Person("Cris",23);
        Person p4 = hashSetDemo.new Person("Ada",21);
        //Person p5 = null;//加到TreeSet中会报错，NPE（NullPointerException），因为往里放时会调用compareTo方法
        Collections.addAll(personSet,p1,p2,p3,p4);

        System.out.println(personSet);

        System.out.println("====重写equals 和 hashCode，检验HashSet======");
        Set<Integer> set = new TreeSet<>();
        Collections.addAll(set, 1,2,1);
        System.out.println("Integer set :" + set);

        System.out.println("====实现CompareTo接口，使用TreeSet进行检验=====");
        Set<String> stringSet = new TreeSet<>();
        Collections.addAll(stringSet, "12 034".split(" "));
        System.out.println("String set : " + stringSet);

        System.out.println("====LinkedHashSet:按照插入顺序输出=====");
        Set<Person> personLinkedHashSet = new LinkedHashSet<Person>();
        Collections.addAll(personLinkedHashSet, p1,p2,p3,p4);
        System.out.println("personLinkedHashSet : " + personLinkedHashSet);

        System.out.println("====实现CompareTo接口，使用TreeSet进行检验 String ,按对比字段 name 的字母顺序输出=====");
        Set<Person> personTreeSet = new TreeSet<>();
        Collections.addAll(personTreeSet, p2, p1, p4, p3);
        System.out.println("personTreeSet : " + personTreeSet);
    }
}
