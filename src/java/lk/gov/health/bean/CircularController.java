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
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import lk.gov.health.entity.AdministrativeDivision;
import lk.gov.health.entity.Circular;
import lk.gov.health.entity.CircularKeyword;
import lk.gov.health.entity.Person;
import lk.gov.health.entity.KeyWord;
import lk.gov.health.facade.CategoryFacade;
import lk.gov.health.facade.CircularFacade;
import lk.gov.health.facade.CircularKeywordFacade;
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
    StreamedContent scCircularById;
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
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    Person person;
    Circular circular;
    List<Circular> circulars;
    List<Circular> divCirculars;
    AdministrativeDivision division;
    String strSearch;

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
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.circular c where c.retired = false and c.administrativeDivision.id = " + getDivision().getId() + "  and k.retired = false and " + searchStringFromText() + " ) order by c1.name";
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

    public String toAddNewCircular() {
        circular = new Circular();
        return "add_circular";
    }

    public void setCircular(Circular circular) {
        System.out.println("setting circular");
        System.out.println("id is " + circular.getId());
        System.out.println("name is " + circular.getName());
        System.out.println("filename is " + circular.getFileName());
        System.out.println("type is " + circular.getFileType());
        this.circular = circular;
    }
    
    public void recordSearchcount(String str){
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
            temStr = temStr + " upper(k.name) like '%" + s.trim().toUpperCase() + "%'   or ";
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
            sql = "select c1 from Circular c1 where c1.id in (select distinct c.id from CircularKeyword k join k.circular c where c.retired = false and k.retired = false and " + searchStringFromText() + " ) order by c1.id desc";
        }
        System.out.println("SQL is " + sql);
        circulars = getCircularFacade().findBySQL(sql);
        return circulars;
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

    public String saveCircular() {
        InputStream in;
        if (file == null) {
            if (circular.getId() == null || circular.getId() == 0) {
                UtilityController.addErrorMessage("Please upload an image");
                return "";
            }else{
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
        if (circular == null || circular.getId() == null || circular.getId() == 0 || circular.getKeywords().trim().equals("")) {
            return;
        }
        List<String> kws = Arrays.asList(circular.getKeywords().split("\\s+"));
        for (String kw : kws) {
            CircularKeyword ck = new CircularKeyword();
            KeyWord keyWord;
            keyWord=getKeyFacade().findFirstBySQL("select k from KeyWord k where lower(k.name)='" + kw.toLowerCase() + "'" );
            if(keyWord==null){
            keyWord=new KeyWord();
            keyWord.setName(kw.toLowerCase());
            getKeyFacade().create(keyWord);
            
            }
            ck.setKeyWord(keyWord);
            ck.setCircular(circular);
            ck.setName(kw.toLowerCase());
            ckFacade.create(ck);
        }

    }

    public KeyWordFacade getKeyFacade() {
        return keyFacade;
    }

    public void setKeyFacade(KeyWordFacade keyFacade) {
        this.keyFacade = keyFacade;
    }
}
