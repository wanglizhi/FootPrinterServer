package spider;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import startUp.DatabaseInit;

/**
 * 爬虫测试
 * 
 * @author wanglizhi
 * 
 */
public class TestSpider {

	/**
	 * @author wanglizhi
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseInit.init();
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table city;");
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table landmark;");
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table hotel;");
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table restaurant;");
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table entertainment;");
		DatabaseInit.getDatabaseHelper().executeQuery("truncate table market;");
		HttpClient client = new DefaultHttpClient();
		CityPage cityPage = new CityPage(client);
		System.out.println("开始工作");
		new Thread(cityPage).start();
	}

}
