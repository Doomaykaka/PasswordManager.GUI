package passwordmanager.gui.decoded;

import java.io.Serializable;

public interface Record extends Serializable {
    public String getLogin();

    public void setLogin(String newLogin);

    public String getPassword();

    public void setPassword(String newPassword);

    public String getInfo();

    public void setInfo(String newInfo);
}
