package HMS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StaffDataManager {
	
	private File staffFile;
	private XSSFWorkbook staffWorkBook;
	private XSSFSheet staffSheet;
	private FileInputStream staffFIS;
	
	public StaffDataManager() {
		
		staffFile = new File("C:\\Users\\her01\\eclipse-workspace\\OOP_Project\\Staff_List.xlsx");
		
		try {
			
			staffFIS = new FileInputStream(staffFile);
			
			staffWorkBook = new XSSFWorkbook(staffFIS);
			staffSheet = staffWorkBook.getSheetAt(0);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public XSSFSheet getSheet()
	{
		return staffSheet;
	}
	
	public void dataClose()
	{
		try {
			staffWorkBook.close();
			staffFIS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
