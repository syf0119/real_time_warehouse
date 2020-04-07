package cn.syf.bean;


public class DimAreaDBEntity {
private String   areaId;
private String   parentId;
private String   areaName;
private String   areaType;



    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    @Override
    public String toString() {
        return "DimAreaDBEntity{" +
                "areaId='" + areaId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", areaType='" + areaType + '\'' +
                '}';
    }
}
