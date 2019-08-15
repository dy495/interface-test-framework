package com.haisheng.framework.testng.custemorGateTest;

import lombok.Data;

@Data
public class PersonProp {
    String customerId, customerType, groupName;
    boolean isMale;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PersonProp){
            PersonProp per = (PersonProp) obj;
            return per.getCustomerId().equals(this.getCustomerId())
                    &&per.getCustomerType().equals(this.getCustomerType())
                    &&per.isMale==this.isMale;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getCustomerId().hashCode()
                + this.getCustomerType().hashCode();
    }
}
