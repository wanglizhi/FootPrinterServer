package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.BlogPO;
import bean.PlanPO;

import startUp.DatabaseInit;

import dataServiceImpl.BlogServiceImpl;
import dataServiceImpl.PlanServiceImpl;


public class BlogServiceImplTest {
	
	public static BlogServiceImpl bsi;
	
	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		bsi = new BlogServiceImpl(8007);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		BlogPO po=new BlogPO(1,1,"前两个是userID和cityID，这是title",new Date());
		po.setPublishTime(new Date());
		int result = -1;
		try {
			result = bsi.add(po);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1, result);
	}

	@Test
	public void testDelete() {
		BlogPO po=new BlogPO(1,1,"前两个是userID和cityID，这是title",new Date());
		boolean result = false;

		try {
			bsi.add(po);
			result = bsi.delete(po.getBlogID());
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testModify() {
		BlogPO po=new BlogPO(1,1,"前两个是userID和cityID，这是title",new Date());
		boolean result = false;

		try {
			bsi.add(po);
			BlogPO po2=new BlogPO(1,2,"前两个是userID和cityID，这是title",new Date());
			result = bsi.modify(po2);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddApplause() {
		try {
			boolean result = bsi.addApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteApplause() {
		try {
			boolean result = bsi.deleteApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCheckApplause() {
		try {
			bsi.addApplause(1, 1);
			boolean result=bsi.checkApplause(1, 1);
			assertEquals(true, result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSearchByID() {
		BlogPO po=new BlogPO(1, 1, "aa",new Date());
		try {
			//bsi.add(po);
			BlogPO po2=bsi.searchByID(1);	
			assertEquals("aa",po2.getTitle());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testSearchByCity() {
		BlogPO po=new BlogPO(1, 1, "aa",new Date());
		try {
			bsi.add(po);
			ArrayList<BlogPO> po2=bsi.searchByCityID(1);	
			assertEquals("aa",po2.get(0).getTitle());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Test
	public void testSearchByTitle() {
		BlogPO po=new BlogPO(1, 1, "aa",new Date());
		try {
			bsi.add(po);
			ArrayList<BlogPO> po2=bsi.searchByUserID(1);	
			assertEquals("aa",po2.get(0).getTitle());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Test
	public void	testSearch(){
		BlogPO po=new BlogPO(1, 1, "aa",new Date());
		try {
			bsi.add(po);
			ArrayList<BlogPO> po2=bsi.search("a");	
			assertEquals(1,po2.get(0).getBlogID());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
