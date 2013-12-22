/*
 * Author
 * Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import java.io.ByteArrayInputStream;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import lk.gov.health.entity.AdministrativeDivision;
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.CircularKeyword;
import lk.gov.health.entity.CircularReplacement;
import lk.gov.health.entity.Person;
import lk.gov.health.entity.KeyWord;
import lk.gov.health.facade.CategoryFacade;
import lk.gov.health.facade.CircularFacade;
import lk.gov.health.facade.CircularKeywordFacade;
import lk.gov.health.facade.CircularReplacementFacade;
import lk.gov.health.facade.KeyWordFacade;
import lk.gov.health.facade.SingleKeyWordFacade;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author buddhika
 */
@ManagedBean
@SessionScoped
public class CircularController implements Serializable {

    StreamedContent scCircular;
    private StreamedContent scCircularById;
    private UploadedFile file;
    @EJB
    CircularFacade circularFacade;
    @EJB
    CategoryFacade catFacade;
    @EJB
    CircularKeywordFacade ckFacade;
    @EJB
    SingleKeyWordFacade skFacade;
    @EJB
    private KeyWordFacade keyFacade;
    @EJB
    CircularReplacementFacade replaceCirFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    Person person;
    Circular circular;
    private String txtOldCircilar;
    Circular oldCircular;
    Circular newCircular;
    private List<Circular> lstOldCirculars;
    private String txtNewCircular;
    private List<Circular> lstNewCirculars;
    List<Circular> circulars;
    List<Circular> popularCircular;
    List<Circular> resentCirculars;
    List<Circular> divCirculars;
    private List<Circular> items = null;
    private List<KeyWord> keyWords;
    AdministrativeDivision division;
    String strSearch;
    private Circular newCir;
    private Circular oldCir;
    private Circular current;
    List<CircularReplacement> circularReplacements;

    public List<CircularReplacement> getCircularReplacements() {
        if (circularReplacements == null) {
            String sql;
            sql = "select r From CircularReplacement r order by r.id desc";
            circularReplacements = getReplaceCirFacade().findBySQL(sql, 10);
        }
        return circularReplacements;
    }

    public void setCircularReplacements(List<CircularReplacement> circularReplacements) {
        this.circularReplacements = circularReplacements;
    }

    public Circular getOldCircular() {
        return oldCircular;
    }

    public void setOldCircular(Circular oldCircular) {
        this.oldCircular = oldCircular;
    }

    public Circular getNewCircular() {
        return newCircular;
    }

    public void setNewCircular(Circular newCircular) {
        this.newCircular = newCircular;
    }

    public SingleKeyWordFacade getSkFacade() {
        return skFacade;
    }

    public void setSkFacade(SingleKeyWordFacade skFacade) {
        this.skFacade = skFacade;
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

    public List<Circular> getDivCirculars() {
        String sql;
        if (strSearch == null || strSearch.trim().equals("")) {
            sql = "select c from Circular c where c.retired = false and c.administrativeDivision.id = " + getDivision().getId() + " order by c.name";
        } else {
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.administrativeDivision c where c.retired = false and c.administrativeDivision.id = " + getDivision().getId() + "  and k.retired = false and " + searchStringFromText() + " ) order by c1.name";
        }
        System.out.println("SQL is " + sql);
        divCirculars = getCircularFacade().findBySQL(sql);
        return divCirculars;
    }

    public void setDivCirculars(List<Circular> divCirculars) {
        this.divCirculars = divCirculars;
    }

    public AdministrativeDivision getDivision() {
        return division;
    }

    public void setDivision(AdministrativeDivision division) {
        this.division = division;
    }

    public String toDivisionalCirculars() {
        if (division == null) {
            return "";
        }
        setStrSearch("");
        return "division_circulars";
    }

