package spider;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.*;
/**
 * 对HTTP请求的处理
 * @author wanglizhi
 *
 */
public class HttpUtils {

	public static String getHtml(HttpClient httpClient,String url){
		try{
			//利用Http get向服务器发起请求
			HttpGet get=new HttpGet(url);
			//获得服务器响应的所有信息
			HttpResponse response=httpClient.execute(get);
			//获得服务器响应回来的消息体（不包括http head）
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				//获得响应的字符集编码信息
				//String charset=EntityUtils.getContentCharSet(entity);
				//InputStream inputStream=entity.getContent();
				//IOUtils.toString(inputStream,charset);
				return EntityUtils.toString(entity);			
			}
		}catch(ClientProtocolException e1){
			e1.printStackTrace();
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	}
	public static byte[] getImage(HttpClient httpClient,String url){
		try{
			//利用Http get向服务器发起请求
			HttpGet get=new HttpGet(url);
			//获得服务器响应的所有信息
			HttpResponse response=httpClient.execute(get);
			//获得服务器响应回来的消息体（不包括http head）
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				//获得响应的字符集编码信息
				//String charset=EntityUtils.getContentCharSet(entity);
				//InputStream inputStream=entity.getContent();
				//IOUtils.toString(inputStream,charset);
				return EntityUtils.toByteArray(entity);			
			}
		}catch(ClientProtocolException e1){
			e1.printStackTrace();
		}catch(IOException e2){
			e2.printStackTrace();
		}
		return null;
	}
}
