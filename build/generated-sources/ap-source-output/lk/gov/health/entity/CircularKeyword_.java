package lk.gov.health.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.KeyWord;
import lk.gov.health.entity.WebUser;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-19T15:38:16")
@StaticMetamodel(CircularKeyword.class)
public class CircularKeyword_ { 

    public static volatile SingularAttribute<CircularKeyword, Long> id;
    public static volatile SingularAttribute<CircularKeyword, Circular> circular;
    public static volatile SingularAttribute<CircularKeyword, Date> createdAt;
    public static volatile SingularAttribute<CircularKeyword, String> name;
    public static volatile SingularAttribute<CircularKeyword, WebUser> retirer;
    public static volatile SingularAttribute<CircularKeyword, Long> searchCount;
    public static volatile SingularAttribute<CircularKeyword, Boolean> retired;
    public static volatile SingularAttribute<CircularKeyword, WebUser> creater;
    public static volatile SingularAttribute<CircularKeyword, Date> retiredAt;
    public static volatile SingularAttribute<CircularKeyword, KeyWord> keyWord;

}