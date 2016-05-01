package startUp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseInit {
	private static Connection conn;
	private static String driver="com.mysql.jdbc.Driver";
	private static String url="jdbc:mysql://127.0.0.1:3306/footprinter";
	private static String user="root";
	private static String password="wanglizhi";
	public static void init(){
		try {
			//加载驱动
			Class.forName(driver);
			//连接数据库
			conn=DriverManager.getConnection(url,user,password);		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static DatabaseHelper getDatabaseHelper(){
		try {
			if(!conn.isClosed()){
				return new DatabaseHelper(conn.createStatement());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}

}
