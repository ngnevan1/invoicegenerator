package util;

import entity.Child;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Csv {
    private static final String MAC_CSV_PATH = "Invoices/00-SourceFiles/SourceData.csv";
    private static final String WIN_CSV_PATH = "C:\\Invoices\\00-SourceFiles\\SourceData.csv";

    public static List<Child> getRowData(int os) throws FileNotFoundException {
        FileReader fileReader;
        if (os == 1) {
            fileReader = new FileReader(WIN_CSV_PATH);
        } else {
            fileReader = new FileReader(MAC_CSV_PATH);
        }
        return new CsvToBeanBuilder<Child>(fileReader)
                .withType(Child.class)
                .withSkipLines(3)
                .withIgnoreEmptyLine(true)
                .withFilter(new ValidChildFilter())
                .build()
                .parse();
    }

}
