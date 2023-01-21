package entity;

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

    public Invoice(String schName, String childName, int childCount, int dateOfInvoice, int dueDateOfInvoice) {
        //set invoice dates
        Calendar invoiceDate = Calendar.getInstance();
        invoiceDate.set(Calendar.DAY_OF_MONTH, dateOfInvoice);
        Calendar invoiceDueDate = Calendar.getInstance();
        invoiceDueDate.set(Calendar.DAY_OF_MONTH, dueDateOfInvoice);
        invoiceDueDate.add(Calendar.MONTH, 1);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        this.invoiceDate = outputDateFormat.format(invoiceDate.getTime());
        this.dueDate = outputDateFormat.format(invoiceDueDate.getTime());
        this.mth = new SimpleDateFormat("MMM").format(invoiceDueDate.getTime());

        //set invoice no. and filename
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("MMMyy");
        String childNameFormatted = String.valueOf(childName);
        childNameFormatted = childNameFormatted.replace("/","");
        this.fileName = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-" + childNameFormatted.replaceAll(" ", ".") + ".pdf";

        this.invoiceNo = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-" + childCount;

        List<String> descriptions = new ArrayList<>();
        List<String> childNames = new ArrayList<>();

        descriptions.add("School Bus Fee for " + childName);
        childNames.add(childNameFormatted);
        this.descs = descriptions;
        this.childNames = childNames;
    }

    public Invoice(List<Child> children, String schName, int childCount, int dateOfInvoice, int dueDateOfInvoice) {
        this(schName, children.get(0).getChildName(), childCount, dateOfInvoice, dueDateOfInvoice);
        Child lineItem = children.get(0);

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

        BigDecimal totalFare = new BigDecimal("0.00");
        for (Child c : children) {
            totalFare = totalFare.add(c.getFare());
        }
        List<BigDecimal> fares = new ArrayList<>();
        fares.add(totalFare);
        this.fares = fares;

        BigDecimal total = new BigDecimal("0.00");
        for (BigDecimal f : this.fares) {
            total = total.add(f);
        }
        this.subtotal = total;
        this.balDue = total;
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
