package divyaganesh.parking.model;

public class Account {
    private String Name;
    private String Email;
    private String Password;
    private String ContactNo;
    private String CarNo;
    private int Image;
    private String id;

    public Account(){

    }

    @Override
    public String toString() {
        return "Account{" +
                "Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", ContactNo='" + ContactNo + '\'' +
                ", CarNo='" + CarNo + '\'' +
                ", image=" + Image +
                ", id='" + id + '\'' +
                '}';
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        this.Image = image;
    }
    public Account(String name, String email, String password, String contactNo, String carNo) {
        Name = name;
        Email = email;
        Password = password;
        ContactNo = contactNo;
        CarNo = carNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
