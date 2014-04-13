package com.coffeeroom;

import com.coffeeroom.dao.BoardDao;
import com.coffeeroom.dao.TopicDao;
import com.coffeeroom.dao.UserDao;
import com.coffeeroom.entity.Board;
import com.coffeeroom.entity.Category;
import com.coffeeroom.entity.Topic;
import com.coffeeroom.entity.User;
import com.coffeeroom.util.JsfUtil;
import com.coffeeroom.util.PaginationHelper;
import com.coffeeroom.util.RepeatPaginator;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("boardController")
@SessionScoped
public class BoardController implements Serializable {

    private Board current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    private List<Topic> topics = null;
    private RepeatPaginator paginator;
    
    @EJB
    private BoardDao boardDao;
    
    @EJB
    private TopicDao topicDao;
    
    @EJB
    private UserDao userDao;

    public BoardDao getBoardDao() {
        return boardDao;
    }

    public BoardController() {
    }

    public RepeatPaginator getPaginator() {
        if(paginator == null)
        {
            paginator = new RepeatPaginator(topics);
        }
        return paginator;
    }
    
    public void setPaginator(List<?> list) {
        paginator = new RepeatPaginator(list);
    }
    
    public int getSize() {
        return paginator.getModel().size();
    }
    
    public List<Topic> getTopics(Board board) {
        topics = topicDao.findByBoard(board);
        getPaginator().setModel(topics);
        //System.out.print("topics non null");
        //System.out.print(topics.size());
        return topics;
    }
    
    public List<Topic> getTopics() {
        return topics;
    }
    
    /*public void updateTopics() {
        topics = null;
        paginator = null;
        System.out.print("update");
    }*/
    
    public Board getSelected() {
        if (current == null) {
            current = new Board();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getBoardDao().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getBoardDao().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
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
        current = (Board) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareView(Board board) {
        current = board;
        return "/board.xhtml?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Board();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            boardDao.addBoard(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoardCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareCreateBoard() {
        current = new Board();
        selectedItemIndex = -1;
        return "/administration.xhtml#boards?faces-redirect=true";
    }

    public String createBoard() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            
            boardDao.addBoard(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoardCreated"));
            recreateModel();
            return prepareCreateBoard();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareEdit() {
        current = (Board) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditBoard() {
        current = (Board) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/editBoard.xhtml?faces-redirect=true";
    }

    public String update() {
        try {
            boardDao.updateBoard(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoardUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String updateBoard() {
        try {
            boardDao.updateBoard(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoardUpdated"));
            return "/administration.xhtml#boards?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String destroy() {
        current = (Board) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyBoard() {
        current = (Board) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "/administration.xhtml#boards?faces-redirect=true";
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
            boardDao.removeBoard(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoardDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = boardDao.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = boardDao.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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

    public String previousBoards() {
        getPagination().previousPage();
        recreateModel();
        return "/administration.xhtml#boards?faces-redirect=true";
    }
    
    public String nextBoards() {
        getPagination().nextPage();
        recreateModel();
        return "/administration.xhtml#boards?faces-redirect=true";
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(boardDao.getAllBoards(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(boardDao.getAllBoards(), true);
    }

    public Board getBoard(java.lang.Integer id) {
        return boardDao.findBoardById(id);
    }
    
    public DataModel findBoardByCategory(Category category) {
        if(category == null)
            return null;
        DataModel boards = new ListDataModel(boardDao.findByCategory(category));
        return boards;        
    }

    @FacesConverter(forClass = Board.class)
    public static class BoardControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BoardController controller = (BoardController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "boardController");
            return controller.getBoard(getKey(value));
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
            if (object instanceof Board) {
                Board o = (Board) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Board.class.getName());
            }
        }

    }

}
