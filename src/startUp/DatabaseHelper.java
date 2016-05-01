package startUp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
	private Statement database;

	public DatabaseHelper(Statement database) {
		this.database = database;
	}

	// ִ�и���ָ��
	public synchronized boolean executeUpdate(String sql) {
		try {
			database.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//ִ�в���ָ��
	public synchronized ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		try {
			rs = database.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	// ����б�
	public synchronized boolean clearTable(String name) {
		try {
			database.executeUpdate("delete from " + name);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public synchronized int getGeneratedKeys(int index){
		ResultSet rs=null;
		try {
			rs=database.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(index);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static  String getIP(){
		InetAddress addr=null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ip=addr.getHostAddress().toString();
		return ip;
	}

}
