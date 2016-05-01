package spider;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import bean.HotelPO;

/**
 * �ԾƵ�ҳ��Ĵ���
 * 
 * @author wanglizhi
 * 
 */
public class HotelPage implements Runnable {
	public static LinkedList<String> urlQueue = new LinkedList<String>();
	public static LinkedList<Integer> cityId = new LinkedList<Integer>();
	private static HotelPO hotel;
	private HttpClient client;
	private DatabaseHelper database;

	public HotelPage(HttpClient client) {
		this.client = client;
		hotel = new HotelPO();
		database = DatabaseInit.getDatabaseHelper();
	}

	public void parseHotelPage(String li) {
		hotel.setCityID(cityId.getFirst());// ����ID
		LinkTag name = ParseUtils.parseTag(li, LinkTag.class, "class",
				"e_attribute");
		System.out.println(name.getAttribute("title"));
		hotel.setName(name.getAttribute("title").trim());// �Ƶ�����
		ParagraphTag intro = ParseUtils.parseTag(li, ParagraphTag.class);
		hotel.setIntroduction(intro.getStringText());// �Ƶ����
		ImageTag image = ParseUtils.parseTag(li, ImageTag.class);
		if (image != null)
			IOHelper.writeImage(image, client, "hotel//" + hotel.getName());// �Ƶ�ͼƬ�Ĵ���

	}

	// ȥ�Ķ����Ծ�����趨��Tag
	public void parseTags(Div tagList) {
		String tag = "";
		List<Span> tags = ParseUtils.parseTags(tagList.toHtml(), Span.class,
				"class", "tag");
		for (int i = 0; i < tags.size(); i++) {
			if (tags != null)
				tag = tag + "_" + tags.get(i).getStringText();
		}
		hotel.setType(tag);
		// �Ƶ��˾��۸�
		Span price = ParseUtils.parseTag(tagList.toHtml(), Span.class, "class",
				"strategy_num");
		if (price != null)
			hotel.setPrice(price.toHtml());
	}

	public void parseHotelUrl(String url) {
		String html = HttpUtils.getHtml(client, url);
		List<Div> tagListDiv = ParseUtils.parseTags(html, Div.class, "class",
				"tag_list");
		BulletList ulList = ParseUtils.parseTag(html, BulletList.class,
				"class", "e_list clrfix");
		List<Bullet> lis = ParseUtils.parseTags(ulList.toHtml(), Bullet.class);
		for (int i = 0; i < lis.size(); i++) {
			parseHotelPage(lis.get(i).toHtml());
			parseTags(tagListDiv.get(i));
			// ��HotelPOд�����ݿ�
			writePO();
		}
	}

	// ��Poд�����ݿ�
	public boolean writePO() {
		String cmd = "insert into hotel (city_id,hotel_name, intro,hotel_type,price)values('"
				+ hotel.getCityID()
				+ "','"
				+ hotel.getName()
				+ "','"
				+ hotel.getIntroduction()
				+ "','"
				+ hotel.getType()
				+ "','"
				+ hotel.getPrice() + "')";
		return database.executeUpdate(cmd);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (!urlQueue.isEmpty()) {
				parseHotelUrl(urlQueue.removeFirst());
				cityId.removeFirst();
			} else {
				RestaurantPage restaurant = new RestaurantPage(client);
				new Thread(restaurant).start();
				break;
			}
//			// ��ü�����������
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
