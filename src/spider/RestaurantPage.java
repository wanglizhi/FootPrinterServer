package spider;

import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;
import bean.RestaurantPO;

/**
 * 饭店数据的爬取，对应美食
 * 
 * @author wanglizhi
 * 
 */
public class RestaurantPage implements Runnable {

	public static LinkedList<String> urlQueue = new LinkedList<String>();
	public static LinkedList<Integer> cityId = new LinkedList<Integer>();
	private static RestaurantPO restaurant;
	private HttpClient client;
	private DatabaseHelper database;

	public RestaurantPage(HttpClient client) {
		this.client = client;
		restaurant = new RestaurantPO();
		database = DatabaseInit.getDatabaseHelper();
	}

	public void parseRestaurantPage(String url) {
		restaurant.setCityID(cityId.getFirst());// 设置ID
		String html = HttpUtils.getHtml(client, url);
		BulletList intro1 = ParseUtils.parseTag(html, BulletList.class,
				"class", "e_list");
		Div intro2 = ParseUtils.parseTag(html, Div.class, "class", "intro_des");
		//考虑为空情况
		if(intro1==null&&intro2!=null)restaurant.setIntroduction(intro2.toHtml());
		else if(intro1!=null&&intro2==null)restaurant.setIntroduction(intro1.toHtml());
		else restaurant.setIntroduction(intro1.toHtml() + intro2.toHtml());// 饭店的介绍，电话，地址
		Div imgbox = ParseUtils.parseTag(html, Div.class, "class",
				"e_focus_imgbox");
		ImageTag image = ParseUtils.parseTag(imgbox.toHtml(), ImageTag.class);
		if (image != null)
			IOHelper.writeImage(image, client,
					"restaurant//" + restaurant.getName());// 饭店图片的储存
	}

	// 去哪儿网对饭店设定的Tag及人均价格
	public void parseTags(Div tagList) {
		String tag = "";
		List<Span> tags = ParseUtils.parseTags(tagList.toHtml(), Span.class,
				"class", "tag");
		for (int i = 0; i < tags.size(); i++) {
			if (tags != null)
				tag = tag + "_" + tags.get(i).getStringText();
		}
		restaurant.setType(tag);
		// 饭店人均价格
		Span price = ParseUtils.parseTag(tagList.toHtml(), Span.class, "class",
				"strategy_num");
		if (price != null)
			restaurant.setPrice(price.toHtml());
	}

	public void parseRestaurantUrl(String url) {
		String html = HttpUtils.getHtml(client, url);
		List<LinkTag> hrefs = ParseUtils.parseTags(html, LinkTag.class,
				"class", "e_attribute");
		List<Div> tagListDiv = ParseUtils.parseTags(html, Div.class, "class",
				"tag_list");
		for (int i = 0; i < hrefs.size(); i++) {
			restaurant.setName(hrefs.get(i).getAttribute("title").trim());// 设置饭店名字
			parseRestaurantPage(hrefs.get(i).getAttribute("href"));// 饭店主页
			parseTags(tagListDiv.get(i));

			// 将RestaurantPO写入数据库
			writePO();
		}
	}

	// 将Po写进数据库
	public boolean writePO() {
		String cmd = "insert into restaurant (city_id,restaurant_name, intro,restaurant_type,price)values('"
				+ restaurant.getCityID()
				+ "','"
				+ restaurant.getName()
				+ "','"
				+ restaurant.getIntroduction()
				+ "','"
				+ restaurant.getType()
				+ "','" + restaurant.getPrice() + "')";
		return database.executeUpdate(cmd);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (!urlQueue.isEmpty()) {
				parseRestaurantUrl(urlQueue.removeFirst());
				cityId.removeFirst();
			} else {
				EntertainmentPage entertainment = new EntertainmentPage(client);
				new Thread(entertainment).start();
				break;
			}
//			// 最好减慢访问速率
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

}
