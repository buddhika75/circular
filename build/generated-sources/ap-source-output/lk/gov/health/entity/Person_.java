package lk.gov.health.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lk.gov.health.entity.WebUser;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-19T15:38:16")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, Long> id;
    public static volatile SingularAttribute<Person, String> description;
    public static volatile SingularAttribute<Person, Date> createdAt;
    public static volatile SingularAttribute<Person, String> name;
    public static volatile SingularAttribute<Person, WebUser> retirer;
    public static volatile SingularAttribute<Person, Boolean> retired;
    public static volatile SingularAttribute<Person, String> retireComments;
    public static volatile SingularAttribute<Person, WebUser> creater;
    public static volatile SingularAttribute<Person, Date> retiredAt;

}