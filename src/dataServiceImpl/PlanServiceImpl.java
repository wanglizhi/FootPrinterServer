package dataServiceImpl;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import bean.PlanItemPO;
import bean.PlanPO;

import dataService.PlanDataService;

public class PlanServiceImpl extends UnicastRemoteObject implements PlanDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public PlanServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public int add(PlanPO planPO) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Add plan--> plan: "+planPO.getTitle());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="insert into plan (user_id,city_id,start_date,end_date,title)values('"+
		planPO.getUserID()+"','"+
		planPO.getCityID()+"','"+
		sdf.format(planPO.getStartDate())+"','"+
		sdf.format(planPO.getEndDate())+"','"+
		planPO.getTitle()+"')";
		database.executeUpdate(cmd);
		return database.getGeneratedKeys(1);
	}

	@Override
	public boolean delete(int planID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="delete from plan where id='"+planID+"'";
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean modify(PlanPO planPO) throws RemoteException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="update plan set user_id='"+planPO.getUserID()+
				"',city_id='"+planPO.getCityID()+
				"',start_date='"+sdf.format(planPO.getStartDate())+
				"',end_date='"+sdf.format(planPO.getEndDate())+
				"',title='"+planPO.getTitle()+
				"',applause_num='"+planPO.getApplauseNum()+
				"' where id='"+planPO.getPlanID()+"'";
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean addApplause(int userID, int planID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("plan add applause--> userID: "+userID+" planID: "+planID);
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(planID)==null){
			return false;
		}
		if(checkApplause(userID, planID)){
			return false;
		}
		cmd="insert into applause_plan(user_id,plan_id)values('"+
				userID+"','"+planID+"')";
		return database.executeUpdate(cmd) && ImplHelper.addApplauseNum("plan", planID);
	}

	@Override
	public boolean deleteApplause(int userID, int planID)
			throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("plan delete applause--> userID: "+userID+" planID: "+planID);
		if(!checkApplause(userID, planID)){
			return false;
		}
		cmd="delete from applause_plan where user_id='"+userID+"' and plan_id='"+planID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("plan", planID);
	}

	@Override
	public boolean checkApplause(int userID, int planID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_plan where user_id='"+userID+"' and plan_id='"+planID+"'";
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

	@Override
	public boolean addPlanItem(PlanItemPO planItemPO) throws RemoteException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="insert into plan_item (plan_id,content_id,content_type,start_time," +
				"end_time,plan_item_date,indexX,indexY,isUsed,remark) values ('"+
				planItemPO.getPlanID()+"','"+
				planItemPO.getLanmarkID()+"','"+
				planItemPO.getLandmarkType()+"','"+
				planItemPO.getStartTime()+"','"+
				planItemPO.getEndTime()+"','"+
				sdf.format(planItemPO.getDate())+"','"+
				planItemPO.getIndexX()+"','"+
				planItemPO.getIndexY()+"',"+
				planItemPO.isUsed()+",'"+
				planItemPO.getComment()+"')";
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean deletePlanItem(int planItemID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="delete from plan_item where id="+planItemID;
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean modifyPlanItem(PlanItemPO planItemPO) throws RemoteException {
		// TODO Auto-generated method stub
//		System.out.println("ModifyPlanItem--> "+planItemPO.getPlanItemID());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="update plan_item set plan_id='"+planItemPO.getPlanID()+
				"',content_id='"+planItemPO.getLanmarkID()+
				"',content_type='"+planItemPO.getLandmarkType()+
				"',start_time='"+planItemPO.getStartTime()+
				"',end_time='"+planItemPO.getEndTime()+
				"',plan_item_date='"+sdf.format(planItemPO.getDate())+
				"',indexX='"+planItemPO.getIndexX()+
				"',indexY='"+planItemPO.getIndexY()+
				"',isUsed="+planItemPO.isUsed()+
				",remark='"+planItemPO.getComment()+
				"' where id='"+planItemPO.getPlanItemID()+"'";
		return database.executeUpdate(cmd);
	}

	@Override
	public ArrayList<PlanItemPO> getPlanItems(int planID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from plan_item where plan_id='"+planID+"'";
		ArrayList<PlanItemPO> planItemList=new ArrayList<PlanItemPO>();
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				planItemList.add(createPlanItemPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planItemList;
	}

	@Override
	public PlanPO searchByID(int planID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from plan where id='"+planID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				return createPlanPO(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlanPO> searchByCity(int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		int size=10;
		cmd="select * from plan where city_id='"+cityID+"' order by applause_num";
		ArrayList<PlanPO> planList=new ArrayList<PlanPO>();
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				if(planList.size()>=size)break;
				planList.add(createPlanPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planList;
	}

	/**
	 * 起止日期？
	 */
	@Override
	public ArrayList<PlanPO> searchByDate(Date date) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	//按关键字查找？
	@Override
	public ArrayList<PlanPO> search(String key) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from plan where title like'%"+key+"%'";
		ArrayList<PlanPO> planList=new ArrayList<PlanPO>();
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				planList.add(createPlanPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planList;
	}
	public static PlanPO createPlanPO(ResultSet result){
		
		try {
			int cityID=result.getInt("city_id");
			ArrayList<URL> cityImageList=CityServiceImpl.getCityImages(cityID);
			URL image=null;
			if(!cityImageList.isEmpty()){
				image=cityImageList.get(0);
			}
			int userID=result.getInt("user_id");
			return new PlanPO(result.getInt("id"),
					userID,
					cityID,
					result.getInt("applause_num"),
					result.getDate("start_date"),
					result.getDate("end_date"),
					result.getString("title"),
					image,
					CityServiceImpl.getCityNameByID(cityID),//cityName
					UserManageServiceImpl.getUserNameByID(userID) //userName
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public PlanItemPO createPlanItemPO(ResultSet result){
		try {
			int contentID=result.getInt("content_id");
			int type=result.getInt("content_type");
			String name=null;
			URL image=null;
			try{
			switch(type){
			case 0:
				name=ImplHelper.getNameByID(contentID, "landmark");
				if(name==null) name=" ";
				image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/landmark/"+ImplHelper.translate(name)+".jpg");
				break;
			case 1:
				name=ImplHelper.getNameByID(contentID, "hotel");
				image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/hotel/"+ImplHelper.translate(name)+".jpg");
				break;
			case 2:
				name=ImplHelper.getNameByID(contentID, "restaurant");
				image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/restaurant/"+ImplHelper.translate(name)+".jpg");
				break;
			case 3:
				name=ImplHelper.getNameByID(contentID, "entertainment");
				image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/entertainment/"+ImplHelper.translate(name)+".jpg");
				break;
			case 4:
				name=ImplHelper.getNameByID(contentID, "market");
				image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/market/"+ImplHelper.translate(name)+".jpg");
				break;
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			return new PlanItemPO(result.getInt("plan_id"),
					result.getInt("id"),
					result.getInt("content_id"),
					result.getInt("content_type"),
					name,//placename
					image,//landmarkImage
					result.getDate("plan_item_date"),
					result.getInt("indexX"),
					result.getInt("indexY"),
					result.getBoolean("isUsed"),
					result.getInt("start_time"),
					result.getInt("end_time"),
					result.getString("remark"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PlanItemPO> getPlanItems(int planID, Date date)
			throws RemoteException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="select * from plan_item where plan_id='"+planID+"'"+" and plan_item_date= '"+sdf.format(date)+"'";
		System.out.println(cmd);
		ArrayList<PlanItemPO> planItemList=new ArrayList<PlanItemPO>();
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				planItemList.add(createPlanItemPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planItemList;
	}

	@Override
	public ArrayList<PlanPO> searchByUser(int userID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from plan where user_id='"+userID+"'";
		ArrayList<PlanPO> planList=new ArrayList<PlanPO>();
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				planList.add(createPlanPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planList;
	}

	@Override
	public boolean addFavourite(int userID, int ItemID) throws RemoteException {
		// TODO Auto-generated method stub
		if(UserManageServiceImpl.getUserNameByID(userID)==null||getPlanIDByItemID(ItemID)==-1){
			return false;
		}
		if(checkFavourite(userID, ItemID)){
			return false;
		}
		cmd="insert into favourite_plan (user_id,plan_item_id) values('" +
				userID+"','"+ItemID+"')";
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean checkFavourite(int userID, int ItemID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from favourite_plan where user_id='"+userID+"' and plan_item_id='"+ItemID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteFavourite(int userID, int ItemID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="delete from favourite_plan where user_id='"+userID+"' and plan_item_id='"+ItemID+"'";
		return database.executeUpdate(cmd);
	}

	
	@Override
	public ArrayList<PlanItemPO> getAllFavourite(int userID, int cityID)
			throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from favourite_plan where user_id='"+userID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<PlanItemPO> planItemList=new ArrayList<PlanItemPO>();
		try {
			while(result.next()){
				int itemID=result.getInt("plan_item_id");
				if(cityID==getCityIDByPlanID(getPlanIDByItemID(itemID))){
					planItemList.add(getPlanItemPOByID(itemID));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planItemList;
	}
	
	//根据planID得到cityID
	private int getCityIDByPlanID(int planID){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select city_id from plan where id='"+planID+"'";
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
	//根据item的id得到plan的id
	private int getPlanIDByItemID(int itemID){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select plan_id from plan_item where id='"+itemID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				int planID=result.getInt("plan_id");
				if(planID<=0){
					return -1;
				}else{
					return planID;
					}
					
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
	//根据item的id得到，itemPO
	private PlanItemPO getPlanItemPOByID(int planItemID){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select * from plan_item where id='"+planItemID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return createPlanItemPO(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
