package com.mmall.java.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 使用List.subList方法得到的子序列其实只是将指针指向了原list，并设置了这个sub list的大小。
 * 使用Arrays.asList(数组的引用)这种方式生成链表时，该链表仍然指向这个数据，因此，对这个链表中数据顺序的改变会影响原数组中元素的顺序。
 * @author Qinyunchan
 * @date Created in 下午4:07 2018/11/13
 * @Modified By
 */

public class SubListAndArraysAsListDemo {


    /**
     * 可变长参数的测试
     * @param args
     */
     public static void test(String... args){
        System.out.println(args.getClass());
        for (String arg : args){
            System.out.println(arg);
        }
     }

    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3, 4};
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, ints);  //list与ints是两个不同的内存区域
        System.out.println(list);  //list: [1, 2, 3, 4]
        System.out.println(Arrays.toString(ints)); //ints: [1, 2, 3, 4]

        List<Integer> sub = list.subList(1,3);
        System.out.println("sub before reverse : " + sub); //sub before reverse : [2, 3]
        Collections.reverse(sub);
        System.out.println("sub after reverse : " + sub);  //sub after reverse : [3, 2]
        System.out.println("list : " + list);  //会影响list：[1, 3, 2, 4]
        System.out.println(Arrays.toString(ints));//不会影响ints: [1, 2, 3, 4]

        //此时的list中的值为：[1, 3, 2, 4]
        //此时的ints中的值为：[1, 2, 3, 4]
        List<Integer> sub2 = Arrays.asList(ints);   //sub2 与 ints指向的是同一块内存
        System.out.println("sub2 before reverse : " + sub2); //sub2 before reverse : [1, 2, 3, 4]
        Collections.reverse(sub2);  //
        System.out.println("sub2 after reverse : " + sub2);  //sub2 after reverse : [4, 3, 2, 1]
        System.out.println("list : " + list);  //list : [1, 3, 2, 4]
        System.out.println(Arrays.toString(ints));  //[4, 3, 2, 1]

        //此时的list中的值为：[1, 3, 2, 4]
        //此时的ints中的值为：[4, 3, 2, 1]
        List<Integer> sub3 = Arrays.asList(list.get(1), list.get(2));  //sub3不会影响list
        System.out.println("sub3 before reverse : " + sub3); //sub3 before reverse : [3, 2]
        Collections.reverse(sub3);  //
        System.out.println("sub3 after reverse : " + sub3);  //sub3 after reverse : [2, 3]
        System.out.println("list : " + list);  //list : [1, 3, 2, 4]
        System.out.println(Arrays.toString(ints));  //[4, 3, 2, 1]

        //此时的list中的值为：[1, 3, 2, 4]
        //此时的ints中的值为：[4, 3, 2, 1]
        List<Integer> sub4 = new ArrayList<Integer>(list.subList(1,3)); //sub4不会影响list
        System.out.println("sub4 before reverse : " + sub4); //sub4 before reverse : [3, 2]
        Collections.reverse(sub4);  //
        System.out.println("sub4 after reverse : " + sub4);  //sub4 after reverse : [2, 3]
        System.out.println("list : " + list);  //list : [1, 3, 2, 4]
        System.out.println(Arrays.toString(ints));  //[4, 3, 2, 1]

        /**
         * 以上测试，证明：
         * 如果想要减少内存中数量级很大的List的大小,不要使用SubList，SubList会一直引用原来的List,导致其无法被GC回收，容易造成OOM(内存溢出)
         * Arrays.asList 同理
         */

        /**
         * 关于Arrays.asList的一些补充
         */
        List<Integer> sub5 = Arrays.asList(ints[0], ints[1], ints[2], ints[3]);  //sub3不会影响list
        System.out.println("sub5 before reverse : " + sub5); //sub5 before reverse : [4, 3, 2, 1]
        Collections.reverse(sub5);  //
        System.out.println("sub5 after reverse : " + sub5);  //sub5 after reverse : [1, 2, 3, 4]
        System.out.println("list : " + list);  //list : [1, 3, 2, 4]
        System.out.println(Arrays.toString(ints));  //[4, 3, 2, 1]

        /**
         * 观察两个test()方法的字节码
         */
        SubListAndArraysAsListDemo.test("1","2");
        String[] ss = {"1","2"};
        SubListAndArraysAsListDemo.test(ss);
        /**
         * 通过字节码可知，可变长参数在传入每个一个参数时会new Array()，变量引用内存各不相同，所以不影响原有Array
         * 但是如果可变长参数传入一个数组，那么内存还是和原有Array共用，此时会影响：
         *  看字节码指令654行: aload 8 可知: 从局部变量8中装载引用类型值入栈
         * (ps: 如果写成 SubListAndArraysAsListDemo.test(new String[]{"1","2"}) 则效果和 90行相同，主要还是看是否 new 了新的内存)
         */
    }
}
