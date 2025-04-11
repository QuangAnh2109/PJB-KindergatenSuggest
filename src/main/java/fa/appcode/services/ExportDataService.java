package fa.appcode.services;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExportDataService {
    void exportParentData(HttpServletResponse response,String principal) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
