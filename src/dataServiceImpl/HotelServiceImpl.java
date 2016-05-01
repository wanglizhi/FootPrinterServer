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

import bean.EntertainmentPO;
import bean.HotelPO;
import bean.PlacePO;
import dataService.PlaceDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class HotelServiceImpl extends UnicastRemoteObject implements PlaceDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;
	
	
	public HotelServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public HotelPO searchByID(int id) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from hotel where id = '" + id + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createHotelPO(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlacePO> searchByCityID(int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from hotel where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> hotelList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				hotelList.add(createHotelPO(result));
			}
			return hotelList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addApplause(int userID, int hotelID) throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null|| searchByID(hotelID)==null){
			return false;
		}
		if(checkApplause(userID, hotelID)){
			return false;
		}
		cmd="insert into applause_hotel(user_id,hotel_id)values('"+
				userID+"','"+hotelID+"')";
		return database.executeUpdate(cmd)&& ImplHelper.addApplauseNum("hotel", hotelID);
	}

	@Override
	public boolean deleteApplause(int userID, int hotelID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(!checkApplause(userID, hotelID)){
			return false;
		}
		cmd="delete from applause_hotel where user_id='"+userID+"' and hotel_id='"+hotelID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("hotel", hotelID);
	}

	@Override
	public boolean checkApplause(int userID, int hotelID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_hotel where user_id='"+userID+"' and hotel_id='"+hotelID+"'";
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
	
	public HotelPO createHotelPO(ResultSet result){
		try {
			String hotelName=result.getString("hotel_name");
			URL image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/hotel/"+ImplHelper.translate(hotelName)+".jpg");
			return new HotelPO(result.getInt("id"),
					result.getInt("city_id"),
					result.getString("hotel_name"),
					result.getString("intro"),
					image,
					result.getString("hotel_type"),
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
		cmd="select * from hotel where hotel_name like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> hotelList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				if(hotelList.size()>=size) break;
				hotelList.add(createHotelPO(result));
			}
			return hotelList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
