package startUp;
import java.util.ArrayList;

import sun.rmi.transport.tcp.*;

public class ServerRunner {

	/**
	 * @param args
	 */
	public static ArrayList<String> clients=new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseInit.init();
		ServerNetworkInit.init();
		System.out.println("Server IP: "+DatabaseHelper.getIP());
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while(true){
//					System.out.println("haha");
////					ServerNetworkInit.userService.clientHost();
//					for(String s:clients){
//						System.out.println(s);
//					}
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			
//		}).start();
	}
	public static void add(String s){
		System.out.println("add"+s);
		clients.add(s);
	}

}
