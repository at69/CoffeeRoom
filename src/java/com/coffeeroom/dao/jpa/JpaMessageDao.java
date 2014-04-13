package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.MessageDao;
import com.coffeeroom.entity.Message;
import com.coffeeroom.entity.Topic;
import com.coffeeroom.entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaMessageDao implements MessageDao {
    @PersistenceContext(unitName = "CoffeeRoomPU")
    private EntityManager em;

    @Override
    public List<Message> getAllMessages() {
        return em.createNamedQuery("Message.findAll").getResultList();
    }
    
    @Override
    public List<Message> findRange(int[] range) {
        Query q = em.createNamedQuery("Message.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public Message findMessageById(int messageId) {
        return em.find(Message.class, messageId);
    }

    @Override
    public Message addMessage(Message message) {
        em.persist(message);
        return message;
    }

    @Override
    public Message updateMessage(Message message) {
        em.merge(message);
        return message;
    }

    @Override
    public void removeMessage(Message message) {
        Message mergedMessage = em.merge(message);
        em.remove(mergedMessage);
    }

    @Override
    public List<Message> findByTopic(Topic topic) {
        return em.createNamedQuery("Message.findByTopicId")
                .setParameter("topicId", topic.getId())
                .getResultList();
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("Message.findAll").getResultList().size();
    }
    
    @Override
    public int countByAuthor(User user) {
        return em.createNamedQuery("Message.findByAuthorId")
                 .setParameter("authorId", user.getId())
                 .getResultList().size();
    }
}
