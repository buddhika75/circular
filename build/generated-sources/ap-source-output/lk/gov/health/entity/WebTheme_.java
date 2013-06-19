package lk.gov.health.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lk.gov.health.entity.WebUser;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-19T15:38:16")
@StaticMetamodel(WebTheme.class)
public class WebTheme_ { 

    public static volatile SingularAttribute<WebTheme, Long> id;
    public static volatile SingularAttribute<WebTheme, String> description;
    public static volatile SingularAttribute<WebTheme, Date> createdAt;
    public static volatile SingularAttribute<WebTheme, String> name;
    public static volatile SingularAttribute<WebTheme, WebUser> retirer;
    public static volatile SingularAttribute<WebTheme, Boolean> retired;
    public static volatile SingularAttribute<WebTheme, String> retireComments;
    public static volatile SingularAttribute<WebTheme, WebUser> creater;
    public static volatile SingularAttribute<WebTheme, Date> retiredAt;

}