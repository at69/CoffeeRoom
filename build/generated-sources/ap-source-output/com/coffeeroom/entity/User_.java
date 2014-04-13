package com.coffeeroom.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-04-10T17:02:56")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, Date> creationDate;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> lastname;
    public static volatile SingularAttribute<User, String> login;
    public static volatile SingularAttribute<User, String> connectionToken;
    public static volatile SingularAttribute<User, String> firstname;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Integer> roleId;

}