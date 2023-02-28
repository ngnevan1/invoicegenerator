package entity;

import util.InvoiceType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Invoice {
    private final String parentName1;

    private final String parentEmail1;

    private String parentName2;

    private String parentEmail2;

    private final List<String> childNames;

    private final String invoiceNo;

    private final String invoiceDate;

    private final String dueDate;

    private final String mth;

    private final List<String> descs;

    private final List<BigDecimal> fares;

    private final BigDecimal subtotal;
    private final BigDecimal balDue;

    private final String fileName;

    public Invoice(List<Child> children, String schName, int childCount, List<Date> invoiceDates, InvoiceType type) {
        String childName = children.get(0).getChildName();
        String childNameFormatted = String.valueOf(childName).trim();
        childNameFormatted = childNameFormatted.replace("/","");

        //set invoice dates
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date invoiceDate = invoiceDates.get(0);
        Date invoiceDueDate = invoiceDates.get(1);
        this.invoiceDate = outputDateFormat.format(invoiceDate);
        this.dueDate = outputDateFormat.format(invoiceDueDate);

        //set invoice no., filename and descriptions
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("MMMyy");
        SimpleDateFormat mthFormat = new SimpleDateFormat("MMM");
        List<String> descriptions = new ArrayList<>();
        List<String> childNames = new ArrayList<>();

        if (type.equals(InvoiceType.REGULAR)) {
            this.mth = mthFormat.format(invoiceDueDate);
            this.fileName = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-" + childNameFormatted.replaceAll(" ", ".") + ".pdf";
            this.invoiceNo = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-" + childCount;
            descriptions.add("School Bus Fee for " + childName);
        } else {
            Calendar mth = Calendar.getInstance();
            mth.setTime(invoiceDate);
            mth.add(Calendar.MONTH, -1);
            this.mth = mthFormat.format(mth.getTime());
            this.fileName = schName + "-CCA-" + fileNameFormat.format(mth.getTime()) + "-" + childNameFormatted.replaceAll(" ", ".") + ".pdf";
            this.invoiceNo = schName + "-" + fileNameFormat.format(mth.getTime()) + "-C" + childCount;
            descriptions.add("CCA Bus Fee for " + childName);
            descriptions.add(children.get(0).getCcaDates());
        }
        childNames.add(childNameFormatted);
        this.descs = descriptions;
        this.childNames = childNames;

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
