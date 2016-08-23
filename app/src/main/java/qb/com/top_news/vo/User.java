package qb.com.top_news.vo;


public class User {
    private int id;
    private String phone;
    private String password;
    private String headPath;
    private String nickname;
    private String signature;

    public User(int id, String phone, String password, String headPath, String nickname, String signature) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.headPath = headPath;
        this.nickname = nickname;
        this.signature = signature;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
