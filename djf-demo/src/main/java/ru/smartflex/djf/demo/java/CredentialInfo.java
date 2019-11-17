package ru.smartflex.djf.demo.java;

public class CredentialInfo {

    private String login;
    private String password;

    @SuppressWarnings("WeakerAccess")
    public CredentialInfo(String login, String password) {
        super();
        this.login = login;
        this.password = password;
    }

    @SuppressWarnings("unused")
    public String getLogin() {
        return login;
    }

    @SuppressWarnings("unused")
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CredentialInfo [login=" + login + "]";
    }

    public boolean isFilled() {
        boolean fok = false;
        if (login != null && password != null) {
            fok = true;
        }
        return fok;
    }
}
