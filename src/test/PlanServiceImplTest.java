package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.PlanItemPO;
import bean.PlanPO;

import startUp.DatabaseInit;

import dataServiceImpl.PlanServiceImpl;

public class PlanServiceImplTest {

	public static PlanServiceImpl psi;

	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		psi = new PlanServiceImpl(8007);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testAdd() {
		// fail("Not yet implemented");
		PlanPO po = new PlanPO(146, 36, 10000, new Date(), new Date(),
				"happy plan");
		int result = -1;
		try {
			result = psi.add(po);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(34, result);
	}

	@Test
	public void testDelete() {
		// fail("Not yet implemented");
		PlanPO po = new PlanPO(146, 37, 1024, new Date(), new Date(),
				"sad plan");
		boolean result = false;

		try {
			psi.add(po);
			result = psi.delete(po.getPlanID());
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testModify() {
		// fail("Not yet implemented");
		PlanPO po = new PlanPO(146, 37, 1024, new Date(), new Date(),
				"sad plan");
		boolean result = false;

		try {
			psi.add(po);
			PlanPO po2 = new PlanPO(146, 37, 1024, new Date(), new Date(),
					"not too sad");
			result = psi.modify(po2);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddApplause() {
		// fail("Not yet implemented");
		try {
			boolean result = psi.addApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testDeleteApplause() {
		// fail("Not yet implemented");
		try {
			boolean result = psi.deleteApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckApplause() {
		// fail("Not yet implemented");
		try {
			psi.addApplause(1, 1);
			boolean result = psi.checkApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddPlanItem() {
		// fail("Not yet implemented");
		PlanItemPO po = new PlanItemPO(1, "nanjing university", new Date(), 0,
				0, true, 0, 240, "very happy");
		po.setLandmarkType(1);
		po.setLanmarkID(1);
		boolean result = false;
		try {
			result = psi.addPlanItem(po);
			assertEquals(result, true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeletePlanItem() {
		// fail("Not yet implemented");
		PlanItemPO po = new PlanItemPO(1, "qixia mountain", new Date(), 1, 0,
				true, 240, 300, "tired");
		po.setLandmarkType(1);
		po.setLanmarkID(1);
		boolean result = false;
		try {
			psi.addPlanItem(po);
			result = psi.deletePlanItem(po.getPlanItemID());
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testModifyPlanItem() {
		// fail("Not yet implemented");
		PlanItemPO po = new PlanItemPO(1, "qixia mountain", new Date(), 1, 0,
				true, 240, 300, "tired");
		boolean result = false;
		try {
			psi.addPlanItem(po);
			PlanItemPO po2 = new PlanItemPO(1, "qixia mountain", new Date(), 1,
					0, true, 240, 320, "hah");
			result = psi.modifyPlanItem(po2);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPlanItems() {
		try {
			ArrayList<PlanItemPO> result = psi.getPlanItems(1);
			assertEquals("", result.get(1).getLandmarkName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSearchByID() {
		try {
			PlanPO po=psi.searchByID(1);
			assertEquals("sad plan", po.getTitle());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testSearchByCity() {
		//fail("Not yet implemented");
		try {
			ArrayList<PlanPO> pos=psi.searchByCity(36);
			assertEquals("happy plan", pos.get(0).getTitle());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPlanItems2() {
		//fail("Not yet implemented");
		try {
			ArrayList<PlanItemPO> pos=psi.getPlanItems(1,new Date());
			assertEquals("happy plan", pos.get(0).getComment());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test(){
		try {
			psi.addFavourite(1, 1);
			psi.checkFavourite(1, 1);
			psi.deleteFavourite(1, 1);
			psi.getAllFavourite(1, 1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSearch() {
		//fail("Not yet implemented");
		
	}

}
