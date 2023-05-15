package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesave.javafx.fxml.DiveSafeFxmlView;
import javafx.event.ActionEvent;

public class PayOrFinishOrCancelMemberController {
	@FXML
	private TextField memberEmail;
	@FXML
	private TextField authorizationCode;
	@FXML
	private Button ConfirmPaymentButton;
	@FXML
	private Button FinishTripButton;
	@FXML
	private Button CancelTripButton;

	// Event Listener on Button[#ConfirmPaymentButton].onAction
	 @FXML
	    /*
	     * @author Shivam
	     */
	    public void CancelTripClicked(ActionEvent event) {
	      String email = memberEmail.getText();
	      
	      if (email == null || email.trim().isEmpty()||!email.contains("@")) {
	        ViewUtils.showError("Invalid email has been entered");
	        
	      }
	      if (ViewUtils.successful(AssignmentController.cancelTrip(email))){
	        memberEmail.setText("");
	        DiveSafeFxmlView.getInstance().refresh();
	        
	      }
	      
	    }

	    @FXML
	    public void ConfirmPaymentClicked(ActionEvent event) {
	        String email = memberEmail.getText();
	        String auth_code = authorizationCode.getText();
	      if (email == null || email.trim().isEmpty()||!email.contains("@")) {
	        ViewUtils.showError("Invalid email has been entered");
	        
	      }
	      if (auth_code == null || auth_code.trim().isEmpty()) {
	        ViewUtils.showError("Invalid authorization code has been entered");
	        
	      }
	      if (ViewUtils.successful(AssignmentController.confirmPayment(email,auth_code))){
	        memberEmail.setText("");
	        authorizationCode.setText("");
	        DiveSafeFxmlView.getInstance().refresh();
	        
	      }
	    }

	    @FXML
	    public void FinishTripClicked(ActionEvent event) {
	      String email = memberEmail.getText();
	      
	      if (email == null || email.trim().isEmpty()||!email.contains("@")) {
	        ViewUtils.showError("Invalid email has been entered");
	        
	      }
	      if (ViewUtils.successful(AssignmentController.finishTrip(email))){
	        memberEmail.setText("");
	        DiveSafeFxmlView.getInstance().refresh();
	        
	      }
	    }

	}

