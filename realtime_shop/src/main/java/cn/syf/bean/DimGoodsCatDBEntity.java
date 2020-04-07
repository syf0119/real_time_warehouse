package cn.syf.bean;

 public class DimGoodsCatDBEntity {
    private String     catId;
    private String     parentId;
    private String     catName;
    private String     cat_level;

     public String getCatId() {
         return catId;
     }

     public void setCatId(String catId) {
         this.catId = catId;
     }

     public String getParentId() {
         return parentId;
     }

     public void setParentId(String parentId) {
         this.parentId = parentId;
     }

     public String getCatName() {
         return catName;
     }

     public void setCatName(String catName) {
         this.catName = catName;
     }

     public String getCat_level() {
         return cat_level;
     }

     public void setCat_level(String cat_level) {
         this.cat_level = cat_level;
     }

     @Override
     public String toString() {
         return "DimGoodsCatDBEntity{" +
                 "catId='" + catId + '\'' +
                 ", parentId='" + parentId + '\'' +
                 ", catName='" + catName + '\'' +
                 ", cat_level='" + cat_level + '\'' +
                 '}';
     }
 }
