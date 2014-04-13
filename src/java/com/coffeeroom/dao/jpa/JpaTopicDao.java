package com.coffeeroom.dao.jpa;

import com.coffeeroom.dao.TopicDao;
import com.coffeeroom.entity.Board;
import com.coffeeroom.entity.Topic;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class JpaTopicDao implements TopicDao {
    @PersistenceContext(unitName = "CoffeeRoomPU")
    private EntityManager em;

    @Override
    public List<Topic> getAllTopics() {
        return em.createNamedQuery("Topic.findAll").getResultList();
    }
    
    @Override
    public List<Topic> findRange(int[] range) {
        Query q = em.createNamedQuery("Topic.findAll");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public Topic findTopicById(int topicId) {
        return em.find(Topic.class, topicId);
    }

    @Override
    public Topic addTopic(Topic topic) {
        em.persist(topic);
        return topic;
    }

    @Override
    public Topic updateTopic(Topic topic) {
        em.merge(topic);
        return topic;
    }

    @Override
    public void removeTopic(Topic topic) {
        Topic mergedTopic = em.merge(topic);
        em.remove(mergedTopic);
    }

    @Override
    public List<Topic> findByBoard(Board board) {
        return em.createNamedQuery("Topic.findByBoardId")
                .setParameter("boardId", board.getId())
                .getResultList();
    }
    
    @Override
    public int count() {
        return em.createNamedQuery("Topic.findAll").getResultList().size();
    }
}
