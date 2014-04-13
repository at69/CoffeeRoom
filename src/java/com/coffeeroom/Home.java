package com.coffeeroom;

import com.coffeeroom.dao.UserDao;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

@Named(value = "home")
@Dependent
public class Home {

    @EJB
    private UserDao userDao;
    /**
     * Creates a new instance of Admin
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Home() {
    }
    
    @PostConstruct
    public void initUsername() {
        String token = Login.getConnectionToken();
        if(token != null)
        {
            this.username = userDao.findUserByConnectionToken(token).getLogin();
        }
    }
}
