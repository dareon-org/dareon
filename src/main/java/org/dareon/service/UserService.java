package org.dareon.service;

import java.util.List;

import org.dareon.domain.User;

public interface UserService {

	public User findByEmail(String email);

	public List<User> list();

	public User save(User user);

}
