package HMS;

import java.util.Scanner;

public class PharmacistMenu {

    private Pharmacist pharmacist;

    public PharmacistMenu(Pharmacist pharmacist, String phaRole) {
        if (pharmacist == null) {
            throw new IllegalArgumentException("Pharmacist object cannot be null.");
        }
        this.pharmacist = pharmacist;
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
                        pharmacist.viewAppointmentOutcomeRecord();
                        break;
                    case 2:
                        handleUpdatePrescriptionStatus(sc);
                        break;
                    case 3:
                        pharmacist.viewMedicationInventory();
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

        boolean success = pharmacist.updatePrescriptionStatus(appointmentId, newStatus);
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

        boolean success = pharmacist.submitReplenishmentRequest(medicationName);
        if (success) {
            System.out.println("Replenishment request submitted successfully.");
        } else {
            System.out.println("Failed to submit replenishment request. Please try again.");
        }
    }
}

