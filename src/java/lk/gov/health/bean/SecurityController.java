/*
 * Author : Dr. M H B Ariyaratne
 * 
 * MO(Health Information), Department of Health Services, Southern Province 
 * and
 * Email : buddhika.ari@gmail.com
 */
package lk.gov.health.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@ManagedBean
@SessionScoped
public class SecurityController  implements Serializable {

    /**
     * Creates a new instance of HOSecurity
     */
    public SecurityController() {
    }

    public static String encrypt(String word) {
        BasicTextEncryptor en = new BasicTextEncryptor();
        en.setPassword("health");
        try {
            return en.encrypt(word);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String hash(String word){
        try{
            BasicPasswordEncryptor en = new BasicPasswordEncryptor();
            return en.encryptPassword(word);
        }catch(Exception e){
            return null;
        }
    }
    
    
    public static boolean matchPassword(String planePassword,String encryptedPassword ){
        BasicPasswordEncryptor en = new BasicPasswordEncryptor();
        return en.checkPassword(planePassword, encryptedPassword);
    }
    
    public static String decrypt(String word){
        BasicTextEncryptor en = new BasicTextEncryptor();
        en.setPassword("health");
        try {
            return en.decrypt(word);
        } catch (Exception ex) {
            return null;
        }
        
    }
    
    
}
