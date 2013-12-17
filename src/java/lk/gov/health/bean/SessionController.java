/*
 * Author : Dr. M H B Ariyaratne
 * 
 * MO(Health Information), Department of Health Services, Southern Province 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;


import lk.gov.health.entity.WebUser;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import lk.gov.health.entity.Person;
import lk.gov.health.entity.WebUserRole;
import lk.gov.health.facade.PersonFacade;
import lk.gov.health.facade.WebUserFacade;
import lk.gov.health.facade.WebUserRoleFacade;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public class SessionController  implements Serializable {

    WebUser loggedUser = null;
    boolean logged = false;
    boolean activated = false;
    String primeTheme;
    String defLocale;
    private boolean admin;
    private boolean circularEditor;
    private boolean circularAdder;
    private boolean circularViewer;

    public boolean isAdmin() {
        System.out.println("checking admin");
        if (loggedUser == null) {
            System.out.println("admin null");
            admin= false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("admin")) {
            System.out.println("admin");
            admin= true;
        } else {
            System.out.println("not admin");
            admin = false;
        }
        return admin;
    }

    
    
    public boolean isCircularEditor() {
    if (loggedUser == null) {
            circularEditor= false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("circular_editor")||loggedUser.getRole().getName().equalsIgnoreCase("admin")) {
            circularEditor=  true;
        } else {
            circularEditor=  false;
        }
        return circularEditor;
    }

    public void setCircularEditor(boolean circularEditor) {
        this.circularEditor = circularEditor;
    }

    public boolean isCircularAdder() {
        if (loggedUser == null) {
            circularAdder= false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("circular_adder")||loggedUser.getRole().getName().equalsIgnoreCase("circular_editor")||loggedUser.getRole().getName().equalsIgnoreCase("admin")) {
            circularAdder=  true;
        } else {
            circularAdder=  false;
        }
        return circularAdder;
    }

    public void setCircularAdder(boolean circularAdder) {
        this.circularAdder = circularAdder;
    }

    public boolean isCircularViewer() {
        if (loggedUser == null) {
            circularViewer= false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("circular_viewer")||loggedUser.getRole().getName().equalsIgnoreCase("circular_editor")||loggedUser.getRole().getName().equalsIgnoreCase("admin")||loggedUser.getRole().getName().equalsIgnoreCase("circular_adder")) {
            circularViewer= true;
        } else {
            circularViewer= false;
        }
        return circularViewer;
    }

    public void setCircularViewer(boolean circularViewer) {
        this.circularViewer = circularViewer;
    }

    
    
     @EJB
    WebUserFacade uFacade;
    @EJB
    PersonFacade pFacade;
    @EJB
    WebUserRoleFacade rFacade;
        //
    WebUser current;
    String userName;
    String passord;
    String newPassword;
    String newPasswordConfirm;
    String newPersonName;
    String newUserName;
    String newDesignation;
    String newInstitution;
    String newPasswordHint;
    String telNo;
    String email;
    String displayName;
    WebUserRole role;

    public WebUserRole getRole() {
        return role;
    }

    public void setRole(WebUserRole role) {
        this.role = role;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private WebUserFacade getFacede() {
        return uFacade;
    }

    public String loginAction() {
//
//        HttpServletRequest request;
//        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        Enumeration headerIter = request.getHeaderNames();
//        String userAgent = request.getHeader("User-Agent");
//
//        while (headerIter.hasMoreElements()) {
//            String headername = (String) headerIter.nextElement();
//            System.out.println("headername + : " + request.getHeader(headername));
//        }
//
//        String clientAddr = request.getRemoteAddr();
//        String clientPc = request.getRemoteHost();
//        System.out.println("Client : " + clientPc + " & client address : " + clientAddr + " & Browser : " + userAgent);
//

        if (login()) {
            return "";
        } else {
            UtilityController.addErrorMessage("Login Failure. Please try again");
            return "";
        }
    }

    private boolean login() {
        if (userName.trim().equals("")) {
            UtilityController.addErrorMessage("Please enter a username");
            return false;
        }
        // password
        if (isFirstVisit()) {
            prepareFirstVisit();
            return true;
        } else {
            //JsfUtil.addSuccessMessage("Checking Old Users");
            return checkUsers();
        }
    }

    private void prepareFirstVisit() {
        WebUser user = new WebUser();
        Person person = new Person();
        person.setName(userName);
        pFacade.create(person);

        WebUserRole myRole;
        myRole = new WebUserRole();
        myRole.setName("circular_editor");
        rFacade.create(myRole);

        myRole = new WebUserRole();
        myRole.setName("circular_adder");
        rFacade.create(myRole);

        myRole = new WebUserRole();
        myRole.setName("circular_viewer");
        rFacade.create(myRole);


        myRole = new WebUserRole();
        myRole.setName("admin");
        rFacade.create(myRole);

        user.setName(SecurityController.encrypt(userName));
        user.setWebUserPassword(SecurityController.hash(passord));
        user.setWebUserPerson(person);
        user.setActivated(true);
        user.setRole(myRole);
        uFacade.create(user);


//        JsfUtil.addSuccessMessage("New User Added");


    }

    @SuppressWarnings("empty-statement")
    private boolean telNoOk() {

        int temp; // temp value to check if the telNo is numeric
        String[] telCodes = {"071", "072", "075", "077", "078"};

        // tel no validation
        //Chaminda to write
        if (telNo.trim().length() == 10) {
            // check if the length of the String is 10 chars
            //System.out.println("length OK !");

            try {
                temp = Integer.parseInt(telNo);
                //check if this is a number
                //System.out.println("Integer OK !");

                for (int j = 0; j < telCodes.length; j++) {
                    // check if the number starts with a valid value
                    //System.out.println("looping OK ! " + telNo.substring(0, 3) + " " + telCodes[j]);

                    if (telNo.substring(0, 3).equalsIgnoreCase(telCodes[j])) {
                        return true;
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            UtilityController.addErrorMessage("Please enter a valid Email \n" + ex.getMessage());
            result = false;
        }
        return result;
    }
    
  

    public String registeUser() {
//        if (!telNoOk()) {
//            JsfUtil.addErrorMessage("Telephone number in correct, Please enter a valid phone number");
//            return "";
//        }

        if(newUserName==null || userName.trim().equals("")){
            UtilityController.addErrorMessage("Please Enter a User Name");
            return "";
        }
        
        if (!userNameAvailable(newUserName)) {
            UtilityController.addErrorMessage("User name already exists. Plese enter another user name");
            return "";
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            UtilityController.addErrorMessage("Password and Re-entered password are not matching");
            return "";
        }
        
        if(!isValidEmailAddress(email)){
            return "";
        }
        
        WebUser user = new WebUser();
        Person person = new Person();
        user.setWebUserPerson(person);
        user.setRole(role);

        person.setName(newPersonName);
        
        pFacade.create(person);
        user.setName(SecurityController.encrypt(newUserName));
        user.setWebUserPassword(SecurityController.hash(newPassword));
        user.setWebUserPerson(person);
        user.setTelNo(telNo);
        user.setEmail(email);
        user.setActivated(Boolean.TRUE);
        
        uFacade.create(user);
        UtilityController.addSuccessMessage("New User Registered.");
        
        user=null;
        person=null;
        newPersonName="";
        newPassword="";
        telNo="";
        email="";
        newUserName="";
        
        return "";
    }
    
    public String changePassword() {
        WebUser user = loggedUser;
        if (!SecurityController.matchPassword(passord, user.getWebUserPassword())) {
            UtilityController.addErrorMessage("The old password you entered is incorrect");
            return "";
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            UtilityController.addErrorMessage("Password and Re-entered password are not maching");
            return "";
        }

        user.setWebUserPassword(SecurityController.hash(newPassword));
        uFacade.edit(user);
        //
        UtilityController.addSuccessMessage("Password changed");
        return "index";
    }

    public Boolean userNameAvailable(String userName) {
        Boolean available = true;
        List<WebUser> allUsers = getFacede().findAll();
        for (WebUser w : allUsers) {
            if (userName.toLowerCase().equals(SecurityController.decrypt(w.getName()).toLowerCase())) {
                available = false;
            }
        }
        return available;
    }

    private boolean isFirstVisit() {
        if (getFacede().count() <= 0) {
//            JsfUtil.addSuccessMessage("First Visit");
            return true;
        } else {
//            JsfUtil.addSuccessMessage("Not, Not First Visit");
            return false;
        }

    }

    private boolean checkUsers() {
//        JsfUtil.addSuccessMessage("Going to check users");
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired = false";
        List<WebUser> allUsers = getFacede().findBySQL(temSQL);
        for (WebUser u : allUsers) {
            if (SecurityController.decrypt(u.getName()).equalsIgnoreCase(userName)) {
//                JsfUtil.addSuccessMessage("A user found");

                if (SecurityController.matchPassword(passord, u.getWebUserPassword())) {
                    setLoggedUser(u);
                    setLogged(Boolean.TRUE);
                    setActivated(u.isActivated());
                    setRole(u.getRole());
                    UtilityController.addSuccessMessage("Logged successfully");
                    return true;
                }
            }
        }
        return false;
    }

    public void logout() {
        setLoggedUser(null);
        setLogged(false);
        setActivated(false);
    }

    public WebUser getCurrent() {
        if (current == null) {
            current = new WebUser();
            Person p = new Person();
            current.setWebUserPerson(p);
        }
        return current;
    }

    public void setCurrent(WebUser current) {
        this.current = current;
    }

    public WebUserFacade getEjbFacade() {
        return uFacade;
    }

    public void setEjbFacade(WebUserFacade ejbFacade) {
        this.uFacade = ejbFacade;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewDesignation() {
        return newDesignation;
    }

    public void setNewDesignation(String newDesignation) {
        this.newDesignation = newDesignation;
    }

    public String getNewInstitution() {
        return newInstitution;
    }

    public void setNewInstitution(String newInstitution) {
        this.newInstitution = newInstitution;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getNewPasswordHint() {
        return newPasswordHint;
    }

    public void setNewPasswordHint(String newPasswordHint) {
        this.newPasswordHint = newPasswordHint;
    }

    public String getNewPersonName() {
        return newPersonName;
    }

    public void setNewPersonName(String newPersonName) {
        this.newPersonName = newPersonName;
    }

    public PersonFacade getpFacade() {
        return pFacade;
    }

    public void setpFacade(PersonFacade pFacade) {
        this.pFacade = pFacade;
    }

    public WebUserFacade getuFacade() {
        return uFacade;
    }

    public void setuFacade(WebUserFacade uFacade) {
        this.uFacade = uFacade;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
       setLogged(activated);
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public WebUserRoleFacade getrFacade() {
        return rFacade;
    }

    public void setrFacade(WebUserRoleFacade rFacade) {
        this.rFacade = rFacade;
    }

    public String getDisplayName() {
        return SecurityController.decrypt(getLoggedUser().getName());
    }

    
    /**
     * Creates a new instance of SessionController
     */
    public SessionController() {
    }

    public boolean isadmin() {
        if (loggedUser == null) {
            return false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("admin")) {
            return true;
        } else {
            return false;
        }
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSuperUser() {
        if (loggedUser == null) {
            return false;
        }
        if (loggedUser.getRole().getName().equalsIgnoreCase("circular_editor")) {
            return true;
        } else {
            return false;
        }

    }

    public void setSuperUser(boolean circular_editor) {
        this.circularEditor = circular_editor;
    }

    public String getDefLocale() {
        defLocale = "en";
        if (getLoggedUser() != null) {
            if (getLoggedUser().getDefLocale() != null) {
                if (!getLoggedUser().getDefLocale().equals("")) {
                    return getLoggedUser().getDefLocale();
                }
            }
        }
        return defLocale;
    }

    public void setDefLocale(String defLocale) {
        this.defLocale = defLocale;
    }

    public String getPrimeTheme() {
        if (primeTheme == null || primeTheme.equals("")) {
            primeTheme = "hot-sneaks";
        }
        if (getLoggedUser() != null) {
            if (getLoggedUser().getPrimeTheme() != null) {
                if (!getLoggedUser().getPrimeTheme().equals("")) {
                    return getLoggedUser().getPrimeTheme();
                }
            }
        }
        return primeTheme;
    }

    public void setPrimeTheme(String primeTheme) {
        this.primeTheme = primeTheme;
    }

    /**
     *
     * @return
     */
    public WebUser getLoggedUser() {
        return loggedUser;
    }

    /**
     *
     * @param loggedUser
     */
    public void setLoggedUser(WebUser loggedUser) {
        this.loggedUser = loggedUser;
    }
}
