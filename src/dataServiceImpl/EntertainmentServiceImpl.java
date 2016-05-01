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
import bean.PlacePO;
import dataService.PlaceDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class EntertainmentServiceImpl extends UnicastRemoteObject implements PlaceDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public EntertainmentServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public EntertainmentPO searchByID(int id) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from entertainment where id = '" + id + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createEntertainmentPO(result);
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
		cmd="select * from entertainment where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> entertainmentList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				entertainmentList.add(createEntertainmentPO(result));
			}
			return entertainmentList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addApplause(int userID, int entertainmentID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(entertainmentID)==null){
			return false;
		}
		if(checkApplause(userID, entertainmentID)){
			return false;
		}
		cmd="insert into applause_entertainment(user_id,entertainment_id)values('"+
				userID+"','"+entertainmentID+"')";
		return database.executeUpdate(cmd)&& ImplHelper.addApplauseNum("entertainment", entertainmentID);
	}

	@Override
	public boolean deleteApplause(int userID, int entertainmentID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(!checkApplause(userID, entertainmentID)){
			return false;
		}
		cmd="delete from applause_entertainment where user_id='"+userID+"' and entertainment_id='"+entertainmentID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("entertainment", entertainmentID);
	}

	@Override
	public boolean checkApplause(int userID, int entertainmentID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_entertainment where user_id='"+userID+"' and entertainment_id='"+entertainmentID+"'";
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
	public EntertainmentPO createEntertainmentPO(ResultSet result){
		try {
			String entertainmentName=result.getString("entertainment_name");
			URL image=new URL("http://"+database.getIP()+"/footPrinter/entertainment/"+ImplHelper.translate(entertainmentName)+".jpg");
			return new EntertainmentPO(result.getInt("id"),
					result.getInt("city_id"),
					result.getString("entertainment_name"),
					result.getString("intro"),					
					image,
					result.getString("entertainment_type"),
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
		cmd="select * from entertainment where entertainment_name like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> entertainmentList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				if(entertainmentList.size()>=size) break;
				entertainmentList.add(createEntertainmentPO(result));
			}
			return entertainmentList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
}
