package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import startUp.DatabaseInit;

import bean.CityPO;

import dataServiceImpl.CityServiceImpl;

public class CityServiceImplTest {
	
	public static CityServiceImpl csi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		csi=new CityServiceImpl(8001);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchByID() {
		CityPO city=null;
		try {
			city=csi.searchByID(4);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("°°É½", city.getName());
		assertEquals(0, city.getApplauseNum());
	}

	@Test
	public void testSearchByName() {
		ArrayList<CityPO> cityList=null;
		try {
			cityList=csi.searchByName("±±¾©");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1, cityList.get(0).getCityID());
		//fail("Not yet implemented");
	}

	@Test
	public void testAddApplause() {
		try {
			assertEquals(false, csi.addApplause(1, 1));
			assertEquals(true, csi.addApplause(1, 5));
			assertEquals(false, csi.addApplause(1, 110));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	@Test
	public void testDeleteApplause() {
		try {
//			assertEquals(false, csi.deleteApplause(2, 1));
			assertEquals(true, csi.deleteApplause(1, 1));
//			assertEquals(false, csi.deleteApplause(1, 110));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	@Test
	public void testCheckApplause() {
		try {
			assertEquals(true, csi.checkApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

}
