package passwordmanager.gui.decoded;

/**
 * Standard implementation of a decrypted record with information about login,
 * password, description
 * 
 * @see Record
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultRecord implements Record {
    /**
     * Class version number for checks when desirializing and serializing class
     * objects
     */
    private static final long serialVersionUID = 1L;
    /**
     * Login value
     */
    private String login;
    /**
     * Password value
     */
    private String password;
    /**
     * Info value
     */
    private String info;

    /**
     * Method for getting login from a record
     * 
     * @return login
     */
    @Override
    public String getLogin() {
        return login;
    }

    /**
     * Method for changing the login of an entry
     * 
     * @param newLogin new login name
     */
    @Override
    public void setLogin(String newLogin) {
        login = newLogin;
    }

    /**
     * Method for getting password from a record
     * 
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Method for changing the password of an entry
     * 
     * @param newPassword new password
     */
    @Override
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Method for obtaining additional information about a record
     * 
     * @return additional information about a record
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Method for changing additional record information
     * 
     * @param newInfo new additional record information
     */
    @Override
    public void setInfo(String newInfo) {
        info = newInfo;
    }

}
