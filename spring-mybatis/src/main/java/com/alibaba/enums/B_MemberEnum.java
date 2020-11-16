package com.alibaba.enums;

public enum B_MemberEnum {
    GOLD(MemberLevelEnum.A_LEVEL, 0.65),
    SILVER(MemberLevelEnum.B_LEVEL, 0.75),
    COPPER(MemberLevelEnum.C_LEVEL, 0.85),
    REGULAR(MemberLevelEnum.REGULAR, 1.0)
    ;

    private MemberLevelEnum memberLevel;
    private double discount;

    B_MemberEnum(MemberLevelEnum memberLevel, double discount) {
        this.memberLevel = memberLevel;
        this.discount = discount;
    }

    public static double getDiscount(MemberLevelEnum memberLevel) {
        for (B_MemberEnum iter : B_MemberEnum.values()) {
            if(iter.memberLevel.equals(memberLevel)) {
                return iter.discount;
            }
        }
        return 0;
    }
}
