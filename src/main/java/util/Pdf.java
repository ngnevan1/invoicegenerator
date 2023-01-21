package util;

import entity.Invoice;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.*;
import java.util.List;

public class Pdf {
    private static final String MAC_PDF_PATH = "Invoices/00-SourceFiles/SourceFile.pdf";
    private static final String WIN_PDF_PATH = "C:\\Invoices\\00-SourceFiles\\SourceFile.pdf";

    public InputStream fillPdf(Invoice invoice, int os) throws IOException {
        // load the document
        InputStream in = null;
        File templateFile;

        if (os == 1) {
            templateFile = new File(WIN_PDF_PATH);
        } else {
            templateFile = new File(MAC_PDF_PATH);
        }

        PDDocument pdfDocument = PDDocument.load(templateFile);

        //get document catalog
        PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {
            // Retrieve an individual field and set its value.
            PDTextField parentName1 = (PDTextField) acroForm.getField("ParentName1");
            parentName1.setValue(invoice.getParentName1());

            PDTextField parentEmail1 = (PDTextField) acroForm.getField("ParentEmail1");
            parentEmail1.setValue(invoice.getParentEmail1());

            if (invoice.getParentName2() != null) {
                PDTextField parentName2 = (PDTextField) acroForm.getField("ParentName2");
                parentName2.setValue(invoice.getParentName2());

                PDTextField parentEmail2 = (PDTextField) acroForm.getField("ParentEmail2");
                parentEmail2.setValue(invoice.getParentEmail2());
            }

            PDTextField invoiceNoField = (PDTextField) acroForm.getField("InvoiceNo");
            invoiceNoField.setValue(invoice.getInvoiceNo());

            PDTextField date = (PDTextField) acroForm.getField("InvoiceDate");
            date.setValue(invoice.getInvoiceDate());

            PDTextField dueDate = (PDTextField) acroForm.getField("DueDate");
            dueDate.setValue(invoice.getDueDate());

            PDTextField mth1 = (PDTextField) acroForm.getField("Mth1");
            mth1.setValue(invoice.getMth());

            PDTextField desc1 = (PDTextField) acroForm.getField("Desc1");
            desc1.setValue(invoice.getDescs().get(0));

            PDTextField fare1 = (PDTextField) acroForm.getField("Fare1");
            fare1.setValue("$" + invoice.getFares().get(0));

            if (invoice.getDescs().size() >= 2) {
                PDTextField mth2 = (PDTextField) acroForm.getField("Mth2");
                mth2.setValue(invoice.getMth());

                PDTextField desc2 = (PDTextField) acroForm.getField("Desc2");
                desc2.setValue(invoice.getDescs().get(1));

                PDTextField fare2 = (PDTextField) acroForm.getField("Fare2");
                fare2.setValue("$" + invoice.getFares().get(1));
            }

            if (invoice.getDescs().size() >= 3) {
                PDTextField mth3 = (PDTextField) acroForm.getField("Mth3");
                mth3.setValue(invoice.getMth());

                PDTextField desc2 = (PDTextField) acroForm.getField("Desc3");
                desc2.setValue(invoice.getDescs().get(2));

                PDTextField fare2 = (PDTextField) acroForm.getField("Fare3");
                fare2.setValue("$" + invoice.getFares().get(2));
            }

            PDTextField subtotal = (PDTextField) acroForm.getField("Subtotal");
            subtotal.setValue("$" + invoice.getSubtotal());

            PDTextField balDue = (PDTextField) acroForm.getField("BalDue");
            balDue.setValue("$" + invoice.getBalDue());

            // Flatten PDF to prevent further editing
            acroForm.flatten();

            // Save File
            String dirName;
            List<String> childNames = invoice.getChildNames();

            for (String childName : childNames) {
                if (os == 1) {
                    // windows
                    dirName = "C:\\Invoices\\" + childName + "\\";
                } else {
                    // mac
                    dirName = "Invoices/" + childName + "/";
                }

                File file = fileWithDirectoryAssurance(dirName, invoice.getFileName());
                pdfDocument.save(file);
            }

            //Construct outputstream for return
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            pdfDocument.save(out);
            in = new ByteArrayInputStream(out.toByteArray());
            pdfDocument.close();
        }
        return in;
    }

    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return new File(directory + "/" + filename);
    }

}
