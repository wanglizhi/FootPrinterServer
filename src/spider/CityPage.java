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
 * �Գ���ҳ��Ĵ���
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
		//���������п�����Ӣ��
		Span enName=ParseUtils.parseTag(html, Span.class,"class","en_title");
		city.setName(name.getStringText().trim());
		if(enName!=null){		
			String s=name.getStringText().trim();
			String[] ss=s.split("<span class="+"\"en_title\""+">");
			city.setName(ss[0]);		
		}
		//System.out.println(name.getStringText());//��ӡ��������
		
		Div introduction=ParseUtils.parseTag(html, Div.class,"class","e_cover_des_l");
		//System.out.println(introduction.getAttribute("title"));//��ӡ���н���
		city.setIntroduction(introduction.getAttribute("title"));
		Div information=ParseUtils.parseTag(html, Div.class,"class","e_cover_des_r");
		//System.out.println(information.getStringText());//������Ϣ
		if(information!=null)city.setInfomation(information.toHtml());
		//��CityPOд�����ݿ�
		writePO();
		//�Ժ�����ҳ�������
		LinkTag tab_shihewanle=ParseUtils.parseTag(html,LinkTag.class,"data-beacon","Tab_shihewanle");
		String shihewanleHtml=HttpUtils.getHtml(client, tab_shihewanle.getAttribute("href"));
		//����
		LinkTag landmark=ParseUtils.parseTag(shihewanleHtml,LinkTag.class,"class","tab_link  selected");
		LandmarkPage.urlQueue.add(landmark.getAttribute("href"));
		LandmarkPage.cityId.add(city.getCityID());
		//����4������
		List<LinkTag> links=ParseUtils.parseTags(shihewanleHtml, LinkTag.class,"class","tab_link ");
		//�Ƶ�
		HotelPage.urlQueue.add(links.get(0).getAttribute("href"));
		HotelPage.cityId.add(city.getCityID());
		//��ʳ
		RestaurantPage.urlQueue.add(links.get(1).getAttribute("href"));
		RestaurantPage.cityId.add(city.getCityID());
		//����
		EntertainmentPage.urlQueue.add(links.get(2).getAttribute("href"));
		EntertainmentPage.cityId.add(city.getCityID());
		//����
		MarketPage.urlQueue.add(links.get(3).getAttribute("href"));
		MarketPage.cityId.add(city.getCityID());
		//����ID��1
		city.setCityID(city.getCityID()+1);
	}
	//��Poд�����ݿ�
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
//			//��ü�����������
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
