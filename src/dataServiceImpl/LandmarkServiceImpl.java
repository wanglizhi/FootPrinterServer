package dataServiceImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import startUp.DatabaseHelper;
import startUp.DatabaseInit;
import bean.LandmarkPO;
import bean.PlacePO;
import dataService.PlaceDataService;

public class LandmarkServiceImpl extends UnicastRemoteObject implements
		PlaceDataService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public LandmarkServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database = DatabaseInit.getDatabaseHelper();
	}

	@Override
	public LandmarkPO searchByID(int landmarkID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from landmark where id = '" + landmarkID + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createLandmarkPO(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public synchronized ArrayList<PlacePO> searchByCityID(int cityID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from landmark where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> landmarkList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				landmarkList.add(createLandmarkPO(result));
			}
			return landmarkList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addApplause(int userID, int landmarkID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(landmarkID)==null){
			return false;
		}
		if(checkApplause(userID, landmarkID)){
			return false;
		}
		cmd="insert into applause_landmark(user_id,landmark_id)values('"+
			userID+"','"+landmarkID+"')";
		return database.executeUpdate(cmd)&&ImplHelper.addApplauseNum("landmark", landmarkID);
	}

	@Override
	public boolean deleteApplause(int userID, int landmarkID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(!checkApplause(userID, landmarkID)){
			return false;
		}
		cmd="delete from applause_landmark where user_id='"+userID+"' and landmark_id='"+landmarkID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("landmark", landmarkID);
	}

	@Override
	public boolean checkApplause(int userID, int landmarkID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_landmark where user_id='"+userID+"' and landmark_id='"+landmarkID+"'";
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

	// 读取PO的操作
	public static LandmarkPO createLandmarkPO(ResultSet result) {
		try {
			String landmarkName=result.getString("landmark_name");
			URL image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/landmark/"+ImplHelper.translate(landmarkName)+".jpg");
			return new LandmarkPO(result.getInt("id"),
					result.getInt("city_id"),
					result.getString("landmark_name"),
					result.getString("introduction"), 
					image,//景点的图片
					result.getString("landmark_type"),
					result.getString("info"),//price
					result.getInt("applause_num"),
					 result.getString("tag"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlacePO> searchByTag(ArrayList<String> tags)
			throws RemoteException {
		// TODO Auto-generated method stub	
		int size=24;
		ArrayList<PlacePO> landmarkList=new ArrayList<PlacePO>();
		try{
			for(String tag:tags){
				cmd="select * from landmark where tag like '%"+tag+"%'";
				ResultSet result=database.executeQuery(cmd);
				while(result.next()){
					if(landmarkList.size()>=size) return landmarkList;;
					landmarkList.add(createLandmarkPO(result));
				}
			}
			
			return landmarkList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlacePO> search(String key) throws RemoteException {
		// TODO Auto-generated method stub
		int size=24;
		System.out.println("Search Landmark--> key:"+key);
		cmd="select * from landmark where landmark_name like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> landmarkList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				if(landmarkList.size()>=size) break;
				landmarkList.add(createLandmarkPO(result));
			}
			return landmarkList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
