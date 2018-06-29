package com.demo.dynamic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dynamic.helpper.DataSource;
import com.demo.dynamic.mapper.UserMapper;
import com.demo.dynamic.model.User;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public User getUserById(Long id) {

		return userMapper.selectByPrimaryKey(id);
	}

	public void save(User user) {
		userMapper.insert(user);
	}

	@DataSource("master")
	public User getUserMasterById(Long id) {

		return userMapper.selectByPrimaryKey(id);
	}

	@DataSource("slave")
	public User getUserSlaveById(Long id) {

		return userMapper.selectByPrimaryKey(id);
	}

	@DataSource("master")
	public void saveMaster(User user) {
		userMapper.insert(user);
		String str = null;
		if (str.equals("")) {
			int i = 1;
		}
	}

	@DataSource("slave")
	public void saveSlave(User user) {
		userMapper.insert(user);
	}

}
