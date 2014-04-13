package com.coffeeroom;

import com.coffeeroom.dao.RoleDao;
import com.coffeeroom.dao.UserDao;
import com.coffeeroom.entity.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("login")
@SessionScoped
public class Login implements Serializable {

    private static String connectionToken = null;

    public static String getConnectionToken() {
        return connectionToken;
    }

    public static void setConnectionToken(String connectionToken) {
        Login.connectionToken = connectionToken;
    }
    
    @EJB
    private UserDao userDao;

    public UserDao getUserDao() {
        return this.userDao;
    }
    
    @EJB
    private RoleDao roleDao;

    public RoleDao getRoleDao() {
        return this.roleDao;
    }
    /**
     * Creates a new instance of Login
     */
    private String username;
    private String password;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public Login() {
    }
    
    public String authenticate() {
        String result;
        User user = userDao.authenticate(this.username, this.password);
        
        if(user == null)
        {
            this.message = "Bad Authentication";
            result = "/login.xhtml?faces-redirect=true";
        }
        else
        {
             setConnectionToken(user.getConnectionToken());
             result = "/home.xhtml?faces-redirect=true&username=" + user.getFirstname();
        }
        return result;
    }
    
    public Boolean checkAuthenticate() {
        if(getConnectionToken() == null || getConnectionToken().length() == 0)
            return false;
        else
            return true;
    }
    
    public String logout() {
        System.out.print("logout");
        setConnectionToken(null);
        return "/home?faces-redirect=true";
    }
    
    public String goToProfile() {
        String token = Login.getConnectionToken();
        if(token != null)
        {
            UserController.setCurrent(userDao.findUserByConnectionToken(token));
        }
        return "/profile.xhtml?faces-redirect=true";
    }
}
