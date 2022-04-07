package wholepackage;

/**
 * @author Emma LU
 * @create 2022-03-30-2:57 pm
 */
public class Post {
    private int userId;
    private String title;
    private String body;

    public Post(int userId, String title, String body){
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public int getUserId(){
        return userId;
    }

    public String getTitle(){
        return title;
    }

    public String getBody(){
        return body;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + " | " + title + "\n" + body;
    }
}
