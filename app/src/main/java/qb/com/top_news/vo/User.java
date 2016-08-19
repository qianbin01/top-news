package qb.com.top_news.vo;


public class User {
    private int id;
    private String phone;
    private String password;
    private String headPath;

    public User() {
    }

    public User(int id, String phone, String password, String headPath) {

        this.id = id;
        this.phone = phone;
        this.password = password;
        this.headPath = headPath;
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
}
