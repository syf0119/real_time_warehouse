package cn.syf.bean;

 public class DimShopsDBEntity {
    private String     shopId;
    private String     areaId;
    private String     shopName;
    private String     shopCompany;


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCompany() {
        return shopCompany;
    }

    public void setShopCompany(String shopCompany) {
        this.shopCompany = shopCompany;
    }

     @Override
     public String toString() {
         return "DimShopsDBEntity{" +
                 "shopId='" + shopId + '\'' +
                 ", areaId='" + areaId + '\'' +
                 ", shopName='" + shopName + '\'' +
                 ", shopCompany='" + shopCompany + '\'' +
                 '}';
     }
 }
