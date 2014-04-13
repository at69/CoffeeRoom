package com.coffeeroom.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-04-10T17:02:56")
@StaticMetamodel(Board.class)
public class Board_ { 

    public static volatile SingularAttribute<Board, Integer> id;
    public static volatile SingularAttribute<Board, Date> creationDate;
    public static volatile SingularAttribute<Board, String> description;
    public static volatile SingularAttribute<Board, String> name;
    public static volatile SingularAttribute<Board, Integer> categoryId;
    public static volatile SingularAttribute<Board, Integer> authorId;

}