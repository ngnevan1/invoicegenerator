package InvoiceGenerator;

import entity.Child;
import entity.Invoice;
import util.Csv;
import util.EmailManager;
import util.Pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final String MAC_PDF_PATH = "Invoices/00-SourceFiles/SourceFile.pdf";
    private static final String WIN_PDF_PATH = "C:\\Invoices\\00-SourceFiles\\SourceFile.pdf";
    private static final String MAC_CSV_PATH = "Invoices/00-SourceFiles/SourceData.csv";
    private static final String WIN_CSV_PATH = "C:\\Invoices\\00-SourceFiles\\SourceData.csv";

    public static void main(String[] args) {

        System.out.println("***Invoice Generator Application v1.2***");
        Scanner scanner = new Scanner(System.in);
        String osName = System.getProperty("os.name");
        int sendEmail = 0;
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

        System.out.println("Please enter the Invoice Date (DD)>");
        int dateOfInvoice = scanner.nextInt();

        System.out.println("Please enter the Invoice Due Date (DD)>");
        int dueDateOfInvoice = scanner.nextInt();

        while (true) {
            System.out.print("Do you want emails to be sent? (1: Yes, 2: No)> ");
            int input = scanner.nextInt();
            if (input >= 1 && input <= 2) {
                if (input == 1) {
                    sendEmail = 1;
                }
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        try {
            List<Child> allChildren = Csv.getRowData(os);
            if (allChildren.isEmpty()) {
                System.out.println("No data found.");
            } else {
                int numRows = allChildren.size();
                int count = 0;
                System.out.println(numRows + " rows detected");

                // get list of duplicates (children on different bus for return trip)
                Set<String> duplicateFound = new HashSet<>();
                Set<String> noDuplicate = new HashSet<>();
                for (Child c : allChildren) {
                    if (!noDuplicate.add(c.getChildName())) {
                        duplicateFound.add(c.getChildName());
                    }
                }
                int childCount = numRows - duplicateFound.size();
                System.out.println(childCount + " invoices to generate");

                // separate each duplicate into their own list to total up fares
                for (String childName : duplicateFound) {
                    List<Child> eachChild = new ArrayList<>();
                    for (Child child : allChildren) {
                        if (child.getChildName().equals(childName)) {
                            eachChild.add(child);
                        }
                    }
                    //generate invoice per duplicate child
                    count = generateInvoice(sendEmail, schName, os, count, childCount, eachChild, dateOfInvoice, dueDateOfInvoice);

                }
                // generate invoice for remaining children
                List<Child> remainingChildren = new ArrayList<>();
                for (Child c : allChildren) {
                    if (!duplicateFound.contains(c.getChildName())) {
                        remainingChildren.add(c);
                    }
                }
                System.out.println("Remaining Children: " + remainingChildren.size());
                for (Child c : remainingChildren) {
                    List<Child> child = new ArrayList<>();
                    child.add(c);
                    count = generateInvoice(sendEmail, schName, os, count, childCount, child, dateOfInvoice, dueDateOfInvoice);
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

    }

    private static int generateInvoice(int sendEmail, String schName, int os, int count, int childCount, List<Child> eachChild, int dateOfInvoice, int dueDateOfInvoice) throws IOException {
        Invoice invoice = new Invoice(eachChild, schName, count, dateOfInvoice, dueDateOfInvoice);
        InputStream pdf = new Pdf().fillPdf(invoice, os);
        sendEmail(schName, invoice, pdf, sendEmail);
        count++;
        System.out.println(count + "/" + childCount + " Invoices Generated: " + invoice.getFileName());
        return count;
    }

    private static void sendEmail(String schName, Invoice invoice, InputStream pdf, int sendEmail) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            if (schName.equals("SJI") && sendEmail == 1) {
                EmailManager emailManager = new EmailManager("sji@rstransport.com.sg", prop.getProperty("smtpAuthPassword"));
                emailManager.sendEmail(invoice, pdf);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}