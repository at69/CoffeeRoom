package com.coffeeroom.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-04-10T17:02:56")
@StaticMetamodel(Topic.class)
public class Topic_ { 

    public static volatile SingularAttribute<Topic, Integer> id;
    public static volatile SingularAttribute<Topic, Date> creationDate;
    public static volatile SingularAttribute<Topic, Integer> boardId;
    public static volatile SingularAttribute<Topic, String> description;
    public static volatile SingularAttribute<Topic, String> name;
    public static volatile SingularAttribute<Topic, Boolean> isLocked;
    public static volatile SingularAttribute<Topic, Integer> authorId;

}