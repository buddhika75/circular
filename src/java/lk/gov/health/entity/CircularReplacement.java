/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import lk.gov.health.data.CircularLanguage;

/**
 *
 * @author Rilwan
 */
@Entity
public class CircularReplacement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Circular replacedCircular;
    private Circular newCircular;
     //Created Properties
    @ManyToOne
    private WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;
    //Retairing properties
    private boolean retired;
    @ManyToOne
    private WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date retiredAt;
    private String retireComments;
    @ManyToOne
    private Person person;
    @ManyToOne
    private Category category;
    private String circularNumber;
    private String topic;
    private Boolean internal;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String contents;
    @ManyToOne
    private AdministrativeDivision administrativeDivision;
    private String originatingFileNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date circularDate;
    private CircularLanguage circularLanguage;
    @ManyToOne
    private SigningAuthority signingAuthority;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] baImage;
    private String fileName;
    private String fileType;
    private String keywords;
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
        if (!(object instanceof CircularReplacement)) {
            return false;
        }
        CircularReplacement other = (CircularReplacement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.entity.CircularReplacement[ id=" + id + " ]";
    }

    public Circular getReplacedCircular() {
        return replacedCircular;
    }

    public void setReplacedCircular(Circular replacedCircular) {
        this.replacedCircular = replacedCircular;
    }

    public Circular getNewCircular() {
        return newCircular;
    }

    public void setNewCircular(Circular newCircular) {
        this.newCircular = newCircular;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public byte[] getBaImage() {
        return baImage;
    }

    public void setBaImage(byte[] baImage) {
        this.baImage = baImage;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
}
