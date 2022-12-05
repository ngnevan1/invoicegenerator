package bean;

import com.opencsv.bean.CsvToBeanFilter;

public class IdFilter implements CsvToBeanFilter {

    public boolean allowLine(String[] line) {
        try {
            Long.parseLong(line[0]);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
