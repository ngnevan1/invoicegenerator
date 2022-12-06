package InvoiceGenerator;

import bean.Child;
import bean.Invoice;
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
                List<Child> allChildren = Csv.getRowData(os);
                if (allChildren.isEmpty()) {
                    System.out.println("No data found.");
                } else {
                    int numRows = allChildren.size();
                    int count = 0;
                    System.out.println(numRows + " rows detected");
                    // get list of duplicates (siblings)
                    Set<String> hasSiblings = new HashSet<>();
                    Set<String> noSiblings = new HashSet<>();
                    for (Child c : allChildren) {
                        if (!noSiblings.add(c.getFatherName()) && !c.getFatherName().isEmpty()) {
                            hasSiblings.add(c.getFatherName());
                        }
                    }

                    // separate each duplicate (sibling) into each own list (own siblings)
                    for (String fatherName : hasSiblings) {
                        List<Child> familyGroup = new ArrayList<>();
                        for (Child child : allChildren) {
                            if (fatherName.equals(child.getFatherName())) {
                                familyGroup.add(child);
                            }
                        }
                        //generate invoice per family
                        Invoice invoice = new Invoice(familyGroup, schName);
                        InputStream pdf = new Pdf().fillPdf(invoice, os);
                        sendEmail(schName, invoice, pdf);
                        count+= familyGroup.size();
                        System.out.println("Invoices Generated for " + count + "/" + numRows + " children");
                        System.out.println(count + "/" + numRows + " Invoices Generated: " + invoice.getFileName());

                    }
                    // generate invoice for remaining children
                    List<Child> remainingChildren = new ArrayList<>();
                    for (Child c : allChildren) {
                        if (!hasSiblings.contains(c.getFatherName())) {
                            remainingChildren.add(c);
                        }
                    }
                    System.out.println("Remaining Children: " + remainingChildren.size());
                    for (Child c : remainingChildren) {
                        List<Child> child = new ArrayList<>();
                        child.add(c);
                        Invoice invoice = new Invoice(child, schName);
                        InputStream pdf = new Pdf().fillPdf(invoice, os);
                        sendEmail(schName, invoice, pdf);
                        count++;
                        System.out.println("Invoices Generated for " + count + "/" + numRows + " children");
                        System.out.println(count + "/" + numRows + " Invoices Generated: " + invoice.getFileName());
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

    private static void sendEmail(String schName, Invoice invoice, InputStream pdf) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            if (schName.equals("SJI")) {
                EmailManager emailManager = new EmailManager("sji@rstransport.com.sg", prop.getProperty("smtpAuthPassword"));
                emailManager.sendEmail(invoice, pdf);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}