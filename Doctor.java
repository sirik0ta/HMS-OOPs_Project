package HMS;

import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Doctor extends Staff {

    protected static String docRole = "Doctor";

    // Constructor with single parameter
    public Doctor(String docID) {
        super(docID, docRole);
        initializeMenu();
    }

    // Overloaded constructor to match Login logic
    public Doctor(String docID, String role) {
        super(docID, role);
        initializeMenu();
    }

    // Menu initialization method
    private void initializeMenu() {
        int choice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("=== Doctor Menu ===");
            System.out.println("1. View Patient Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability");
            System.out.println("5. Accept/Decline Appointment Requests");
            System.out.println("6. Record Appointment Outcome");
            System.out.println("7. Log out");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewPatientRecords();
                    break;
                case 2:
                    updatePatientMedicalRecords();
                    break;
                case 3:
                    viewPersonalSchedule();
                    break;
                case 4:
                    setAvailability();
                    break;
                case 5:
                    manageAppointmentRequests();
                    break;
                case 6:
                    recordAppointmentOutcome();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    // 1. View Patient Records
    private void viewPatientRecords() {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        System.out.println("Enter Patient ID to view records: ");
        Scanner sc = new Scanner(System.in);
        String patientID = sc.nextLine();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(0).getStringCellValue().equals(patientID)) {
                System.out.println("Patient Medical Records:");
                System.out.println("Name: " + row.getCell(1).getStringCellValue());
                System.out.println("Blood Type: " + row.getCell(4).getStringCellValue());
                System.out.println("Past Diagnoses: " + row.getCell(6).getStringCellValue());
                break;
            }
        }

        patientData.dataClose();
    }

    // 2. Update Patient Medical Records
    private void updatePatientMedicalRecords() {
        PatientDataManager patientData = new PatientDataManager();
        XSSFSheet sheet = patientData.getSheet();

        System.out.println("Enter Patient ID to update records: ");
        Scanner sc = new Scanner(System.in);
        String patientID = sc.nextLine();

        System.out.println("Enter new diagnosis/treatment: ");
        String newRecord = sc.nextLine();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            
            if (row.equals(null)) {
                row = sheet.createRow(i); // Create the row if it doesn't exist
            }

            XSSFCell cell = row.getCell(i);
            if (cell.equals(null)) {
                cell = row.createCell(i); // Create the cell if it doesn't exist
            }
            
            if (row.getCell(0).getStringCellValue().equals(patientID)) {
                //String currentRecords = row.getCell(9).getStringCellValue();
                row.getCell(9).setCellValue(newRecord);
                patientData.writeIntoFile();
                break;
            }
        }

        patientData.writeIntoFile();
        patientData.dataClose();
        System.out.println("Patient medical records updated successfully.");
    }

    // 3. View Personal Schedule
    private void viewPersonalSchedule() {
        DoctorDataManager doctorData = new DoctorDataManager();
        XSSFSheet sheet = doctorData.getSheet();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(0).getStringCellValue().equals(getUserID())) {
                System.out.println("Upcoming Appointments: " + row.getCell(1).getStringCellValue());
                break;
            }
        }

        doctorData.dataClose();
    }

    // 4. Set Availability
    private void setAvailability() {
        DoctorDataManager doctorData = new DoctorDataManager();
        XSSFSheet sheet = doctorData.getSheet();

        System.out.println("Enter new available slots (comma-separated): ");
        Scanner sc = new Scanner(System.in);
        String newSlots = sc.nextLine();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row.getCell(0).getStringCellValue().equals(getUserID())) {
                row.getCell(1).setCellValue(newSlots);
                break;
            }
        }

        doctorData.writeIntoFile();
        doctorData.dataClose();
        System.out.println("Availability updated successfully.");
    }

    private void manageAppointmentRequests() {
		DoctorDataManager doctorData = new DoctorDataManager();
		PatientDataManager patientData = new PatientDataManager();
	
		Scanner sc = new Scanner(System.in);
	
		XSSFSheet doctorSheet = doctorData.getSheet();
		XSSFSheet patientSheet = patientData.getSheet();
	
		System.out.println("Your Appointment Requests:");
		for (int i = 1; i <= doctorSheet.getLastRowNum(); i++) {
			XSSFRow row = doctorSheet.getRow(i);
			if (row.getCell(0).getStringCellValue().equals(getUserID())) { // Assuming Doctor ID in column 0
				String appointmentRequests = row.getCell(4).getStringCellValue(); // Assuming appointment requests in column 4
				System.out.println("Requested Appointments: " + appointmentRequests);
			}
		}
	
		System.out.println("Enter the appointment request to manage (exact format): ");
		String selectedRequest = sc.nextLine();
	
		System.out.println("Do you want to accept or decline this request? (accept/decline): ");
		String action = sc.nextLine();
	
		for (int i = 1; i <= doctorSheet.getLastRowNum(); i++) {
			XSSFRow doctorRow = doctorSheet.getRow(i);
			if (doctorRow.getCell(0).getStringCellValue().equals(getUserID())) {
				String appointmentRequests = doctorRow.getCell(4).getStringCellValue();
				appointmentRequests = appointmentRequests.replace(selectedRequest, "").trim(); // Remove request
				doctorRow.getCell(4).setCellValue(appointmentRequests);
	
				if (action.equalsIgnoreCase("accept")) {
					// Add to confirmed appointments
					String confirmedAppointments = doctorRow.getCell(3).getStringCellValue(); // Column 3 stores confirmed appointments
					doctorRow.getCell(3).setCellValue(confirmedAppointments + " | " + selectedRequest);
	
					// Update Patient Data
					for (int j = 1; j <= patientSheet.getLastRowNum(); j++) {
						XSSFRow patientRow = patientSheet.getRow(j);
						if (patientRow.getCell(0).getStringCellValue().equals(selectedRequest.split(",")[0])) { // Match Patient ID
							String scheduledAppointments = patientRow.getCell(9).getStringCellValue(); // Column 9 stores scheduled appointments
							patientRow.getCell(9).setCellValue(scheduledAppointments + " | " + selectedRequest);
							break;
						}
					}
	
					System.out.println("Appointment accepted.");
				} else if (action.equalsIgnoreCase("decline")) {
					System.out.println("Appointment declined.");
				} else {
					System.out.println("Invalid action. No changes made.");
				}
				break;
			}
		}
	
		doctorData.writeIntoFile();
		patientData.writeIntoFile();
		doctorData.dataClose();
		patientData.dataClose();
	}



	private void recordAppointmentOutcome() {
		PatientDataManager patientData = new PatientDataManager();
		XSSFSheet patientSheet = patientData.getSheet();
	
		Scanner sc = new Scanner(System.in);
	
		System.out.println("Enter the Patient ID for the appointment: ");
		String patientID = sc.nextLine();
	
		System.out.println("Enter the Date of Appointment (e.g., 2024-11-25): ");
		String appointmentDate = sc.nextLine();
	
		System.out.println("Enter the Type of Service (e.g., Consultation, X-Ray, Blood Test): ");
		String typeOfService = sc.nextLine();
	
		System.out.println("Enter the prescribed medication (or 'none' if none): ");
		String medication = sc.nextLine();
	
		System.out.println("Enter the consultation notes: ");
		String consultationNotes = sc.nextLine();
	
		for (int i = 1; i <= patientSheet.getLastRowNum(); i++) {
			XSSFRow row = patientSheet.getRow(i);
			if (row.getCell(0).getStringCellValue().equals(patientID)) { // Match Patient ID
				String history = row.getCell(10).getStringCellValue(); // Column 10 stores appointment history
				String newHistory = "Date: " + appointmentDate +
									", Service: " + typeOfService +
									", Medication: " + medication +
									", Notes: " + consultationNotes;
				row.getCell(10).setCellValue(history + " | " + newHistory);
				break;
			}
		}
	
		patientData.writeIntoFile();
		patientData.dataClose();
		System.out.println("Appointment outcome recorded successfully.");
	}
	
	
}
