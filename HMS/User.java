package HMS;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class User {

	private String userID;
	private String userName;
	private String userGender;
	private String userRole;
	private int age;
	private String DOB;
	private String bloodType;
	private String contactInfo;
	private XSSFSheet userSheet;
	PatientDataManager patientData;
	StaffDataManager staffData;
	private int i;
	
	public User(String userID, String userRole) {
		this.userID = userID;
		this.userRole = userRole;
		
		if(userRole.equals("Patient"))
		{
			patientData = new PatientDataManager();
			userSheet = patientData.getSheet();
			
			for(i=0; i<userSheet.getLastRowNum(); i++)
			{
				if(userSheet.getRow(i).getCell(0).getStringCellValue().equals(userID))
				{
					userName = userSheet.getRow(i).getCell(1).getStringCellValue();
					DOB = userSheet.getRow(i).getCell(2).getStringCellValue();
					userGender = userSheet.getRow(i).getCell(3).getStringCellValue();
					bloodType = userSheet.getRow(i).getCell(4).getStringCellValue();
					contactInfo = userSheet.getRow(i).getCell(5).getStringCellValue();
					break;
				}
			}
			
		}
		else
		{
			staffData = new StaffDataManager();
			userSheet = staffData.getSheet();
			for(i=0; i<userSheet.getLastRowNum(); i++)
			{
				if(userSheet.getRow(i).getCell(0).getStringCellValue().equals(userID))
				{
					userName = userSheet.getRow(i).getCell(1).getStringCellValue();
					userGender = userSheet.getRow(i).getCell(3).getStringCellValue();
					age = (int) userSheet.getRow(i).getCell(4).getNumericCellValue();
					break;
				}
			}
		}
		
	}
	
	public String getUserID()
	{
		return userID;
	}
	
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getUserGender()
	{
		return userGender;
	}
	
	public void setUserGender(String userGender)
	{
		this.userGender = userGender;
	}
	
	public String getUserRole()
	{
		return userRole;
	}
	
	public void setUserRole(String userRole)
	{
		this.userRole = userRole;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
		
		userSheet.getRow(i).getCell(5).setCellValue(contactInfo);
		
		patientData.writeIntoFile();
		
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
}
