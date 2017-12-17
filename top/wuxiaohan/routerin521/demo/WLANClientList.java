package top.wuxiaohan.routerin521.demo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WLANClientList
{
	public WLANClientList()
	{
		
	}
	
	/**
	 * 本类的主体
	 * */
	public String getListInString() throws IOException
	{
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(Utils.cookieStoreBulider())
				.build();
		
		//获取DHCP服务器的用户列表
		HttpGet httpGet = Utils.HttpGetBuilder("http://192.168.1.1/userRpm/WlanStationRpm.htm?Page=1");

		String str = "";
		String temp;
		String group = "";
		
		//HttpGet httpGet = new HttpGet("http://www.uestc.edu.cn");
		
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		try {
		    //System.out.println(response1.getStatusLine());	//返回响应状态
		    
		    HttpEntity entity1 = response1.getEntity();
		    //System.out.println(entity1.getContentType());	//返回响应头部
		    
		    if(entity1 != null)
		    {
		    	InputStream inputStream = entity1.getContent();
		    	Scanner scanner = new Scanner(inputStream,"GB2312");
		    	
		    	while(scanner.hasNextLine())
		    	{
		    		temp = scanner.nextLine();
		    		str += temp;
		    		//System.out.println(temp);	    		
		    	}
		    	inputStream.close();
		    	scanner.close();
		    }
		    
		    Pattern pattern = Pattern.compile("hostList=new Array\\((.*)\\,0\\,0\\s\\)\\;");
		    
			Matcher matcher = pattern.matcher(str);
		
			while(matcher.find())
				group = matcher.group(0);
			int length = group.length();
			group = group.substring(19,length-7);
	
		    EntityUtils.consume(entity1);
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("error.");
		}
		
		finally 
		{
		    response1.close();
		}
		return group;
	}
	
	
	/**
	 * @return 第一次get为列，第二次get为行
	 * 第0列为MAC地址
	 * 第1列为认证方式
	 * 第5列为发送包数
	 * 第6列为收到包数
	 * */
	public ArrayList<ArrayList<String>> getList()
	{
		ArrayList<String> A = new ArrayList<String>();	//MAC地址
		ArrayList<String> B = new ArrayList<String>();	//认证、连接、连接(WPA)、连接(WPA-PSK)、连接(WPA2)、连接(WPA2-PSK)、802-1X、加入、启用、关闭、断开
		ArrayList<String> C = new ArrayList<String>();	//功能未知
		ArrayList<String> D = new ArrayList<String>();	//功能未知
		ArrayList<String> E = new ArrayList<String>();	//功能未知
		ArrayList<String> F = new ArrayList<String>();	//发送包数
		ArrayList<String> G = new ArrayList<String>();	//收到包数
		
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		
		String line = "";
		try
		{
			line = getListInString();
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("getList error.");
		}
		
		int length = line.length();
		int index = line.indexOf(",");
		
		//System.out.println(line.substring(0, index));
		A.add(line.substring(0, index));
		
		int i = 1;
		while(i < 50 && length > 0 && index > 0 && index <= length)
		{
			line = line.substring(index+1,length);
			index = line.indexOf(",");
			length = line.length();
			if(index >= 0)
			{
				//System.out.println(line.substring(0, index));
				if(i%7==0)
					A.add(line.substring(0, index));
				else if(i%7==1)
					B.add(line.substring(0, index));
				else if(i%7==2)
					C.add(line.substring(0, index));
				else if(i%7==3)
					D.add(line.substring(0, index));
				else if(i%7==4)
					E.add(line.substring(0, index));
				else if(i%7==5)
					F.add(line.substring(0, index));
				else if(i%7==6)
					G.add(line.substring(0, index));
			}
				
			else
			{
				//System.out.println(line);
				G.add(line);
				break;
			}
			i++;
		}

		table.add(A);
		table.add(B);
		table.add(C);
		table.add(D);
		table.add(E);
		table.add(F);
		table.add(G);
		
		return table;
	}
}
