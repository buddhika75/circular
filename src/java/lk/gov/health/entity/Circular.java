/*
 * Author
 * Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 */
package lk.gov.health.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lk.gov.health.data.CircularLanguage;

/**
 *
 * @author buddhika
 */
@Entity
public class Circular implements Serializable {
    @OneToMany(mappedBy = "circular",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CircularKeyword> circularKeywords;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//Main Properties
    String name;
    String code;
    String description;
    private boolean circularLetter;
    //Created Properties
    @ManyToOne
    WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date createdAt;
    //Retairing properties
    boolean retired;
    @ManyToOne
    WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date retiredAt;
    String retireComments;
    @ManyToOne
    Person person;
    @ManyToOne
    Category category;
    String circularNumber;
    String topic;
    Boolean internal;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String contents;
    @ManyToOne
    AdministrativeDivision administrativeDivision;
    String originatingFileNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date circularDate;
    CircularLanguage circularLanguage;
    @ManyToOne
    SigningAuthority signingAuthority;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] baImage;
    String fileName;
    String fileType;
    String keywords;
    
    
    private Boolean replaced;
    @ManyToOne
    private Circular replaceBy;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date replacedAt;
    @ManyToOne
    private WebUser replacedUser;

    public List<CircularKeyword> getCircularKeywords() {
        return circularKeywords;
    }

    public void setCircularKeywords(List<CircularKeyword> circularKeywords) {
        this.circularKeywords = circularKeywords;
    }
    
    
   
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    

    public String getCircularNumber() {
        return circularNumber;
    }

    public void setCircularNumber(String circularNumber) {
        this.circularNumber = circularNumber;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public AdministrativeDivision getAdministrativeDivision() {
        return administrativeDivision;
    }

    public void setAdministrativeDivision(AdministrativeDivision administrativeDivision) {
        this.administrativeDivision = administrativeDivision;
    }

    public String getOriginatingFileNo() {
        return originatingFileNo;
    }

    public void setOriginatingFileNo(String originatingFileNo) {
        this.originatingFileNo = originatingFileNo;
    }

    public Date getCircularDate() {
        return circularDate;
    }

    public void setCircularDate(Date circularDate) {
        this.circularDate = circularDate;
    }

    public CircularLanguage getCircularLanguage() {
        return circularLanguage;
    }

    public void setCircularLanguage(CircularLanguage circularLanguage) {
        this.circularLanguage = circularLanguage;
    }

    public SigningAuthority getSigningAuthority() {
        return signingAuthority;
    }

    public void setSigningAuthority(SigningAuthority signingAuthority) {
        this.signingAuthority = signingAuthority;
    }
    
    

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getBaImage() {
        return baImage;
    }

    public void setBaImage(byte[] baImage) {
        this.baImage = baImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Circular)) {
            return false;
        }
        Circular other = (Circular) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.UnitImage[ id=" + id + " ]";
    }


    public boolean isCircularLetter() {
        return circularLetter;
    }

    public void setCircularLetter(boolean circularLetter) {
        this.circularLetter = circularLetter;
    }

    public Boolean getReplaced() {
        return replaced;
    }

    public void setReplaced(Boolean replaced) {
        this.replaced = replaced;
    }

    public Circular getReplaceBy() {
        return replaceBy;
    }

    public void setReplaceBy(Circular replaceBy) {
        this.replaceBy = replaceBy;
    }

    public Date getReplacedAt() {
        return replacedAt;
    }

    public void setReplacedAt(Date replacedAt) {
        this.replacedAt = replacedAt;
    }

    public WebUser getReplacedUser() {
        return replacedUser;
    }

    public void setReplacedUser(WebUser replacedUser) {
        this.replacedUser = replacedUser;
    }
}
