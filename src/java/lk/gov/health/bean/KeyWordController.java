/*
 * Author : Dr. M H B Ariyaratne
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import lk.gov.health.facade.KeyWordFacade;
import lk.gov.health.entity.KeyWord;
import java.io.Serializable;
import java.util.Arrays;
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
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.CircularKeyword;
import lk.gov.health.facade.CircularKeywordFacade;
import lk.gov.health.facade.SingleKeyWordFacade;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public final class KeyWordController implements Serializable {

    @EJB
    private KeyWordFacade keyWordFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    private KeyWord current;
    private List<KeyWord> items = null;
    List<KeyWord> selectedItems = null;
    String selectText = "";
    private String bulkKeyword;
    @EJB
    private CircularKeywordFacade ckFacade;
    @EJB
    private KeyWordFacade keyFacade;
    @EJB
    SingleKeyWordFacade skFacade;
    private Circular circular;
    private List<KeyWord> popularKeyword;

    public KeyWordController() {
    }

    public List<KeyWord> getSelectedItems() {
        if (getSelectText().trim().equals("")) {
            selectedItems = getFacade().findBySQL("Select d From KeyWord d where d.retired=false  order by d.name");
        } else {
            selectedItems = getFacade().findBySQL("Select d From KeyWord d where d.retired=false  and upper(d.name) like '%" + getSelectText().toUpperCase() + "%' order by d.name");
        }
        return selectedItems;
    }

    public void setSelectedItems(List<KeyWord> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<KeyWord> getItems() {
        if (items == null || items.isEmpty()) {
            items = getFacade().findBySQL("Select d From KeyWord d where d.retired=false order by d.name");
        }
        return items;
    }

    public void setItems(List<KeyWord> items) {
        this.items = items;
    }

    public KeyWord getCurrent() {
        System.out.println("getting current");
        if (current == null) {
            current = new KeyWord();
        }
        System.out.println("current is " + current.toString());
        return current;
    }

    public void setCurrent(KeyWord current) {
        this.current = current;
    }

    private KeyWordFacade getFacade() {
        return keyWordFacade;
    }

    public KeyWord searchItem(String itemName, boolean createNewIfNotPresent) {
        KeyWord temItem;
        temItem = getFacade().findFirstBySQL("select i from KeyWord i where i.retired=false and upper(i.name) = '" + itemName.toUpperCase() + "'");
        if (temItem != null) {
            return temItem;
        } else if (createNewIfNotPresent) {
            temItem = new KeyWord();
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
        current = new KeyWord();
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

    public List<KeyWord> completePlayer(String query) {
        List<KeyWord> suggestions = getKeyWordFacade().findBySQL("select k from KeyWord k where lower(k.name) like '%" + query.toLowerCase() + "%'");
        return suggestions;
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

    public KeyWordFacade getKeyWordFacade() {
        return keyWordFacade;
    }

    public void setKeyWordFacade(KeyWordFacade keyWordFacade) {
        this.keyWordFacade = keyWordFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public CircularKeywordFacade getCkFacade() {
        return ckFacade;
    }

    public void setCkFacade(CircularKeywordFacade ckFacade) {
        this.ckFacade = ckFacade;
    }

    public KeyWordFacade getKeyFacade() {
        return keyFacade;
    }

    public void setKeyFacade(KeyWordFacade keyFacade) {
        this.keyFacade = keyFacade;
    }

    public Circular getCircular() {
        return circular;
    }

    public void setCircular(Circular circular) {
        this.circular = circular;
    }

    public String getBulkKeyword() {
        return bulkKeyword;
    }

    public void setBulkKeyword(String bulkKeyword) {
        this.bulkKeyword = bulkKeyword;
    }

    private boolean check() {
        String sql = "Select s from KeyWord s where s.retired=false and " + " upper(s.bulkKeyword)='" + getCurrent().getName().toUpperCase().trim() + "'";
       List<KeyWord> list=getKeyFacade().findBySQL(sql);
        if (!list.isEmpty()) {
            System.err.println("Size"+list.size());
            return true;
        }



        return false;
    }
    
    public void saveBulkKeyWord() {
        
        /*if (check()) {
            UtilityController.addErrorMessage(" This Keyword is Already Exists  ");
            return;
        }*/
        
        List<String> kws = Arrays.asList(bulkKeyword.split("\\s+"));
        for (String kw : kws) {
            KeyWord keyWord;
            keyWord = getKeyFacade().findFirstBySQL("select k from KeyWord k where lower(k.name)='" + kw.toLowerCase() + "'");
            if (keyWord == null) {
                keyWord = new KeyWord();
                keyWord.setName(kw.toLowerCase());
                getKeyFacade().create(keyWord);

            }
            setBulkKeyword(" ");
            
        }
    }

    public List<KeyWord> getPopularKeyword() {

        String sql;
        sql = ("select k from KeyWord k");
        popularKeyword = skFacade.findBySQL(sql);

        return popularKeyword;
    }

    public void setPopularKeyword(List<KeyWord> popularKeyword) {
        this.popularKeyword = popularKeyword;
    }

    @FacesConverter("keyWordControllerConverter")
    public static class KeyWordControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            KeyWordController controller = (KeyWordController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "keyWordController");
            return controller.keyWordFacade.find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            if(value==null || value.trim().equals("null")){
                value="";
            }
            try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0l;
            }
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
            if (object instanceof KeyWord) {
                KeyWord o = (KeyWord) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + KeyWordController.class.getName());
            }
        }
    }
}
