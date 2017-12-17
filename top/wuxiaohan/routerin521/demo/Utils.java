package top.wuxiaohan.routerin521.demo;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * 本软件用到的工具类
 * */
public class Utils
{
	//路由器后台登录密码
	public static String pwd = "521521521";
	
	/**
	 * 生成HttpGet对象
	 * @return 返回HttpGet对象
	 * */
	public static HttpGet HttpGetBuilder(String url)
	{
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Upgrade-Insecure-Requests","1");
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
		httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		httpGet.setHeader("Referer","http://192.168.1.1/userRpm/MenuRpm.htm");
		httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.9");
		return httpGet;
	}
	
	/**
	 * 生成cookies（仅针对于本程序而言）
	 * */
	public static CookieStore cookieStoreBulider()
	{
		CookieStore cookieStore = new BasicCookieStore();
		
		BasicClientCookie cookie = new BasicClientCookie("Authorization", "Basic%20YWRtaW46" + asciiToBase64(pwd));	//登录密码保存于此
		cookie.setDomain("192.168.1.1");
		cookie.setPath("/");
		
		cookieStore.addCookie(cookie);
		return cookieStore;
	}
	
	public static String changeCharset(String str, String newCharset) throws UnsupportedEncodingException 
	{
        if(str != null) 
        {
            //用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
            byte[] bs = str.getBytes();
            return new String(bs, newCharset);    //用新的字符编码生成字符串
        }
        return null;
    }
	
	public static String asciiToBase64(String pwd)
	{
		byte[] temp = pwd.getBytes();
		short ch[] = new short[temp.length];
		
		for(int i = 0; i < temp.length; i++)
			ch[i] = (short)(temp[i]&0xFF);
		
		int quote = ch.length / 3;
		int remain = ch.length % 3;
		byte[] coded;
		if(remain!=0)	
			coded = new byte[quote*4 + 4];
		else 
			coded = new byte[quote*4];
		
		for(int i = 0; i < quote; i++)	
		{
			coded[4*i] = (byte) (ch[3*i]>>2);
			coded[4*i+1] = (byte)(((ch[3*i]&3)<<4) + (ch[3*i+1]>>4));
			coded[4*i+2] = (byte)(((ch[3*i+1]&15)<<2) + (ch[3*i+2]>>6));
			coded[4*i+3] = (byte)(ch[3*i+2]&63);
		}
		switch(remain)	
		{
		default:
		case 0:
			break;
		case 1:
			coded[4*quote] = (byte)(ch[3*quote]>>2);
			coded[4*quote+1] = (byte)64;
			coded[4*quote+2] = (byte)64;
			coded[4*quote+3] = (byte)64;
			break;
		case 2:
			coded[4*quote] = (byte)(ch[3*quote]>>2);
			coded[4*quote+1] = (byte)(((ch[3*quote]&3)<<4) + (ch[3*quote+1]>>4));
			coded[4*quote+2] = (byte)((ch[3*quote+1]&15)<<2);
			coded[4*quote+3] = (byte)64;
			break;
		}
		
		for(int i = 0; i < coded.length; i++)	
		{
			if(coded[i]<26)
				coded[i]+=65;
			else if(coded[i]<52)
				coded[i]+=71;
			else if(coded[i]<62)
				coded[i]-=4;
			else if(coded[i]==62)
				coded[i]=43;
			else if(coded[i]==63)
				coded[i]=47;
			else if(coded[i]==64)
				coded[i]=61;
		}
		String base64 = new String(coded);

		return base64;
	}
	
}

class Charset
{
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块      */
    public static final String US_ASCII = "US-ASCII";
    /** ISO拉丁字母表 No.1，也叫做ISO-LATIN-1     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /** 8 位 UCS 转换格式     */
    public static final String UTF_8 = "UTF-8";
    /** 16 位 UCS 转换格式，Big Endian(最低地址存放高位字节）字节顺序     */
    public static final String UTF_16BE = "UTF-16BE";
    /** 16 位 UCS 转换格式，Litter Endian（最高地址存放地位字节）字节顺序     */
    public static final String UTF_16LE = "UTF-16LE";
    /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识     */
    public static final String UTF_16 = "UTF-16";
    /** 中文超大字符集     **/
    public static final String GBK = "GBK";
    
    public static final String GB2312 = "GB2312";
}
