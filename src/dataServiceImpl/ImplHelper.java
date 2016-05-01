package dataServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

public class ImplHelper {

	public static boolean addApplauseNum(String tableName,int id){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select applause_num from "+tableName+" where id='"+id+"'";
		ResultSet result=database.executeQuery(cmd);
		int applauseNum=0;
		try {
			if(result.next()){
				applauseNum=result.getInt("applause_num");
			}
			applauseNum++;
			cmd="update "+tableName+" set applause_num='"+applauseNum+"' where id='"+id+"'";
			return database.executeUpdate(cmd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteApplauseNum(String tableName,int id){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select applause_num from "+tableName+" where id='"+id+"'";
		ResultSet result=database.executeQuery(cmd);
		int applauseNum=0;
		try {
			if(result.next()){
				applauseNum=result.getInt("applause_num");
			}
			if(applauseNum<=0) return false;
			else{
				applauseNum--;
				cmd="update "+tableName+" set applause_num='"+applauseNum+"' where id='"+id+"'";
				return database.executeUpdate(cmd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getNameByID(int id,String tableName){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select "+tableName+"_name from "+tableName+" where id='"+id+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return result.getString(tableName+"_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getCityIDByPlaceID(int placeID,String tableName){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select city_id from "+tableName+" where id='"+placeID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return result.getInt("city_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String objectToString(Serializable obj) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String serStr = null;
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
			objectOutputStream.close();
			byteArrayOutputStream.close();
			System.out.println(serStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serStr;
	}

	public static Object stringToObject(String s) {
		Object o=null;
		try {
			String redStr = java.net.URLDecoder.decode(s, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);

			o = objectInputStream.readObject();

			objectInputStream.close();
			byteArrayInputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	
	public static  boolean isConnect(URL url) {   
            HttpURLConnection con=null;
            int state=-1;
            try {  
                con = (HttpURLConnection) url.openConnection();  
                state = con.getResponseCode();  
                if (state == 200) {  
                    System.out.println("ok");
                    return true;
                }
            }catch (Exception ex) {  
        }  
        return false;  
    }  
	
	//×Ö·û±àÂë
	public static String translate(String str) {
		byte[] bytes  = null;
		try {
			bytes = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = "";
		for(byte b : bytes){
			s += ("%" + Integer.toHexString(b & 0xFF));
		}
	return s;
}
}
