package com.coffeeroom.dao;

import javax.ejb.Local;
import com.coffeeroom.entity.User;
import java.util.List;

@Local
public interface UserDao {
    public List<User> getAllUsers();
    
    public List<User> findRange(int[] range);

    public User findUserById(int userId);
    
    public User authenticate(String login, String password);
    
    public User findUserByConnectionToken(String connectionToken);
    
    public User addUser(User user);

    public User updateUser(User user);
    
    public void removeUser(User user);

    public User findByMail(String mail);
    
    public int count();
}
