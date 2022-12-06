package bean;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;

public class Child {
    @CsvBindByName(column = "ID", required = true)
    private String childId;

    @CsvBindByName(column = "Parent or Guardian", required = true)
    private String parentType;

    @CsvBindByName(column = "Father's Name")
    private String fatherName;

    @CsvBindByName(column = "Father's Email Address")
    private String fatherEmail;

    @CsvBindByName(column = "Mother's Name")
    private String motherName;

    @CsvBindByName(column = "Mother's Email Address")
    private String motherEmail;

    @CsvBindByName(column = "Guardian's Name")
    private String guardianName;

    @CsvBindByName(column = "Guardian's Email")
    private String guardianEmail;

    @CsvBindByName(column = "Name", required = true)
    private String childName;

    @CsvBindByName(column = "FARE", required = true, locale = "en-SG")
    private BigDecimal fare;

    public Child() {
    }

    public String getChildId() {
        return childId;
    }

    public String getParentType() {
        return parentType;
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

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public String getChildName() {
        return childName;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
