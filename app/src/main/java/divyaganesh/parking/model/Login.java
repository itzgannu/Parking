package divyaganesh.parking.model;

public class Login {
    private String Email;
    private String Password;

    public Login(){
    }

    public Login(String Email, String Password){
        this.Email = Email;
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
