package model;

import java.io.Serializable;

public class Donation implements Identifiable<Long>, Serializable {
    private Long idCase;
    private String donorFirstName;
    private String donorLastName;
    private String address;
    private String phoneNumber;
    private float amountDonated;

    public Donation(Long idCase, String donorFirstName, String donorLastName, String address, String phoneNumber, float amountDonated) {
        this.idCase = idCase;
        this.donorFirstName = donorFirstName;
        this.donorLastName = donorLastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.amountDonated = amountDonated;
    }

    public Donation() {

    }

    public Long getIdCase() {
        return idCase;
    }

    public void setIdCase(Long idCase) {
        this.idCase = idCase;
    }

    public String getDonorFirstName() {
        return donorFirstName;
    }

    public void setDonorFirstName(String donorFirstName) {
        this.donorFirstName = donorFirstName;
    }

    public String getDonorLastName() {
        return donorLastName;
    }

    public void setDonorLastName(String donorLastName) {
        this.donorLastName = donorLastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(float amountDonated) {
        this.amountDonated = amountDonated;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }  private Long id;

    @Override
    public String toString() {
        return "Donation with " +
                "id=" + id +
                ", idCase=" + idCase +
                ", donorFirstName='" + donorFirstName + '\'' +
                ", donorLastName='" + donorLastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", amountDonated=" + amountDonated ;

    }


}
