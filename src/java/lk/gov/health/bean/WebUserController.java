/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package lk.gov.health.bean;

import lk.gov.health.entity.Person;
import lk.gov.health.facade.WebUserFacade;
import lk.gov.health.facade.WebUserRoleFacade;
import lk.gov.health.entity.WebUser;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class WebUserController implements Serializable {
    
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    @EJB
    private WebUserFacade ejbFacade;
    @EJB
    WebUserRoleFacade roleFacade;
    List<WebUser> items;
    List<WebUser> searchItems;
    private WebUser current;
    String selectText = "";
    String language;
    
    public String getLanguage() {
        if (language == null || language.equals("")) {
            language = "en";
        }
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public void changeLocale(String loc) {
        language = loc;
    }
    
    public WebUserController() {
    }
    
    public List<WebUser> getItems() {
        if (items == null) {
            items = getFacade().findBySQL("Select d From WebUser d");
        }
        return items;
    }
    
    public void setItems(List<WebUser> items) {
        this.items = items;
    }
    
    public WebUser getCurrent() {
        if (current == null) {
            current = new WebUser();
        }
        if (current.getWebUserPerson() == null) {
            Person p = new Person();
            current.setWebUserPerson(p);
        }
        return current;
    }
    
    public void setCurrent(WebUser current) {
        this.current = current;
    }
    
    private WebUserFacade getFacade() {
        return ejbFacade;
    }
    
    public WebUser searchItem(String itemName, boolean createNewIfNotPresent) {
        WebUser searchedItem = null;
        List<WebUser> temItems;
        temItems = getFacade().findAll("name", itemName, true);
        if (temItems.size() > 0) {
            searchedItem = (WebUser) temItems.get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new WebUser();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance().getTime());
            searchedItem.setCreater(sessionController.loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }
    
    private void recreateModel() {
        items = null;
    }
    
    public void prepareAdd() {
        current = new WebUser();
    }
    
    public void saveSelected() {
        if (current == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getId() == null || current.getId() == 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        selectText = "";
    }
    
    public List<WebUser> getToApproveUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired=false AND u.activated=false";
        return getEjbFacade().findBySQL(temSQL);
    }
    
    public SessionController getSessionController() {
        return sessionController;
    }
    
    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    
    public WebUserFacade getEjbFacade() {
        return ejbFacade;
    }
    
    public void setEjbFacade(WebUserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public WebUserRoleFacade getRoleFacade() {
        return roleFacade;
    }
    
    public void setRoleFacade(WebUserRoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }
    
    public List<WebUser> getSearchItems() {
        if (searchItems == null) {
            if (selectText.equals("")) {
                searchItems = getFacade().findAll("name", true);
            } else {
                searchItems = getFacade().findAll("name", "%" + selectText + "%",
                        true);
                if (searchItems.size() > 0) {
                    current = (WebUser) searchItems.get(0);
                } else {
                    current = null;
                }
            }
        }
        return searchItems;
    }
    
    public void setSearchItems(List<WebUser> searchItems) {
        this.searchItems = searchItems;
    }
    
    public void delete() {
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("deleteSuccessful"));
        } else {
            UtilityController.addErrorMessage(new MessageController().getValue("nothingToDelete"));
        }
        recreateModel();
        getItems();
        current = null;
    }
    
    public String getSelectText() {
        return selectText;
    }
    
    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }
    
    @FacesConverter(forClass = WebUser.class)
    public static class WebUserControllerConverter implements Converter {
        
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebUserController controller = (WebUserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webUserController");
            return controller.ejbFacade.find(getKey(value));
        }
        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }
        
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WebUser) {
                WebUser o = (WebUser) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + WebUserController.class.getName());
            }
        }
    }
}
