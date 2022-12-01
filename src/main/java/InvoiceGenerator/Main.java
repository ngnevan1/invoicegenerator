package InvoiceGenerator;

import com.opencsv.exceptions.CsvValidationException;
import util.Csv;
import util.Ftp;
import util.Pdf;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            //List<List<Object>> values = Google.getSheetRows();
            List<Map<String, String>> values = Csv.getRowData();
            if (values.isEmpty()) {
                System.out.println("No data found.");
            } else {
//                for (List<Object> row : values) {
//                    Map<String, String> rowData = new HashMap<>();
//                    for (int i = 0; i < 20; i++) {
//                        String key = (String) values.get(0).get(i);
//                        String value = row.get(i) == null ? "" : (String) row.get(i);
//                        rowData.put(key, value);
//                    }

                for (Map<String, String> rowData : values) {

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

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

}