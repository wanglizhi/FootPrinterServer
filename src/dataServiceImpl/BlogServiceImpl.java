package dataServiceImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import bean.BlogPO;

import dataService.BlogDataService;

public class BlogServiceImpl extends UnicastRemoteObject implements BlogDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public BlogServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public int add(BlogPO blogPO) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Add blog-->userID: "+blogPO.getUserID()+" title: "+blogPO.getTitle());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="insert into blog (user_id, city_id, title, publish_time) values ('"+
		blogPO.getUserID()+"','"+
		blogPO.getCityID()+"','"+
		blogPO.getTitle()+"','"+
		sdf.format(blogPO.getPublishTime())+"')";
		database.executeUpdate(cmd);
		return database.getGeneratedKeys(1);
	}

	/**
	 * É¾³ý¶ÔÓ¦ÔÞ±í
	 */
	@Override
	public boolean delete(int blogID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Delete blog-->blogID: "+blogID);
		cmd="delete from blog where id='"+blogID+"'";
		if(database.executeUpdate(cmd)){
			cmd="delete from applause_blog where blog_id='"+blogID+"'";
			return database.executeUpdate(cmd);
		}
		return false;
	}

	@Override
	public boolean modify(BlogPO blogPO) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Modify blog--> userName: "+blogPO.getUserName()+" title: "+blogPO.getTitle());
		cmd="update blog set user_id='"+blogPO.getUserID()+
				"',city_id ='"+blogPO.getCityID()+
				"',title ='"+blogPO.getTitle()+
				"',applause_num= '"+blogPO.getApplauseNum()+
				"' where id='"+blogPO.getBlogID()+"'";
		return database.executeUpdate(cmd);
	}

	@Override
	public boolean addApplause(int userID, int blogID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("blog add applause--> userID: "+userID+" blogID: "+blogID);
		if(UserManageServiceImpl.getUserNameByID(userID)==null||searchByID(blogID)==null){
			return false;
		}
		if(checkApplause(userID, blogID)){
			return false;
		}
		cmd="insert into applause_blog(user_id,blog_id)values('"+
				userID+"','"+blogID+"')";
		return database.executeUpdate(cmd)&& ImplHelper.addApplauseNum("blog", blogID);
	}

	@Override
	public boolean deleteApplause(int userID, int blogID)
			throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("delete add applause--> userID: "+userID+" blogID: "+blogID);
		cmd="delete from applause_blog where user_id='"+userID+"' and blog_id='"+blogID+"'";
		return database.executeUpdate(cmd)&& ImplHelper.deleteApplauseNum("blog", blogID);
	}

	@Override
	public boolean checkApplause(int userID, int blogID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from applause_blog where user_id='"+userID+"' and blog_id='"+blogID+"'";
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
	public BlogPO searchByID(int blogID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from blog where id='"+blogID+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			while(result.next()){
				return createBlogPO(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<BlogPO> searchByCityID(int cityID) throws RemoteException {
		// TODO Auto-generated method stub
		int size=10;
		cmd="select * from blog where city_id='"+cityID+"' order by applause_num";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<BlogPO> blogList=new ArrayList<BlogPO>();
		try {
			while(result.next()){
				if(blogList.size()>=size) break;
				blogList.add(createBlogPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blogList;
	}
	
	@Override
	public ArrayList<BlogPO> searchByUserID(int userID) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from blog where user_id='"+userID+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<BlogPO> blogList=new ArrayList<BlogPO>();
		try {
			while(result.next()){
				blogList.add(createBlogPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blogList;
	}

	@Override
	public ArrayList<BlogPO> search(String key) throws RemoteException {
		// TODO Auto-generated method stub
		cmd="select * from blog where title like '%"+key+"%'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<BlogPO> blogList=new ArrayList<BlogPO>();
		try {
			while(result.next()){
				blogList.add(createBlogPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blogList; 
	}
	
	public static BlogPO createBlogPO(ResultSet result){
		
		try {
			int blogID=result.getInt("id");
			int userID=result.getInt("user_id");
			int cityID=result.getInt("city_id");
			URL blogURL=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/blog/"+blogID);
			URL userImage=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/head/"+userID+".jpg");
			return new BlogPO(blogID,
					userID,
					cityID,
					UserManageServiceImpl.getUserNameByID(userID),//username
					CityServiceImpl.getCityNameByID(cityID),//cityname
					result.getString("title"),
					result.getInt("applause_num"),
					blogURL,//URL
					result.getDate("publish_time"),
					userImage);//userImage
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
