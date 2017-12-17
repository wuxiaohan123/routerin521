package top.wuxiaohan.routerin521.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
	public static void main(String[] args) throws IOException
	{	
		showInFormat();
	}
	
	public static void showInFormat() throws FileNotFoundException
	{
		DHCPClientList dhcpClientList = new DHCPClientList();
		WLANClientList wlanClientList = new WLANClientList();
		
		ArrayList<ArrayList<String>> dhcpTable = dhcpClientList.getList();
		ArrayList<ArrayList<String>> wlanTable = wlanClientList.getList();
		
		System.out.println("当前接入路由器的无线设备如下：");		
		for(int i = 0, indexOfMAC = -1; i < wlanTable.get(0).size(); i++)	//行打印
		{
			indexOfMAC = dhcpTable.get(1).indexOf(wlanTable.get(0).get(i));	//从WLAN表里读出来MAC地址（第0列），匹配到DHCP表里的顺序上去
			
			System.out.print("设备名："+dhcpTable.get(0).get(indexOfMAC));
			System.out.print("，设备IP地址："+dhcpTable.get(2).get(indexOfMAC));
			System.out.print("，接收包数："+wlanTable.get(6).get(i));
			System.out.println("，发送包数："+wlanTable.get(5).get(i));
		}
		System.out.println();
	}	
}
