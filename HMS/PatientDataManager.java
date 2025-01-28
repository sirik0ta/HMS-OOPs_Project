package HMS;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PatientDataManager {
    private static final String FILE_PATH = "Patient_List.xlsx";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public PatientDataManager() {
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            System.out.println("Error accessing patient data: " + e.getMessage());
        }
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void writeIntoFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            workbook.write(fos);
        } catch (Exception e) {
            System.out.println("Error writing to patient data file: " + e.getMessage());
        }
    }

    public void dataClose() {
        try {
            workbook.close();
        } catch (Exception e) {
            System.out.println("Error closing workbook: " + e.getMessage());
        }
    }
}
