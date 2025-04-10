package fa.appcode.services.impl;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import fa.appcode.common.vo.ParentVoExportData;
import fa.appcode.services.AccountService;
import fa.appcode.services.ExportDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ExportDataServiceImp implements ExportDataService {
    @Autowired
    private AccountService accountService;

    @Override
    public void exportParentData(HttpServletResponse response, String principal) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String filename = "Parent-Enroll-Data.csv";
        response.setContentType("text/csv");
        String[] headers = { "Parent ID", "Name", "Email", "Phone Number", "Enroll Schools" };
        CSVWriter csvWriter = new CSVWriter(response.getWriter());

// Write the headers manually
        csvWriter.writeNext(headers);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        StatefulBeanToCsv<ParentVoExportData> writer = new StatefulBeanToCsvBuilder<ParentVoExportData>(response.getWriter())
                .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false).build();
        writer.write(accountService.exportParentData(principal));
        csvWriter.close();

    }
}
