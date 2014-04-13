package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.RoleDao;
import com.coffeeroom.entity.Role;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaRoleDao implements RoleDao {
    @PersistenceContext(unitName = "CoffeeRoomPU")
    private EntityManager em;

    @Override
    public List<Role> getAllRoles() {
        return em.createNamedQuery("Role.findAll").getResultList();
    }
    
    @Override
    public List<Role> findRange(int[] range) {
        Query q = em.createNamedQuery("Role.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public Role findRoleById(int roleId) {
        return em.find(Role.class, roleId);
    }

    @Override
    public Role addRole(Role role) {
        em.persist(role);
        return role;
    }

    @Override
    public Role updateRole(Role role) {
        em.merge(role);
        return role;
    }

    @Override
    public void removeRole(Role role) {
        Role mergedRole = em.merge(role);
        em.remove(mergedRole);
    }
    
    @Override
    public Role findAdminRole() {
        Role role = null;
        
        List<Role> roles = em.createNamedQuery("Role.findByCode")
                            .setParameter("code", "Administrator")
                            .getResultList();

        if(roles.size() == 1)
        {
            role = roles.get(0);
        }
        if(role != null)
        {
            em.merge(role);
        }
        return role;
    }

    @Override
    public Role findModeratorRole() {
        Role role = null;
        
        List<Role> roles = em.createNamedQuery("Role.findByCode")
                            .setParameter("code", "Moderator")
                            .getResultList();

        if(roles.size() == 1)
        {
            role = roles.get(0);
        }
        if(role != null)
        {
            em.merge(role);
        }
        return role;
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("Role.findAll").getResultList().size();
    }
}
