package spider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.*;
import org.htmlparser.visitors.HtmlPage;

import startUp.DatabaseHelper;
import startUp.DatabaseInit;

import dataServiceImpl.Config;
import dataServiceImpl.ImplHelper;

public class Test {

	public static String url="http://localhost/footPrinter/layout.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageIcon im=new ImageIcon("http://www.baidu.com/img/bdlogo.gif");
		System.out.println(im.getImage());
//		HttpClient client = new DefaultHttpClient();
//		String html=HttpUtils.getHtml(client, url);
//		System.out.println(html);
		
		
//		String[] ss="_升旗_国家象征".split("_");
//		System.out.println(ss.length);
		
//		DatabaseInit.init();
//		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
//		URL image=null;
//		try {
////			image=new URL("http://"+database.getIP()+"/footPrinter/landmark/MINT玩具博物馆.jpg");
//			image=new URL("http://"+database.getIP()+"/footPrinter/landmark/Jogyesa.jpg");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(ImplHelper.isConnect(image)){
//			System.out.println(Math.ceil(1/3.0));
//		}
//		ImageIcon ii = new ImageIcon(image);
		
//		try {
//			System.out.println(image.openStream());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("结束");
//		}
		
		
		
//		//初始化景点的tag		
//		DatabaseInit.init();
//		DatabaseHelper database=DatabaseInit.getDatabaseHelper();
//		String cmd=null;
//		for(int i=01;i<=1909;i++){
//			cmd="update landmark set tag='"+getRandomTag()+"' where id='"+i+"'";
//			database.executeUpdate(cmd);
//		}
		
		
		
//		String s="东京<span class="+"en_title"+">Tokyo</span>";
//		String[] ss=s.split("<span class="+"en_title"+">");
//		System.out.println(ss[0]);
//		
//		
//		HttpClient client=new DefaultHttpClient();
//		String html=HttpUtils.getHtml(client, url);
//		BulletList ulList=ParseUtils.parseTag(html, BulletList.class, "class", "e_list clrfix");
//		//System.out.println(ulList.toHtml());
//		List<Bullet> li=ParseUtils.parseTags(ulList.toHtml(), Bullet.class);
//		String s=li.get(0).toHtml();
//		Span strategy_num=ParseUtils.parseTag(s, Span.class, "class", "strategy_num");
//		
//		//正则表达式
////		String haha=strategy_num.getStringText();
//		System.out.println(strategy_num.toHtml());
////		Pattern pattern=Pattern.compile("{<i>(.*?)</i>}");
////		Matcher matcher=pattern.matcher(haha);
////		//String price=haha.substring(matcher.start(), matcher.end());
////		if(matcher.find()){
////			System.out.println(matcher.group());
////		}
//		
	}
	public static String getRandomTag(){
		int index=(int)(Math.random()*Config.tagNames.length);
		return Config.tagNames[index];
	}

}
