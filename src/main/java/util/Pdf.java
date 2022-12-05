package util;

import bean.Invoice;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Pdf {
    private static final String MAC_FILE_PATH = "Invoices/SourceFiles/SourceFile.pdf";
    private static final String WIN_FILE_PATH = "C:\\Invoices\\SourceFile.pdf";

    public String fillPdf(Invoice invoice, int os, String schName) throws IOException {
        // load the document
        File templateFile;

        if (os == 1) {
            templateFile = new File(WIN_FILE_PATH);
        } else {
            templateFile = new File(MAC_FILE_PATH);
        }

        PDDocument pdfDocument = PDDocument.load(templateFile);

        //get document catalog
        PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

        //set invoice date
        Calendar invoiceDate = Calendar.getInstance();
        invoiceDate.set(Calendar.DAY_OF_MONTH, 25);
        Calendar invoiceDueDate = Calendar.getInstance();
        invoiceDueDate.set(Calendar.DAY_OF_MONTH, 5);
        invoiceDueDate.add(Calendar.MONTH, 1);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        //set invoice no. and filename
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("MMMyy");
        String invoiceNo = schName + "-" + fileNameFormat.format(invoiceDueDate.getTime()) + "-" + invoice.getChildId();
        String fileName = invoiceNo + ".pdf";

        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {
            // Retrieve an individual field and set its value.
            if (invoice.getParentType().equals("Parent")) {
                PDTextField parentName1 = (PDTextField) acroForm.getField("ParentName1");
                parentName1.setValue(invoice.getFatherName());

                PDTextField parentEmail1 = (PDTextField) acroForm.getField("ParentEmail1");
                parentEmail1.setValue(invoice.getFatherEmail());

                PDTextField parentName2 = (PDTextField) acroForm.getField("ParentName2");
                parentName2.setValue(invoice.getMotherName());

                PDTextField parentEmail2 = (PDTextField) acroForm.getField("ParentEmail2");
                parentEmail2.setValue(invoice.getMotherEmail());
            } else {
                PDTextField parentName1 = (PDTextField) acroForm.getField("ParentName1");
                parentName1.setValue(invoice.getGuardianName());

                PDTextField parentEmail1 = (PDTextField) acroForm.getField("ParentEmail1");
                parentEmail1.setValue(invoice.getGuardianEmail());
            }

            PDTextField invoiceNoField = (PDTextField) acroForm.getField("InvoiceNo");
            invoiceNoField.setValue(invoiceNo);

            PDTextField date = (PDTextField) acroForm.getField("InvoiceDate");
            date.setValue(outputDateFormat.format(invoiceDate.getTime()));

            PDTextField dueDate = (PDTextField) acroForm.getField("DueDate");
            dueDate.setValue(outputDateFormat.format(invoiceDueDate.getTime()));

            PDTextField mth = (PDTextField) acroForm.getField("Mth1");
            mth.setValue(new SimpleDateFormat("MMM").format(invoiceDueDate.getTime()));

            PDTextField desc1 = (PDTextField) acroForm.getField("Desc1");
            desc1.setValue("School Bus Fee for " + invoice.getChildName());

            PDTextField fare1 = (PDTextField) acroForm.getField("Fare1");
            fare1.setValue("$" + invoice.getFare());

            PDTextField subtotal = (PDTextField) acroForm.getField("Subtotal");
            subtotal.setValue(invoice.getFare());

            PDTextField balDue = (PDTextField) acroForm.getField("BalDue");
            balDue.setValue(invoice.getFare());

            // Flatten PDF to prevent further editing
            acroForm.flatten();

            // Save File
            String dirName;
            String childName = invoice.getChildName();

            if (os == 1) {
                // windows
                dirName = "C:\\Invoices\\" + childName + "\\";
            } else {
                // mac
                dirName = "Invoices/" + childName + "/";
            }

            File file = fileWithDirectoryAssurance(dirName, fileName);
            pdfDocument.save(file);
            pdfDocument.close();
        }
        return fileName;
    }

    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return new File(directory + "/" + filename);
    }

}
