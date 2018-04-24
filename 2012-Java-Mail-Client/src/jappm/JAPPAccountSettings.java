/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

/**
 *
 * @author Ivan
 */
public class JAPPAccountSettings {

    public int settingsId = -1;
    public int accountId = -1;
    public String accountName = "";
    public String name = "";
    public String email = "";
    public String userIn = "";
    public String userOut = "";
    public String serverIn = "";
    public String serverOut = "";
    public int serverInTypeId = 1;
    public String serverInType = "IMAP";
    public boolean serverInIsSSL = false;
    public boolean serverOutIsSSL = false;
    public int serverInPort = -1;
    public int serverOutPort = -1;
    public String passwordIn = "";
    public String passwordOut = "";

    public JAPPAccountSettings(String accountName) {
        this.accountName = accountName;
    }

    public JAPPAccountSettings(int settingsId,
            int accountId,
            String accountName,
            String name,
            String email,
            String userIn,
            String userOut,
            String serverIn,
            String serverOut,
            int serverInTypeId,
            String serverInType,
            boolean serverInIsSSL,
            boolean serverOutIsSSL,
            int serverInPort,
            int serverOutPort,
            String passwordIn,
            String passwordOut) {


        this.settingsId = settingsId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.name = name;
        this.email = email;
        this.userIn=userIn;
        this.userOut=userOut;
        this.serverIn = serverIn;
        this.serverOut = serverOut;
        this.serverInTypeId = serverInTypeId;
        this.serverInType = serverInType;
        this.serverInIsSSL = serverInIsSSL;
        this.serverOutIsSSL = serverOutIsSSL;
        this.serverInPort = serverInPort;
        this.serverOutPort = serverOutPort;
        this.passwordIn = passwordIn;
        this.passwordOut = passwordOut;
    }

    public void fullReinit(Integer settingsId,
            Integer accountId,
            String accountName,
            String name,
            String email,
            String userIn,
            String userOut,
            String serverIn,
            String serverOut,
            Integer serverInTypeId,
            String serverInType,
            Integer serverOutTypeId,
            String serverOutType,
            boolean serverInIsSSL,
            boolean serverOutIsSSL,
            Integer serverInPort,
            Integer serverOutPort,
            String passwordIn,
            String passwordOut) {


        this.settingsId = settingsId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.name = name;
        this.email = email;
        this.serverIn = serverIn;
        this.serverOut = serverOut;
        this.serverInTypeId = serverInTypeId;
        this.serverInType = serverInType;
        this.serverInIsSSL = serverInIsSSL;
        this.serverOutIsSSL = serverOutIsSSL;
        this.serverInPort = serverInPort;
        this.serverOutPort = serverOutPort;
        this.passwordIn = passwordIn;
        this.passwordOut = passwordOut;
    }

    public Integer getAccountId() {
        return this.accountId;
    }

    //TODO get and set methods for all fields
    @Override
    public String toString() {
        return this.accountName;
    }
}
