package util;

import bean.IdFilter;
import bean.Invoice;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Csv {
    private static final String MAC_FILE_PATH = "Invoices/SourceFiles/SourceData.csv";
    private static final String WIN_FILE_PATH = "C:\\Invoices\\SourceData.csv";

    public static List<Invoice> getRowData(int os) throws FileNotFoundException {
        FileReader fileReader;
        if (os == 1) {
            fileReader = new FileReader(WIN_FILE_PATH);
        } else {
            fileReader = new FileReader(MAC_FILE_PATH);
        }
        return new CsvToBeanBuilder<Invoice>(fileReader)
                .withType(Invoice.class)
                .withSkipLines(3)
                .withIgnoreEmptyLine(true)
                .withFilter(new IdFilter())
                .build()
                .parse();
    }

}
