package spider;

import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;
import bean.CityPO;
/**
 * 对城市页面的处理
 * @author wanglizhi
 *
 */
public class CityPage implements Runnable{
	public static LinkedList<String> urlQueue=new LinkedList<String>();
	private static CityPO city;
	private HttpClient client;
	private DatabaseHelper database;
	public CityPage(HttpClient client){
		this.client=client;
		this.database=DatabaseInit.getDatabaseHelper();
		city=new CityPO();
		city.setCityID(1);
		IOHelper.readCityUrl("citys.txt");
	}
	public void parseCityPage(String url){
		String html=HttpUtils.getHtml(client, url);
		Span name=ParseUtils.parseTag(html, Span.class,"class","e_cover_title");
		//城市名称有可能有英文
		Span enName=ParseUtils.parseTag(html, Span.class,"class","en_title");
		city.setName(name.getStringText().trim());
		if(enName!=null){		
			String s=name.getStringText().trim();
			String[] ss=s.split("<span class="+"\"en_title\""+">");
			city.setName(ss[0]);		
		}
		//System.out.println(name.getStringText());//打印城市名称
		
		Div introduction=ParseUtils.parseTag(html, Div.class,"class","e_cover_des_l");
		//System.out.println(introduction.getAttribute("title"));//打印城市介绍
		city.setIntroduction(introduction.getAttribute("title"));
		Div information=ParseUtils.parseTag(html, Div.class,"class","e_cover_des_r");
		//System.out.println(information.getStringText());//旅游信息
		if(information!=null)city.setInfomation(information.toHtml());
		//将CityPO写进数据库
		writePO();
		//吃喝玩乐页面的连接
		LinkTag tab_shihewanle=ParseUtils.parseTag(html,LinkTag.class,"data-beacon","Tab_shihewanle");
		String shihewanleHtml=HttpUtils.getHtml(client, tab_shihewanle.getAttribute("href"));
		//景点
		LinkTag landmark=ParseUtils.parseTag(shihewanleHtml,LinkTag.class,"class","tab_link  selected");
		LandmarkPage.urlQueue.add(landmark.getAttribute("href"));
		LandmarkPage.cityId.add(city.getCityID());
		//其他4个链接
		List<LinkTag> links=ParseUtils.parseTags(shihewanleHtml, LinkTag.class,"class","tab_link ");
		//酒店
		HotelPage.urlQueue.add(links.get(0).getAttribute("href"));
		HotelPage.cityId.add(city.getCityID());
		//美食
		RestaurantPage.urlQueue.add(links.get(1).getAttribute("href"));
		RestaurantPage.cityId.add(city.getCityID());
		//娱乐
		EntertainmentPage.urlQueue.add(links.get(2).getAttribute("href"));
		EntertainmentPage.cityId.add(city.getCityID());
		//购物
		MarketPage.urlQueue.add(links.get(3).getAttribute("href"));
		MarketPage.cityId.add(city.getCityID());
		//城市ID加1
		city.setCityID(city.getCityID()+1);
	}
	//将Po写进数据库
	public boolean writePO(){
		String cmd="insert into city (id, city_name, introduction, info)values('"+city.getCityID()+"','"+
	city.getName()+"','"+city.getIntroduction()+"','"+city.getInformation()+"')";
		return database.executeUpdate(cmd);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!urlQueue.isEmpty()){
				parseCityPage(urlQueue.removeFirst());			
			}else{
				LandmarkPage landmark=new LandmarkPage(client);
				new Thread(landmark).start();
				break;
			}
//			//最好减慢访问速率
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
