package com.coffeeroom.dao;

import com.coffeeroom.entity.Topic;
import com.coffeeroom.entity.Board;
import java.util.List;
import javax.ejb.Local;

@Local
public interface TopicDao {
    public List<Topic> getAllTopics();
    
    public List<Topic> findRange(int[] range);

    public Topic findTopicById(int topicId);
    
    public Topic addTopic(Topic topic);

    public Topic updateTopic(Topic topic);
    
    public void removeTopic(Topic topic);
    
    public List<Topic> findByBoard(Board board);
    
    public int count();
}
