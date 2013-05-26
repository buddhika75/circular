/*
 * MSc(Biomedical Informatics) Project
 * 
 * Development and Implementation of a Web-based Combined Data Repository of 
 Genealogical, Clinical, Laboratory and Genetic Data 
 * and
 * a Set of Related Tools
 */
package lk.gov.health.bean;

import lk.gov.health.facade.WebUserRoleFacade;
import lk.gov.health.entity.WebUserRole;
import java.io.Serializable;
import java.util.ArrayList;
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
public final class WebUserRoleController implements Serializable {

    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;
    @EJB
    private WebUserRoleFacade ejbFacade;
    List<WebUserRole> adminRoles;
    List<WebUserRole> circularEditorRoles;
    List<WebUserRole> circularAdderRoles;
    List<WebUserRole> circularViewerRoles;
    List<WebUserRole> userRoles;
    private WebUserRole current;
    private List<WebUserRole> items = null;
    String selectText = "";

    public List<WebUserRole> getUserRoles() {
        System.out.println("user is admin - " + sessionController.isAdmin());
        System.out.println("user is editor - " + sessionController.isCircularEditor());
        System.out.println("user is adder - " + sessionController.isCircularAdder());
        System.out.println("user is viewer - " + sessionController.isCircularViewer());
        if (sessionController.isAdmin()) {
            userRoles = getAdminRoles();

            System.out.println("User roles are " + userRoles.toString());
            System.out.println("User Role count is " + userRoles.size());
        } else if (sessionController.isCircularAdder()) {
            userRoles = getCircularAdderRoles();

            System.out.println("User roles are " + userRoles.toString());
            System.out.println("User Role count is " + userRoles.size());
        } else if (sessionController.isCircularEditor()) {
            userRoles = getCircularEditorRoles();

            System.out.println("User roles are " + userRoles.toString());
            System.out.println("User Role count is " + userRoles.size());
        } else if (sessionController.isCircularViewer()) {
            userRoles = getCircularViewerRoles();

            System.out.println("User roles are " + userRoles.toString());
            System.out.println("User Role count is " + userRoles.size());
        } else {
            userRoles = new ArrayList<WebUserRole>();
        }


        return userRoles;
    }

    public void setUserRoles(List<WebUserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public List<WebUserRole> getCircularEditorRoles() {
        circularEditorRoles = getFacade().findBySQL("Select d From WebUserRole d where d.name = 'circular_viewer' or d.name = 'circular_adder' or d.name = 'circular_editor' ");
        return circularEditorRoles;
    }

    public void setCircularEditorRoles(List<WebUserRole> circularEditorRoles) {
        this.circularEditorRoles = circularEditorRoles;
    }

    public List<WebUserRole> getCircularAdderRoles() {
        circularAdderRoles = getFacade().findBySQL("Select d From WebUserRole d where d.name = 'circular_viewer' or d.name = 'circular_adder' ");
        return circularAdderRoles;
    }

    public void setCircularAdderRoles(List<WebUserRole> circularAdderRoles) {
        this.circularAdderRoles = circularAdderRoles;
    }

    public List<WebUserRole> getCircularViewerRoles() {
        circularViewerRoles = getFacade().findBySQL("Select d From WebUserRole d where d.name = 'circular_viewer'");
        return circularViewerRoles;
    }

    public void setCircularViewerRoles(List<WebUserRole> circularViewerRoles) {
        this.circularViewerRoles = circularViewerRoles;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public WebUserRoleFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(WebUserRoleFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebUserRoleController() {
    }

    public List<WebUserRole> getAdminRoles() {
        adminRoles = getFacade().findBySQL("Select d From WebUserRole d");
        System.out.println("Count of admins roles is " + adminRoles.size());
        return adminRoles;
    }

    public void setAdminRoles(List<WebUserRole> adminRoles) {
        this.adminRoles = adminRoles;
    }

    public WebUserRole getCurrent() {
        return current;
    }

    public void setCurrent(WebUserRole current) {
        this.current = current;
    }

    private WebUserRoleFacade getFacade() {
        return ejbFacade;
    }

    public List<WebUserRole> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = WebUserRole.class)
    public static class WebUserRoleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebUserRoleController controller = (WebUserRoleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webUserRoleController");
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
            if (object instanceof WebUserRole) {
                WebUserRole o = (WebUserRole) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + WebUserRoleController.class.getName());
            }
        }
    }
}
