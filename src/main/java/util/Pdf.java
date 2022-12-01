package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.*;
import java.util.Map;

public class Pdf {
    private static final String SOURCE_FILE_PATH = "/SourceFile.pdf";
    private static final String MAC_FILE_PATH = "Invoices/SourceFiles/SourceFile.pdf";
    private static final String WIN_FILE_PATH = "C:\\Invoices\\SourceFile.pdf";

    public InputStream fillPdf(Map<String, String> formData, int os, int custom) throws IOException {
        // load the document
        InputStream template = null;
        File templateFile = null;
        if (custom == 1) {
            template = Pdf.class.getResourceAsStream(SOURCE_FILE_PATH);
            if (template == null) {
                throw new FileNotFoundException("SourceFile.pdf is not found: " + SOURCE_FILE_PATH);
            }
        } else {
            if (os == 1) {
                templateFile = new File(WIN_FILE_PATH);
            } else {
                templateFile = new File(MAC_FILE_PATH);
            }
        }
            PDDocument pdfDocument;
            if (custom == 1) {
                pdfDocument = PDDocument.load(template);
            } else {
                pdfDocument = PDDocument.load(templateFile);
            }
            //get document catalog
            PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

            // as there might not be an AcroForm entry a null check is necessary
            if (acroForm != null) {
                // Retrieve an individual field and set its value.
                PDTextField parentName = (PDTextField) acroForm.getField( "ParentName" );
                parentName.setValue(formData.get("ParentName"));

                PDTextField addressLine1 = (PDTextField) acroForm.getField( "AddressLine1" );
                addressLine1.setValue(formData.get("AddressLine1"));

                PDTextField addressLine2 = (PDTextField) acroForm.getField( "AddressLine2" );
                addressLine2.setValue(formData.get("AddressLine2"));

                PDTextField invoiceNo = (PDTextField) acroForm.getField( "InvoiceNo" );
                invoiceNo.setValue(formData.get("InvoiceNo"));

                PDTextField date = (PDTextField) acroForm.getField( "Date" );
                date.setValue(formData.get("Date"));

                PDTextField dueDate = (PDTextField) acroForm.getField( "DueDate" );
                dueDate.setValue(formData.get("DueDate"));

                PDTextField month1 = (PDTextField) acroForm.getField( "Mth1" );
                month1.setValue(formData.get("Month"));

                PDTextField month2 = (PDTextField) acroForm.getField( "Mth2" );
                month2.setValue(formData.get("Month"));

                PDTextField description1 = (PDTextField) acroForm.getField( "Desc1" );
                description1.setValue(formData.get("Description1"));

                PDTextField fare1 = (PDTextField) acroForm.getField( "Fare1" );
                fare1.setValue(formData.get("Fare1"));

                PDTextField child1 = (PDTextField) acroForm.getField( "Child1" );
                child1.setValue(formData.get("Child1"));

                PDTextField description2 = (PDTextField) acroForm.getField( "Desc2" );
                description2.setValue(formData.get("Description2"));

                PDTextField fare2 = (PDTextField) acroForm.getField( "Fare2" );
                fare2.setValue(formData.get("Fare2"));

                PDTextField child2 = (PDTextField) acroForm.getField( "Child2" );
                child2.setValue(formData.get("Child2"));

                PDTextField subtotal = (PDTextField) acroForm.getField( "Subtotal" );
                subtotal.setValue(formData.get("Subtotal"));

                PDTextField balanceDue = (PDTextField) acroForm.getField( "BalDue" );
                balanceDue.setValue(formData.get("BalanceDue"));

                // Flatten PDF to prevent further editing
                acroForm.flatten();

                // Save File
                String dirName;

                if (os == 1) {
                    // windows
                    dirName = "C:\\Invoices\\" + formData.get("ParentName") + "\\";
                } else {
                    // mac
                    dirName = "Invoices/" + formData.get("ParentName") + "/";
                }

                String fileName = "SJI-" + formData.get("Month") + "-" + formData.get("ParentName") + ".pdf";

                File file = fileWithDirectoryAssurance(dirName, fileName);
                pdfDocument.save(file);

                // Construct output stream for return
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                pdfDocument.save(out);
                pdfDocument.close();
                return new ByteArrayInputStream(out.toByteArray());
            }
        return template;
    }

    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return new File(directory + "/" + filename);
    }

}
