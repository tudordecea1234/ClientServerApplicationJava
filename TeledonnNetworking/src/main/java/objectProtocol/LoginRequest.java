package objectProtocol;

import model.Volunteer;

public class LoginRequest implements Request{
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    public LoginRequest(String email,String password){
        this.password=password;
        this.email=email;
    }

}
