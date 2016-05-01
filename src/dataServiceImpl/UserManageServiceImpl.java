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
import bean.UserPO;
import dataService.UserManageDataService;
/**
 * 
 * @author wanglizhi
 *
 */
public class UserManageServiceImpl extends UnicastRemoteObject implements UserManageDataService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatabaseHelper database;
	private String cmd;

	public UserManageServiceImpl(int port) throws RemoteException {
		super(port);
		// TODO Auto-generated constructor stub
		database=DatabaseInit.getDatabaseHelper();
	}

	@Override
	public boolean register(UserPO user) {
		// TODO Auto-generated method stub
		//先判断是否有同名用户
		System.out.println("Register user--> userName: "+user.getUserName());
		String ss="select count(*) from user where user_name='"+user.getUserName()+"'";
		ResultSet result=database.executeQuery(ss);
		try {
			if(result.next()){
				int count=result.getInt(1);
				if(count>=1){
					System.out.println("-->fail: userName exist");
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		cmd="insert into user (user_name,user_password,sex,qq,tel,realname," +
				"address,registered_date,isLogin,tags) values ('"+
				user.getUserName()+"','"+
				user.getPassword()+"',"+
				user.isSex()+",'"+
				user.getQq()+"','"+
				user.getTel()+"','"+
				user.getRealName()+"','"+
				user.getAddress()+"','"+
				sdf.format(user.getRegisteredDate())+"',"+
				false+",'"+//注册后直接登录
				user.getTags()+"')";
		if(database.executeUpdate(cmd)){
			System.out.println("-->sucess~~");
			return true;
		}else{
			System.out.println("-->fail~~");
			return false;
		}
		
	}

	@Override
	public UserPO login(UserPO userPO) {
		// TODO Auto-generated method stub
		System.out.println("Login user--> userName: "+userPO.getUserName());
		cmd="select * from user where user_name='"+userPO.getUserName()+"'";
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				boolean isLogin=result.getBoolean("isLogin");
				UserPO user=createUserPO(result);
				if(userPO.getPassword().equals(user.getPassword())&&(!isLogin)){
					cmd="update user set isLogin= "+true+" where user_name= '"+userPO.getUserName()+"'";
					database.executeUpdate(cmd);
					System.out.println("-->sucess~~");
					return user;
				}else{
					System.out.println("-->fail~~");
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("-->fail~~");
		return null;
	}

	@Override
	public boolean modifyUser(UserPO userPO) {
		// TODO Auto-generated method stub
		System.out.println("Modify user--> userName: "+userPO.getUserName());
		cmd="update user set user_name='"+userPO.getUserName()+
				"',user_password='"+userPO.getPassword()+
				"',sex="+userPO.isSex()+
				",qq='"+userPO.getQq()+
				"',tel='"+userPO.getTel()+
				"',realname='"+userPO.getRealName()+
				"',tags='"+userPO.getTags()+
				"' where id='"+userPO.getUserID()+"'";
		return database.executeUpdate(cmd);
	}

	@Override
	public ArrayList<UserPO> searchUser(String name) {
		// TODO Auto-generated method stub
		cmd="select * from user where user_name='"+name+"'";
		ResultSet result=database.executeQuery(cmd);
		ArrayList<UserPO> userList=new ArrayList<UserPO>();
		try {
			while(result.next()){
				userList.add(createUserPO(result));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}
	
	public UserPO createUserPO(ResultSet result){
		try {
			int id=result.getInt("id");
			URL image=new URL("http://"+DatabaseHelper.getIP()+"/footPrinter/head/"+id+".jpg");
			return new UserPO(result.getInt("id"),
					result.getString("user_name"),
					result.getString("user_password"),
					image,//头像======================================
					result.getBoolean("sex"),
					result.getString("qq"),
					result.getString("tel"),
					result.getString("realname"),
					result.getString("address"),
					result.getString("tags"),
					result.getDate("registered_date"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//根据ID查找姓名
	public static String getUserNameByID(int id){
		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
		String cmd="select user_name from user where id="+id;
		ResultSet result=database.executeQuery(cmd);
		try {
			if(result.next()){
				return result.getString("user_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean exit(int UserID) {
		// TODO Auto-generated method stub
		cmd="update user set isLogin = "+false+" where id= '"+UserID+"'";
		return database.executeUpdate(cmd);
	}

}
