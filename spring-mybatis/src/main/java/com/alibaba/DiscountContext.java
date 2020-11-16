package com.alibaba;

import com.alibaba.entity.ItemEntity;
import com.alibaba.entity.MemberEntity;
import com.alibaba.service.DisCountService;
import com.alibaba.service.impl.A_CompanyServiceImpl;
import com.alibaba.service.impl.B_CompanyServiceImpl;
import com.alibaba.service.impl.C_CompanyServiceImpl;

/**
 * 策略类
 */
public class DiscountContext {

    private DisCountService disCountService;

    public void setDisCountService(DisCountService disCountService) {
        this.disCountService = disCountService;
    }

    public double calcPrice(ItemEntity item, MemberEntity member){
        switch (member.getCompany()) {
            case A:
                disCountService = new A_CompanyServiceImpl();
                break;
            case B:
                disCountService = new B_CompanyServiceImpl();
                break;
            case C:
                disCountService = new C_CompanyServiceImpl();
                break;
            default:
                throw new IllegalArgumentException("会员所属公司信息错误");
        }
        return disCountService.calcPrice(item, member);
    }
}
