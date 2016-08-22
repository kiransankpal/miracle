package model.pojo;

public class ViewObjAttribute {
    public ViewObjAttribute() {
        super();
    }
    
    private String columnName;
     private String columnAliasName;


     public ViewObjAttribute(String columnName, String columnAliasName) {
      super();
      this.columnName = columnName;
      this.columnAliasName = columnAliasName;
     }

     public void setColumnName(String columnName) {
      this.columnName = columnName;
     }

     public String getColumnName() {
      return columnName;
     }

     public void setColumnAliasName(String columnAliasName) {
      this.columnAliasName = columnAliasName;
     }

     public String getColumnAliasName() {
      return columnAliasName;
     }
}