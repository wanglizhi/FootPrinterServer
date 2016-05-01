package spider;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.*;
/**
 * ��HTTP����Ĵ���
 * @author wanglizhi
 *
 */
public class HttpUtils {

	public static String getHtml(HttpClient httpClient,String url){
		try{
			//����Http get���������������
			HttpGet get=new HttpGet(url);
			//��÷�������Ӧ��������Ϣ
			HttpResponse response=httpClient.execute(get);
			//��÷�������Ӧ��������Ϣ�壨������http head��
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				//�����Ӧ���ַ���������Ϣ
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
			//����Http get���������������
			HttpGet get=new HttpGet(url);
			//��÷�������Ӧ��������Ϣ
			HttpResponse response=httpClient.execute(get);
			//��÷�������Ӧ��������Ϣ�壨������http head��
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				//�����Ӧ���ַ���������Ϣ
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
