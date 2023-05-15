package ca.mcgill.ecse.divesafe.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.Assignment.AssignmentState;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.User;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

public class AssignmentController {
  private static DiveSafe diveSafe = DiveSafeApplication.getDiveSafe();

  private AssignmentController() {}

  public static List<TOAssignment> getAssignments() {
    List<TOAssignment> assignments = new ArrayList<>();

    for (var assignment : diveSafe.getAssignments()) {
      var newTOAssignment = wrapAssignment(assignment);
      assignments.add(newTOAssignment);
    }

    return assignments;
  }

  /*
   * @author Shivam Aery, Mohammed Elsayed
   */
  public static String initiateAssignment() {
    String error = "";


    ArrayList<Assignment> allAss = new ArrayList<Assignment>();
    int memberIndex = 0;
    for (int i = 0; i < diveSafe.getGuides().size(); i++) {
      Guide currentGuide = diveSafe.getGuide(i);
      for (Member currentMember : diveSafe.getMembers()) {


        if (currentMember.hasAssignment() == true) {
          memberIndex++;
          continue;
        }

        if (!currentMember.getGuideRequired()) {
          Assignment a = new Assignment("", 1, currentMember.getNumDays(), currentMember, diveSafe);
          diveSafe.addAssignment(a);
          allAss.add(a);
          currentMember.getAssignment().setState(AssignmentState.Assigned);
          DiveSafePersistence.save();
          memberIndex++;
          continue;
        }

        int memberWeeks = currentMember.getNumDays();
        int totalWeeks = diveSafe.getNumDays();
        int start = 0;
        int end = 0;

        int busyWeeks = 0;
        for (int k = 0; k < currentGuide.getAssignments().size(); k++) {
          busyWeeks += currentGuide.getAssignments().get(k).getMember().getNumDays();
        }
        if ((totalWeeks - busyWeeks) >= memberWeeks) {
          start = busyWeeks + 1;
          end = start + memberWeeks - 1;
          Assignment a = new Assignment("", start, end, currentMember, diveSafe);
          a.setGuide(diveSafe.getGuide(i));
          diveSafe.addAssignment(a);
          currentMember.getAssignment().setState(AssignmentState.Assigned);
          allAss.add(a);
          DiveSafePersistence.save();
          memberIndex++;
          continue;

        }
        memberIndex++;
      }
    }
    for (Member m : diveSafe.getMembers()) {
      if (m.hasAssignment() == false) {
        error = "Assignments could not be completed for all members";
        return error;
      }
    }

    return "";

  }



  /*
   * @author Shivam Aery, Mohammed Elsayed, Adam Abouzahr
   */

