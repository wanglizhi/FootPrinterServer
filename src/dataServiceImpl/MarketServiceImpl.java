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
import bean.MarketPO;
import bean.PlacePO;
import dataService.PlaceDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class MarketServiceImpl extends UnicastRemoteObject implements PlaceDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public MarketServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public MarketPO searchByID(int id) throws RemoteException {
		// TODO Auto-generated method stub
		cmd = "select * from market where id = '" + id + "'";
		ResultSet result = database.executeQuery(cmd);
		try {
			while(result.next())
				return createMarketPO(result);
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
		cmd="select * from market where city_id= '"+cityID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> marketList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				marketList.add(createMarketPO(result));
			}
			return marketList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addApplause(int userID, int marketID) throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(marketID)==null){
			return false;
		}
		if(checkApplause(userID, marketID)){
			return false;
		}
		cmd="insert into applause_market(user_id,market_id)values('"+
				userID+"','"+marketID+"')";
		return database.executeUpdate(cmd)&& ImplHelper.addApplauseNum("market", marketID);
	}

	@Override
	public boolean deleteApplause(int userID, int marketID)
			throws RemoteException {
		// TODO Auto-generated method stub
		if(!checkApplause(userID, marketID)){
			return false;
		}
		cmd="delete from applause_market where user_id='"+userID+"' and market_id='"+marketID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("market", marketID);
	}

	@Override
	public boolean checkApplause(int userID, int marketID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_market where user_id='"+userID+"' and market_id='"+marketID+"'";
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
	public MarketPO createMarketPO(ResultSet result){
		try {
			String marketName=result.getString("market_name");
			URL image=new URL("http://"+database.getIP()+"/footPrinter/market/"+ImplHelper.translate(marketName)+".jpg");
			return new MarketPO(result.getInt("id"),
					result.getInt("city_id"),
					result.getString("market_name"),
					result.getString("intro"),
					image,
					result.getString("market_type"),
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
		cmd="select * from market where market_name like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlacePO> marketList=new ArrayList<PlacePO>();
		try{
			while(result.next()){
				if(marketList.size()>=size) break;
				marketList.add(createMarketPO(result));
			}
			return marketList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
