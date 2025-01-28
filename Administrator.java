package HMS;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Administrator extends User {

    // File paths
    private static final String STAFF_FILE = "Staff_List.xlsx";
    private static final String APPOINTMENTS_FILE = "Appointment_List.xlsx";
    private static final String INVENTORY_FILE = "Medicine_List.xlsx";

    public Administrator(String adminID, String adminRole) {
        super(adminID, adminRole);
        displayMenu();
    }

    public void displayMenu() {
        int choice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Administrator Menu:");
            System.out.println("1. Manage Staff");
            System.out.println("2. Manage Appointments");
            System.out.println("3. Manage Inventory");
            System.out.println("4. Log Out");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageStaff();
                    break;
                case 2:
                    manageAppointments();
                    break;
                case 3:
                    manageInventory();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
        sc.close();
    }

    // ================== STAFF MANAGEMENT ==================
    private void manageStaff() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Add Staff");
        System.out.println("2. Update Staff");
        System.out.println("3. Remove Staff");
        System.out.println("4. Display Staff");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addStaff();
                break;
            case 2:
                updateStaff();
                break;
            case 3:
                removeStaff();
                break;
            case 4:
                displayStaff();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addStaff() {
        try {
            FileInputStream fis = new FileInputStream(STAFF_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Staff ID: ");
            String staffID = sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Role (Doctor/Pharmacist): ");
            String role = sc.nextLine();

            int lastRowNum = sheet.getLastRowNum() + 1;
            XSSFRow newRow = sheet.createRow(lastRowNum);
            newRow.createCell(0).setCellValue(staffID);
            newRow.createCell(1).setCellValue(name);
            newRow.createCell(2).setCellValue(role);

            fis.close();

            FileOutputStream fos = new FileOutputStream(STAFF_FILE);
            workbook.write(fos);
            workbook.close();
            fos.close();

            System.out.println("Staff added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding staff: " + e.getMessage());
        }
    }

    private void updateStaff() {
        try {
            FileInputStream fis = new FileInputStream(STAFF_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Staff ID to update: ");
            String staffID = sc.nextLine();

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(staffID)) {
                    System.out.print("Enter New Name: ");
                    String newName = sc.nextLine();
                    row.getCell(1).setCellValue(newName);

                    System.out.print("Enter New Role (Doctor/Pharmacist): ");
                    String newRole = sc.nextLine();
                    row.getCell(2).setCellValue(newRole);

                    found = true;
                    break;
                }
            }

            if (found) {
                FileOutputStream fos = new FileOutputStream(STAFF_FILE);
                workbook.write(fos);
                fos.close();
                System.out.println("Staff updated successfully.");
            } else {
                System.out.println("Staff ID not found.");
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error updating staff: " + e.getMessage());
        }
    }

    private void removeStaff() {
        try {
            FileInputStream fis = new FileInputStream(STAFF_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Staff ID to remove: ");
            String staffID = sc.nextLine();

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(staffID)) {
                    sheet.removeRow(row);
                    found = true;
                    break;
                }
            }

            if (found) {
                FileOutputStream fos = new FileOutputStream(STAFF_FILE);
                workbook.write(fos);
                fos.close();
                System.out.println("Staff removed successfully.");
            } else {
                System.out.println("Staff ID not found.");
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error removing staff: " + e.getMessage());
        }
    }

    private void displayStaff() {
        try {
            FileInputStream fis = new FileInputStream(STAFF_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            System.out.println("Staff List:");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    System.out.println("ID: " + row.getCell(0).getStringCellValue() +
                                       ", Name: " + row.getCell(1).getStringCellValue() +
                                       ", Role: " + row.getCell(2).getStringCellValue());
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error displaying staff: " + e.getMessage());
        }
    }

    // ================== APPOINTMENT MANAGEMENT ==================
    private void manageAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. View Appointments");
        System.out.println("2. Update Appointment Status");
        System.out.println("3. Remove Completed Appointments");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAppointments();
                break;
            case 2:
                updateAppointmentStatus();
                break;
            case 3:
                removeCompletedAppointments();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewAppointments() {
		try {
			FileInputStream fis = new FileInputStream(APPOINTMENTS_FILE);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
	
			System.out.println("Appointments:");
			System.out.println("-------------------------------------------------------------");
			System.out.printf("%-15s %-15s %-15s %-10s\n", "Appointment ID", "Patient ID", "Doctor ID", "Status");
			System.out.println("-------------------------------------------------------------");
	
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					String appointmentID = row.getCell(0).getStringCellValue();
					String patientID = row.getCell(1).getStringCellValue();
					String doctorID = row.getCell(2).getStringCellValue();
					String status = row.getCell(3).getStringCellValue();
	
					System.out.printf("%-15s %-15s %-15s %-10s\n", appointmentID, patientID, doctorID, status);
				}
			}
	
			workbook.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("Error viewing appointments: " + e.getMessage());
		}
	}
	

    private void updateAppointmentStatus() {
		try {
			FileInputStream fis = new FileInputStream(APPOINTMENTS_FILE);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
	
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter Appointment ID to update: ");
			String appointmentID = sc.nextLine();
	
			boolean found = false;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null && row.getCell(0).getStringCellValue().equals(appointmentID)) {
					System.out.print("Enter New Status (Scheduled, Canceled, Completed): ");
					String newStatus = sc.nextLine();
					row.getCell(3).setCellValue(newStatus);
	
					found = true;
					break;
				}
			}
	
			if (found) {
				FileOutputStream fos = new FileOutputStream(APPOINTMENTS_FILE);
				workbook.write(fos);
				fos.close();
				System.out.println("Appointment status updated successfully.");
			} else {
				System.out.println("Appointment ID not found.");
			}
	
			workbook.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("Error updating appointment status: " + e.getMessage());
		}
	}
	

    private void removeCompletedAppointments() {
		try {
			FileInputStream fis = new FileInputStream(APPOINTMENTS_FILE);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
	
			for (int i = sheet.getLastRowNum(); i > 0; i--) { // Traverse in reverse to avoid index shifts
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					String status = row.getCell(3).getStringCellValue();
					if (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Canceled")) {
						sheet.removeRow(row);
					}
				}
			}
	
			FileOutputStream fos = new FileOutputStream(APPOINTMENTS_FILE);
			workbook.write(fos);
			fos.close();
			workbook.close();
			fis.close();
	
			System.out.println("Completed or canceled appointments removed successfully.");
		} catch (Exception e) {
			System.out.println("Error removing completed appointments: " + e.getMessage());
		}
	}
	

    // ================== INVENTORY MANAGEMENT ==================
    private void manageInventory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. View Inventory");
        System.out.println("2. Approve Replenishment Request");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewInventory();
                break;
            case 2:
                approveReplenishmentRequest();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewInventory() {
		try {
			FileInputStream fis = new FileInputStream(INVENTORY_FILE);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
	
			System.out.println("Inventory Details:");
			System.out.println("--------------------------------------------------");
			System.out.printf("%-20s %-10s %-10s\n", "Item Name", "Stock", "Threshold");
			System.out.println("--------------------------------------------------");
	
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					String itemName = row.getCell(0).getStringCellValue();
					double stock = row.getCell(1).getNumericCellValue();
					double threshold = row.getCell(2).getNumericCellValue();
	
					System.out.printf("%-20s %-10.0f %-10.0f\n", itemName, stock, threshold);
				}
			}
	
			workbook.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("Error viewing inventory: " + e.getMessage());
		}
	}
	

    private void approveReplenishmentRequest() {
		try {
			FileInputStream fis = new FileInputStream(INVENTORY_FILE);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
	
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter Item Name for Replenishment: ");
			String itemName = sc.nextLine();
	
			boolean found = false;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null && row.getCell(0).getStringCellValue().equalsIgnoreCase(itemName)) {
					System.out.print("Enter Replenishment Amount: ");
					double replenishmentAmount = sc.nextDouble();
	
					// Update stock
					double currentStock = row.getCell(1).getNumericCellValue();
					row.getCell(1).setCellValue(currentStock + replenishmentAmount);
	
					found = true;
					break;
				}
			}
	
			if (found) {
				FileOutputStream fos = new FileOutputStream(INVENTORY_FILE);
				workbook.write(fos);
				fos.close();
				System.out.println("Replenishment approved successfully!");
			} else {
				System.out.println("Item not found in inventory.");
			}
	
			workbook.close();
			fis.close();
		} catch (Exception e) {
			System.out.println("Error approving replenishment request: " + e.getMessage());
		}
	}
	
}
