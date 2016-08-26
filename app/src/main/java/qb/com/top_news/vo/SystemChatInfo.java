package qb.com.top_news.vo;


public class SystemChatInfo {
    private int id;
    private String name;
    private String head;
    private String time;
    private String content;
    private boolean isComing;

    public SystemChatInfo(String name, String head, String time, String content, boolean isComing) {
        this.name = name;
        this.head = head;
        this.time = time;
        this.content = content;
        this.isComing = isComing;
    }

    public SystemChatInfo() {

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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isComing() {
        return isComing;
    }

    public void setComing(boolean coming) {
        isComing = coming;
    }
}
