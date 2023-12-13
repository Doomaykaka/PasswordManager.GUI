package passwordmanager.gui.decoded;

public class DefaultRecord implements Record {
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;
    private String info;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String newLogin) {
        login = newLogin;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String newInfo) {
        info = newInfo;
    }

}
