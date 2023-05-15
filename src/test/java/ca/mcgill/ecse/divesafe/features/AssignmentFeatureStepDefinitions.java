package ca.mcgill.ecse.divesafe.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AssignmentFeatureStepDefinitions {

  private DiveSafe diveSafe;
  private String error;
  private int errorCntr;

  /*
   * These key variables are used to fetch the data from the map
   */
  private static final String hotelRequired = "hotelRequired";
  private static final String guideRequired = "guideRequired";
  private static final String emergencyContact = "emergencyContact";
  private static final String password = "password";
  private static final String email = "email";
  private static final String name = "name";
  private static final String startDate = "startDate";
  private static final String numDays = "numDays";
  private static final String weight = "weight";
  private static final String discount = "discount";
  private static final String items = "items";
  private static final String quantity = "quantity";
  private static final String priceOfGuidePerDay = "priceOfGuidePerDay";
  private static final String pricePerDay = "pricePerDay";
  private static final String startDay = "startDay";
  private static final String endDay = "endDay";
  private static final String guideEmail = "guideEmail";
  private static final String memberEmail = "memberEmail";


  /**
   * @author Mohammed Elsayed
   * @param dataTable This method sets the dive safe system
   */

  @Given("the following DiveSafe system exists:")
  public void the_following_dive_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {

    diveSafe = DiveSafeApplication.getDiveSafe();
    error = "";
    errorCntr = 0;

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    diveSafe.setStartDate(Date.valueOf(rows.get(0).get(startDate)));
    diveSafe.setNumDays(Integer.valueOf(rows.get(0).get(numDays)));
    diveSafe.setPriceOfGuidePerDay(Integer.valueOf(rows.get(0).get(priceOfGuidePerDay)));

  }

  /**
   * @author Mohammed Elsayed
   * @param dataTable This method adds equipment to the dive safe system
   */

  @Given("the following pieces of equipment exist in the system:")
  public void the_following_pieces_of_equipment_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> columns = rows.get(i);
      diveSafe.addEquipment(columns.get(name), Integer.parseInt(columns.get(weight)),
          Integer.parseInt(columns.get(pricePerDay)));
    }

  }

  /*
   * Adding all relevant bundles in the system
   * 
   * @author Mohammed Elsayed
   * 
   * @param dataTable table of data
   */
  @Given("the following equipment bundles exist in the system:")
  public void the_following_equipment_bundles_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {

      Map<String, String> columns = rows.get(i); // working on a single row a time

      // initialize the new EquipmentBundle
      EquipmentBundle newEquipmentBundle =
          new EquipmentBundle(columns.get(name), Integer.parseInt(columns.get(discount)), diveSafe);

      // Fetch the names of the equipments to be added to the bundle
      List<String> equipmentNamesInNewBundle = Arrays.asList(columns.get(items).split(","));
      // Fetch the quantity of each equipment
      List<String> equipmentsQuantity = Arrays.asList(columns.get(quantity).split(","));
      // get all equipments in the system
      List<Equipment> existingEquipments = diveSafe.getEquipments();

      for (int j = 0; j < equipmentNamesInNewBundle.size(); j++) {
        String equipmentName = equipmentNamesInNewBundle.get(j); // Fetch the equipment's name to be
                                                                 // added to the bundle

        // start looking for the equipment by its name
        Equipment equipment = null;
        for (Equipment temp : existingEquipments) {
          if (temp.getName().equals(equipmentName)) {
            equipment = temp;
            break; // break once found
          }
        }

        // add the equipment to the bundle
        newEquipmentBundle.addBundleItem(Integer.parseInt(equipmentsQuantity.get(j)), diveSafe,
            equipment);
      }
    }
  }


  /*
   * Adding all guides in the system
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param dataTable table of data
   */
  @Given("the following guides exist in the system:")
  public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (int i = 0; i < rows.size(); i++) {

      Map<String, String> columns = rows.get(i);

      new Guide(columns.get(email), columns.get(password), columns.get(name),
          columns.get(emergencyContact), diveSafe);
    }

  }

  /*
   * Adding all members in the system
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param dataTable tables of data
   */
  @Given("the following members exist in the system:")
  public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (int i = 0; i < rows.size(); i++) {

      Map<String, String> columns = rows.get(i);

      Member newMember = new Member(columns.get(email), columns.get(password), columns.get(name),
          columns.get(emergencyContact), Integer.parseInt(columns.get(numDays)),
          Boolean.parseBoolean(columns.get(guideRequired)),
          Boolean.parseBoolean(columns.get(hotelRequired)), diveSafe);

      List<String> itemBookings = Arrays.asList(columns.get("itemBookings").split(","));
      List<String> itemQuantity = Arrays.asList(columns.get("itemBookingQuantities").split(","));

      for (int j = 0; j < itemBookings.size(); j++) {
        Item.getWithName(itemBookings.get(j)).addItemBooking(Integer.parseInt(itemQuantity.get(j)),
            diveSafe, newMember);

      }

    }
  }

  /*
   * Admin attempts to start assignment process
   * 
   * @author Shivam Aery, Mohammed Elsayed
   */
  @When("the administrator attempts to initiate the assignment process")
  public void the_administrator_attempts_to_initiate_the_assignment_process() {
    // Write code here that turns the phrase above into concrete actions
    try {
      callController(AssignmentController.initiateAssignment());



    }

    catch (Exception e) {
      error = e.getMessage();

    }


  }

  /*
   * Adding all assignments to the system
   * 
   * @author Mohammed Elsayed
   * 
   * @param dataTable table of data
   */
  @Then("the following assignments shall exist in the system:")
  public void the_following_assignments_shall_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (int i = 0; i < rows.size(); i++) {
      Map<String, String> columns = rows.get(i);

      Assignment assignment = null;
      for (Assignment aAssignment : diveSafe.getAssignments()) {
        if (aAssignment.getMember().getEmail().equals(columns.get(memberEmail))) {
          assignment = aAssignment;
          break;
        }
      }

      // check if assignment exists
      assertNotNull(assignment);

      // check assignment startday
      assertEquals(assignment.getStartDay(), Integer.parseInt(columns.get(startDay)));

      // check assignment endday
      assertEquals(assignment.getEndDay(), Integer.parseInt(columns.get(endDay)));

      if (columns.get(guideEmail) == null) {
        assertNull(assignment.getGuide());
      } else {
        assertEquals(assignment.getGuide().getEmail(), columns.get(guideEmail));
      }
    }
  }

  /*
   * Checking that an assignment is correct
   * 
   * @author Mohammed Elsayed
   * 
   * @param memberEmail
   * 
   * @param assignmentState
   */
  @Then("the assignment for {string} shall be marked as {string}")
  public void the_assignment_for_shall_be_marked_as(String memberEmail, String assignmentState) {
    // Write code here that turns the phrase above into concrete actions
    Member member = (Member) User.getWithEmail(memberEmail); // Getting the member
    assertEquals(assignmentState, member.getAssignment().getAssignmentStateFullName());
  }

  /*
   * Checking number of assignments is correct
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param expectedNumberOfAssignments
   */
  @Then("the number of assignments in the system shall be {string}")
  public void the_number_of_assignments_in_the_system_shall_be(String expectedNumberOfAssignments) {
    // Write code here that turns the phrase above into concrete actions


    assertEquals(Integer.parseInt(expectedNumberOfAssignments), diveSafe.getAssignments().size());
  }

  /*
   * Raising a specific error message
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param expectedError
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String expectedError) {
    assertTrue(error.contains(expectedError));
  }



  /*
   * Checking that the following assignments exist in the system
   * 
   * @author Shivam Aery
   * 
   * @param dataTable a data table
   */
  @Given("the following assignments exist in the system:")
  public void the_following_assignments_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) throws Exception {



    // Extracting the components of the table

    List<Map<String, String>> rows = dataTable.asMaps();

    // Extracting the components of the table
    for (var row : rows) {

      String memberEmail = row.get("memberEmail");
      String guideEmail = row.get("guideEmail");
      int startDay = Integer.parseInt(row.get("startDay"));
      int endDay = Integer.parseInt(row.get("endDay"));
      String authCode = row.get("authorizationCode");

      Member member = (Member) User.getWithEmail(memberEmail);
      Assignment assignment = new Assignment(authCode, startDay, endDay, member, diveSafe);

      member.setAssignment(assignment);

      if (guideEmail != null) {
        assignment.setGuide((Guide) User.getWithEmail(guideEmail));
      }
    }


  }

  /*
   * Checking that admin tries to confirm a payment
   * 
   * @author Shivam Aery, Mohammed Elsayed
   *
   * @param string member email that is being confirmed
   * 
   * @param string2 authorization code
   */
  @When("the administrator attempts to confirm payment for {string} using authorization code {string}")
  public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(
      String string, String string2) {

    try {
      callController(AssignmentController.confirmPayment(string, string2));
    } catch (Exception e) {
      error = e.getMessage();
    }
  }

  /*
   * Checking that an assignment and authorization code correspond
   * 
   * @author Shivam Aery
   * 
   * @param string2 the authorization code
   * 
   * @param string member's email
   */
  @Then("the assignment for {string} shall record the authorization code {string}")
  public void the_assignment_for_shall_record_the_authorization_code(String string,
      String string2) {
    // Write code here that turns the phrase above into concrete actions
    var assignment = ((Member) Member.getWithEmail(string)).getAssignment();
    assertEquals(string2, assignment.getAuthorizationCode());

  }
  /*
   * Checking that there is no member associated with a specific email
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param string member email
   */

  @Then("the member account with the email {string} does not exist")
  public void the_member_account_with_the_email_does_not_exist(String string) {
    // Write code here that turns the phrase above into concrete actions
    Assert.assertNull(findMemberFromEmail(string));

  }

  /*
   * Checking the number of members in system
   * 
   * @author Shivam Aery
   * 
   * @param string the correct number of members in system
   */
  @Then("there are {string} members in the system")
  public void there_are_members_in_the_system(String string) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(Integer.parseInt(string), diveSafe.getMembers().size());
  }

  /*
   * Checking that an assignment is correct
   * 
   * @author Shivam Aery
   * 
   * @param message
   */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String message) {
    System.out.println(error);
    assertTrue(error.contains(message));
  }

  /*
   * Checking if an admin can cancel a member's trip
   * 
   * @author Shivam Aery, Mohammed Elsayed
   * 
   * @param string
   */
  @When("the administrator attempts to cancel the trip for {string}")
  public void the_administrator_attempts_to_cancel_the_trip_for(String string) {
    // Write code here that turns the phrase above into concrete actions
    try {
      callController(AssignmentController.cancelTrip(string));


    }

    catch (Exception e) {
      error = e.getMessage();

    }
  }

  /*
   * Updating system if member has paid for trip
   * 
   * @author Shivam Aery, Mohammed Elsayed, Nour Ktaily
   * 
   * @param string
   */
  @Given("the member with {string} has paid for their trip")
  public void the_member_with_has_paid_for_their_trip(String memberEmail) {
    Member member = (Member) Member.getWithEmail(memberEmail);
    member.getAssignment().pay("authorizationCode");
  }

  /*
   * Checking that an assignment is correct
   * 
   * @author Shivam Aery
   * 
   * @param email
   * 
   * @param percentRefund
   */
  @Then("the member with email address {string} shall receive a refund of {string} percent")
  public void the_member_with_email_address_shall_receive_a_refund_of_percent(String email,
      String percentRefund) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(((Member) Member.getWithEmail(email)).getAssignment().getRefundPercentage(),
        Integer.parseInt(percentRefund));
  }

  /*
   * Checking that a member has started trip
   * 
   * @author Shivam Aery
   * 
   * @param email
   */
  @Given("the member with {string} has started their trip")
  public void the_member_with_has_started_their_trip(String email) {
    // Write code here that turns the phrase above into concrete actions
    var assignment = ((Member) Member.getWithEmail(email)).getAssignment();
    assignment.pay("asdf");// give random valid code
    assignment.start();// start trip
  }

  /*
   * Admin tries to finish trip for member
   * 
   * @author Shivam Aery
   * 
   * @param string email of member
   */
  @When("the administrator attempts to finish the trip for the member with email {string}")
  public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(
      String string) {
    // Write code here that turns the phrase above into concrete actions
    try {
      callController(AssignmentController.finishTrip(string));



    }

    catch (Exception e) {
      error = e.getMessage();

    }
  }

  /*
   * Admin bans memberr
   * 
   * @author Shivam Aery
   * 
   * @param string email of member
   */
  @Given("the member with {string} is banned")
  public void the_member_with_is_banned(String email) {
    // Write code here that turns the phrase above into concrete actions
    var assignment = ((Member) Member.getWithEmail(email)).getAssignment();
    assignment.start();// start trip without paying moves to banned
  }

  /*
   * Checks if member is correct
   * 
   * @author Shivam Aery
   * 
   * @param string email of member
   * 
   * @param string2 actual state of member (i.e. banned)
   */
  @Then("the member with email {string} shall be {string}")
  public void the_member_with_email_shall_be(String email, String state) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(
        ((Member) (Member.getWithEmail(email))).getAssignment().getAssignmentStateFullName(),
        state);
  }

  /*
   * Admin tries to start trips for day string
   * 
   * @author Shivam Aery
   * 
   * @param string day admin tries to start trips for
   */
  @When("the administrator attempts to start the trips for day {string}")
  public void the_administrator_attempts_to_start_the_trips_for_day(String string) {
    // Write code here that turns the phrase above into concrete actions
    try {
      callController(AssignmentController.startTripsForDay(Integer.parseInt(string)));


    }

    catch (Exception e) {
      error = e.getMessage();

    }


  }
  /*
   * Member tries to cancel trip
   * 
   * @author Shivam Aery, Nour Ktaily
   * 
   * @param string email of member
   */

  @Given("the member with {string} has cancelled their trip")
  public void the_member_with_has_cancelled_their_trip(String string) {
    Member member = (Member) Member.getWithEmail(string);
    var assignment = member.getAssignment();
    assignment.cancel(); // cancel the trip
  }
  /*
   * Admin changes status of member to finished
   * 
   * @author Shivam Aery,Nour Ktaily
   * 
   * @param string email of member
   */

  @Given("the member with {string} has finished their trip")
  public void the_member_with_has_finished_their_trip(String string) {
   // find a user with the same email address
    Member member = (Member) Member.getWithEmail(string);
    var assignment = member.getAssignment();
    assignment.pay("asdf");
    assignment.start();
    assignment.finish(); // finish the trip
    
  }


  /**
   * Helper method used to find a member of the divesafe system from its email address
   * 
   * @author Mohammed Elsayed
   * @param email Email of the member to find in the divesafe system instance
   * @return
   */
  private Member findMemberFromEmail(String email) {
    List<Member> memberList = DiveSafeApplication.getDiveSafe().getMembers();
    for (Member member : memberList) {
      if (member.getEmail().equals(email)) {
        return member;
      }
    }
    return null;
  }
  private void callController(String result) {
    if (!result.isEmpty()) {
      error += result;
      errorCntr += 1;
    }
  }

}



