package com.alibaba.service;

import com.alibaba.entity.ItemEntity;
import com.alibaba.entity.MemberEntity;

/**
 * 折扣服务
 */
public interface DisCountService {

    double calcPrice(ItemEntity itemEntity, MemberEntity memberEntity);
}
