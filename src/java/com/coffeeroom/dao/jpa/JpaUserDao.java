package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.UserDao;
import com.coffeeroom.encryptmanager.EncryptManager;
import com.coffeeroom.entity.User;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaUserDao implements UserDao {
    @PersistenceContext(name="ECursus-ejbPU")
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        return em.createNamedQuery("User.findAll").getResultList();
    }
    
    @Override
    public List<User> findRange(int[] range) {
        Query q = em.createNamedQuery("User.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public User findUserById(int userId) {
        return em.find(User.class, userId);
    }

    @Override
    public User findUserByConnectionToken(String connectionToken) {
        User user = null;
        List<User> users = em.createNamedQuery("User.findByConnectionToken")
                        .setParameter("connectionToken", connectionToken)
                        .getResultList();
            
        if(users.size() == 1)
        {
            user = users.get(0);
        }
        return user;
    }

    @Override
    public User authenticate(String login, String password) {
        String hashedPassword;
        User user = null;
        try {
            hashedPassword = EncryptManager.getInstance().encrypt(password);
            List<User> users = em.createNamedQuery("User.findByAuthentication")
                                .setParameter("login", login)
                                .setParameter("password", hashedPassword)
                                .getResultList();
            
            if(users.size() == 1)
            {
                user = users.get(0);
            }
            if(user != null)
            {
                UUID token = UUID.randomUUID();
                user.setConnectionToken(token.toString());
                
                em.merge(user);
                return user;
            }
        } catch (Exception ex) {
            Logger.getLogger(JpaUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public User addUser(User user) {
        String hashedPassword;
        try {
            hashedPassword = EncryptManager.getInstance().encrypt(user.getPassword());
            user.setPassword(hashedPassword);
        } catch (Exception ex) {
            Logger.getLogger(JpaUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        em.merge(user);
        return user;
    }

    @Override
    public void removeUser(User user) {
        User mergedUser = em.merge(user);
        em.remove(mergedUser);
    }

    @Override
    public User findByMail(String mail) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                        .setParameter("email", mail);
        
        List<User> users = query.getResultList();
        User user = null;
        
        if(users.size() == 1)
        {
            user = users.get(0);
        }
        return user;
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("User.findAll").getResultList().size();
    }
}
