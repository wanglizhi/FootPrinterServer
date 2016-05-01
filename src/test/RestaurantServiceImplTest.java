package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.PlacePO;
import bean.RestaurantPO;

import startUp.DatabaseInit;

import dataServiceImpl.RestaurantServiceImpl;

public class RestaurantServiceImplTest {

	public static RestaurantServiceImpl rsi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		rsi=new RestaurantServiceImpl(8006);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchByID() {
		RestaurantPO r=null; 
		try {
			r=rsi.searchByID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("Ò¦¼Ç³´¸Îµê(¹ÄÂ¥µê)", r.getName());
	}

	@Test
	public void testSearchByCityID() {
		ArrayList<PlacePO> rList=null;
		try {
			rList=rsi.searchByCityID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("Ò¦¼Ç³´¸Îµê(¹ÄÂ¥µê)", rList.get(0).getName());
	}

	@Test
	public void testAddApplause() {
		try {
			assertEquals(true, rsi.addApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteApplause() {
		try {
			assertEquals(true, rsi.deleteApplause(1,1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
