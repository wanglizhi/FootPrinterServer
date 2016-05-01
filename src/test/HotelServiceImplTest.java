package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.HotelPO;
import bean.PlacePO;

import startUp.DatabaseInit;

import dataServiceImpl.HotelServiceImpl;

public class HotelServiceImplTest {

	public static HotelServiceImpl hsi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		hsi=new HotelServiceImpl(8003);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSearchByID(){
		HotelPO h=null;
		try {
			h=hsi.searchByID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("±±¾©å¾Óî¾©º½±ö¹Ý", h.getName());
		assertEquals(1, h.getCityID());
	}
	
	@Test
	public void testSearchByCityID(){
		ArrayList<PlacePO> hList=null;
		try {
			hList=hsi.searchByCityID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("±±¾©å¾Óî¾©º½±ö¹Ý", hList.get(0).getName());
	}
	
	@Test
	public void testAddApplause(){
		try {
			assertEquals(true, hsi.addApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
