package qb.com.top_news.vo;


public class Contact {
    private int id;
    private String name;
    private String phone;
    private String sortKey;
    public Contact() {
    }

    public Contact(int id, String name, String phone,String sortKey) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sortKey=sortKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sortKey='" + sortKey + '\'' +
                '}';
    }
}

