package com.alibaba.entity;

import com.alibaba.enums.CompanyEnum;
import com.alibaba.enums.MemberLevelEnum;

public class MemberEntity {

    /**
     * 会员类型
     */
    private MemberLevelEnum memberLevel;

    /**
     * 所属公司
     */
    private CompanyEnum company;

    public MemberEntity() {

    }

    public MemberEntity(CompanyEnum company, MemberLevelEnum memberLevel) {
        this.memberLevel = memberLevel;
        this.company = company;
    }


    public MemberLevelEnum getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevelEnum memberLevel) {
        this.memberLevel = memberLevel;
    }

    public CompanyEnum getCompany() {
        return company;
    }

    public void setCompany(CompanyEnum company) {
        this.company = company;
    }
}
