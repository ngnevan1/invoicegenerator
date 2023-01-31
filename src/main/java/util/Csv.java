package util;

import entity.Child;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Csv {
    private static final String MAC_REG_CSV_PATH = "Invoices/00-SourceFiles/RegBus.csv";
    private static final String WIN_REG_CSV_PATH = "C:\\Invoices\\00-SourceFiles\\RegBus.csv";
    private static final String MAC_CCA_CSV_PATH = "Invoices/00-SourceFiles/CcaBus.csv";
    private static final String WIN_CCA_CSV_PATH = "C:\\Invoices\\00-SourceFiles\\CcaBus.csv";

    public static List<Child> getRowData(int os, InvoiceType type) throws FileNotFoundException {
        FileReader fileReader = null;
        if (type.equals(InvoiceType.REGULAR)) {
            if (os == 1) {
                fileReader = new FileReader(WIN_REG_CSV_PATH);
            } else {
                fileReader = new FileReader(MAC_REG_CSV_PATH);
            }
            return new CsvToBeanBuilder<Child>(fileReader)
                    .withType(Child.class)
                    .withSkipLines(3)
                    .withIgnoreEmptyLine(true)
                    .withFilter(line -> line[3].length() == 5 && !line[3].equals("Level"))
                    .build()
                    .parse();
        } else if (type.equals(InvoiceType.CCA)) {
            if (os == 1) {
                fileReader = new FileReader(WIN_CCA_CSV_PATH);
            } else {
                fileReader = new FileReader(MAC_CCA_CSV_PATH);
            }
            return new CsvToBeanBuilder<Child>(fileReader)
                    .withType(Child.class)
                    .withSkipLines(3)
                    .withIgnoreEmptyLine(true)
                    .withFilter(line -> line[2].length() == 5 && !line[2].equals("Level"))
                    .build()
                    .parse();
        }
            return null;

    }

}
