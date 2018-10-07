package com.envision.api;

import java.sql.*;


public class GetConnection {

	  public static void main(String[] args){
	        try{
	            //Class.forName()方法加载驱动程序
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("成功加载MySQL驱动！");
	        }catch(ClassNotFoundException e1){
	            System.out.println("找不到MySQL驱动!");
	            e1.printStackTrace();
	        }
	        
	        String url="jdbc:mysql://192.168.1.235:3306/mysql";    //JDBC的URL    
	        //DriverManager对象的getConnection()方法，获得一个Connection对象
	        Connection conn;
	        try {
	            conn = DriverManager.getConnection(url,"root","E4FlameH");
	            //Statement对象
	            Statement stmt = conn.createStatement(); //Statement对象
	            System.out.print("成功连接到数据库！");
	            
	            stmt.close();
	            conn.close();
	        } catch (SQLException e){
	            e.printStackTrace();
	        }
	    }
}
