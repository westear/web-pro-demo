package com.mmall.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否需要从excel数据中赋值的实体类属性
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface IsNeed {

    /**
     * 是否需要从解析excel赋值
     * @return boolean
     */
    boolean isNeeded() default true;

    /**
     * 导出列顺序
     * @return int
     */
    int sort() default 0;
}