  public static String cancelTrip(String userEmail) {
    String error = "";
    // find a user with the same email address
    User user = Member.getWithEmail(userEmail);

    // check whether the user with that email is member or not
    if ((!(user instanceof Member member)) || user == null) { // if not member
      error = "Member with email address " + userEmail + " does not exist";
      return error;
    }

    var assignment = member.getAssignment();



    try {
      assignment.cancel();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    DiveSafePersistence.save(DiveSafeApplication.getDiveSafe());



    return "";

  }

  /*
   * @author Shivam Aery, Mohammed Elsayed, Adam Abouzahr
   */
  public static String finishTrip(String userEmail) {

    String error = "";
    User u = Member.getWithEmail(userEmail);

    if (!(u instanceof Member m)) {
      error = "Member with email address " + userEmail + " does not exist";
      return error;
    }
    Assignment assignment = m.getAssignment();

    switch (assignment.getAssignmentState()) {
      case Assigned:
      case Paid:
        error = "Cannot finish a trip which has not started";
        return error;
      case Banned:
        error = "Cannot finish the trip due to a ban";
        return error;
      case Cancelled:
        error = "Cannot finish a trip which has been cancelled";
        return error;
      case Started:
        assignment.finish();
        try {
          DiveSafePersistence.save(); // Update the persistence layer
        } catch (RuntimeException e) {
          return e.getMessage();
        }
        break;
      case Finished:
      default:
        break;

    }



    return error;
  }



  /*
   * @author Shivam Aery
   */
  public static String startTripsForDay(int day) {
    String error = "";

    if (day < 1) {
      error = "day number has to be at least 1";
      return error;
    }
    if (day > DiveSafeApplication.getDiveSafe().getNumDays()) {
      error = "Day number cannot be bigger than the number of days in the seasons";
      return error;

    }
    for (Assignment assignment : DiveSafeApplication.getDiveSafe().getAssignments()) {

      if (assignment.getStartDay() == day) {

        try {
          assignment.start();
          DiveSafePersistence.save(DiveSafeApplication.getDiveSafe());
        } catch (Exception e) {
          error = e.getMessage();
          return error;

        }



      }

    }



    return error;
  }

  /*
   * @author Shivam Aery, Nour Ktaily
   */
  public static String confirmPayment(String userEmail, String authorizationCode) {
    String error = "";
    Member member = (Member) Member.getWithEmail(userEmail);
    // Verify that user email is not null
    if (member == null) {

      error = "Member with email address " + userEmail + " does not exist";
      return error;
    }
    // Verify that user email is not empty
    if (userEmail.isEmpty()) {

      error = "Invalid email address";
      return error;
    }

    // Verify authorization code is not empty
    if (authorizationCode.isEmpty()) {
      error = "Invalid authorization code";
      return error;
    }
    // Verify that member has an assignment
    if (member.getAssignment() == null) {
      error = "Member has no assignment yet";
      return error;
    }



    try {
      member.getAssignment().pay(authorizationCode);
      DiveSafePersistence.save();
    } catch (Exception e) {
      error = e.getMessage();
    }
    return error;
  }

  /*
   * @author Shivam Aery
   */
  public static String toggleBan(String userEmail) {
    String error = "";

    Member member = Member.getWithEmail(userEmail);
    if (member.getAssignment().pay(member.getAssignment().getAuthorizationCode()) == false) {

      try {
        member.getAssignment().setState(Assignment.AssignmentState.Banned);
        DiveSafePersistence.save(DiveSafeApplication.getDiveSafe());
      } catch (Exception e) {
        error = e.getMessage();
        return error;

      }



    }



    return error;
  }

  /**
   * Helper method used to wrap the information in an <code>Assignment</code> instance in an
   * instance of <code>TOAssignment</code>.
   *
   * @author Harrison Wang Oct 19, 2021
   * @param assignment - The <code>Assignment</code> instance to transfer the information from.
   * @return A <code>TOAssignment</code> instance containing the information in the
   *         <code>Assignment</code> parameter.
   */
  private static TOAssignment wrapAssignment(Assignment assignment) {
    var member = assignment.getMember();

    // Initialize values for all necessary parameters.
    String memberEmail = member.getEmail();
    String memberName = member.getName();
    String guideEmail = assignment.hasGuide() ? assignment.getGuide().getEmail() : "";
    String guideName = assignment.hasGuide() ? assignment.getGuide().getName() : "";
    String hotelName = assignment.hasHotel() ? assignment.getHotel().getName() : "";

    int numDays = member.getNumDays();
    int startDay = assignment.getStartDay();
    int endDay = assignment.getEndDay();
    int totalCostForGuide = assignment.hasGuide() ? numDays * diveSafe.getPriceOfGuidePerDay() : 0;
    /*
     * Calculate the totalCostForEquipment.
     *
     * Sum the costs of all booked items depending on if they are an Equipment or EquipmentBundle
     * instance to get the equipmentCostPerDay for this assignment.
     *
     * Multiply equipmentCostPerDay by nrOfDays to get totalCostForEquipment.
     */
    int equipmentCostPerDay = 0;
    for (var bookedItem : member.getItemBookings()) {
      Item item = bookedItem.getItem();
      if (item instanceof Equipment equipment) {
        equipmentCostPerDay += equipment.getPricePerDay() * bookedItem.getQuantity();
      } else if (item instanceof EquipmentBundle bundle) {
        int bundleCost = 0;
        for (var bundledItem : bundle.getBundleItems()) {
          bundleCost += bundledItem.getEquipment().getPricePerDay() * bundledItem.getQuantity();
        }
        // Discount only applicable if assignment includes guide, so check for that before applying
        // discount
        if (assignment.hasGuide()) {
          bundleCost = (int) (bundleCost * ((100.0 - bundle.getDiscount()) / 100.0));
        }
        equipmentCostPerDay += bundleCost * bookedItem.getQuantity();
      }
    }
    int totalCostForEquipment = equipmentCostPerDay * numDays;

    return new TOAssignment(memberEmail, memberName, guideEmail, guideName, hotelName, startDay,
        endDay, totalCostForGuide, totalCostForEquipment, hotelName, hotelName,
        totalCostForEquipment);
  }



}
