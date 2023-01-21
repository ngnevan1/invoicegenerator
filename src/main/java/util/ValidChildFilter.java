package util;

import com.opencsv.bean.CsvToBeanFilter;

public class ValidChildFilter implements CsvToBeanFilter {

    public boolean allowLine(String[] line) {
        // check if level is valid
        return line[3].length() == 5 && !line[3].equals("Level");
    }
}
