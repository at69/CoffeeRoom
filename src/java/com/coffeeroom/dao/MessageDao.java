package com.coffeeroom.dao;

import com.coffeeroom.entity.Message;
import com.coffeeroom.entity.Topic;
import com.coffeeroom.entity.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MessageDao {
    public List<Message> getAllMessages();
    
    public List<Message> findRange(int[] range);

    public Message findMessageById(int messageId);
    
    public Message addMessage(Message message);

    public Message updateMessage(Message message);
    
    public void removeMessage(Message message);
    
    public List<Message> findByTopic(Topic topic);
    
    public int count();
    
    public int countByAuthor(User user);
}
