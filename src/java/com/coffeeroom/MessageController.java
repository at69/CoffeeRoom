package com.coffeeroom;

import com.coffeeroom.dao.MessageDao;
import com.coffeeroom.dao.UserDao;
import com.coffeeroom.entity.Message;
import com.coffeeroom.entity.Topic;
import com.coffeeroom.entity.User;
import com.coffeeroom.util.JsfUtil;
import com.coffeeroom.util.PaginationHelper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {

    private Message current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @EJB
    private UserDao userDao;
    @EJB
    private MessageDao messageDao;

    public MessageDao getMessageDao() {
        return this.messageDao;
    }

    public MessageController() {
    }

    public Message getSelected() {
        if (current == null) {
            current = new Message();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getMessageDao().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getMessageDao().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
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
        current = (Message) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareView(Message message) {
        current = message;
        return "/topic.xhtml?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Message();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String prepareNewMessage(Topic topic) {
        current = new Message();
        current.setTopicId(topic.getId());
        selectedItemIndex = -1;
        return "/newMessage.xhtml?faces-redirect=true";
    }
    
    public String prepareNewMessage(Topic topic, Message quote) {
        current = new Message();
        User author = userDao.findUserById(quote.getAuthorId());
        String authorInfo = author.getLastname() + " " + author.getFirstname();
        String content = "<blockquote><p>" + quote.getContent() + "</p><small>" + authorInfo + "</small></blockquote>";
        current.setContent(content);
        current.setTopicId(topic.getId());
        selectedItemIndex = -1;
        return "/newMessage.xhtml?faces-redirect=true";
    }

    public String create() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            messageDao.addMessage(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String createNewMessage() {
        try {
            User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
            
            current.setAuthorId(author.getId());
            current.setCreationDate(new Date());
            messageDao.addMessage(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
            return prepareView(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Message) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEdit(Message message) {
        
        current = message;
        return "/editMessage.xhtml?faces-redirect=true";
    }

    public String update() {
        try {
            messageDao.updateMessage(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updateMessage() {
        
        User author = userDao.findUserByConnectionToken(Login.getConnectionToken());
        String authorInfo = author.getLastname() + " " + author.getFirstname();
        DateFormat formatter = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.US);
        //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        //DateFormat tf = DateFormat.getTimeInstance(DateFormat.AM_PM_FIELD, Locale.US);
        Date now = new Date();
        String editDate = (String)formatter.format(now);
        String content = "<br/><br/><p style='text-decoration: italic;'>Edited by " + authorInfo + " on " + editDate + "</p>";
        
        current.setContent(current.getContent()+content);
        
        try {
            messageDao.updateMessage(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
            return "/topic.xhtml?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Message) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyMessage(Message message) {
        current = message;
        performDestroy();
        recreatePagination();
        recreateModel();
        return "topic";
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
            messageDao.removeMessage(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MessageDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = messageDao.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = messageDao.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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
        return JsfUtil.getSelectItems(messageDao.getAllMessages(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(messageDao.getAllMessages(), true);
    }

    public Message getMessage(java.lang.Integer id) {
        return messageDao.findMessageById(id);
    }

    public DataModel findMessageByTopic(Topic topic) {
        if(topic == null)
            return null;
        DataModel messages = new ListDataModel(messageDao.findByTopic(topic));
        return messages;
    }
    
    @FacesConverter(forClass = Message.class)
    public static class MessageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MessageController controller = (MessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "messageController");
            return controller.getMessage(getKey(value));
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
            if (object instanceof Message) {
                Message o = (Message) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Message.class.getName());
            }
        }

    }

}
