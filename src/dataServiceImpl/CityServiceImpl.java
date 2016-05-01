package dataServiceImpl;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import bean.CityPO;
import dataService.CityDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class CityServiceImpl extends UnicastRemoteObject implements CityDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public CityServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public CityPO searchByID(int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from city where id = '" + cityID + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createCityPO(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<CityPO> searchByName(String name) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from city where city_name like '%"+name+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<CityPO> cityList=new ArrayList<CityPO>();
		try {
			while(result.next())
				cityList.add(createCityPO(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cityList;
	}

	@Override
	public boolean addApplause(int userID, int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("city add applause--> userID: "+userID+" cityID: "+cityID);
		//如果没有对应的ID，则不能赞
		if(UserManageServiceImpl.getUserNameByID(userID)==null||CityServiceImpl.getCityNameByID(cityID)==null){
			return false;
		}
		//已经赞过，不能再赞
		if(checkApplause(userID, cityID)){
			return false;
		}
		cmd="insert into applause_city(user_id,city_id)values('"+
				userID+"','"+cityID+"')";
		return database.executeUpdate(cmd)&& ImplHelper.addApplauseNum("city", cityID);
	}

	@Override
	public boolean deleteApplause(int userID, int cityID)
			throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("city delete applause--> userID: "+userID+" cityID: "+cityID);
		//如果没赞过，false
		if(!checkApplause(userID, cityID)){
			return false;
		}
		cmd="delete from applause_city where user_id='"+userID+"' and city_id='"+cityID+"'";
		return database.executeUpdate(cmd) && ImplHelper.deleteApplauseNum("city", cityID);
	}

	@Override
	public boolean checkApplause(int userID, int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_city where user_id='"+userID+"' and city_id='"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next())
				return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	//读取城市的PO，===========图片赋值问题
	public CityPO createCityPO(ResultSet result){
		try {
			int cityID=result.getInt("id");
			return new CityPO(cityID,
					result.getString("city_name"),
					result.getString("introduction"),
					24,//城市的图片数目先存为24，客户端获取数据后再改
					getCityImages(cityID),//城市对应的图片，先赋值为null
					result.getInt("applause_num"),
					result.getString("info"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//根据ID得到name，同时可以判断是否有这个ID
	public static String getCityNameByID(int id){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select city_name from city where id='"+id+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return result.getString("city_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据城市Id得到图片
	 * @param cityID
	 * @return
	 */
	public static ArrayList<URL> getCityImages(int cityID){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select * from landmark where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<URL> landmarkList=new ArrayList<URL>();
		try{
			while(result.next()){
				String landmarkName=result.getString("landmark_name");
				URL image=new URL("http://"+database.getIP()+"/footPrinter/landmark/"+ImplHelper.translate(landmarkName)+".jpg");
				landmarkList.add(image);
				//System.out.println(image.toString());
			}
			return landmarkList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return landmarkList;
	}

}
