package com.coffeeroom;

import com.coffeeroom.dao.MessageDao;
import com.coffeeroom.dao.TopicDao;
import com.coffeeroom.dao.UserDao;
import com.coffeeroom.entity.Board;
import com.coffeeroom.entity.Message;
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
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named("topicController")
@SessionScoped
public class TopicController implements Serializable {

    private Topic current;
    private Topic parentTopic;
    private Topic childTopic;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private List<Message> messages = null;
    private RepeatPaginator paginator;
    
    @EJB
    private UserDao userDao;
    @EJB
    private MessageDao messageDao;
    @EJB
    private TopicDao topicDao;

    public int getSize() {
        return paginator.getModel().size();
    }
    
    public TopicDao getTopicDao() {
        return this.topicDao;
    }

    public Topic getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(Topic topicParent) {
        this.parentTopic = topicParent;
    }

    public Topic getChildTopic() {
        return childTopic;
    }

    public void setChildTopic(Topic topicChild) {
        this.childTopic = topicChild;
    }

    public TopicController() {
    }

    public Topic getSelected() {
        if (current == null) {
            current = new Topic();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getTopicDao().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getTopicDao().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    public RepeatPaginator getPaginator() {
        if(paginator == null)
        {
            paginator = new RepeatPaginator(messages);
        }
        return paginator;
    }
    
    public void setPaginator(List<?> list) {
        paginator = new RepeatPaginator(list);
    }

    public List<Message> getMessages(Topic topic) {
        messages = messageDao.findByTopic(topic);
        getPaginator().setModel(messages);
        return messages;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Topic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareView(Topic topic) {
        current = topic;
        return "/topic.xhtml?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Topic();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String prepareNewTopic(Board board) {
        current = new Topic();
        current.setBoardId(board.getId());
        return "/newTopic.xhtml?faces-redirect=true";
    }

    public String create() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            current.setIsLocked(false);
            topicDao.addTopic(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TopicCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String createNewTopic() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            current.setIsLocked(false);
            topicDao.addTopic(current);
           
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TopicCreated"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Topic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEdit(Topic topic) {
        current = topic;
        return "/editTopic.xhtml?faces-redirect=true";
    }

    public String update() {
        try {
            topicDao.updateTopic(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TopicUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updateTopic() {
        try {
            topicDao.updateTopic(current);
            //paginator.updateModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TopicUpdated"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void topicBoardChanged(ValueChangeEvent e) {
        current = (Topic) ((UIInput) e.getSource()).getAttributes().get("topic");
        
        String newBoard = e.getNewValue().toString();
        int boardId = Integer.parseInt(newBoard);
        current.setBoardId(boardId);
        
        updateTopic();
    }

    public String destroy() {
        current = (Topic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyTopic(Topic topic) {
        current = topic;
        performDestroy();
        return "/board.xhtml?faces-redirect=true";
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
            topicDao.removeTopic(current);
            //paginator.updateModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TopicDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = topicDao.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = topicDao.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(topicDao.getAllTopics(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(topicDao.getAllTopics(), true);
    }

    public Topic getTopic(java.lang.Integer id) {
        return topicDao.findTopicById(id);
    }

    public String merge() {
        if(parentTopic.getId() == childTopic.getId())
        {
            JsfUtil.addErrorMessage("Merged topics must be different");
            return "Merge";
        }
        else
        {
            List<Message> childMessages = messageDao.findByTopic(childTopic);
            System.out.print(childMessages.size());
            for(Message m : childMessages)
            {
                m.setTopicId(parentTopic.getId());
                messageDao.updateMessage(m);
            }
            performDestroy();
            recreatePagination();
            recreateModel();
            return "List";
        }
    }
    
    public String mergeTopics(Topic childTopic, Topic parentTopic) {
        
        //current = childTopic;
        List<Message> messagesToMove = messageDao.findByTopic(childTopic);
        for(Message m : messagesToMove) {
            m.setTopicId(parentTopic.getId());
            messageDao.updateMessage(m);
        }
        
        try {
            topicDao.removeTopic(childTopic);
            //paginator.updateModel();
            JsfUtil.addSuccessMessage("Topics merged successfully");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }  
        
        //current = parentTopic;
        System.out.print("call merge");
        return prepareView(parentTopic);
    }
    
    public void mergeTopicListener(ValueChangeEvent e) { 
        childTopic = (Topic) ((UIInput) e.getSource()).getAttributes().get("childTopic");
        
        String destination = e.getNewValue().toString();
        int topicId = Integer.parseInt(destination);
        if(topicId != 0) {
            parentTopic = getTopic(topicId);
            if(childTopic != null && parentTopic != null)
                mergeTopics(childTopic, parentTopic);
        }
    }
    
    public Boolean checkLocked(Boolean value) {
        current = (Topic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return current.getIsLocked() == value;
    }
    
    public void lock() {
        current = (Topic) getItems().getRowData();
        current.setIsLocked(true);
        update();
        updateCurrentItem();
    }
    
    public void unlock() {
        current = (Topic) getItems().getRowData();
        current.setIsLocked(false);
        update();
        updateCurrentItem();
    }
    
    public void lock(Topic topic) {
        current = topic;
        current.setIsLocked(true);
        updateTopic();
    }
    
    public void unlock(Topic topic) {
        current = topic;
        current.setIsLocked(false);
        updateTopic();
    }
    
    public List<Topic> findTopicByBoard(Board board) {
        if(board == null) {
            return null;
        }
        List<Topic> topics = topicDao.findByBoard(board);
        return topics;
    }
    
    @FacesConverter(forClass = Topic.class)
    public static class TopicControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TopicController controller = (TopicController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "topicController");
            return controller.getTopic(getKey(value));
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
            if (object instanceof Topic) {
                Topic o = (Topic) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Topic.class.getName());
            }
        }
    }

}
