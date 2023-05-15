package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesave.javafx.fxml.DiveSafeFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UpdateDeleteRegisterMemberController {

  @FXML
  private Button DeleteMemberButton;

  @FXML
  private TextField MemberEmail;

  @FXML
  private TextField MemberName;

  @FXML
  private TextField MemberPassword;

  @FXML
  private Button RegisterMemberButton;

  @FXML
  private Button UpdateMemberButton;

  @FXML
  private TextField emergencyContact;

  @FXML
  private TextField numberOfDays;

/*
 * @author Shivam
 */
    @FXML
    public void DeleteMemberClicked(ActionEvent event) {
       String member_email = MemberEmail.getText();
     
       if (member_email == null || member_email.trim().isEmpty()|| !member_email.contains("@")) {
         ViewUtils.showError("Please input a valid member email");
         
       }
       else
       {
         if(ViewUtils.successful(MemberController.deleteMember(member_email))) {
           MemberEmail.setText("");
           DiveSafeFxmlView.getInstance().refresh();
           
         }
         
       }
    }

    @FXML
    public void RegisterMemberClicked(ActionEvent event) {
      String member_email = MemberEmail.getText();
      String member_password = MemberPassword.getText();
      String emerge_contact = emergencyContact.getText();
      String number_of_days = numberOfDays.getText();
      String member_name = MemberName.getText();
      List<String> itemsName = new ArrayList<String>();
      List<Integer> itemsQuantity = new ArrayList<Integer>();
     
      if (member_email == null || member_email.trim().isEmpty() || !member_email.contains("@")) {
        ViewUtils.showError("Please input a valid member email");
        
      }
      else if (member_password  == null || member_password.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid password");
        
      }
      
      else if (emerge_contact == null || emerge_contact.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid emergency Contact");
      }
      else if (!(Integer.valueOf(number_of_days)>0)) {
        ViewUtils.showError("Please input a valid number of days");
        
      }
      else
      {
        int num_days = Integer.parseInt(number_of_days); 
        if(ViewUtils.successful(MemberController.registerMember(member_email, member_password,emerge_contact,member_name, num_days, false, false, itemsName, itemsQuantity)));
        {
        MemberEmail.setText("");
        MemberPassword.setText("");
        emergencyContact.setText("");
        numberOfDays.setText("");
        MemberName.setText("");
        DiveSafeFxmlView.getInstance().refresh();
        }
      }

    }

    @FXML
    public void UpdateMemberClicked(ActionEvent event) {
      String member_email = MemberEmail.getText();
      String member_password = MemberPassword.getText();
      String emerge_contact = emergencyContact.getText();
      String number_of_days = numberOfDays.getText();
      String member_name = MemberName.getText();
      if (member_email == null || member_email.trim().isEmpty()|| !member_email.contains("@")) {
        ViewUtils.showError("Please input a valid member email");
        
      }
      else if (member_password  == null || member_password.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid password");
        
      }
      
      else if (emerge_contact == null || emerge_contact.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid emergency Contact");
      }
      else if (!(Integer.valueOf(number_of_days)>0)) {
        ViewUtils.showError("Please input a valid number of days");
        
      }
      
      else
      {
        int num_days = Integer.parseInt(number_of_days);
        if (ViewUtils.successful(MemberController.updateMember(member_email, member_password,emerge_contact, member_name, num_days, false, false, null, null))) {
          MemberEmail.setText("");
          MemberPassword.setText("");
          emergencyContact.setText("");
          numberOfDays.setText("");
          DiveSafeFxmlView.getInstance().refresh();
          
        }
        
      }
    }

}
