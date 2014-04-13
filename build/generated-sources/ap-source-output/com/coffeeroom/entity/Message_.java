package com.coffeeroom.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-04-10T17:02:56")
@StaticMetamodel(Message.class)
public class Message_ { 

    public static volatile SingularAttribute<Message, Integer> id;
    public static volatile SingularAttribute<Message, String> content;
    public static volatile SingularAttribute<Message, Date> creationDate;
    public static volatile SingularAttribute<Message, Integer> topicId;
    public static volatile SingularAttribute<Message, Integer> authorId;

}