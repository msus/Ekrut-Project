package ekrut.client.gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ekrut.client.EKrutClient;
import ekrut.client.managers.ClientReportManager;
import ekrut.entity.Report;
import ekrut.entity.ReportType;
import ekrut.entity.User;
import ekrut.entity.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ReportChooserController implements Initializable{

    @FXML
    private ComboBox<String> areaComboBox;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private Label nameInitialsLabel;

    @FXML
    private Label reportErrorLabel;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button viewReportButton;

    @FXML
    private ComboBox<String> yearComboBox;
    
    // TBD check what is this doing
    private EKrutClient client;
    
    User user = new User(UserType.CEO, "talga", "tal123", "tal", "gaon", "31231",
			"talgaon4@gmail.com", "0522613732", "north");
    
    ClientReportManager  clientReportManager = new ClientReportManager(client);

    @FXML
    void viewReport(ActionEvent event) throws Exception {
    	// Handle empty combo box first
    	if (typeComboBox.getValue() == null || areaComboBox.getValue() == null || 
    			monthComboBox.getValue() == null || yearComboBox.getValue() == null) {
        	// location can be null if the report is an order report
    		if(!typeComboBox.getValue().equals("Orders Report") && locationComboBox.getValue() == null) {
    			reportErrorLabel.setText("Error, you have to choose all the parameters");
    		}
    	}
    	// Try to fetch report
    	else {
    		// Create a lcoalDateTime instance
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM yyyy");
    		String dateString = monthComboBox.getValue() + " " + yearComboBox.getValue();
    		LocalDateTime date = LocalDateTime.parse(dateString, formatter);
    		
    		ReportType type = ReportType.valueOf(typeComboBox.getValue().toUpperCase());
    		
    		Report report = clientReportManager.getReport(
    				areaComboBox.getValue(), locationComboBox.getValue(), type, date);
    		
    		if (report == null) {
    			reportErrorLabel.setText("Error, there is not such report");
    		}
    		else {
    			/* go to report view screen
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportChooser.fxml"));
    			baseTemplateController.getBaseTempalteController().setRightWIndow(root);
    			*/
    		}
    	}
    }
    
    
    /*
    public void displayUserDetails(User user) {
    	nameInitialsLabel.setText(user.getFirstName().substring(0, 1).toUpperCase() +
    							 user.getLastName().substring(0, 1).toUpperCase());}
    							 
	String[] north = {"Haifa", "Akko", "Karmiel"};
	String[] south = {"Dimona", "Eilat", "Netivot"};
	String[] UAE = {"Dubai", "Abu-Dabi", "Queit"}; 
    */
    
    
    private void setAreaComboBox() {
    	//TBD better way to create those areas list
    	String[] areas = null;
    	// Set area comboBox
 		if (user.getUserType().equals(UserType.CEO)) {
 			areas = new String[3];
 			areas[0] = "North";
 			areas[1] = "South";
 			areas[2] = "UAE";
 		}
 		else if(user.getUserType().equals(UserType.AREA_MANAGER)) {
 			if (user.getArea().equals("north")) {
 				areas = new String[1];
 				areas[0] = "North";
 			}
 			else if (user.getArea().equals("south")) {
 				areas = new String[1];
 				areas[0] = "South";
 			}
 			else if (user.getArea().equals("uae")){
 				areas = new String[1];
 	 			areas[0] = "UAE";
 			}
 			//if the user is not have am area somehow, maybe this is not necessary
 			else {
 				//return to home page?
 			}
 		}
 		areaComboBox.getItems().addAll(areas);
    }
    
    private void setLocationComboBox(String area) {
    	
    	 ArrayList<String> locations = clientReportManager.fetchFacilitiesByArea(area);
    	 // Convert array list into a array
    	 String[] locationsArr = locations.toArray(new String[locations.size()]);
    	 locationComboBox.getItems().addAll(locationsArr);
    }
    
    // If the Report type is an order report than disable locations
    @FXML
    void setLocationsByType(ActionEvent event) {
    	String type = typeComboBox.getValue();
    	areaComboBox.setDisable(false);
    	
    	if (type.equals("Orders Report")) {
     			locationComboBox.setPromptText("Not Available");
     			locationComboBox.setDisable(true);
    	}
    }
    
    // Set the locations by the chosen area if the report type is not order
    @FXML
    void setLocationsByArea(ActionEvent event) {
    	String type = typeComboBox.getValue();
    	String area = areaComboBox.getValue();
    	if (!type.equals("Orders Report")) {
 			locationComboBox.setPromptText("Choose Location");
 			locationComboBox.getItems().clear();
 			locationComboBox.setDisable(false);
 			setLocationComboBox(area);
    	}
    }
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Set months, years, types combo boxes
		// Can be changed later to English months
 		String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
 		monthComboBox.getItems().addAll(months);
 		
 		String[] years = {"2021", "2022", "2023"};
 		yearComboBox.getItems().addAll(years);
 		
 		String[] types = {"Orders Report", "Inventory Report", "Customer Activity Report"};
 		typeComboBox.getItems().addAll(types);		
 		
 		// i think it should be here
 		setAreaComboBox();
 		
	}
	
}