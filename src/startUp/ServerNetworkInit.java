package startUp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

import dataServiceImpl.BlogServiceImpl;
import dataServiceImpl.CityServiceImpl;
import dataServiceImpl.EntertainmentServiceImpl;
import dataServiceImpl.HotelServiceImpl;
import dataServiceImpl.LandmarkServiceImpl;
import dataServiceImpl.MarketServiceImpl;
import dataServiceImpl.PlanServiceImpl;
import dataServiceImpl.RecommandServiceImpl;
import dataServiceImpl.RestaurantServiceImpl;
import dataServiceImpl.UploadServiceImpl;
import dataServiceImpl.UserManageServiceImpl;

public class ServerNetworkInit {

	public static void init(){
		try {
			RMISocketFactory.setSocketFactory(new SMRMISocket());
			LocateRegistry.createRegistry(8012);
			LandmarkServiceImpl landmarkService=new LandmarkServiceImpl(8012);		
			Naming.bind("rmi://localhost:8012/Landmark", landmarkService);
			CityServiceImpl cityService=new CityServiceImpl(8002);
			LocateRegistry.createRegistry(8002);
			Naming.bind("rmi://localhost:8002/City", cityService);
			HotelServiceImpl hotelService=new HotelServiceImpl(8003);
			LocateRegistry.createRegistry(8003);
			Naming.bind("rmi://localhost:8003/Hotel", hotelService);
			RestaurantServiceImpl restaurantService=new RestaurantServiceImpl(8004);
			LocateRegistry.createRegistry(8004);
			Naming.bind("rmi://localhost:8004/Restaurant", restaurantService);
			EntertainmentServiceImpl entertainmentServie=new EntertainmentServiceImpl(8005);
			LocateRegistry.createRegistry(8005);
			Naming.bind("rmi://localhost:8005/Entertainment", entertainmentServie);
			MarketServiceImpl marketService=new MarketServiceImpl(8006);
			LocateRegistry.createRegistry(8006);
			Naming.bind("rmi://localhost:8006/Market", marketService);
			UserManageServiceImpl userService=new UserManageServiceImpl(8007);
			LocateRegistry.createRegistry(8007);
			Naming.bind("rmi://localhost:8007/User", userService);
			PlanServiceImpl planService=new PlanServiceImpl(8008);
			LocateRegistry.createRegistry(8008);
			Naming.bind("rmi://localhost:8008/Plan", planService);
			BlogServiceImpl blogService=new BlogServiceImpl(8009);
			LocateRegistry.createRegistry(8009);
			Naming.bind("rmi://localhost:8009/Blog", blogService);
			RecommandServiceImpl recommandService=new RecommandServiceImpl(8010);
			LocateRegistry.createRegistry(8010);
			Naming.bind("rmi://localhost:8010/Recommand", recommandService);
			UploadServiceImpl uploadService=new UploadServiceImpl(8011);
			LocateRegistry.createRegistry(8011);
			Naming.bind("rmi://localhost:8011/Upload", uploadService);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("创建远程对象发生异常！"); 
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("发生URL畸形异常！");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("发生重复绑定对象异常！"); 
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ServerNetwork init end!");
	}
}
