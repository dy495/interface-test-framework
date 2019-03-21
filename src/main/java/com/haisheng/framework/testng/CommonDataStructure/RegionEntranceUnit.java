package com.haisheng.framework.testng.CommonDataStructure;


import lombok.Data;

@Data
public class RegionEntranceUnit {

    private String regionId;
    private String entranceId;
    private String status;

    public RegionEntranceUnit() {}
    public RegionEntranceUnit(String regionId, String entranceId, String status) {
        this.regionId = regionId;
        this.entranceId = entranceId;
        this.status = status;
    }




    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RegionEntranceUnit){
            RegionEntranceUnit unit = (RegionEntranceUnit) obj;
            return unit.getEntranceId().equals(this.getEntranceId())
                    && unit.getRegionId().equals(this.getRegionId())
                    && unit.getStatus().equals(this.getStatus());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getRegionId().hashCode()
                + this.getEntranceId().hashCode()
                + this.getStatus().hashCode();
    }
}
