package wholepackage;

/**
 * @author Emma LU
 * @create 2022-03-30-3:34 pm
 */
public class UserPost {

    private String token;
    private Object user;

    public UserPost(String token, Object user){
        this.token = token;
        this.user = user;
    }

    public String getToken(){
        return token;
    }

    public Object getUser(){
        return user;
    }

    @Override
    public String toString() {
        return "Token: " + token + " | " + user + "\n";
    }

}
