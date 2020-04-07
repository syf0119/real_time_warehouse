package cn.syf.bean;

 public class DimGoodsDBEntity {
    private String    goodsId;
    private String    goodsName;
    private String    shopId;
    private String    goodsCatId;


     public String getGoodsId() {
         return goodsId;
     }

     public void setGoodsId(String goodsId) {
         this.goodsId = goodsId;
     }

     public String getGoodsName() {
         return goodsName;
     }

     public void setGoodsName(String goodsName) {
         this.goodsName = goodsName;
     }

     public String getShopId() {
         return shopId;
     }

     public void setShopId(String shopId) {
         this.shopId = shopId;
     }

     public String getGoodsCatId() {
         return goodsCatId;
     }

     public void setGoodsCatId(String goodsCatId) {
         this.goodsCatId = goodsCatId;
     }

     @Override
     public String toString() {
         return "DimGoodsDBEntity{" +
                 "goodsId='" + goodsId + '\'' +
                 ", goodsName='" + goodsName + '\'' +
                 ", shopId='" + shopId + '\'' +
                 ", goodsCatId='" + goodsCatId + '\'' +
                 '}';
     }
 }
