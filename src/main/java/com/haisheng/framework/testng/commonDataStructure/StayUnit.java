package com.haisheng.framework.testng.commonDataStructure;

import lombok.Data;

@Data
public class StayUnit {

    private String regionId;
    private String status;

    public StayUnit() {}
    public StayUnit(String regionId, String status) {
        this.regionId = regionId;
        this.status = status;
    }




    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StayUnit){
            StayUnit unit = (StayUnit) obj;
            return unit.getRegionId().equals(this.getRegionId())
                    && unit.getStatus().equals(this.getStatus());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getRegionId().hashCode()
                + this.getStatus().hashCode();
    }
}
