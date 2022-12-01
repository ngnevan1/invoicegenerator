package util;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Csv {
    private static final String SOURCE_FILE_PATH = "/SourceData.csv";
    private static final String MAC_FILE_PATH = "Invoices/SourceFiles/SourceData.csv";
    private static final String WIN_FILE_PATH = "C:\\Invoices\\SourceData.csv";

    public static List<Map<String, String>> getRowData(int os, int custom) throws IOException, CsvValidationException {
        List<Map<String, String>> returnValue = new ArrayList<>();
        Map<String, String> values;
        CSVReaderHeaderAware reader;

        if (custom == 1) {
            InputStream csvData = Csv.class.getResourceAsStream(SOURCE_FILE_PATH);
            if (csvData == null) {
                throw new FileNotFoundException("SourceData.csv is not found: " + SOURCE_FILE_PATH);
            }
            reader = new CSVReaderHeaderAware(new InputStreamReader(csvData));
        } else {
            if (os == 1) {
                reader = new CSVReaderHeaderAware(new FileReader(WIN_FILE_PATH));
            } else {
                reader = new CSVReaderHeaderAware(new FileReader(MAC_FILE_PATH));
            }
        }

        while ((values = reader.readMap()) != null) {
            returnValue.add(values);
        }

        return returnValue;
    }

}
