package lk.gov.health.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import lk.gov.health.data.CircularLanguage;
import lk.gov.health.entity.AdministrativeDivision;
import lk.gov.health.entity.Category;
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.Person;
import lk.gov.health.entity.SigningAuthority;
import lk.gov.health.entity.WebUser;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-07-24T10:42:07")
@StaticMetamodel(Circular.class)
public class Circular_ { 

    public static volatile SingularAttribute<Circular, Person> person;
    public static volatile SingularAttribute<Circular, Date> replacedAt;
    public static volatile SingularAttribute<Circular, Boolean> retired;
    public static volatile SingularAttribute<Circular, WebUser> creater;
    public static volatile SingularAttribute<Circular, String> circularNumber;
    public static volatile SingularAttribute<Circular, Date> circularDate;
    public static volatile SingularAttribute<Circular, byte[]> baImage;
    public static volatile SingularAttribute<Circular, Long> id;
    public static volatile SingularAttribute<Circular, String> description;
    public static volatile SingularAttribute<Circular, Date> createdAt;
    public static volatile SingularAttribute<Circular, String> name;
    public static volatile SingularAttribute<Circular, WebUser> retirer;
    public static volatile SingularAttribute<Circular, String> retireComments;
    public static volatile SingularAttribute<Circular, CircularLanguage> circularLanguage;
    public static volatile SingularAttribute<Circular, Boolean> internal;
    public static volatile SingularAttribute<Circular, String> topic;
    public static volatile SingularAttribute<Circular, String> fileType;
    public static volatile SingularAttribute<Circular, String> keywords;
    public static volatile SingularAttribute<Circular, WebUser> replacedUser;
    public static volatile SingularAttribute<Circular, String> contents;
    public static volatile SingularAttribute<Circular, AdministrativeDivision> administrativeDivision;
    public static volatile SingularAttribute<Circular, SigningAuthority> signingAuthority;
    public static volatile SingularAttribute<Circular, String> code;
    public static volatile SingularAttribute<Circular, String> originatingFileNo;
    public static volatile SingularAttribute<Circular, Circular> replaceBy;
    public static volatile SingularAttribute<Circular, Boolean> replaced;
    public static volatile SingularAttribute<Circular, Category> category;
    public static volatile SingularAttribute<Circular, String> fileName;
    public static volatile SingularAttribute<Circular, Date> retiredAt;
    public static volatile SingularAttribute<Circular, Boolean> circularLetter;

}