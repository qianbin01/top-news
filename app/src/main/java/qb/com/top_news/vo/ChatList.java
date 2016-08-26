package qb.com.top_news.vo;


public class ChatList {
    private int id;
    private String name;
    private String head;
    private String time;
    private String content;
    private int isRead;
    private int count;
    public ChatList( String name, String head, String time, String content,int isRead,int count) {

        this.name = name;
        this.head = head;
        this.time = time;
        this.content = content;
        this.isRead=isRead;
        this.count=count;
    }

    public ChatList() {
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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
