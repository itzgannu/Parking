package divyaganesh.parking.model;

/*
* This class will be used to assign data / retrieve data
* We also use this class in implementing recycler view model adapter class
* Adapter class will access the class members & assign CarNo & Date to the list of parking activity
* We need to create a mutable class to read data of parking list collection when user logs in
* */

import java.io.Serializable;

public class Parking implements Serializable {
    private String BuildingNo;
    private String CarNo;
    private String Date;
    private String Email;
    private String HostNo;
    private String Hours;
    private Double Lat;
    private Double Long;
    private String Address;
    private String id;

    public Parking() {
    }

    public Parking(String buildingNo, String carNo, String date, String email, String hostNo, String hours, Double lat, Double aLong, String address) {
        BuildingNo = buildingNo;
        CarNo = carNo;
        Date = date;
        Email = email;
        HostNo = hostNo;
        Hours = hours;
        Lat = lat;
        Long = aLong;
        Address = address;
    }

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

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "BuildingNo='" + BuildingNo + '\'' +
                ", CarNo='" + CarNo + '\'' +
                ", Date='" + Date + '\'' +
                ", Email='" + Email + '\'' +
                ", HostNo='" + HostNo + '\'' +
                ", Hours='" + Hours + '\'' +
                ", Lat=" + Lat +
                ", Long=" + Long +
                ", Address='" + Address + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
