package InvoiceGenerator;

import com.opencsv.exceptions.CsvValidationException;
import util.Csv;
import util.Ftp;
import util.Pdf;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final String MAC_PDF_PATH = "Invoices/SourceFiles/SourceFile.pdf";
    private static final String WIN_PDF_PATH = "C:\\Invoices\\SourceFile.pdf";
    private static final String MAC_CSV_PATH = "Invoices/SourceFiles/SourceData.csv";
    private static final String WIN_CSV_PATH = "C:\\Invoices\\SourceData.csv";
    public static void main(String[] args) {

        System.out.println("***Invoice Generator Application v1***");
        Scanner scanner = new Scanner(System.in);
        int os;
        while(true) {
            System.out.print("Select Operating System (1: Windows, 2: Mac)> ");
            os = scanner.nextInt();
            if(os >= 1 && os <= 2) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        int custom;
        while(true) {
            System.out.print("Select Source Files (1: Built In, 2: Self Provided)> ");
            custom = scanner.nextInt();
            if(custom >= 1 && custom <= 2) {
                if (custom == 2) {
                    System.out.println("Please ensure that the files are in the following directory:");
                    if (os == 1) {
                        System.out.println(WIN_PDF_PATH);
                        System.out.println(WIN_CSV_PATH);
                    } else {
                        System.out.println(MAC_PDF_PATH);
                        System.out.println(MAC_CSV_PATH);
                    }
                }
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }

        System.out.print("Enter 1 to continue> ");
        if (scanner.nextInt() == 1) {

            try {
                //List<List<Object>> values = Google.getSheetRows();
                List<Map<String, String>> values = Csv.getRowData(os, custom);
                if (values.isEmpty()) {
                    System.out.println("No data found.");
                } else {
//                for (List<Object> row : values) {
//                    Map<String, String> rowData = new HashMap<>();
//                    for (int i = 0; i < 20; i++) {
//                        String key = (String) values.get(0).get(i);
//                        String value = row.get(i) == null ? "" : (String) row.get(i);
//                        rowData.put(key, value);
                    int numRows = values.size();
                    int count = 0;
                    System.out.println(numRows + " rows detected");
                    for (Map<String, String> rowData : values) {
//                    for (String i : rowData.keySet()) {
//                        System.out.println("key: " + i + " value: " + rowData.get(i));
//                    }
                        //Fill PDF
                        InputStream inputStream = new Pdf().fillPdf(rowData, os, custom);

                        String parentName = removeSpaces(rowData.get("ParentName"));

                        String fileName = "SJI-" + rowData.get("Month") + "-" + parentName + ".pdf";
                        count++;
                        System.out.println(count + "/" + numRows + " Invoice(s) Generated: " + fileName);

                        //Ftp.upload(parentName, fileName, inputStream);
                    }
                }
                System.out.println("***Invoice Generation Complete***");
                System.out.println("To retrieve your invoices, please check the following directory:");
                if (os == 1) {
                    System.out.println("C:\\Invoices\\ParentName");
                } else {
                    System.out.println("Invoices/ParentName");
                }
                System.out.println("Goodbye!");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CsvValidationException e) {
                System.out.println("CSV file not correctly formatted");
            }
        } else {
            System.out.println("Goodbye!");
        }
    }

    public static String removeSpaces (String input) {
        return input.replaceAll("\\s+","");
    }

}