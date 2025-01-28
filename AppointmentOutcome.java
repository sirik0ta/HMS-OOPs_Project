package HMS;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Scanner;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AppointmentOutcome {

    private static final String APPOINTMENT_FILE = "Appointments_List.xlsx";

    private String appointmentID;
    private String patientID;
    private String doctorID;
    private LocalDate appointmentDate;
    private String serviceType;
    private String medicationName;
    private String medicationStatus;
    private String consultationNotes;

    public AppointmentOutcome(String appointmentID, String patientID, String doctorID, LocalDate appointmentDate,
            String serviceType, String medicationName, String medicationStatus, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.medicationName = medicationName;
        this.medicationStatus = medicationStatus;
        this.consultationNotes = consultationNotes;
    }

    public String getAppointmentId() {
        return appointmentID;
    }

    public String getMedicationStatus() {
        return medicationStatus;
    }

    public void setMedicationStatus(String newStatus) {
        this.medicationStatus = newStatus;
    }

    // Add Appointment Outcome
    public void addAppointmentOutcome() {
        try {
            FileInputStream fis = new FileInputStream(APPOINTMENT_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum() + 1;
            XSSFRow row = sheet.createRow(lastRowNum);

            row.createCell(0).setCellValue(appointmentID);
            row.createCell(1).setCellValue(patientID);
            row.createCell(2).setCellValue(doctorID);
            row.createCell(3).setCellValue(appointmentDate.toString());
            row.createCell(4).setCellValue(serviceType);
            row.createCell(5).setCellValue(medicationName);
            row.createCell(6).setCellValue(medicationStatus);
            row.createCell(7).setCellValue(consultationNotes);

            fis.close();

            FileOutputStream fos = new FileOutputStream(APPOINTMENT_FILE);
            workbook.write(fos);
            workbook.close();
            fos.close();

            System.out.println("Appointment outcome added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding appointment outcome: " + e.getMessage());
        }
    }

    // Fetch Appointment Outcome by ID
    public static void fetchAppointmentOutcome(String appointmentID) {
        try {
            FileInputStream fis = new FileInputStream(APPOINTMENT_FILE);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(appointmentID)) {
                    System.out.println("Appointment Outcome Details:");
                    System.out.println("Appointment ID: " + row.getCell(0).getStringCellValue());
                    System.out.println("Patient ID: " + row.getCell(1).getStringCellValue());
                    System.out.println("Doctor ID: " + row.getCell(2).getStringCellValue());
                    System.out.println("Date: " + row.getCell(3).getStringCellValue());
                    System.out.println("Service Type: " + row.getCell(4).getStringCellValue());
                    System.out.println("Medication: " + row.getCell(5).getStringCellValue());
                    System.out.println("Medication Status: " + row.getCell(6).getStringCellValue());
                    System.out.println("Consultation Notes: " + row.getCell(7).getStringCellValue());
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Appointment outcome not found.");
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Error fetching appointment outcome: " + e.getMessage());
        }
    }
}
