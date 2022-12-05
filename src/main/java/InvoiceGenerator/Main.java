package InvoiceGenerator;

import bean.Invoice;
import util.Csv;
import util.Pdf;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final String MAC_PDF_PATH = "Invoices/SourceFiles/SourceFile.pdf";
    private static final String WIN_PDF_PATH = "C:\\Invoices\\SourceFile.pdf";
    private static final String MAC_CSV_PATH = "Invoices/SourceFiles/SourceData.csv";
    private static final String WIN_CSV_PATH = "C:\\Invoices\\SourceData.csv";

    public static void main(String[] args) {

        System.out.println("***Invoice Generator Application v1.1***");
        Scanner scanner = new Scanner(System.in);
        String osName = System.getProperty("os.name");
        String schName = "SJI";
        int os = osName.equals("Mac OS X") ? 2 : 1;

        System.out.println("This program is running on " + osName);
        System.out.println("Please ensure that the files are in the following directory:");
        if (os == 1) {
            System.out.println(WIN_PDF_PATH);
            System.out.println(WIN_CSV_PATH);
        } else {
            System.out.println(MAC_PDF_PATH);
            System.out.println(MAC_CSV_PATH);
        }
        System.out.print("Enter 1 to continue> ");
        if (scanner.nextInt() == 1) {

            try {
                List<Invoice> values = Csv.getRowData(os);
                if (values.isEmpty()) {
                    System.out.println("No data found.");
                } else {
                    int numRows = values.size();
                    int count = 0;
                    System.out.println(numRows + " rows detected");
                    for (Invoice invoice : values) {
                        String fileName = new Pdf().fillPdf(invoice, os, schName);
                        count++;
                        System.out.println(count + "/" + numRows + " Invoices Generated: " + fileName);
                    }
                }
                System.out.println("***Invoice Generation Complete***");
                System.out.println("To retrieve your invoices, please check the following directory:");
                if (os == 1) {
                    System.out.println("C:\\Invoices\\ParentName");
                } else {
                    System.out.println("Invoices/ParentName");
                }
                System.out.println("...Press any key to continue...");
                scanner.nextLine();
                scanner.nextLine();
                System.out.println("Goodbye!");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Goodbye!");
        }
    }

}