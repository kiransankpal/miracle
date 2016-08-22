package view.bean.backing;

import java.util.Date;

import java.text.SimpleDateFormat;

public class Main {

  public static void main(String[] args) {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

    String strDate = sdf.format(date);
    System.out.println("formatted date in mm/dd/yy : " + strDate);

    
    sdf = new SimpleDateFormat("dd/MM/yy");
    strDate = sdf.format(date);
    System.out.println("formatted date in dd/MM/yyyy : " + strDate);

  }
}