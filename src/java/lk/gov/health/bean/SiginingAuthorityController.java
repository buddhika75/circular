/*
 * Author : Dr. M H B Ariyaratne
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import lk.gov.health.facade.SiginingAuthorityFacade;
import lk.gov.health.entity.SiginingAuthority;
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
public final class SiginingAuthorityController implements Serializable {

    @EJB
    private SiginingAuthorityFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private SiginingAuthority current;
    private List<SiginingAuthority> items = null;
    List<SiginingAuthority> selectedItems = null;
    String selectText = "";

    public SiginingAuthorityController() {
    }

    public List<SiginingAuthority> getSelectedItems() {
        if (getSelectText().trim().equals("") ){
            selectedItems = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false  order by d.name");
        }else{
        selectedItems = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false  and upper(d.name) like '%" +  getSelectText().toUpperCase() + "%' order by d.name");
        }
        return selectedItems;
    }

    public void setSelectedItems(List<SiginingAuthority> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<SiginingAuthority> getItems() {
        if (items == null) {
            items = getFacade().findBySQL("Select d From SiginingAuthority d where d.retired=false order by d.name");
        }
        return items;
    }

    public void setItems(List<SiginingAuthority> items) {
        this.items = items;
    }

    public SiginingAuthority getCurrent() {
        System.out.println("getting current");
        if (current == null) {
            current = new SiginingAuthority();
        }
        System.out.println("current is " + current.toString());
        return current;
    }

    public void setCurrent(SiginingAuthority current) {
        this.current = current;
    }

    private SiginingAuthorityFacade getFacade() {
        return ejbFacade;
    }

    public SiginingAuthority searchItem(String itemName, boolean createNewIfNotPresent) {
        SiginingAuthority temItem;
        temItem = getFacade().findFirstBySQL("select i from SiginingAuthority i where i.retired=false and upper(i.name) = '" + itemName.toUpperCase() + "'");
        if (temItem != null) {
            return temItem;
        } else if (createNewIfNotPresent) {
            temItem = new SiginingAuthority();
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
        current = new SiginingAuthority();
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

    public SiginingAuthorityFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(SiginingAuthorityFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = SiginingAuthority.class)
    public static class SiginingAuthorityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SiginingAuthorityController controller = (SiginingAuthorityController) facesContext.getApplication().getELResolver().
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
            if (object instanceof SiginingAuthority) {
                SiginingAuthority o = (SiginingAuthority) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + SiginingAuthorityController.class.getName());
            }
        }
    }
}