    public StreamedContent getCircularById() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            Circular temCir = getCircularFacade().find(Long.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(temCir.getBaImage()), temCir.getFileType());
        }
    }

    public void setScCircularById(StreamedContent scCircularById) {
        this.scCircularById = scCircularById;
    }

    public StreamedContent getScCircular() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (circular == null) {
            return new DefaultStreamedContent();
        } else {
            Circular temCir = circular;
            System.out.println(temCir.getFileType());
            System.out.println(temCir.getFileName());
            return new DefaultStreamedContent(new ByteArrayInputStream(temCir.getBaImage()), temCir.getFileType(), temCir.getFileName());
        }
    }

    public StreamedContent getCurrentCircular() {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("getCurrentCircular");
        if (circular == null) {
            System.out.println("return default stream contents");
            return new DefaultStreamedContent();
        } else {
            System.out.println("circular is not null");
            Circular temCir = circular;
            System.out.println(temCir.getFileType());
            System.out.println(temCir.getFileName());
            return new DefaultStreamedContent(new ByteArrayInputStream(temCir.getBaImage()), temCir.getFileType(), temCir.getFileName());
        }
    }

    public String toAddNewCircular() {
        setCircular(new Circular());
        return "add_circular";
    }

    public String toViewCircular() {
        return "circular";
    }

    public CircularController() {
        System.out.println("circular controller constructor called");
    }

    public String getStrSearch() {
        return strSearch;
    }

    public void setStrSearch(String strSearch) {
        this.strSearch = strSearch;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public CircularFacade getCircularFacade() {
        return circularFacade;
    }

    public void setCircularFacade(CircularFacade circularFacade) {

        this.circularFacade = circularFacade;
    }

    public Circular getCircular() {
        System.out.println("getting circular");
        if (circular == null) {
            System.out.println("new circular");
            circular = new Circular();
        }
        System.out.println("return circular " + circular.getFileName() + " of " + circular.getFileType());
        return circular;
    }

    private void recreateModel() {
        items = null;
    }

    public void delete() {
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.loggedUser);
            getCircularFacade().edit(current);
            UtilityController.addSuccessMessage("Delete Successful");
        } else {
            UtilityController.addErrorMessage("Select a Circular");
        }
        recreateModel();
        getItems();
        circular = null;
    }

    public void setCircular(Circular circular) {
        System.out.println("setting circular");
        System.out.println("id is " + circular.getId());
        System.out.println("name is " + circular.getName());
        System.out.println("filename is " + circular.getFileName());
        System.out.println("type is " + circular.getFileType());
        this.circular = circular;
    }

    public void recordSearchcount(String str) {
        KeyWord newSk;
        String sql;
        sql = "select kw from SingleKeyword kw where upper(kw.name) = '" + str + "'";
        newSk = getSkFacade().findFirstBySQL(sql);
        newSk.setSearchCount(newSk.getSearchCount() + 1);
        getSkFacade().edit(newSk);
    }

    public String searchStringFromText() {
        String temStr = " (";
        String[] splited = strSearch.split("\\s+");
        for (String s : splited) {
            temStr = temStr + " upper(c.circularNumber) like '%" + s.trim().toUpperCase() + "%' or ";
            temStr = temStr + " upper(c.topic) like '%" + s.trim().toUpperCase() + "%' or ";
            temStr = temStr + " upper(w.name) like '%" + s.trim().toUpperCase() + "%'   or ";
        }
        temStr = temStr.substring(0, temStr.length() - 4);
        temStr = temStr + " ) ";
        return temStr;
    }

    public String searchStringFromOldText() {
        String temStr = " (";
        String[] splited = txtOldCircilar.split("\\s+");
        for (String s : splited) {
            temStr = temStr + " upper(c.circularNumber) like '%" + s.trim().toUpperCase() + "%' or ";
            temStr = temStr + " upper(c.topic) like '%" + s.trim().toUpperCase() + "%' or ";
            temStr = temStr + " upper(w.name) like '%" + s.trim().toUpperCase() + "%'   or ";
        }
        temStr = temStr.substring(0, temStr.length() - 4);
        temStr = temStr + " ) ";
        return temStr;
    }

    public List<Circular> getCirculars() {
        String sql;
        if (strSearch == null || strSearch.trim().equals("")) {
            sql = "select c from Circular c where c.retired = false order by c.id desc";
        } else {
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.circular c join k.keyWord w where c.retired = false and k.retired = false and " + searchStringFromText() + " ) order by c1.id desc";

        }
        System.out.println("SQL is " + sql);
        circulars = getCircularFacade().findBySQL(sql, 10);
        return circulars;
    }

    public List<CircularReplacement> replacedBy(Circular original) {
        String s;
        s = "Select r From CircularReplacement r where r.retired=false and r.replacedCircular=:rc";

        Map m = new HashMap();
        m.put("rc", original);

        return getReplaceCirFacade().findBySQL(s, m);
    }

    public void setCirculars(List<Circular> circulars) {
        this.circulars = circulars;
    }

    public CategoryFacade getCatFacade() {
        return catFacade;
    }

    public void setCatFacade(CategoryFacade catFacade) {
        this.catFacade = catFacade;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    private boolean check() {
        //   HashMap hm=new HashMap();
        
        /*if(getCircular().getCircularNumber()==null){
            return false;
        }*/

        String sql = "Select s from Circular s where s.retired=false and " + " upper(s.circularNumber)='" + getCircular().getCircularNumber().toUpperCase().trim() + "'";
        Circular tmp = getCircularFacade().findFirstBySQL(sql);
        if (tmp!=null) {
            System.err.println("Size" + tmp.getCircularNumber());
            return true;
        }
        return false;
    }

    public Boolean circularNumAvailable(String circularNumber) {
        Boolean available = true;
        List<Circular> lstcircularNumber = getCircularFacade().findAll();
        for (Circular w : lstcircularNumber) {
            if (circularNumber.toLowerCase().equals(w.getCircularNumber().toLowerCase())) {
                available = false;
            }
        }
        return available;
    }

    public String saveCircular() {
        InputStream in;
        /*if(getCircular().getCircularNumber()==null|| getCircular().getCircularNumber()=="" ){
         UtilityController.addErrorMessage("Please Enter Circular Number");
         return "";
         }*/

        /*if (!circularNumAvailable(circular.getCircularNumber())) {
         UtilityController.addErrorMessage("Circular Number already exists. Plese enter another user name");
         return "";
         }*/

        if (file == null) {
            if (circular.getId() == null || circular.getId() == 0) {
                UtilityController.addErrorMessage("Please upload an image");
                return "";
            } else {
                circularFacade.edit(circular);
                UtilityController.addSuccessMessage("Changes Saved");
                return "";
            }
        }
        System.out.println("file name is not null");
        System.out.println(file.getFileName());
        try {
            circular.setFileName(file.getFileName());
            circular.setFileType(file.getContentType());
            in = file.getInputstream();
            circular.setBaImage(IOUtils.toByteArray(in));
            if (circular.getId() == null || circular.getId() == 0) {
                if (check()) {
                    UtilityController.addErrorMessage("Circular Number already Exist");
                    return "";
                }
                circularFacade.create(circular);
                UtilityController.addSuccessMessage("New File Saved");
            } else {
                circularFacade.edit(circular);
                UtilityController.addSuccessMessage("Changes Saved");
            }
            addKeyWords();
            setCircular(new Circular());
            return "";
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return "";
        }
    }

    private void addKeyWords() {
        System.out.println("adding keyWords" + getKeyWords().size());
        for (KeyWord keyWord : getKeyWords()) {
            CircularKeyword ck = new CircularKeyword();
            ck.setKeyWord(keyWord);
            ck.setCircular(circular);
            ckFacade.create(ck);
        }
        setKeyWords(new ArrayList<KeyWord>());
    }

    public KeyWordFacade getKeyFacade() {
        return keyFacade;
    }

    public void setKeyFacade(KeyWordFacade keyFacade) {
        this.keyFacade = keyFacade;
    }

    public List<Circular> getPopularCircular() {
        System.out.println("getting circular");
        if (circular == null) {
            System.out.println("new circular");
            circular = new Circular();
        }
        System.out.println("return circular " + circular.getFileName() + " of " + circular.getFileType());
        return popularCircular;
    }

    public void setPopularCircular(List<Circular> popularCircular) {
        this.popularCircular = popularCircular;
    }

    public List<Circular> getResentCirculars() {
        String sql;
        sql = "select c from Circular c where c.retired = false order by c.id desc";
        System.out.println("SQL is " + sql);
        resentCirculars = getCircularFacade().findBySQL(sql, 1);
        return resentCirculars;
    }

    public void setResentCirculars(List<Circular> resentCirculars) {
        this.resentCirculars = resentCirculars;
    }

    private boolean circulars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTxtOldCircilar() {
        return txtOldCircilar;
    }

    public void setTxtOldCircilar(String txtOldCircilar) {
        this.txtOldCircilar = txtOldCircilar;
    }

    public List<Circular> getLstOldCirculars() {

        String sql;
        if (getTxtOldCircilar() == null || getTxtOldCircilar().trim().equals("")) {
            sql = "select c from Circular c where c.retired = false order by c.id desc";
        } else {
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.circular c join k.keyWord w where c.retired = false and k.retired = false and " + searchStringFromOldText() + " ) order by c1.id desc";

        }
        System.out.println("SQL is " + sql);
        circulars = getCircularFacade().findBySQL(sql);
        lstOldCirculars = getCircularFacade().findBySQL(sql);
        return lstOldCirculars;
    }

    public void setLstOldCirculars(List<Circular> lstOldCirculars) {
        this.lstOldCirculars = lstOldCirculars;
    }

    public String getTxtNewCircular() {

        return txtNewCircular;
    }

    public void setTxtNewCircular(String txtNewCircular) {
        this.txtNewCircular = txtNewCircular;
    }

    /*public List<Circular> getLstNewCirculars() {
     String sql;
     sql = "Select c from Circular c where c.retired=false";
     lstNewCirculars = getCircularFacade().findBySQL(sql);
     return lstNewCirculars;
     }*/
    public List<Circular> getLstNewCirculars() {

        String sql;
        if (getTxtNewCircular() == null || getTxtNewCircular().trim().equals("")) {
            sql = "select c from Circular c where c.retired = false order by c.id desc";
        } else {
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.circular c join k.keyWord w where c.retired = false and k.retired = false and " + searchStringFromOldText() + " ) order by c1.id desc";

        }
        System.out.println("SQL is " + sql);
        circulars = getCircularFacade().findBySQL(sql);
        lstNewCirculars = getCircularFacade().findBySQL(sql);
        return lstNewCirculars;
    }

    public void setLstNewCirculars(List<Circular> lstNewCirculars) {
        this.lstNewCirculars = lstNewCirculars;
    }

    public List<KeyWord> getKeyWords() {
        System.out.println("getting keyWords");
        return keyWords;
    }

    public void setKeyWords(List<KeyWord> keyWords) {
        System.out.println("setting keyWords");
        this.keyWords = keyWords;

    }

    public Circular getNewCir() {
        return newCir;
    }

    public void setNewCir(Circular newCir) {
        this.newCir = newCir;
    }

    public Circular getOldCir() {
        return oldCir;
    }

    public void setOldCir(Circular oldCir) {
        this.oldCir = oldCir;
    }

    private boolean checkAvai() {
        String sql = "Select s from CircularReplacement s where s.retired=false and " + " upper(s.name)='" + getCurrent().getName().toUpperCase().trim() + "'";
        List<CircularReplacement> list = getReplaceCirFacade().findBySQL(sql);
        if (!list.isEmpty()) {
            System.err.println("Size" + list.size());
            return true;
        }

        return false;
    }
    
    private boolean checkrep() {
        String sql = "Select s from CircularReplacement s where s.retired=false and " + " upper(s.replacedCircular)='" + getCircular().getCircularNumber().toUpperCase().trim() + "'";
        CircularReplacement tmp = replaceCirFacade.findFirstBySQL(sql);
        if (tmp!=null) {
            System.err.println("Size" + tmp.getReplacedCircular());
            return true;
        }
        return false;
    }
    
    public void replaceCircular() {
        CircularReplacement rep = new CircularReplacement();
        if (newCircular.equals(oldCircular)) {
            UtilityController.addErrorMessage("You Have Select Same Circulars");
            return;
        }

        /*if (checkAvai()) {
         UtilityController.addErrorMessage(" This Replacement is Already Exists  ");
         return;
         }*/
        
        /*if (checkrep()) {
            UtilityController.addErrorMessage(" This Replacement is Already Exists  ");
            return;
        }*/

        if (newCircular == null || oldCircular == null) {
            UtilityController.addErrorMessage("Please Select Circulars");
            return;
        }
        rep.setReplacedCircular(oldCircular);
        rep.setNewCircular(newCircular);
        getReplaceCirFacade().create(rep);
        circularReplacements = null;
        UtilityController.addSuccessMessage("Added");

    }

    public CircularReplacementFacade getReplaceCirFacade() {
        return replaceCirFacade;
    }

    public void setReplaceCirFacade(CircularReplacementFacade replaceCirFacade) {
        this.replaceCirFacade = replaceCirFacade;
    }

    public List<Circular> getItems() {
        if (items == null || items.isEmpty()) {
            items = circularFacade.findBySQL("Select d From Circular d where d.retired=false order by d.name");
        }
        return items;
    }

    public void setItems(List<Circular> items) {
        this.items = items;
    }

    public Circular getCurrent() {
        System.out.println("getting current");
        if (current == null) {
            current = new Circular();
        }
        System.out.println("current is " + current.toString());
        return current;

    }

    public void setCurrent(Circular current) {
        this.current = current;
    }

    public StreamedContent getScCircularById() {
        return scCircularById;
    }

    @FacesConverter(forClass = Circular.class)
    public static class CircularControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CircularController controller = (CircularController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "circularController");
            return controller.getCircularFacade().find(getKey(value));
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
            if (object instanceof Circular) {
                Circular o = (Circular) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + CircularController.class.getName());
            }
        }
    }
}
