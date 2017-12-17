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

public class DHCPClientList
{	

	public DHCPClientList()
	{
		refreshList();
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
		HttpGet httpGet = Utils.HttpGetBuilder("http://192.168.1.1/userRpm/AssignedIpAddrListRpm.htm");

		String str = "";
		String temp;
		String group = "";
		
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
		    
		    Pattern pattern = Pattern.compile("DHCPDynList=new Array\\((.*)\\,0\\,0\\s\\)\\;\\<\\/script\\>\\<script\\stype\\=\\\"text\\/javascript\\\"\\>var\\sDHCPDynPara");
		    
			Matcher matcher = pattern.matcher(str);

			while(matcher.find())
				group = matcher.group(0);
			int length = group.length();
			group = group.substring(22,length-62);

		    EntityUtils.consume(entity1);
		} finally {
		    response1.close();
		}
		return group;
	}
	
	/**
	 * @return 第一次get为列，第二次get为行
	 * 第0列为设备名
	 * 第1列为MAC地址
	 * 第2列为IP地址
	 * 第3列为DHCP剩余有效时间
	 * */
	public ArrayList<ArrayList<String>> getList()
	{
		ArrayList<String> A = new ArrayList<String>();	//第0列为设备名
		ArrayList<String> B = new ArrayList<String>();	//第1列为MAC地址
		ArrayList<String> C = new ArrayList<String>();	//第2列为IP地址
		ArrayList<String> D = new ArrayList<String>();	//第3列为DHCP剩余有效时间
		
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
				if(i%4==0)
					A.add(line.substring(0, index));
				else if(i%4==1)
					B.add(line.substring(0, index));
				else if(i%4==2)
					C.add(line.substring(0, index));
				else if(i%4==3)
					D.add(line.substring(0, index));
			}
			else
			{
				//System.out.println(line);
				D.add(line);
				break;
			}
			i++;
		}

		table.add(A);
		table.add(B);
		table.add(C);
		table.add(D);
		
		return table;
	}
	
	public void refreshList()
	{
		
	}
}
