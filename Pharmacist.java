package HMS;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Pharmacist extends Staff {

    private static final String APPOINTMENT_FILE = "Appointment_List.xlsx";
    private static final String INVENTORY_FILE = "Medicine_List.xlsx";
    private static final int APPOINTMENT_ID_COLUMN = 0;
    private static final int MEDICATION_STATUS_COLUMN = 6;
    private static final int ITEM_NAME_COLUMN = 0;
    private static final int QUANTITY_COLUMN = 1;

    public Pharmacist(String phaID, String role) {
        super(phaID, role);
        
        displayMenu();
    }
    
    private void displayMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (choice != 5) {
            try {
                System.out.println("\nPharmacist Menu:");
                System.out.println("1. View Appointment Outcome Records");
                System.out.println("2. Update Prescription Status");
                System.out.println("3. View Medication Inventory");
                System.out.println("4. Submit Replenishment Request");
                System.out.println("5. Log out");
                System.out.print("Enter your choice: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    sc.next(); // Consume invalid input
                    continue;
                }

                choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewAppointmentOutcomeRecord();
                        break;
                    case 2:
                        handleUpdatePrescriptionStatus(sc);
                        break;
                    case 3:
                        viewMedicationInventory();
                        break;
                    case 4:
                        handleSubmitReplenishmentRequest(sc);
                        break;
                    case 5:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                sc.nextLine(); // Clear the scanner buffer
            }
        }

        sc.close();
    }

    private void handleUpdatePrescriptionStatus(Scanner sc) {
        System.out.println("Enter Appointment ID: ");
        String appointmentId = sc.nextLine();

        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("Appointment ID cannot be empty. Operation canceled.");
            return;
        }

        System.out.println("Enter New Prescription Status: ");
        String newStatus = sc.nextLine();

        if (newStatus == null || newStatus.trim().isEmpty()) {
            System.out.println("Prescription status cannot be empty. Operation canceled.");
            return;
        }

        boolean success = updatePrescriptionStatus(appointmentId, newStatus);
        if (success) {
            System.out.println("Prescription status updated successfully.");
        } else {
            System.out.println("Failed to update prescription status. Please check the Appointment ID and try again.");
        }
    }

    private void handleSubmitReplenishmentRequest(Scanner sc) {
        System.out.println("Enter Medication Name: ");
        String medicationName = sc.nextLine();

        if (medicationName == null || medicationName.trim().isEmpty()) {
            System.out.println("Medication name cannot be empty. Operation canceled.");
            return;
        }

        boolean success = submitReplenishmentRequest(medicationName);
        if (success) {
            System.out.println("Replenishment request submitted successfully.");
        } else {
            System.out.println("Failed to submit replenishment request. Please try again.");
        }
    }

    // View appointment outcome records
    public void viewAppointmentOutcomeRecord() {
        try (FileInputStream fis = new FileInputStream(APPOINTMENT_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println("Appointment Outcomes:");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    String appointmentId = getStringCellValue(row, APPOINTMENT_ID_COLUMN);
                    String medicationStatus = getStringCellValue(row, MEDICATION_STATUS_COLUMN);
                    System.out.println("Appointment ID: " + appointmentId + ", Medication Status: " + medicationStatus);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
    }

    // Update prescription status
    public boolean updatePrescriptionStatus(String appointmentId, String newStatus) {
        try (FileInputStream fis = new FileInputStream(APPOINTMENT_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && getStringCellValue(row, APPOINTMENT_ID_COLUMN).equals(appointmentId)) {
                    row.getCell(MEDICATION_STATUS_COLUMN).setCellValue(newStatus);
                    try (FileOutputStream fos = new FileOutputStream(APPOINTMENT_FILE)) {
                        workbook.write(fos);
                    }
                    return true; // Success
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating the prescription status: " + e.getMessage());
        }
        return false; // Failure
    }

    // View medication inventory
    public void viewMedicationInventory() {
        try (FileInputStream fis = new FileInputStream(INVENTORY_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println("Medication Inventory:");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    String itemName = getStringCellValue(row, ITEM_NAME_COLUMN);
                    int quantity = getNumericCellValue(row, QUANTITY_COLUMN);
                    System.out.println("Item: " + itemName + ", Quantity: " + quantity);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the inventory file: " + e.getMessage());
        }
    }

    // Submit replenishment request
    public boolean submitReplenishmentRequest(String itemName) {
        try (FileInputStream fis = new FileInputStream(INVENTORY_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && getStringCellValue(row, ITEM_NAME_COLUMN).equalsIgnoreCase(itemName)) {
                    int currentQuantity = getNumericCellValue(row, QUANTITY_COLUMN);
                    row.getCell(QUANTITY_COLUMN).setCellValue(currentQuantity + 1); // Increment by 1 for simplicity
                    try (FileOutputStream fos = new FileOutputStream(INVENTORY_FILE)) {
                        workbook.write(fos);
                    }
                    return true; // Success
                }
            }
        } catch (IOException e) {
            System.out.println("Error submitting replenishment request: " + e.getMessage());
        }
        return false; // Failure
    }

    // Helper methods
    private String getStringCellValue(XSSFRow row, int column) {
        return row.getCell(column) != null ? row.getCell(column).getStringCellValue() : "N/A";
    }

    private int getNumericCellValue(XSSFRow row, int column) {
        return row.getCell(column) != null ? (int) row.getCell(column).getNumericCellValue() : 0;
    }
}


