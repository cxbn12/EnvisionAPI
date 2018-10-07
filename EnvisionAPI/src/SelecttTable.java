package com.envision.api;

import java.sql.*;

public class SelecttTable {

	 public static void main(String[] args){
	        try{
	            //Class.forName()方法加载驱动程序
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("成功加载MySQL驱动！");
	                
	            String url="jdbc:mysql://192.168.1.235:3306/intern_practice_DB";    //JDBC的URL    
	            Connection conn;

	            conn = DriverManager.getConnection(url,"root","E4FlameH");
	            Statement stmt = conn.createStatement(); //Statement对象
	            System.out.println("成功连接到数据库！");

	            String sql = "select * from intern_practice_DB.practice_pyr_SH";    //SQL
	            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
	                System.out.println("record_id"+"\t"+"date"+"\t"+"pyr_id"+"\t"+"sunhours");
	                while (rs.next()){
	                    System.out.print(rs.getInt(1) + "\t");
	                    System.out.print(rs.getString(2) + "\t");
	                    System.out.print(rs.getInt(3) + "\t");
	                    System.out.print(rs.getFloat(4) + "\t");
	                    System.out.println();
	                }
	                rs.close();
	                stmt.close();
	                conn.close();
	            }catch(Exception e)
	            {
	                e.printStackTrace();
	            }
	    }
}
