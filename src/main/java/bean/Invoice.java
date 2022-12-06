package bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Invoice {
    private String parentName1;

    private String parentEmail1;

    private String parentName2;

    private String parentEmail2;

    private List<String> childNames;

    private String invoiceNo;

    private final String invoiceDate;

    private final String dueDate;

    private final String mth;

    private List<String> descs;

    private List<BigDecimal> fares;

    private BigDecimal subtotal;
    private BigDecimal balDue;

    private final String fileName;

    public Invoice(String schName, List<Child> childIds) {
        //set invoice dates
        Calendar invoiceDate = Calendar.getInstance();
        invoiceDate.set(Calendar.DAY_OF_MONTH, 25);
        Calendar invoiceDueDate = Calendar.getInstance();
        invoiceDueDate.set(Calendar.DAY_OF_MONTH, 5);
        invoiceDueDate.add(Calendar.MONTH, 1);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        this.invoiceDate = outputDateFormat.format(invoiceDate.getTime());
        this.dueDate = outputDateFormat.format(invoiceDueDate.getTime());
        this.mth = new SimpleDateFormat("MMM").format(invoiceDueDate.getTime());

        //set invoice no. and filename
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("MMMyy");
        invoiceNo = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-";

        for (Child c : childIds) {
            invoiceNo+= c.getChildId() + ".";
        }
        invoiceNo = removeLastChar(invoiceNo);
        fileName = invoiceNo + ".pdf";
    }

    public Invoice(List<Child> children, String schName) {
        this(schName, children);
        Child lineItem = children.get(0);

        if (lineItem.getParentType().equals("Parent")) {
            if (lineItem.getFatherName().isEmpty() || lineItem.getFatherName() == null) {
                parentName1 = lineItem.getMotherName();
                parentEmail1 = lineItem.getMotherEmail();
            } else if (lineItem.getMotherName().isEmpty() || lineItem.getMotherName() == null) {
                parentName1 = lineItem.getFatherName();
                parentEmail1 = lineItem.getFatherEmail();
            } else {
                parentName1 = lineItem.getFatherName();
                parentEmail1 = lineItem.getFatherEmail();

                parentName2 = lineItem.getMotherName();
                parentEmail2 = lineItem.getMotherEmail();
            }
        } else {
            parentName1 = lineItem.getGuardianName();
            parentEmail1 = lineItem.getGuardianEmail();
        }
        List<String> descriptions = new ArrayList<>();
        List<BigDecimal> fares = new ArrayList<>();
        List<String> childNames = new ArrayList<>();
        for (Child c : children) {
           descriptions.add("School Bus Fee for " + c.getChildName());
           fares.add(c.getFare());
            childNames.add(c.getChildName());
        }
        this.descs = descriptions;
        this.fares = fares;
        this.childNames = childNames;

        BigDecimal total = new BigDecimal("0.00");
        for (BigDecimal f : fares) {
            total = total.add(f);
        }
        this.subtotal = total;
        this.balDue = total;
    }

    public static String removeLastChar(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    public String getParentName1() {
        return parentName1;
    }

    public String getParentEmail1() {
        return parentEmail1;
    }

    public String getParentName2() {
        return parentName2;
    }

    public String getParentEmail2() {
        return parentEmail2;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getMth() {
        return mth;
    }

    public List<String> getDescs() {
        return descs;
    }

    public List<BigDecimal> getFares() {
        return fares;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getBalDue() {
        return balDue;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getChildNames() {
        return childNames;
    }

}
