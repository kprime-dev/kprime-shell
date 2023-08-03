package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;

import java.util.Properties;

public class ServerRequiredParams {
    private String context;
    private String serverName;
    private String address;
    private String userName;
    private String userPass;

    public static ServerRequiredParams fromProperties(Properties properties) {
        String serverName = properties.getProperty(Commandable.must_arg_server_name, "");
        return new ServerRequiredParams(
                properties.getProperty(Commandable.must_arg_context, ""),
                serverName,
                properties.getProperty(serverName, ""),
                properties.getProperty(Commandable.must_arg_user_name, ""),
                properties.getProperty(Commandable.must_arg_user_pass, "")
        );
    }

    public ServerRequiredParams(String context, String serverName, String address, String userName, String userPass) {
        this.context = context;
        this.serverName = serverName;
        this.address = address;
        this.userName = userName;
        this.userPass = userPass;
    }

    public String getContext() { return context; }

    public String getServerName() {
        return serverName;
    }

    public String getAddress() {
        return address;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setContext(String context) { this.context = context; }

    public void setServerName(String serverName) {this.serverName = serverName; }

}
