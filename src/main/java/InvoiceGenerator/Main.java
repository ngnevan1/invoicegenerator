package InvoiceGenerator;

import util.Ftp;
import util.Google;
import util.Pdf;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            List<List<Object>> values = Google.getSheetRows();
            if (values == null || values.isEmpty()) {
                System.out.println("No data found.");
            } else {
                //	System.out.println("Parent Email	Parent Name	AddressLine1	AddressLine2	Invoice No.	Date	Due Date	Month	Description1	Fare1	Child1	Description2	Fare2	Child2	Description3	Fare3	Child3	Subtotal	Discount	Balance Due");
                for (List<Object> row : values) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 0; i < 20; i++) {
                        String key = (String) values.get(0).get(i);
                        String value = row.get(i) == null ? "" : (String) row.get(i);
                        rowData.put(key, value);
                    }
                    Pdf pdf = new Pdf();
                    for (String i : rowData.keySet()) {
                        System.out.println("key: " + i + " value: " + rowData.get(i));
                    }
                    InputStream inputStream = pdf.fillPdf(rowData);
                    System.out.println("PDF Filled");
                    String parentName = rowData.get("ParentName");
                    String fileName = "SJI-" + rowData.get("Month") + "-" + parentName + ".pdf";

                    Ftp.upload(parentName, fileName, inputStream);
                }
            }

        } catch (IOException | GeneralSecurityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}