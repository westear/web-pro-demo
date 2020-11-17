package com.alibaba.enums;

public enum A_MemberEnum {
    SUPER_VIP(MemberLevelEnum.A_LEVEL, 0.7),
    VIP(MemberLevelEnum.B_LEVEL, 0.9),
    REGULAR(MemberLevelEnum.REGULAR, 1.0)
    ;

    private MemberLevelEnum memberLevel;
    private double discount;

    A_MemberEnum(MemberLevelEnum memberLevel, double discount) {
        this.memberLevel = memberLevel;
        this.discount = discount;
    }

    public static double getDiscount(MemberLevelEnum memberLevel) {
        for (A_MemberEnum iter : A_MemberEnum.values()) {
            if(iter.memberLevel.equals(memberLevel)) {
                return iter.discount;
            }
        }
        return 0;
    }
}
