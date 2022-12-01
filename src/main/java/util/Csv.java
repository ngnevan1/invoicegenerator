package util;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Csv {
    private static final String SOURCE_FILE_PATH = "/SourceData.csv";

    public static List<Map<String, String>> getRowData() throws IOException, CsvValidationException {
        List<Map<String, String>> returnValue = new ArrayList<>();
        Map<String, String> values;

        InputStream csvData = Csv.class.getResourceAsStream(SOURCE_FILE_PATH);
        if (csvData == null) {
            throw new FileNotFoundException("SourceData.csv is not found: " + SOURCE_FILE_PATH);
        }

        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new InputStreamReader(csvData));

        while ((values = reader.readMap()) != null) {
            returnValue.add(values);
        }

        return returnValue;
    }

}
