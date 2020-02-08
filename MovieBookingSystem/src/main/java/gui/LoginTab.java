package gui;

import datamodel.CurrentUser;
import datamodel.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

// controller for both the top tabs and login tab!

public class LoginTab {
    @FXML private Text actiontarget;
    @FXML private TextField username;

    private BookingTab bookingTabCtrl;
    private Database db;
       
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
    	
        if(!db.isConnected()) {
	        // inform the user that there is no check against the database
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Login fail");
	        alert.setHeaderText(null);
	        alert.setContentText("No database connection! Cannot check user credentials.");
	        alert.showAndWait();      	
        } else {
            String uname = username.getText();

            if(!db.login(uname)){

                actiontarget.setText("");

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Login fail");
                alert.setHeaderText(null);
                alert.setContentText("No such user exists.");
                alert.showAndWait();

                CurrentUser.instance().loginAs(null);
                bookingTabCtrl.userChanged();

            }else{
                // setting the user name
                CurrentUser.instance().loginAs(uname);

                // inform the user about logging in
                actiontarget.setText("Sign in user "+uname);

                // inform booking tab of user change
                bookingTabCtrl.userChanged();

            }
        }
    }

    public void initialize() {
    	System.out.println("Initializing LoginTab.");
        /*
    	rootLogin.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                btnSignIn.fire();
                ev.consume();
            }
        });
        */
    }
        
    // helpers
    // use this pattern to send data down to controllers at initialization
    public void setBookingTab(BookingTab bookingTabCtrl) {
    	System.out.println("LoginTab sets bookingTab:"+bookingTabCtrl);
    	this.bookingTabCtrl = bookingTabCtrl; 	
    }
    
    public void setDatabase(Database db) {
    	this.db = db;
    }
    
}