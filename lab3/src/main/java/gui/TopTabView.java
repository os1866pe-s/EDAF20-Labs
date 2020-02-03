package gui;

import javafx.scene.Parent;
import datamodel.Database;
import javafx.fxml.FXML;

public class TopTabView {
	@FXML private Parent aLoginTab;
	@FXML private LoginTab aLoginTabController;

	@FXML private Parent aBookingTab;
	@FXML private BookingTab aBookingTabController;
	
	public void initialize() {
		System.out.println("Initializing TopTabView");
		
		// send the booking controller reference to the login controller
		// in order to pass data between the two
		aLoginTabController.setBookingTab(aBookingTabController);
	}
	
	public void setDatabase(Database db) {
		aLoginTabController.setDatabase(db);
		aBookingTabController.setDatabase(db);
	}
}
