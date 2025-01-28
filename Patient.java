package HMS;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Patient extends User {

    // Constants for Excel column indices
    private static final int PATIENT_ID_COLUMN = 0;
    private static final int PASSWORD_COLUMN = 2;
    private static final int SCHEDULED_APPOINTMENTS_COLUMN = 7;
    private static final int AVAILABLE_SLOTS_COLUMN = 2;
    private static final int APPOINTMENT_HISTORY_COLUMN = 8;

    private String patientDOB;
    private String patientBloodType;
    private String patientContactInfo;
    private List<String> medicalHistory;

    public Patient(String patientID, String patientRole) {
        super(patientID, patientRole);

        int choice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("1. Change Password\n" +
                    "2. View Medical Record\n" +
                    "3. Update Personal Information\n" +
                    "4. View Available Appointment Slots\n" +
                    "5. Schedule Appointment\n" +
                    "6. Reschedule Appointment\n" +
                    "7. Cancel Appointment\n" +
                    "8. View Scheduled Appointments\n" +
                    "9. View Appointment History\n" +
                    "10. Log out");
            System.out.println("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    changePassword(getUserID());
                    break;
                case 2:
                    viewingMedicalRecord();
                    break;
                case 3:
                    updateContactInformation();
                    break;
                case 4:
                    viewAvailableAppointmentSlots();
                    break;
                case 5:
                    scheduleAppointment();
                    break;
                case 6:
                    rescheduleAppointment();
                    break;
                case 7:
                    cancelAppointment();
                    break;
                case 8:
                    viewScheduledAppointments();
                    break;
                case 9:
                    viewAppointmentHistory();
                    break;
                case 10:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 10);
    }

    // Change Password
    public void changePassword(String userId) {
        Scanner sc = new Scanner(System.in);
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        System.out.println("Enter your current password: ");
        String currentPwd = sc.nextLine();

        boolean authenticated = false;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(userId) &&
                row.getCell(PASSWORD_COLUMN).getStringCellValue().equals(currentPwd)) {
                authenticated = true;
                break;
            }
        }

        if (!authenticated) {
            System.out.println("Invalid current password!");
            patientData.dataClose();
            return;
        }

        System.out.println("Enter your new password: ");
        String newPwd = sc.nextLine();
        System.out.println("Confirm your new password: ");
        String confirmPwd = sc.nextLine();

        if (!newPwd.equals(confirmPwd)) {
            System.out.println("Passwords do not match! Try again.");
            patientData.dataClose();
            return;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(userId)) {
                row.getCell(PASSWORD_COLUMN).setCellValue(newPwd);
                patientData.writeIntoFile();
                System.out.println("Password updated successfully!");
                break;
            }
        }

        patientData.dataClose();
    }

    // View Medical Record
    public void viewingMedicalRecord() {
        System.out.println("Patient ID: " + getUserID());
        System.out.println("Name: " + getUserName());
        System.out.println("Gender: " + getUserGender());
        System.out.println("Date of Birth: " + getDOB());
        System.out.println("Blood Type: " + getBloodType());
        System.out.println("Contact Information: " + getContactInfo());
        System.out.println("Medical History: ");
        if (medicalHistory != null) {
            for (String entry : medicalHistory) {
                System.out.println("- " + entry);
            }
        } else {
            System.out.println("No medical history available.");
        }
    }

    // Update Contact Information
    public void updateContactInformation() {
        Scanner sc = new Scanner(System.in);
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        System.out.println("Enter your new contact information: ");
        String newContactInfo = sc.nextLine();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                row.getCell(6).setCellValue(newContactInfo); 
                //patientContactInfo = newContactInfo;
                patientData.writeIntoFile();
                System.out.println("Contact information updated successfully.");
                break;
            }
        }

        patientData.dataClose();
    }

    // View Available Appointment Slots
    public void viewAvailableAppointmentSlots() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Doctor ID to view available slots: ");
        String doctorID = sc.nextLine();

        DoctorDataManager doctorData = new DoctorDataManager();
        XSSFSheet sheet = doctorData.getSheet();
        
        System.out.println("Available Slots for " + doctorID + ": ");
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);            
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(doctorID)) {
                System.out.println(row.getCell(AVAILABLE_SLOTS_COLUMN).getStringCellValue());
                break;
            }
        }

        doctorData.dataClose();
    }

    // Schedule Appointment
    public void scheduleAppointment() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Doctor ID: ");
        String doctorID = sc.nextLine();

        // Validate Doctor ID
        DoctorDataManager doctorData = new DoctorDataManager();
        XSSFSheet doctorSheet = doctorData.getSheet();
        boolean doctorExists = false;
        String availableSlots = "";

        for (int i = 1; i <= doctorSheet.getLastRowNum(); i++) {
            XSSFRow row = doctorSheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(doctorID)) {
                doctorExists = true;
                availableSlots = row.getCell(AVAILABLE_SLOTS_COLUMN).getStringCellValue();
                break;
            }
        }

        if (!doctorExists) {
            System.out.println("Invalid Doctor ID. Please try again.");
            doctorData.dataClose();
            return;
        }

        System.out.println("Available slots for Doctor " + doctorID + ": " + availableSlots);
        System.out.println("Enter the appointment slot: ");
        String selectedSlot = sc.nextLine();

        // Validate selected slot
        if (!availableSlots.contains(selectedSlot)) {
            System.out.println("Invalid or unavailable slot. Please choose a valid slot.");
            doctorData.dataClose();
            return;
        }

        // Check if the slot is already scheduled by the patient
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet patientSheet = patientData.getSheet();
        boolean slotAlreadyScheduled = false;

        for (int i = 1; i <= patientSheet.getLastRowNum(); i++) {
            XSSFRow row = patientSheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                String scheduledAppointments = row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).getStringCellValue();
                if (scheduledAppointments != null && scheduledAppointments.contains(selectedSlot)) {
                    slotAlreadyScheduled = true;
                    break;
                }
            }
        }

        if (slotAlreadyScheduled) {
            System.out.println("You have already scheduled this slot. Please select a different slot.");
            doctorData.dataClose();
            patientData.dataClose();
            return;
        }

        // Add slot to patient's scheduled appointments
        boolean appointmentAdded = updatePatientScheduledAppointments(selectedSlot, true);

        if (!appointmentAdded) {
            System.out.println("Failed to schedule the appointment. Please try again.");
            doctorData.dataClose();
            patientData.dataClose();
            return;
        }

        // Remove slot from doctor's available slots
        updateDoctorAvailability(doctorID, selectedSlot, false);

        System.out.println("Appointment scheduled successfully!");

        // Close data managers
        doctorData.dataClose();
        patientData.dataClose();
    }

    // Reschedule Appointment
    public void rescheduleAppointment() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the current appointment to reschedule: ");
        String currentAppointment = sc.nextLine();

        System.out.println("Enter the new appointment slot: ");
        String newSlot = sc.nextLine();

        // Add old appointment to doctor's availability
        updateDoctorAvailability("DoctorID", currentAppointment, true);

        // Update patient scheduled appointments
        updatePatientScheduledAppointments(currentAppointment, false);
        updatePatientScheduledAppointments(newSlot, true);

        // Remove new appointment from doctor's availability
        updateDoctorAvailability("DoctorID", newSlot, false);

        System.out.println("Appointment rescheduled successfully!");
    }

    // Helper Functions for Excel Operations
    private void updateDoctorAvailability(String doctorID, String slot, boolean add) {
        DoctorDataManager doctorData = new DoctorDataManager();
        XSSFSheet doctorSheet = doctorData.getSheet();

        for (int i = 1; i <= doctorSheet.getLastRowNum(); i++) {
            XSSFRow row = doctorSheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(doctorID)) {
                String availableSlots = row.getCell(AVAILABLE_SLOTS_COLUMN).getStringCellValue();
                availableSlots = (availableSlots == null) ? "" : availableSlots.trim();

                if (add) {
                    availableSlots += (availableSlots.isEmpty() ? "" : " | ") + slot;
                } else {
                    availableSlots = availableSlots.replace(slot, "").replaceAll("\\|\\s*\\|", "|").trim();
                }

                row.getCell(AVAILABLE_SLOTS_COLUMN).setCellValue(availableSlots);
                break;
            }
        }

        doctorData.writeIntoFile();
        doctorData.dataClose();
    }
	

    private boolean updatePatientScheduledAppointments(String slot, boolean add) {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet patientSheet = patientData.getSheet();
        boolean updated = false;

        for (int i = 1; i <= patientSheet.getLastRowNum(); i++) {
            XSSFRow row = patientSheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                String scheduledAppointments = row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).getStringCellValue();
                scheduledAppointments = (scheduledAppointments == null) ? "" : scheduledAppointments.trim();

                if (add) {
                    // Add slot if not already present
                    if (!scheduledAppointments.contains(slot)) {
                        scheduledAppointments += (scheduledAppointments.isEmpty() ? "" : " | ") + slot;
                        updated = true;
                    }
                } else {
                    // Remove slot if present
                    if (scheduledAppointments.contains(slot)) {
                        scheduledAppointments = scheduledAppointments.replace(slot, "").replaceAll("\\|\\s*\\|", "|").trim();
                        if (scheduledAppointments.startsWith("|")) scheduledAppointments = scheduledAppointments.substring(1).trim();
                        if (scheduledAppointments.endsWith("|")) scheduledAppointments = scheduledAppointments.substring(0, scheduledAppointments.length() - 1).trim();
                        updated = true;
                    }
                }

                row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).setCellValue(scheduledAppointments);
                break;
            }
        }

        if (updated) {
            patientData.writeIntoFile();
        }

        patientData.dataClose();
        return updated;
    }



    private void moveAppointmentToHistory(String slot) {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet patientSheet = patientData.getSheet();

        for (int i = 1; i <= patientSheet.getLastRowNum(); i++) {
            XSSFRow row = patientSheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                // Get scheduled appointments
                String scheduledAppointments = row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).getStringCellValue();
                scheduledAppointments = (scheduledAppointments == null) ? "" : scheduledAppointments.trim();
                scheduledAppointments = scheduledAppointments.replace(slot, "").replaceAll("\\|\\s*\\|", "|").trim();
                row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).setCellValue(scheduledAppointments);

                // Update history
                String history = row.getCell(APPOINTMENT_HISTORY_COLUMN).getStringCellValue();
                history = (history == null) ? "" : history.trim();
                history += (history.isEmpty() ? "" : " | ") + slot;
                row.getCell(APPOINTMENT_HISTORY_COLUMN).setCellValue(history);
                break;
            }
        }

        patientData.writeIntoFile();
        patientData.dataClose();
    }
	
    public void cancelAppointment() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the appointment slot to cancel: ");
        String slotToCancel = sc.nextLine();

        // Step 1: Remove appointment from patient's scheduled appointments
        boolean removed = updatePatientScheduledAppointments(slotToCancel, false);

        if (!removed) {
            System.out.println("Appointment slot not found in your scheduled appointments.");
            return;
        }

        // Step 2: Add slot to appointment history
        moveAppointmentToHistory(slotToCancel);

        // Step 3: Add slot back to the doctor's availability
        System.out.println("Enter the Doctor ID for the appointment: ");
        String doctorID = sc.nextLine();
        updateDoctorAvailability(doctorID, slotToCancel, true);

        System.out.println("Appointment canceled successfully!");
    }
	
    public void viewScheduledAppointments() {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                String scheduledAppointments = row.getCell(SCHEDULED_APPOINTMENTS_COLUMN).getStringCellValue();
                if (scheduledAppointments == null || scheduledAppointments.isEmpty()) {
                    System.out.println("You have no scheduled appointments.");
                } else {
                    System.out.println("Your Scheduled Appointments:");
                    for (String appointment : scheduledAppointments.split("\\|")) {
                        System.out.println("- " + appointment.trim());
                    }
                }
                break;
            }
        }

        patientData.dataClose();
    }

    public void viewAppointmentHistory() {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(PATIENT_ID_COLUMN).getStringCellValue().equals(getUserID())) {
                String appointmentHistory = row.getCell(APPOINTMENT_HISTORY_COLUMN).getStringCellValue();
                if (appointmentHistory == null || appointmentHistory.isEmpty()) {
                    System.out.println("No appointment history available.");
                } else {
                    System.out.println("Your Appointment History:");
                    for (String appointment : appointmentHistory.split("\\|")) {
                        System.out.println("- " + appointment.trim());
                    }
                }
                break;
            }
        }

        patientData.dataClose();
    }
	

  
}
