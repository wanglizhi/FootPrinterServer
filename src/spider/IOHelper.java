package spider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.htmlparser.tags.ImageTag;
/**
 * IO的工具类
 * @author wanglizhi
 *
 */
public class IOHelper {
	// 文件流，缓冲流
		private static BufferedReader br;
		private static FileReader fileReader;
		public IOHelper(){			
		}
//		public static void main(String args[]){
//			Reader.readCityUrl("spiderUrls//citys.txt");
//		}
		public static void writeImage(ImageTag imageTag,HttpClient client,String imagePath){
			System.out.println(imageTag.getImageURL()+"    "+imagePath);
			if(imageTag.getImageURL()==null||imageTag.getImageURL().equals(""))return;
			byte[] image=HttpUtils.getImage(client, imageTag.getImageURL());
			try {
				IOUtils.write(image, new FileOutputStream(imagePath+".jpg"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static void readCityUrl(String fileName){
			try {
				fileReader = new FileReader("spiderUrls//"+fileName);
				br = new BufferedReader(fileReader);
				String url="";
				while((url=br.readLine())!=null){
					url.trim();
					CityPage.urlQueue.add(url);
					//System.out.println(url);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
