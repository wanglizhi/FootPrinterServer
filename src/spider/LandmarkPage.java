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

import bean.LandmarkPO;

/**
 * �Ծ���ҳ��Ĵ���
 * 
 * @author wanglizhi
 * 
 */
public class LandmarkPage implements Runnable {
	public static LinkedList<String> urlQueue = new LinkedList<String>();
	public static LinkedList<Integer> cityId = new LinkedList<Integer>();
	private static LandmarkPO landmark;
	private HttpClient client;
	private DatabaseHelper database;

	public LandmarkPage(HttpClient client) {
		this.client = client;
		landmark = new LandmarkPO();
		database = DatabaseInit.getDatabaseHelper();
	}

	public void parseLandmarkPage(String url) {
		landmark.setCityID(cityId.getFirst());// ���ó���Id
		String html = HttpUtils.getHtml(client, url);
		Div intro = ParseUtils.parseTag(html, Div.class, "class", "intro_des");
		if(intro!=null)landmark.setIntroduction(intro.getAttribute("title"));// �������
		BulletList ul = ParseUtils.parseTag(html, BulletList.class, "class",
				"e_list");
		if(ul!=null)landmark.setInformation(ul.toHtml());// ������Ϣ����Ʊ������ʱ�䡢��ַ���绰
		Div imageDiv = ParseUtils.parseTag(html, Div.class, "class",
				"e_focus_imgbox");
		ImageTag image = ParseUtils.parseTag(imageDiv.toHtml(), ImageTag.class);
		if(image!=null)IOHelper.writeImage(image, client, "landmark/" + landmark.getName());// ����ͼƬ����
	}

	public void parseTags(Div tagList) {
		String tag = "";
		List<Span> tags = ParseUtils.parseTags(tagList.toHtml(), Span.class,
				"class", "tag");
		for (int i = 0; i < tags.size(); i++) {
			tag = tag + "_" + tags.get(i).getStringText();
		}
		landmark.setType(tag);
	}

	public void parseLandmarkUrl(String url) {
		String html = HttpUtils.getHtml(client, url);
		List<LinkTag> hrefs = ParseUtils.parseTags(html, LinkTag.class,
				"class", "e_attribute");
		List<Div> tagListDiv = ParseUtils.parseTags(html, Div.class, "class",
				"tag_list");
		for (int i = 0; i < hrefs.size(); i++) {
			landmark.setName(hrefs.get(i).getAttribute("title"));// ��������
			parseLandmarkPage(hrefs.get(i).getAttribute("href"));			
			parseTags(tagListDiv.get(i));
			// ��LandmarkPOд�����ݿ�
			writePO();
		}
	}

	// ��Poд�����ݿ�
	public boolean writePO() {
		String cmd = "insert into landmark (city_id,landmark_name, introduction,info,landmark_type)values('"
				+ landmark.getCityID()
				+ "','"
				+ landmark.getName()
				+ "','"
				+ landmark.getIntroduction()
				+ "','"
				+ landmark.getInformation() + "','" + landmark.getType() + "')";
		return database.executeUpdate(cmd);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (!urlQueue.isEmpty()) {
				parseLandmarkUrl(urlQueue.removeFirst());
				cityId.removeFirst();
			} else {
				HotelPage hotel = new HotelPage(client);
				new Thread(hotel).start();
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
