package com.demo.test.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo.dynamic.model.Book;
import com.demo.dynamic.model.User;
import com.demo.dynamic.service.BookService;
import com.demo.dynamic.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring.xml" })
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	private ExecutorService service = Executors.newFixedThreadPool(20);

	@Test
	public void testQuery() {

		User masterUser = userService.getUserMasterById(1L);
		System.out.println(masterUser);

		System.out.println("-------------------------------分割线---------------------------------");

		User slaveUser = userService.getUserSlaveById(1L);
		System.out.println(slaveUser);

	}

	@Test
	public void testQueryThread() {

		for (int i = 0; i < 20; i++) {
			if (i % 2 == 0) {
				service.execute(new Runnable() {

					@Override
					public void run() {
						User masterUser = userService.getUserMasterById(1L);
						System.out.println(masterUser);

					}
				});
			} else {

				service.execute(new Runnable() {

					@Override
					public void run() {
						User slaveUser = userService.getUserSlaveById(1L);
						System.out.println(slaveUser);

					}
				});
			}
		}

		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		service.shutdown();

	}

	@Test
	public void testSave() {
		User master = new User();
		master.setAge(0);
		master.setPassword("master");
		master.setUserName("主机");
		userService.saveMaster(master);

		User slave = new User();
		slave.setAge(100);
		slave.setPassword("slave");
		slave.setUserName("备机");
		userService.saveSlave(slave);
	}

	@Test
	public void testTrans() {
		User master = new User();
		master.setAge(111);
		master.setPassword("1111");
		master.setUserName("testTrans");
		userService.saveMaster(master);

	}

	@Test
	public void testDifTableQuery() {

		for (int i = 0; i < 20; i++) {
			if (i % 2 == 0) {
				service.execute(new Runnable() {

					@Override
					public void run() {
						Book book = bookService.getBookById(1L);
						System.out.println(book);

					}
				});
			} else {

				service.execute(new Runnable() {

					@Override
					public void run() {
						User slaveUser = userService.getUserSlaveById(1L);
						System.out.println(slaveUser);

					}
				});
			}
		}

		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		service.shutdown();

	}

}
