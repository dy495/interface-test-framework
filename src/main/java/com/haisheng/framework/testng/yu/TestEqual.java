package com.haisheng.framework.testng.yu;

import lombok.Data;

@Data
public class TestEqual {

    private String id;
    private String name;
    private String distrinctCode;
    private String distrinctName;
    private String mgrName;
    private String mgrPhone;


    public boolean equal(TestEqual obj) {
        if ( this.id.equals(obj.id) &&
                this.name.equals(obj.name) &&
                this.distrinctCode.equals(obj.distrinctCode) &&
                this.distrinctName.equals(obj.distrinctName) &&
                this.mgrName.equals(obj.mgrName) &&
                this.mgrPhone.equals(obj.mgrPhone)
        ) {
            return true;
        } else {
            return false;
        }
    }
}
