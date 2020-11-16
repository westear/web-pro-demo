package com.alibaba.service.impl;

import com.alibaba.entity.ItemEntity;
import com.alibaba.entity.MemberEntity;
import com.alibaba.enums.A_MemberEnum;
import com.alibaba.enums.B_MemberEnum;
import com.alibaba.enums.CompanyEnum;
import com.alibaba.service.DisCountService;

import java.util.Objects;

public class B_CompanyServiceImpl implements DisCountService {

    @Override
    public double calcPrice(ItemEntity itemEntity, MemberEntity memberEntity) {
        if(Objects.isNull(itemEntity.getPrice())) {
            return 0L;
        }
        if(!CompanyEnum.B.equals(memberEntity.getCompany())) {
            throw new IllegalArgumentException("会员所属公司信息错误");
        }
        if(Objects.isNull(memberEntity.getCompany()) || Objects.isNull(memberEntity.getMemberLevel())) {
            throw new IllegalArgumentException("信息不完整");
        }
        if(A_MemberEnum.getDiscount(memberEntity.getMemberLevel()) == 0) {
            throw new IllegalArgumentException("会员信息中不找不到对应会员等级");
        }
        return itemEntity.getPrice() * B_MemberEnum.getDiscount(memberEntity.getMemberLevel());
    }
}
