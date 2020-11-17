package com.alibaba;

import com.alibaba.entity.ItemEntity;
import com.alibaba.entity.MemberEntity;
import com.alibaba.enums.CompanyEnum;
import com.alibaba.enums.MemberLevelEnum;

public class MainTest {

    public static void main(String[] args) {
        ItemEntity item1 = new ItemEntity(1L, "AA", "aa", 1000L);
        ItemEntity item2 = new ItemEntity(2L, "BB", "bb", 50000L);

        MemberEntity member_a_super_vip = new MemberEntity(CompanyEnum.A, MemberLevelEnum.A_LEVEL);
        MemberEntity member_b_silver = new MemberEntity(CompanyEnum.B, MemberLevelEnum.B_LEVEL);
        MemberEntity member_c_regular = new MemberEntity(CompanyEnum.C, MemberLevelEnum.C_LEVEL);

        DiscountContext context = new DiscountContext();

        System.out.println(context.calcPrice(item1, member_a_super_vip));
        System.out.println(context.calcPrice(item2, member_b_silver));
        System.out.println(context.calcPrice(item2, member_c_regular));
    }
}
