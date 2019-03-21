package com.haisheng.framework.testng.CommonDataStructure;


import lombok.Data;

@Data
public class MapRegionEntranceUnit {

    private String mapId;
    private String regionId;
    private String entranceId;
    private String status;

    public MapRegionEntranceUnit() {}
    public MapRegionEntranceUnit(String mapId, String regionId, String entranceId, String status) {
        this.mapId = mapId;
        this.regionId = regionId;
        this.entranceId = entranceId;
        this.status = status;
    }




    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MapRegionEntranceUnit){
            MapRegionEntranceUnit unit = (MapRegionEntranceUnit) obj;
            return unit.getMapId().equals(this.getMapId())
                    && unit.getEntranceId().equals(this.getEntranceId())
                    && unit.getRegionId().equals(this.getRegionId())
                    && unit.getStatus().equals(this.getStatus());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getMapId().hashCode()
                + this.getRegionId().hashCode()
                + this.getEntranceId().hashCode()
                + this.getStatus().hashCode();
    }
}
