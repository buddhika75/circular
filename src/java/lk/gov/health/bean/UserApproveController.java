/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.bean;

import lk.gov.health.entity.WebUser;
import lk.gov.health.entity.WebUserRole;
import lk.gov.health.facade.WebUserFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Buddhika
 */
@ManagedBean
@RequestScoped
public class UserApproveController implements Serializable {

    List<WebUser> toApproveUsers;
    List<WebUser> users;
    WebUser selectedUser;
    //
    @EJB
    WebUserFacade userFacade;
    String activateComments;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public List<WebUser> getUsers() {
        String temSql;
            temSql = "SELECT a FROM WebUser a WHERE a.retired=false AND a.webUserPerson.institution.id = " + sessionController.getLoggedUser().getRestrictedInstitution().getId() + " ORDER BY a.name ";
        return getUserFacade().findBySQL(temSql);
    }

    public void setUsers(List<WebUser> users) {
        this.users = users;
    }

    
    /**
     * Creates a new instance of UserApproveController
     */
    public UserApproveController() {
    }

    public String getActivateComments() {
        return activateComments;
    }

    public void setActivateComments(String activateComments) {
        this.activateComments = activateComments;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(WebUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<WebUser> getToApproveUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired=false AND u.activated=false";
        return getUserFacade().findBySQL(temSQL);
    }

    public void setToApproveUsers(List<WebUser> toApproveUsers) {
        this.toApproveUsers = toApproveUsers;
    }

    public WebUserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(WebUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void saveUser() {
        if (selectedUser == null) {
            UtilityController.addErrorMessage("Please select a user");
            return;
        }
        userFacade.edit(selectedUser);
        UtilityController.addSuccessMessage("Successfully activated");
    }

    public void approveUser() {
        if (selectedUser == null) {
            UtilityController.addErrorMessage("Please select a user to approve");
            return;
        }
        selectedUser.setActivated(true);
        selectedUser.setActivatedAt(Calendar.getInstance().getTime());
        selectedUser.setActivator(sessionController.loggedUser);
        selectedUser.setActivateComments(activateComments);
        userFacade.edit(selectedUser);
        selectedUser = null;
        UtilityController.addSuccessMessage("Successfully activated");
    }
}
