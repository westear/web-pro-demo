package com.alibaba.enums;

public enum C_MemberEnum {

    C_ONE(MemberLevelEnum.A_LEVEL, 0.8),
    REGULAR(MemberLevelEnum.REGULAR, 1.0)
    ;

    private MemberLevelEnum memberLevel;
    private double discount;

    C_MemberEnum(MemberLevelEnum memberLevel, double discount) {
        this.memberLevel = memberLevel;
        this.discount = discount;
    }

    public static double getDiscount(MemberLevelEnum memberLevel) {
        for (C_MemberEnum iter : C_MemberEnum.values()) {
            if(iter.memberLevel.equals(memberLevel)) {
                return iter.discount;
            }
        }
        return 0;
    }
}
