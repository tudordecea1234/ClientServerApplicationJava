package dtos;

import java.io.Serializable;

public class VolunteerDTO implements Serializable {
    public VolunteerDTO(String email){
        this.email=email;
        this.password="";
    }
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

    private String email;
    private String password;
}
