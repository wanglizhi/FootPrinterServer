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
import bean.PlacePO;
import bean.RestaurantPO;
import dataService.PlaceDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class RestaurantServiceImpl extends UnicastRemoteObject implements PlaceDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;
	
	public RestaurantServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public RestaurantPO searchByID(int id) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from restaurant where id = '" + id + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createRestaurantPO(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlacePO> searchByCityID(int cityID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from restaurant where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> restaurantList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				restaurantList.add(createRestaurantPO(result));
			}
			return restaurantList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addApplause(int userID, int restaurantID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(restaurantID)==null){
			return false;
		}
		if(checkApplause(userID, restaurantID)){
			return false;
		}
		cmd="insert into applause_restaurant(user_id,restaurant_id)values('"+
				userID+"','"+restaurantID+"')";
		return database.executeUpdate(cmd)&&ImplHelper.addApplauseNum("restaurant", restaurantID);
	}

	@Override
	public boolean deleteApplause(int userID, int restaurantID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(!checkApplause(userID, restaurantID)){
			return false;
		}
		cmd="delete from applause_restaurant where user_id='"+userID+"' and restaurant_id='"+restaurantID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("restaurant", restaurantID);
	}

	@Override
	public boolean checkApplause(int userID, int restaurantID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_restaurant where user_id='"+userID+"' and restaurant_id='"+restaurantID+"'";
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
	public RestaurantPO createRestaurantPO(ResultSet result){
		try {
			String restaurantName=result.getString("restaurant_name");
			URL image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/restaurant/"+ImplHelper.translate(restaurantName)+".jpg");
			return new RestaurantPO(result.getInt("id"),
					result.getInt("city_id"),
					result.getString("restaurant_name"),
					result.getString("intro"),
					image,
					result.getString("restaurant_type"),
					result.getString("price"),
					result.getInt("applause_num"));
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
		return null;
	}

	@Override
	public ArrayList<PlacePO> search(String key) throws RemoteException {
		// TODO Auto-generated method stub
		int size=24;
		cmd="select * from restaurant where restaurant_name like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> restaurantList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				if(restaurantList.size()>=size) break;
				restaurantList.add(createRestaurantPO(result));
			}
			return restaurantList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
