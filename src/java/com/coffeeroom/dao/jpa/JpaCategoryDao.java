package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.CategoryDao;
import com.coffeeroom.entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaCategoryDao implements CategoryDao {
    @PersistenceContext(unitName = "CoffeeRoomPU")
    private EntityManager em;

    @Override
    public List<Category> getAllCategories() {
        return em.createNamedQuery("Category.findAll").getResultList();
    }
    
    @Override
    public List<Category> findRange(int[] range) {
        Query q = em.createNamedQuery("Category.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public Category findCategoryById(int categoryId) {
        return em.find(Category.class, categoryId);
    }

    @Override
    public Category addCategory(Category category) {
        em.persist(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        em.merge(category);
        return category;
    }

    @Override
    public void removeCategory(Category category) {
        Category mergedCategory =  em.merge(category);
        em.remove(mergedCategory);
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("Category.findAll").getResultList().size();
    }
}
