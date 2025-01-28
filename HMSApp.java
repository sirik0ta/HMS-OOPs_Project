package HMS;

import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class HMSApp {
	
	public static void main(String[] Args)
	{
		String userId, userRole;
		boolean success = false;
		Login log;
		int choice;
		Scanner sc = new Scanner(System.in);
		
		do
		{
			System.out.println("1. Login\n"
					+ "2. Exit");
			System.out.println("Enter your choice: ");
			choice = sc.nextInt();
			
			switch(choice)
			{
				case 1:
				    log = new Login();
				    User loggedInUser = log.login();
				    if (loggedInUser != null) {
				        userPage(loggedInUser);
				        success = true;
				    } else {
				        System.out.println("Login Failed!");
				    }
				    break;

			}
			
		} while(choice < 2 && choice > 0 && success == false);
		
	}
	
	public static void userPage(User user) {
	    if (user instanceof Patient) {
	        // Patient-specific operations
	        System.out.println("Welcome, Patient " + user.getUserID());
	    } else if (user instanceof Doctor) {
	        // Doctor-specific operations
	        System.out.println("Welcome, Doctor " + user.getUserID());
	    } else if (user instanceof Pharmacist) {
	        // Pharmacist-specific operations
	        System.out.println("Welcome, Pharmacist " + user.getUserID());
	    } else if (user instanceof Administrator) {
	        // Administrator-specific operations
	        System.out.println("Welcome, Administrator " + user.getUserID());
	    } else {
	        System.out.println("Role not recognized.");
	    }
	}

}
