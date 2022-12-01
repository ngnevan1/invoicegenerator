package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.*;
import java.util.Map;

public class Pdf {
    private static final String SOURCE_FILE_PATH = "/SourceFile.pdf";

    public InputStream fillPdf(Map<String, String> formData) throws IOException {
        // load the document
        InputStream template = Pdf.class.getResourceAsStream(SOURCE_FILE_PATH);
        if (template == null) {
            throw new FileNotFoundException("SourceFile.pdf is not found: " + SOURCE_FILE_PATH);
        }

        try (PDDocument pdfDocument = PDDocument.load(template)) {
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
                child1.setValue(formData.get("Fare1"));

                PDTextField description2 = (PDTextField) acroForm.getField( "Desc2" );
                description2.setValue(formData.get("Description2"));

                PDTextField fare2 = (PDTextField) acroForm.getField( "Fare2" );
                fare2.setValue(formData.get("Fare2"));

                PDTextField child2 = (PDTextField) acroForm.getField( "Child2" );
                child2.setValue(formData.get("Fare2"));

                PDTextField subtotal = (PDTextField) acroForm.getField( "Subtotal" );
                subtotal.setValue(formData.get("Subtotal"));

                PDTextField balanceDue = (PDTextField) acroForm.getField( "BalDue" );
                balanceDue.setValue(formData.get("BalanceDue"));

                // Flatten PDF to prevent further editing
                acroForm.flatten();

                // Construct output stream for return
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                String fileName = "SJI-" + formData.get("Month") + "-" + formData.get("ParentName") + ".pdf";
                pdfDocument.save(fileName);
                pdfDocument.save(out);
                pdfDocument.close();
                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                return in;


//				String fileName = "SJI-" + formData.get("Month") + "-" + formData.get("ParentName") + ".pdf";
//
//				// Save and close the filled out form.
//	            pdfDocument.save(fileName);
//				pdfDocument.close();
            }
        }
        return template;
    }
}
