package passwordmanager.gui.decoded;

import java.io.Serializable;

/**
 * Decrypted record with information about login, password, description
 * 
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface IRecord extends Serializable {
    /**
     * Method for getting login from a record
     * 
     * @return login
     */
    public String getLogin();

    /**
     * Method for changing the login of an entry
     * 
     * @param newLogin new login name
     */
    public void setLogin(String newLogin);

    /**
     * Method for getting password from a record
     * 
     * @return password
     */
    public String getPassword();

    /**
     * Method for changing the password of an entry
     * 
     * @param newPassword new password
     */
    public void setPassword(String newPassword);

    /**
     * Method for obtaining additional information about a record
     * 
     * @return additional information about a record
     */
    public String getInfo();

    /**
     * Method for changing additional record information
     * 
     * @param newInfo new additional record information
     */
    public void setInfo(String newInfo);
}
