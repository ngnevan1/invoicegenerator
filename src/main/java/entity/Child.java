package entity;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;

public class Child {

    @CsvBindByName(column = "Level")
    private String level;

    @CsvBindByName(column = "Father's Name")
    private String fatherName;

    @CsvBindByName(column = "Father's Email Address")
    private String fatherEmail;

    @CsvBindByName(column = "Mother's Name")
    private String motherName;

    @CsvBindByName(column = "Mother's Email Address")
    private String motherEmail;

    @CsvBindByName(column = "Name", required = true)
    private String childName;

    @CsvBindByName(column = "FARE", required = true, locale = "en-SG")
    private BigDecimal fare;

    public Child() {
    }

    public String getLevel() {
        return level;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getFatherEmail() {
        return fatherEmail;
    }

    public String getMotherName() {
        return motherName;
    }

    public String getMotherEmail() {
        return motherEmail;
    }

    public String getChildName() {
        return childName;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
