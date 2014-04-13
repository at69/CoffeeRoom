package com.coffeeroom.dao;

import com.coffeeroom.entity.Category;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CategoryDao {
    public List<Category> getAllCategories();
    
    public List<Category> findRange(int[] range);

    public Category findCategoryById(int categoryId);
    
    public Category addCategory(Category category);

    public Category updateCategory(Category category);
    
    public void removeCategory(Category category);
    
    public int count();
}
