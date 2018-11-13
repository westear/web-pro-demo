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
    }
}
