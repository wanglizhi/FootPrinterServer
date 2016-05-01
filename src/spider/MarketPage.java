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

import bean.MarketPO;

/**
 * ���̳�ҳ��Ĵ���
 * 
 * @author wanglizhi
 * 
 */
public class MarketPage implements Runnable {

	public static LinkedList<String> urlQueue = new LinkedList<String>();
	public static LinkedList<Integer> cityId = new LinkedList<Integer>();
	private static MarketPO market;
	private HttpClient client;
	private DatabaseHelper database;

	public MarketPage(HttpClient client) {
		this.client = client;
		market = new MarketPO();
		database = DatabaseInit.getDatabaseHelper();
	}

	public void parseEntertainmentPage(String url) {
		market.setCityID(cityId.getFirst());// ����ID
		String html = HttpUtils.getHtml(client, url);
		BulletList intro1 = ParseUtils.parseTag(html, BulletList.class,
				"class", "e_list");
		Div intro2 = ParseUtils.parseTag(html, Div.class, "class", "intro_des");
		if(intro1==null&&intro2!=null)market.setIntroduction(intro2.toHtml());
		else if(intro1!=null&&intro2==null)market.setIntroduction(intro1.toHtml());
		else market.setIntroduction(intro1.toHtml() + intro2.toHtml());// �̳��Ľ��ܣ��绰����ַ
		Div imgbox = ParseUtils.parseTag(html, Div.class, "class",
				"e_focus_imgbox");
		ImageTag image = ParseUtils.parseTag(imgbox.toHtml(), ImageTag.class);
		if (market != null)
			IOHelper.writeImage(image, client, "market//" + market.getName());// �̳�ͼƬ�Ĵ���
	}

	// ȥ�Ķ������̳��趨��Tag���˾��۸�
	public void parseTags(Div tagList) {
		String tag = "";
		List<Span> tags = ParseUtils.parseTags(tagList.toHtml(), Span.class,
				"class", "tag");
		for (int i = 0; i < tags.size(); i++) {
			if (tags != null)
				tag = tag + "_" + tags.get(i).getStringText();
		}
		market.setType(tag);
		// �̳��˾��۸�
		Span price = ParseUtils.parseTag(tagList.toHtml(), Span.class, "class",
				"strategy_num");
		if (price != null)
			market.setPrice(price.toHtml());
	}

	public void parseEntertainmentUrl(String url) {
		String html = HttpUtils.getHtml(client, url);
		List<LinkTag> hrefs = ParseUtils.parseTags(html, LinkTag.class,
				"class", "e_attribute");
		List<Div> tagListDiv = ParseUtils.parseTags(html, Div.class, "class",
				"tag_list");
		for (int i = 0; i < hrefs.size(); i++) {
			market.setName(hrefs.get(i).getAttribute("title").trim());// �����̳�����
			parseEntertainmentPage(hrefs.get(i).getAttribute("href"));// �̳���ҳ
			parseTags(tagListDiv.get(i));

			// ��MarketPOд�����ݿ�
			writePO();
		}
	}

	// ��Poд�����ݿ�
	public boolean writePO() {
		String cmd = "insert into market (city_id,market_name, intro,market_type,price)values('"
				+ market.getCityID()
				+ "','"
				+ market.getName()
				+ "','"
				+ market.getIntroduction()
				+ "','"
				+ market.getType()
				+ "','"
				+ market.getPrice() + "')";
		return database.executeUpdate(cmd);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (!urlQueue.isEmpty()) {
				parseEntertainmentUrl(urlQueue.removeFirst());
				cityId.removeFirst();
			} else {
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
