package securitybasicauth.demo;

public class LoginModelUser {

    private String email;
    private String password;

    public LoginModelUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginModelUser() {
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
}
