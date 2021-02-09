package ru.geekbrains;

public class CloudAuthMessage implements Message{
    private String cmd;
    private String login;
    private String password;
    private String nickname;
    private String path;
    private boolean result;


    public CloudAuthMessage(String cmd, String login, String password) {
        this.cmd = cmd;
        this.login = login;
        this.password = password;
    }

    public CloudAuthMessage(String cmd, String nickname, String path, boolean result) {
        this.cmd = cmd;
        this.nickname = nickname;
        this.path = path;
        this.result = result;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPath() {
        return path;
    }

    public String getCmd() {
        return cmd;
    }

    public boolean getResult() {
        return result;
    }
}
