package HMS;

import java.io.FileInputStream;
import java.util.Scanner;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Login {

    private static final String STAFF_FILE = "Staff_List.xlsx";
    private static final String PATIENT_FILE = "Patient_List.xlsx";

    public static User login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Login ===");
        System.out.print("Enter User ID: ");
        String userID = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Attempt login for staff
        User staffUser = attemptStaffLogin(userID, password);
        if (staffUser != null) {
            return staffUser;
        }

        // Attempt login for patient
        User patientUser = attemptPatientLogin(userID, password);
        if (patientUser != null) {
            return patientUser;
        }

        System.out.println("Login failed. Invalid credentials.");
        return null;
    }

    private static User attemptStaffLogin(String userID, String password) {
        try {
            FileInputStream fis = new FileInputStream(STAFF_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(userID)) {
                    String storedPassword = row.getCell(5).getStringCellValue();
                    String role = row.getCell(2).getStringCellValue();

                    if (storedPassword.equals(password)) {
                        System.out.println("Login successful!");
                        workbook.close();
                        fis.close();

                        // Return appropriate User object based on role
                        switch (role.toLowerCase()) {
                            case "administrator":
                                return new Administrator(userID, role);
                            case "doctor":
                                return new Doctor(userID, role);
                            case "pharmacist":
                                return new Pharmacist(userID, role);
                            default:
                                System.out.println("Role not recognized.");
                                return null;
                        }
                    } else {
                        System.out.println("Invalid password for staff. Please try again.");
                    }
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error during staff login: " + e.getMessage());
        }
        return null;
    }

    private static User attemptPatientLogin(String userID, String password) {
        try {
            FileInputStream fis = new FileInputStream(PATIENT_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(userID)) {
                    String storedPassword = row.getCell(2).getStringCellValue(); // Assuming password is in column 2
                    String role = "Patient";

                    if (storedPassword.equals(password)) {
                        System.out.println("Login successful!");
                        workbook.close();
                        fis.close();

                        // Return a Patient object
                        return new Patient(userID, role);
                    } else {
                        System.out.println("Invalid password for patient. Please try again.");
                    }
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error during patient login: " + e.getMessage());
        }
        return null;
    }
}

