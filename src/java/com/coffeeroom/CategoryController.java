package com.coffeeroom;

import com.coffeeroom.dao.CategoryDao;
import com.coffeeroom.dao.UserDao;
import com.coffeeroom.entity.Category;
import com.coffeeroom.entity.User;
import com.coffeeroom.util.JsfUtil;
import com.coffeeroom.util.PaginationHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named("categoryController")
@SessionScoped
public class CategoryController implements Serializable {

    private Category current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @EJB
    private UserDao userDao;
    @EJB
    private CategoryDao categoryDao;

    public CategoryDao getCategoryDao() {
        return this.categoryDao;
    }

    public CategoryController() {
    }

    public Category getSelected() {
        if (current == null) {
            current = new Category();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getCategoryDao().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getCategoryDao().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Category) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Category();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            categoryDao.addCategory(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareCreateCategory() {
        current = new Category();
        selectedItemIndex = -1;
        return "/administration.xhtml?faces-redirect=true";
    }
    
    public String createCategory() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            categoryDao.addCategory(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryCreated"));
            recreateModel();
            return prepareCreateCategory();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Category) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditCategory() {
        current = (Category) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/editCategory.xhtml?faces-redirect=true";
    }

    public String update() {
        try {
            categoryDao.updateCategory(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updateCategory() {
        try {
            categoryDao.updateCategory(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryUpdated"));
            return "/administration.xhtml?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Category) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyCategory() {
        current = (Category) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/administration.xhtml?faces-redirect=true";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            categoryDao.removeCategory(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CategoryDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = categoryDao.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = categoryDao.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public String nextCategories() {
        getPagination().nextPage();
        recreateModel();
        return "/administration.xhtml?faces-redirect=true";
    }

    public String previousCategories() {
        getPagination().previousPage();
        recreateModel();
        return "/administration.xhtml?faces-redirect=true";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(categoryDao.getAllCategories(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(categoryDao.getAllCategories(), true);
    }

    public Category getCategory(java.lang.Integer id) {
        return categoryDao.findCategoryById(id);
    }

    @FacesConverter(forClass = Category.class)
    public static class CategoryControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoryController controller = (CategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoryController");
            return controller.getCategory(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Category) {
                Category o = (Category) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Category.class.getName());
            }
        }

    }

}
