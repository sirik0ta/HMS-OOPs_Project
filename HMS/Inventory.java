package HMS;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Inventory {

    private static final String INVENTORY_FILE = "Inventory_List.xlsx";

    // Add New Item to Inventory
    public void addNewItem(String itemName, int initialStock, int threshold) {
        try {
            FileInputStream fis = new FileInputStream(INVENTORY_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum() + 1;
            XSSFRow row = sheet.createRow(lastRowNum);

            row.createCell(0).setCellValue(itemName);
            row.createCell(1).setCellValue(initialStock);
            row.createCell(2).setCellValue(threshold);

            fis.close();

            FileOutputStream fos = new FileOutputStream(INVENTORY_FILE);
            workbook.write(fos);
            workbook.close();
            fos.close();

            System.out.println("New inventory item added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding new inventory item: " + e.getMessage());
        }
    }

    // Update Inventory Stock
    public void updateStock(String itemName, int stockChange) {
        try {
            FileInputStream fis = new FileInputStream(INVENTORY_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equalsIgnoreCase(itemName)) {
                    int currentStock = (int) row.getCell(1).getNumericCellValue();
                    row.getCell(1).setCellValue(currentStock + stockChange);
                    found = true;
                    break;
                }
            }

            if (found) {
                FileOutputStream fos = new FileOutputStream(INVENTORY_FILE);
                workbook.write(fos);
                fos.close();
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("Item not found in inventory.");
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    // Fetch Inventory Details
    public void fetchInventory() {
        try {
            FileInputStream fis = new FileInputStream(INVENTORY_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            System.out.println("Inventory Details:");
            System.out.println("-------------------------------------------");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    String itemName = row.getCell(0).getStringCellValue();
                    int stock = (int) row.getCell(1).getNumericCellValue();
                    int threshold = (int) row.getCell(2).getNumericCellValue();

                    System.out.printf("Item: %s, Stock: %d, Threshold: %d\n", itemName, stock, threshold);

                    // Low stock alert
                    if (stock < threshold) {
                        System.out.println("ALERT: Low stock for " + itemName + "!");
                    }
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        }
    }



    public void requestReplenishment(String itemName, int quantity) {
        try (FileInputStream fis = new FileInputStream("Inventory_List.xlsx");
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0); // Assuming inventory data is in the first sheet
            boolean found = false;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equalsIgnoreCase(itemName)) {
                    int currentStock = (int) row.getCell(1).getNumericCellValue(); // Assuming stock is in column 2
                    row.getCell(1).setCellValue(currentStock + quantity);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Item not found in inventory.");
            } else {
                try (FileOutputStream fos = new FileOutputStream("Inventory_List.xlsx")) {
                    workbook.write(fos);
                    System.out.println("Replenishment request submitted for " + itemName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error handling replenishment request: " + e.getMessage());
        }
    }
}
