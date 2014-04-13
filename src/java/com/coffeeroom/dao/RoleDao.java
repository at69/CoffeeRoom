package com.coffeeroom.dao;

import com.coffeeroom.entity.Role;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RoleDao {
    public List<Role> getAllRoles();
    
    public List<Role> findRange(int[] range);

    public Role findRoleById(int roleId);
    
    public Role addRole(Role role);

    public Role updateRole(Role role);
    
    public void removeRole(Role role);

    public Role findAdminRole();
    
    public Role findModeratorRole();
    
    public int count();
}
