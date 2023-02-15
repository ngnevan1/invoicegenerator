package InvoiceGenerator;

import entity.Child;
import entity.Invoice;
import util.Csv;
import util.EmailManager;
import util.InvoiceType;
import util.Pdf;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String MAC = "Invoices/00-SourceFiles/";
    private static final String WIN = "C:\\Invoices\\00-SourceFiles\\";
    private static final String MAC_PDF_PATH = MAC + "SourceFile.pdf";
    private static final String WIN_PDF_PATH = WIN + "SourceFile.pdf";
    private static final String MAC_REG_CSV_PATH = MAC + "RegBus.csv";
    private static final String WIN_REG_CSV_PATH = WIN + "RegBus.csv";
    private static final String MAC_CCA_CSV_PATH = MAC + "CcaBus.csv";
    private static final String WIN_CCA_CSV_PATH = WIN + "CcaBus.csv";
    private static final String SCH_NAME = "SJI";

    public static void main(String[] args) throws IOException {

        System.out.println("***Invoice Generator Application v2***");
        File file = new File("output.txt");
        PrintWriter output = new PrintWriter(file);
        output.println("Invoices Started Generating at " + new Date());

        Scanner sc = new Scanner(System.in);
        String osName = System.getProperty("os.name");
        String schName = SCH_NAME;
        int os = osName.equals("Mac OS X") ? 2 : 1;
        InvoiceType type;
        System.out.println("This program is running on " + osName);
            System.out.print("Please select what type of invoices you wish to generate (1: Regular Bus, 2: CCA)> ");
            int input = sc.nextInt();
            while (true) {
                if (input == 1) {
                    type = InvoiceType.REGULAR;
                    break;
                } else if (input == 2) {
                    type = InvoiceType.CCA;
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        generateInvoice(os, type, schName, output);

        System.out.println("...Press any key to continue...");
        sc.nextLine();
        sc.nextLine();
        output.println("Invoices Finished Generating at " + new Date());
        output.close();
        sendEmail(Files.newInputStream(file.toPath()));
        System.out.println("Goodbye!");
    }


    private static void generateInvoice(int os, InvoiceType type, String schName, PrintWriter output) {
        try {
            // get invoice dates from user
            List<Date> invoiceDates = datePicker();
            boolean sendEmail = emailPicker();

            // parse provided csv for data
            List<Child> allChildren = Csv.getRowData(os, type);

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
                output.println(childCount + " invoices to generate");

                // separate each duplicate into their own list to total up fares
                for (String childName : duplicateFound) {
                    List<Child> eachChild = new ArrayList<>();
                    for (Child child : allChildren) {
                        if (child.getChildName().equals(childName)) {
                            eachChild.add(child);
                        }
                    }
                    //generate invoice per duplicate child
                    count = generateInvoice(sendEmail, schName, os, count, childCount, eachChild, invoiceDates,type, output);

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
                    count = generateInvoice(sendEmail, schName, os, count, childCount, child, invoiceDates,type, output);
                }
            }
            System.out.println("***Invoice Generation Complete***");
            System.out.println("To retrieve your invoices, please check the following directory:");
            if (os == 1) {
                System.out.println("C:\\Invoices\\ParentName");
            } else {
                System.out.println("Invoices/ParentName");
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred! Please fix the following and re-run the program");
            System.out.println("Please ensure that the files are in the following directory:");
            if (os == 1) {
                System.out.println(WIN_PDF_PATH);
                if (type.equals(InvoiceType.REGULAR)) {
                    System.out.println(WIN_REG_CSV_PATH);
                } else {
                    System.out.println(WIN_CCA_CSV_PATH);
                }
            } else if (os == 2) {
                System.out.println(MAC_PDF_PATH);
                if (type.equals(InvoiceType.CCA)) {
                    System.out.println(MAC_REG_CSV_PATH);
                } else {
                    System.out.println(MAC_CCA_CSV_PATH);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean emailPicker() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Do you want emails to be sent? (1: Yes, 2: No)> ");
        int input = sc.nextInt();
        return input == 1;
    }

    private static List<Date> datePicker() {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

        try {
            System.out.println("Please enter the Invoice Date (DD/MM/YY)>");
            Date invoiceDate = dateFormat.parse(sc.nextLine());

            System.out.println("Please enter the Invoice Due Date (DD/MM/YY)>");
            Date invoiceDueDate = dateFormat.parse(sc.nextLine());

            List<Date> invoiceDates = new ArrayList<>();
            invoiceDates.add(invoiceDate);
            invoiceDates.add(invoiceDueDate);
            return invoiceDates;
        } catch (ParseException e) {
            System.out.println("Please enter the date in a valid format e.g. 25/12/2023");
        }
        return null;
    }

    private static int generateInvoice(boolean sendEmail, String schName, int os, int count, int childCount, List<Child> eachChild, List<Date> invoiceDates, InvoiceType type, PrintWriter output) throws IOException {

        Invoice invoice = new Invoice(eachChild, schName, count, invoiceDates, type);
        InputStream pdf = new Pdf().fillPdf(invoice, os);
        count++;
        System.out.println(count + "/" + childCount + " Invoices Generated: " + invoice.getFileName());
        output.println(count + "/" + childCount + " Invoices Generated: " + invoice.getFileName());

        if (sendEmail) {
            if (count == 199) {
                try {
                    System.out.println("***DO NOT CLOSE PROGRAM***");
                    System.out.println("The email server only allows 400 emails to be sent per hour per email address. As a result, this program will pause for 62 minutes to allow this quota to be reset.");
                    System.out.println("1. Please keep this application open");
                    System.out.println("2. Please ensure that this computer does not go to sleep or shuts down.");
                    TimeUnit.MINUTES.sleep(60);
                    System.out.println("SLEEP END");
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            sendEmail(schName, invoice, pdf, type, output);
        }
        return count;
    }

    private static void sendEmail(String schName, Invoice invoice, InputStream pdf, InvoiceType type, PrintWriter output) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            if (schName.equals("SJI")) {
                EmailManager emailManager = new EmailManager("sji@rstransport.com.sg", prop.getProperty("smtpAuthPassword"));
                emailManager.sendEmail(invoice, pdf, type, output);
            }

        } catch (IOException ex) {
            ex.printStackTrace(output);
            ex.printStackTrace();
        }
    }

    private static void sendEmail(InputStream logFile) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }

            //load a properties file from class path, inside static method
            prop.load(input);
                EmailManager emailManager = new EmailManager("sji@rstransport.com.sg", prop.getProperty("smtpAuthPassword"));
                emailManager.sendEmail(logFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}