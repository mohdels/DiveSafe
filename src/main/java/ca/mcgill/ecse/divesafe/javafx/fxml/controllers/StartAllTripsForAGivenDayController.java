package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesave.javafx.fxml.DiveSafeFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StartAllTripsForAGivenDayController {

    @FXML
    private Button StartTripsForAllMembersButton;

    @FXML
    private TextField startdate;
    
    @FXML
    private Button NumberOfDaysOfSeasonButton;

    @FXML
    private TextField startdateofseason;

    @FXML
    void NumberOfDaysOfSeasonClicked(ActionEvent event) {
       if (!(Integer.valueOf(startdateofseason.getText())>0)) {
         ViewUtils.showError("Please enter number that is greater than 0");
         
       }
       
       else {
         DiveSafeApplication.getDiveSafe().setNumDays(Integer.parseInt(startdateofseason.getText()));
         startdateofseason.setText("");
         DiveSafeFxmlView.getInstance().refresh();
       }
    }

    @FXML
    /*
     * @author Shivam && Ahmed Bachir
     */
    
    void StartTripsForAllMembersClicked(ActionEvent event) {
     String start_date = this.startdate.getText();
    
     
    if (start_date == null || start_date.trim().isEmpty()) {
      ViewUtils.showError("Please enter a non empty start date");
      
    }
    
    if (!(Integer.valueOf(start_date)>0)) {
      ViewUtils.showError("Please enter a start_date that is a valid number");
      
    }
    
    
    else {
      int start_day = Integer.parseInt(start_date);
      if(ViewUtils.successful(AssignmentController.startTripsForDay(start_day))) {
      this.startdate.setText("");
      
      
      DiveSafeFxmlView.getInstance().refresh();
      }
      
    }
    
      
      }

}

