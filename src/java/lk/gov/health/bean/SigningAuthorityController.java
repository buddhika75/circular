/*
 * Author : Dr. M H B Ariyaratne
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import lk.gov.health.facade.SigningAuthorityFacade;
import lk.gov.health.entity.SigningAuthority;
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
public final class SigningAuthorityController implements Serializable {

    @EJB
    private SigningAuthorityFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private SigningAuthority current;
    private List<SigningAuthority> items = null;
    List<SigningAuthority> selectedItems = null;
    String selectText = "";

    public SigningAuthorityController() {
    }

    public List<SigningAuthority> getSelectedItems() {
        if (getSelectText().trim().equals("") ){
            selectedItems = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false  order by d.name");
        }else{
        selectedItems = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false  and upper(d.name) like '%" +  getSelectText().toUpperCase() + "%' order by d.name");
        }
        return selectedItems;
    }

    public void setSelectedItems(List<SigningAuthority> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<SigningAuthority> getItems() {
        if (items == null) {
            items = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false order by d.name");
        }
        return items;
    }

    public void setItems(List<SigningAuthority> items) {
        this.items = items;
    }

    public SigningAuthority getCurrent() {
        System.out.println("getting current");
        if (current == null) {
            current = new SigningAuthority();
        }
        System.out.println("current is " + current.toString());
        return current;
    }

    public void setCurrent(SigningAuthority current) {
        this.current = current;
    }

    private SigningAuthorityFacade getFacade() {
        return ejbFacade;
    }

    public SigningAuthority searchItem(String itemName, boolean createNewIfNotPresent) {
        SigningAuthority temItem;
        temItem = getFacade().findFirstBySQL("select i from SiginingAuthority i where i.retired=false and upper(i.name) = '" + itemName.toUpperCase() + "'");
        if (temItem != null) {
            return temItem;
        } else if (createNewIfNotPresent) {
            temItem = new SigningAuthority();
            temItem.setName(itemName);
            temItem.setCreatedAt(Calendar.getInstance().getTime());
            temItem.setCreater(sessionController.loggedUser);
            getFacade().create(temItem);
            return temItem;
        } else {
            return null;
        }
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareAdd() {
        System.out.println("prepairing to add");
        current = new SigningAuthority();
        System.out.println("current is " + getCurrent().toString());
    }

    public void saveSelected() {
        if (current == null) {
            UtilityController.addSuccessMessage(new MessageController().getValue("nothingToSave"));
            return;
        } else if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        getItems();
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

    public SigningAuthorityFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(SigningAuthorityFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = SigningAuthority.class)
    public static class SiginingAuthorityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SigningAuthorityController controller = (SigningAuthorityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "siginingAuthorityController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SigningAuthority) {
                SigningAuthority o = (SigningAuthority) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + SigningAuthorityController.class.getName());
            }
        }
    }
}
