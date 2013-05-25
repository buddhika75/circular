/*
 * Author : Dr. M H B Ariyaratne
 *
 * MO(Health Information), Department of Health Services, Southern Province
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@RequestScoped
public class MenuController implements Serializable {

    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    @ManagedProperty(value = "#{messageProvider}")
    private MessageController messageProvider;
    MenuModel model;

    public MessageController getMessageProvider() {
        return messageProvider;
    }

    public void setMessageProvider(MessageController messageProvider) {
        this.messageProvider = messageProvider;
    }

    private String getLabel(String key) {
        return getMessageProvider().getValue(key);
    }

    public MenuController() {
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public void createMenu() {

        model = new DefaultMenuModel();

        MenuItem item;

        item = new MenuItem();
        item.setValue(getLabel("home"));
        item.setUrl("index.xhtml");
        model.addMenuItem(item);

        item = new MenuItem();
        item.setValue("Probation");
        item.setUrl("probation_index.xhtml");
        model.addMenuItem(item);

        item = new MenuItem();
        item.setValue("Administration");
        item.setUrl("admin_index.xhtml");
        model.addMenuItem(item);

        model.addSubmenu(userSubmenu());
    }

    private Submenu dpccsSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(getLabel("Children"));

        MenuItem item;

        item = new MenuItem();
        item.setValue(getLabel("addNewChild"));
        item.setUrl("new_child.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("searchNewChild"));
        item.setUrl("search_child.xhtml");
        submenu.getChildren().add(item);



        item = new MenuItem();
        item.setValue(getLabel("preferences"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu userSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(getLabel("user"));

        MenuItem item;

        item = new MenuItem();
        item.setValue(getLabel("changePassword"));
        item.setUrl("change_password.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("preferences"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu adminSubmenu() {
        Submenu submenu;

        MenuItem item;

        submenu = new Submenu();
        submenu.setLabel(getLabel("admin"));

        item = new MenuItem();
        item.setValue(getLabel("activateAccounts"));
        item.setUrl("admin_activate_users.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("manageDemographicData"));
        item.setUrl("demography_index.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("manageAccounts"));
        item.setUrl("admin_user_previlages.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("removeUserAccounts"));
        item.setUrl("admin_remove_users.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("InstitutionTypes"));
        item.setUrl("institution_type.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("institutions"));
        item.setUrl("institutions.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("designationsCategory"));
        item.setUrl("designation_category.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("Designations"));
        item.setUrl("designation_level.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("Designations"));
        item.setUrl("designation.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("DPDHSAreas"));
        item.setUrl("district.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(getLabel("Province"));
        item.setUrl("province.xhtml");
        submenu.getChildren().add(item);


        return submenu;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    @PostConstruct
    public void init() {
        try {
            createMenu();
        } catch (Exception e) {
            System.out.println("Error in init method. It is " + e.getMessage());
        }
    }
}
