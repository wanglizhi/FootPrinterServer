package dataServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import bean.BlogPO;
import bean.LandmarkPO;
import bean.PlanPO;

import dataService.RecommandDataService;
/**
 * 这个类是不是该拆分到各个DataService中去？
 * @author wanglizhi
 *
 */
public class RecommandServiceImpl extends UnicastRemoteObject implements RecommandDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecommandServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
	}

	//根据tag还是热度？
	@Override
	public ArrayList<PlanPO> recommandPlan(int userID) throws RemoteException {
		// TODO Auto-generated method stub
		//每个人至多有10个推荐
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		int planNum=12;
		ArrayList<PlanPO> planList=new ArrayList<PlanPO>();
		ArrayList<Integer> planIDList=new ArrayList<Integer>(); 
		String[] tags=getTagsByUserID(userID);//获得用户tag
		for(String tag:tags){//取至多前三的tag？====
			String cmd="select distinct plan_id from plan_item " +
					"left join landmark on plan_item.content_id=landmark.id " +
					"where (landmark.tag= '"+tag+"' and plan_item.content_type= '0')";
			ResultSet result=database.executeQuery(cmd);
			try {
				while(result.next()){
					int planID=result.getInt("plan_id");
					if(!planIDList.contains(planID)&&planIDList.size()<=planNum){
						planIDList.add(planID);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(planIDList.size()<planNum){
			String cmd="select * from plan order by applause_num";
			ResultSet result=database.executeQuery(cmd);
			try {
				while(result.next()){
					if(planIDList.size()>=planNum) break;
					int planID=result.getInt("id");
					if(!planIDList.contains(planID)){
						planIDList.add(planID);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int planID:planIDList){
			String cmd="select * from plan where id='"+planID+"'";
			ResultSet result=database.executeQuery(cmd);
			try {
				while(result.next()){
					planList.add(PlanServiceImpl.createPlanPO(result));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("推荐计划--"+planList.size());
		return planList;
	}

	
	//根据热度推荐
	@Override
	public ArrayList<BlogPO> recommandBlog(int userID) throws RemoteException {
		// TODO Auto-generated method stub
		int num=5;
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select * from blog order by applause_num";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<BlogPO> blogList=new ArrayList<BlogPO>();
		try {
			while(result.next()){
				if(blogList.size()>=num){
					return blogList;
				}
				blogList.add(BlogServiceImpl.createBlogPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("推荐博客--"+blogList.size());
		return blogList;
	}

	//根据tag，用户的tag，找到对应的景点
	@Override
	public ArrayList<LandmarkPO> recommandLandmark(int userID)
			throws RemoteException {
		// TODO Auto-generated method stub
		int num=5;
		String[] tags=getTagsByUserID(userID);
		ArrayList<LandmarkPO> landmarkList=new ArrayList<LandmarkPO>();
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		if(tags.length==2){
		for(String tag:tags){
			String cmd="select * from landmark where tag like '%"+tag+"%' order by applause_num";
			ResultSet result=database.executeQuery(cmd);
			try {
				while(result.next()){
					if(landmarkList.size()>=num){
						System.out.println("推荐景点--"+landmarkList.size());
						return landmarkList;
					}
					landmarkList.add(LandmarkServiceImpl.createLandmarkPO(result));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
			String cmd="select * from landmark order by applause_num";
			ResultSet result=database.executeQuery(cmd);
			try {
				while(result.next()){
					if(landmarkList.size()>=num){
						System.out.println("推荐景点--"+landmarkList.size());
						return landmarkList;
					}
					landmarkList.add(LandmarkServiceImpl.createLandmarkPO(result));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("推荐景点--"+landmarkList.size());
		return landmarkList;
	}
	
	//得到用户的tags
	public String[] getTagsByUserID(int userID){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select tags from user where id='"+userID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				String tags=result.getString("tags");
				System.out.println("user tags-->"+tags);
				return tags.split("_");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
