package divyaganesh.parking.model;

/*
* This class will be used to assign data / retrieve data
* We also use this class in implementing recycler view model adapter class
* Adapter class will access the class members & assign CarNo & Date to the list of parking activity
* We need to create a mutable class to read data of parking list collection when user logs in
* */

public class Parking {
    private String BuildingNo;
    private String CarNo;
    private String Date;
    private String Email;
    private String HostNo;
    private String Lat;
    private String Long;
    private String Address;

    public Parking(String BuildingNo, String CarNo, String Date, String Email, String HostNo, String Lat, String Long, String Address){
        this.BuildingNo = BuildingNo;
        this.CarNo = CarNo;
        this.Date = Date;
        this.Email = Email;
        this.HostNo = HostNo;
        this.Lat = Lat;
        this.Long = Long;
        this.Address = Address;
    }

    public Parking(){}

    public String getBuildingNo() {
        return BuildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        BuildingNo = buildingNo;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHostNo() {
        return HostNo;
    }

    public void setHostNo(String hostNo) {
        HostNo = hostNo;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
