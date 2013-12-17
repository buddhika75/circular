/*
 * Author : Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 * and open the template in the editor.
 */
package lk.gov.health.bean;


import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import lk.gov.health.entity.Person;
import lk.gov.health.entity.WebUser;
import lk.gov.health.facade.PersonFacade;
import lk.gov.health.facade.WebUserFacade;

/**
 *
 * @author Buddhika
 */
@Named
@RequestScoped
public class UserApproveController implements Serializable {

    DataModel<WebUser> toApproveUsers;
    DataModel<WebUser> users;
    WebUser selectedUser;
    Person selectedPerson;
    //
    @EJB
    WebUserFacade userFacade;
    @EJB
    PersonFacade personFacade;
    //
    String activateComments;
    @Inject
    private SessionController sessionController;
    

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

 
    

    public void setUsers(DataModel<WebUser> users) {
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

    public DataModel<WebUser> getToApproveUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired=false AND u.activated=false";
        List<WebUser> lst;
        lst = getUserFacade().findBySQL(temSQL);
        return new ListDataModel<WebUser>(lst);
    }

    public void setToApproveUsers(DataModel<WebUser> toApproveUsers) {
        this.toApproveUsers = toApproveUsers;
    }

    public WebUserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(WebUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void addUserToPerson(){
        
    }
    
    public void saveUser() {
        if (selectedUser == null) {
            UtilityController.addErrorMessage("Please select a user");
            return;
        }

        userFacade.edit(selectedUser);

        selectedUser = null;
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

        

       

        
    }

    
    public void removeUser() {
        if (selectedUser == null) {
            UtilityController.addErrorMessage("Please select a user to remove");
            return;
        }
        selectedUser.setActivated(false);
        selectedUser.setRetired(true);
        selectedUser.setRetiredAt(Calendar.getInstance().getTime());
        selectedUser.setRetirer(sessionController.loggedUser);
        selectedUser.setRetireComments(activateComments);
        userFacade.edit(selectedUser);

        selectedUser = null;

        UtilityController.addSuccessMessage("Successfully activated");
    }
    
    
    
}
