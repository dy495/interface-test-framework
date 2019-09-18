package com.haisheng.framework.testng.operationcenter.shelf;

import lombok.Data;

@Data
public class MySensor {
    String unitCode;
    String type;

    public MySensor(String unitCode, String type) {
        this.unitCode = unitCode;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MySensor){
            MySensor mySensor = (MySensor)obj;
            return mySensor.getType().equals(this.getType())
                    && mySensor.getUnitCode().equals(this.getUnitCode());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getUnitCode().hashCode()
                + this.getType().hashCode();
    }

}
