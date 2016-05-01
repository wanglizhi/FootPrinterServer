package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.MarketPO;
import bean.PlacePO;

import startUp.DatabaseInit;

import dataServiceImpl.MarketServiceImpl;

public class MarketServiceImplTest {

	public static MarketServiceImpl msi;
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		msi=new MarketServiceImpl(8006);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearchByID() {
		MarketPO m=null;
		try {
			m=msi.searchByID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("西单大悦城", m.getName());
	}

	@Test
	public void testSearchByCityID() {
		ArrayList<PlacePO> mList=null;
		try {
			mList=msi.searchByCityID(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("西单大悦城", mList.get(0).getName());
	}

	@Test
	public void testAddApplause() {
		try {
			assertEquals(true, msi.addApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteApplause() {
		try {
			assertEquals(true, msi.deleteApplause(1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
