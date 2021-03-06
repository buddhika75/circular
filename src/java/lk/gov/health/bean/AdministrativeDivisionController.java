/*
 * Author : Dr. M H B Ariyaratne
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import lk.gov.health.facade.AdministrativeDivisionFacade;
import lk.gov.health.entity.AdministrativeDivision;
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
public final class AdministrativeDivisionController implements Serializable {

    @EJB
    private AdministrativeDivisionFacade ejbFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private AdministrativeDivision current;
    private List<AdministrativeDivision> items = null;
    List<AdministrativeDivision> selectedItems = null;
    String selectText = "";

    public AdministrativeDivisionController() {
    }


   
    
    public List<AdministrativeDivision> getSelectedItems() {
        if (getSelectText().trim().equals("") ){
            selectedItems = getFacade().findBySQL("Select d From AdministrativeDivision d where d.retired=false  order by d.name");
        }else{
        selectedItems = getFacade().findBySQL("Select d From AdministrativeDivision d where d.retired=false  and upper(d.name) like '%" +  getSelectText().toUpperCase() + "%' order by d.name");
        }
        return selectedItems;
    }

    public void setSelectedItems(List<AdministrativeDivision> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<AdministrativeDivision> getItems() {
        if (items == null|| items.isEmpty()) {
            items = getFacade().findBySQL("Select d From AdministrativeDivision d where d.retired=false order by d.name");
        }
        return items;
    }

    public void setItems(List<AdministrativeDivision> items) {
        this.items = items;
    }

    public AdministrativeDivision getCurrent() {
        System.out.println("getting current");
        if (current == null) {
            current = new AdministrativeDivision();
        }
        System.out.println("current is " + current.toString());
        return current;
    }

    public void setCurrent(AdministrativeDivision current) {
        this.current = current;
    }

    private AdministrativeDivisionFacade getFacade() {
        return ejbFacade;
    }

    public AdministrativeDivision searchItem(String itemName, boolean createNewIfNotPresent) {
        AdministrativeDivision temItem;
        temItem = getFacade().findFirstBySQL("select i from AdministrativeDivision i where i.retired=false and upper(i.name) = '" + itemName.toUpperCase() + "'");
        if (temItem != null) {
            return temItem;
        } else if (createNewIfNotPresent) {
            temItem = new AdministrativeDivision();
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
        current = new AdministrativeDivision();
        System.out.println("current is " + getCurrent().toString());
    }
    
    private boolean check() {
        String sql = "Select s from AdministrativeDivision s where s.retired=false and "+ " upper(s.name)='" + getCurrent().getName().toUpperCase().trim() + "'";
       List<AdministrativeDivision> list=ejbFacade.findBySQL(sql);
        if (!list.isEmpty()) {
            System.err.println("Size"+list.size());
            return true;
        }
        return false;
    }
    
    public void saveSelected() {
        
        /*if (check()) {
            UtilityController.addErrorMessage(" This Administrative Division Already Exists  ");
            return;
        }*/
        
        if (current == null) {
            UtilityController.addSuccessMessage(new MessageController().getValue("nothing To Save"));
            return;
        } else if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedOldSuccessfully"));
        } else {
            if (check()) {
            UtilityController.addErrorMessage(" This Administrative Division is Already Exists  ");
            return;
        }
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.loggedUser);
            getFacade().create(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("savedNewSuccessfully"));
        }
        recreateModel();
        getItems();
        setCurrent(new AdministrativeDivision());
        //setCurrent(current=null);
    }

    public void delete() {
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("delete Successful"));
        } else {
            UtilityController.addErrorMessage(new MessageController().getValue("nothing To Delete"));
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

    public AdministrativeDivisionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AdministrativeDivisionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    @FacesConverter(forClass = AdministrativeDivision.class)
    public static class AdministrativeDivisionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AdministrativeDivisionController controller = (AdministrativeDivisionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "administrativeDivisionController");
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
            if (object instanceof AdministrativeDivision) {
                AdministrativeDivision o = (AdministrativeDivision) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + AdministrativeDivisionController.class.getName());
            }
        }
    }
}
