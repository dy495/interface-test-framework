package com.haisheng.framework.testng.commonDataStructure;


import lombok.Data;

@Data
public class Axis {
    private String startX;
    private String startY;

    public Axis(String startX, String startY) {
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Axis){
            Axis unit = (Axis) obj;
            return unit.getStartX().equals(this.getStartX())
                    && unit.getStartY().equals(this.getStartY());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getStartX().hashCode()
                + this.getStartY().hashCode();
    }
}
