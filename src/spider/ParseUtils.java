package spider;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
/**
 * ��HtmlParser�Ĺ�����
 * @author wanglizhi
 *
 */
public class ParseUtils {
	/**
	 * ��ȡĳ������ֵ�ı�ǩ���б�
	 * @param html ����ȡ��html�ı�
	 * @param tagType��ǩ������
	 * @param attributeName ĳ�����Ե�����
	 * @param attributeValue ����Ӧȡ��ֵ
	 */
	public static <T extends TagNode> List<T> parseTags(String html,final Class<T> tagType,
			final String attributeName,final String attributeValue){
		try{
			//����һ��html������
			Parser parser=new Parser();
			parser.setInputHTML(html);
			NodeList tagList=parser.parse(
					new NodeFilter(){
						private static final long serialVersionUID = 1L;

						@Override
						public boolean accept(Node node) {
							// TODO Auto-generated method stub
							if(node.getClass()==tagType){
								if(attributeName==null){
									return true;
								}
								T tn=(T)node;
								String attrValue=tn.getAttribute(attributeName);
								if(attrValue!=null&& attrValue.equals(attributeValue)){
									return true;
								}
							}
							return false;
						}
					});
			List<T> tags=new ArrayList<T>();
			for(int i=0;i<tagList.size();i++){
				T t=(T)tagList.elementAt(i);
				tags.add(t);
			}
			return tags;
		}catch(ParserException e){
			e.printStackTrace();
		}
		return null;	
	}
	/**
	 * ��ȡĳ������ֵ�ı�ǩ
	 * @param html ����ȡ��html�ı�
	 * @param tagType��ǩ������
	 * @param attributeName ĳ�����Ե�����
	 * @param attributeValue ����Ӧȡ��ֵ
	 */
	public static <T extends TagNode> T parseTag(String html,final Class<T> tagType,final String attributeName,final String attributeValue){  
        List<T> tags=parseTags(html, tagType, attributeName, attributeValue);  
        if(tags!=null&&tags.size()>0){  
            return tags.get(0);  
        }  
        return null;  
    }
	/** 
     * ��ȡ����ĳ������ֵ�ı�ǩ 
     * @param html ����ȡ��html�ı� 
     * @param tagType ��ǩ������ 
     * @return 
     */  
    public static <T extends TagNode> T parseTag(String html,final Class<T> tagType){  
        return parseTag(html, tagType,null,null);  
    }  
    /** 
     * ��ȡ����ĳ������ֵ�ı�ǩ�б� 
     * @param html ����ȡ��html�ı� 
     * @param tagType ��ǩ������ 
     * @return 
     */  
    public static <T extends TagNode> List<T>  parseTags(String html,final Class<T> tagType){  
        return parseTags(html, tagType,null,null);  
    }  

}
