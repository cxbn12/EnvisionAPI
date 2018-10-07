package com.envision.api;

import java.sql.*;

public class UpdateDeleteTable {
	
	   public static void main(String[] args)throws Exception{	   
	        try{
	          
	            Class.forName("com.mysql.jdbc.Driver");
	           // System.out.println("MySQL driver added！");
	                
	            String url="jdbc:mysql://192.168.1.235:3306/intern_practice_DB";  
	            Connection conn;

	            conn = DriverManager.getConnection(url,"root","E4FlameH");
	            Statement stmt = conn.createStatement(); 
	            System.out.println("connected to database！");

	            
	            //String sql = "select * from intern_practice_DB.energy_patched";  
	            /*8  ResultSet rs = stmt.executeQuery(sql);
	                System.out.println("record_id"+"\t"+"date"+"\t"+"meter_id"+"\t"+"energy");
	                while (rs.next()){
	                    System.out.print(rs.getInt(1) + "\t");
	                    System.out.print(rs.getString(2) + "\t");
	                    System.out.print(rs.getInt(3) + "\t");
	                    System.out.print(rs.getFloat(4) + "\t");
	                    System.out.println();
	                }
	                **/
	          
	                
	            String sql1 = "insert into intern_practice_DB.practice_pyr_SH values(?,?,?,?)";
	            PreparedStatement pst = conn.prepareStatement(sql1);
	            pst.setInt(1,998526);
	            pst.setString(2,"2018-09-10");
	            pst.setInt(3, 1062);
	            pst.setFloat(4,200);
	            pst.executeUpdate();
	            System.out.println(pst);
	            
	           /** ResultSet rs = stmt.executeQuery(sql);
               System.out.println("record_id"+"\t"+"date"+"\t"+"meter_id"+"\t"+"energy");
                while (rs.next()){
                    System.out.print(rs.getInt(1) + "\t");
                    System.out.print(rs.getString(2) + "\t");
                    System.out.print(rs.getInt(3) + "\t");
                    System.out.print(rs.getFloat(4) + "\t");
                    System.out.println();
                } **/
	            
	           // rs.close();
	            stmt.close();
	            conn.close();
	            }catch(Exception e)
	            {
	                e.printStackTrace();
	            }
	        
	   }
}

