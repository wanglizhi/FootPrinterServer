package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.UserPO;

import dataServiceImpl.UserManageServiceImpl;

import startUp.DatabaseInit;
/**
 * 
 * @author SongShuo
 *
 */
public class UserManageServiceImplTest {
	
	public static UserManageServiceImpl umi;

	@Before
	public void setUp() throws Exception {
		DatabaseInit.init();
		umi = new UserManageServiceImpl(8007);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUserManageServiceImpl() {
		//fail("Not yet implemented");
	}

	@Test
	public void testRegister() {
		UserPO user = new UserPO(1, "ss", "123456", null,true,"3321234567","13900000000","song shuo","nju", "_",new Date());
		boolean result = false;
		result= umi.register(user);
		assertEquals(true,result);
		
	}

	@Test
	public void testLogin() {
		UserPO inputUser = new UserPO(1, "ss", "123456", null,false,null,null,null,null,null,null);
		UserPO returnUser = umi.login(inputUser);
		//assertEquals("song shuo",returnUser.getRealName());
		assertEquals("nju",returnUser.getAddress());
		
	}

	@Test
	public void testModifyUser() {
		UserPO inputUser = new UserPO(2, "ssss", "123456", null,true,"0000","0000","song shuo","nju",null,null);
		boolean result = false;
		result = umi.modifyUser(inputUser);
		assertEquals(result, true);
	}

	@Test
	public void testSearchUser() {
		ArrayList<UserPO> userList = null;//=new ArrayList<UserPO>();
		String inputName = "ss";
		userList = umi.searchUser(inputName);
		assertEquals("nju",userList.get(0).getAddress());
		
	}

	@Test
	public void testCreateUserPO() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserNameByID() {
		int inputID = 1;
		String resultName = null;
		resultName = umi.getUserNameByID(inputID);
		assertEquals("ss",resultName);
	}

}
