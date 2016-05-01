package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.EntertainmentPO;
import bean.PlacePO;

import startUp.DatabaseInit;

import dataServiceImpl.EntertainmentServiceImpl;

public class EntertainmentServiceImplTest {

	public static EntertainmentServiceImpl esi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		esi=new EntertainmentServiceImpl(8002);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchByID() {
		EntertainmentPO e=null;
		try {
			e=esi.searchByID(1);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertEquals("À¶÷ìç³ÎÅ", e.getName());
		assertEquals(1, e.getCityID());
	}

	@Test
	public void testSearchByCityID() {
		ArrayList<PlacePO> eList=null;
		try {
			eList=esi.searchByCityID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("À¶÷ìç³ÎÅ", eList.get(0).getName());
	}

	@Test
	public void testAddApplause() {
		try {
			assertEquals(false, esi.addApplause(1, 1));
			assertEquals(true, esi.addApplause(1, 2));
			assertEquals(false, esi.addApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testDeleteApplause() {
		try {
			assertEquals(true, esi.deleteApplause(1, 2));
			assertEquals(false, esi.deleteApplause(10, 2));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckApplause() {
		fail("Not yet implemented");
	}


}
