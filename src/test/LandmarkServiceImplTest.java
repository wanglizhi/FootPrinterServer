package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.LandmarkPO;
import bean.PlacePO;

import startUp.DatabaseInit;

import dataServiceImpl.LandmarkServiceImpl;

public class LandmarkServiceImplTest {

	public static LandmarkServiceImpl lsi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		lsi=new LandmarkServiceImpl(8004);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchByID() {
		LandmarkPO l=null;
		try {
			l=lsi.searchByID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("天安门广场", l.getName());
	}

	@Test
	public void testSearchByCityID() {
		ArrayList<PlacePO> lList=null;
		try {
			lList=lsi.searchByCityID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("天安门广场", lList.get(0).getName());
	}

	@Test
	public void testAddApplause() {
		try {
			assertEquals(false, lsi.addApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteApplause() {
		try {
			assertEquals(true, lsi.deleteApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
