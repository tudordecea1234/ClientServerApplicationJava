package model;

import java.io.Serializable;

public class CharityCase implements Identifiable<Long>, Serializable {
    private Long id;
    private String caseName;
    private float totalAmount;


    public CharityCase(){}
    public CharityCase(String denumire, float totalAmount) {
        this.caseName = denumire;
        this.totalAmount = totalAmount;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String denumire) {
        this.caseName = denumire;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "CharityCase{" +
                "id=" + id +
                ", caseName='" + caseName + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
