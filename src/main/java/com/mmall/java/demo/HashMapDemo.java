package com.mmall.java.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 添加hashMap扩容树化示例，bebug map 采用 view as Object
 * @author Qinyunchan
 * @date Created in 上午11:37 2018/11/12
 * @Modified By
 */

public class HashMapDemo {

    class MapKey{
        private static final String REG = "[0-9]+";

        private String key;

        public MapKey(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj){
                return true;
            }
            if(obj == null || getClass() != obj.getClass()){
                return false;
            }
            HashMapDemo.MapKey mapKey = (HashMapDemo.MapKey) obj;
            return !(
                    key != null
                            ? !key.equals(mapKey.key)
                            : mapKey.key != null
            );
        }

        /**
         * 方法故意将所有数字字符串key的hash值返回1，其他字符串key的hash值返回2。
         * @return
         */
        @Override
        public int hashCode() {
            if (key == null) {
                return 0;
            }
            Pattern pattern = Pattern.compile(REG);
            if (pattern.matcher(key).matches()){
                return 1;
            }
            else{
                return 2;
            }
        }

        @Override
        public String toString() {
            return key;
        }
    }

    public static void main(String[] args) {
        HashMapDemo HashMapDemo = new HashMapDemo();
        Map<HashMapDemo.MapKey,String> map = new HashMap<>();

        /**
         * bucket(桶)的bin个数 < TREEIFY_THRESHOLD = 8
         * bin采用链表方式存储:Node->next->next->......
         */
        //第一阶段
//        for (int i = 0; i < 6; i++) {
//            map.put(HashMapDemo.new MapKey(String.valueOf(i)), "A");
//        }

        /**
         * bucket(桶)的bin个数 > TREEIFY_THRESHOLD = 8 but capacity < MIN_TREEIFY_CAPACITY = 64
         * bin采用链表方式存储:Node->next->next->......
         */
        //第二阶段
//        for (int i = 0; i < 10; i++) {
//            map.put(HashMapDemo.new MapKey(String.valueOf(i)), "A");
//        }


        /**
         * bucket(桶)的bin个数 > TREEIFY_THRESHOLD = 8 && capacity > MIN_TREEIFY_CAPACITY = 64
         * 树化存储，采用红黑树结构
         * map输出无序化
         */
        //第三阶段
        for (int i = 0; i < 50; i++) {
            map.put(HashMapDemo.new MapKey(String.valueOf(i)), "A");
        }

        /**
         * 一个bucket中树化，不影响另一个bucket树化,
         * bucket是否树化只取决于bin个数 > TREEIFY_THRESHOLD && capacity < MIN_TREEIFY_CAPACITY
         */
        //第四阶段
        map.put(HashMapDemo.new MapKey("X"), "B");
        map.put(HashMapDemo.new MapKey("Y"), "B");
        map.put(HashMapDemo.new MapKey("Z"), "B");

        System.out.println(map);
    }
}
