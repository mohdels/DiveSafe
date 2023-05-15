package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;



import java.util.List;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.TOAssignment;
import ca.mcgill.ecse.divesave.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AssignmentPageController {
  @FXML
  private TableView<TOAssignment> overviewTable;
  @FXML
  private TextField emailTextField;
  @FXML
  private TextField codeTextField;
  @FXML
  private Button startButton;
  @FXML
  private Button finishButton;
  @FXML
  private Button CancelButton;
  @FXML
  private TextField DayTextField;
  @FXML
  private Button payButton;
  @FXML
  private Button initiateButton;
  @FXML
  private Button initiateButton1;
  ObservableList<TOAssignment> data;

  /**
   * controller for initializing the fxml page
   * 
   * @author Mohammed Elsayed
   */
  @FXML
  public void initialize() {
    TableColumn member = new TableColumn("Member Email");
    TableColumn guide = new TableColumn("Guide Email");
    TableColumn fDay = new TableColumn("From Day");
    TableColumn tDay = new TableColumn("To Day");
    TableColumn status = new TableColumn("Status");
    TableColumn authCode = new TableColumn("Auth Code");
    TableColumn refund = new TableColumn("Refund");
    TableColumn guideCost = new TableColumn("Guide Cost");
    TableColumn totalCost = new TableColumn("Total Cost");
    TableColumn prizeDiscount = new TableColumn("Discount");

    overviewTable.getColumns().addAll(member, guide, fDay, tDay, status, authCode, refund,
        guideCost, totalCost, prizeDiscount);
    data = getAss();

    member.setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("memberEmail"));
    guide.setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("guideEmail"));
    fDay.setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("startDay"));
    tDay.setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("endDay"));
    status.setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("status"));
    authCode
        .setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("authorizationCode"));
    refund.setCellValueFactory(
        new PropertyValueFactory<TOAssignment, String>("refundedPercentageAmount"));
    guideCost
        .setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("totalCostForGuide"));
    totalCost.setCellValueFactory(
        new PropertyValueFactory<TOAssignment, String>("totalCostForEquipment"));
    prizeDiscount
        .setCellValueFactory(new PropertyValueFactory<TOAssignment, String>("prizeDiscount"));

    // Step 4: add data inside table
    overviewTable.setItems(data);

    overviewTable.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT,
        e -> overviewTable.setItems(getAss()));

    // register refreshable nodes
    DiveSafeFxmlView.getInstance().registerRefreshEvent(overviewTable);
  }

 


  

  

  /**
   * method called when refreshes button is clicked and refreshes the view assignment table
   * 
   * @author Mohammed Elsayed
   * @param event
   */
  // Event Listener on Button[#initiateButton].onAction
  @FXML
  public void initiateClicked(ActionEvent event) {

    DiveSafeFxmlView.getInstance().refresh();

  }

  /**
   * method called when initiate trips button is clicked and initiates respective trips
   * 
   * @author Mohammed Elsayed
   * @param event
   */
  @FXML
  public void initiateClicked1(ActionEvent event) {
    try {
      AssignmentController.initiateAssignment();
    } catch (Exception e) {

      e.printStackTrace();
    }
    DiveSafeFxmlView.getInstance().refresh();

  }

  /**
   * gets the assignments and returns an array of ToAssignment in ObservableList format
   * 
   * @author Mohammed Elsayed
   */
  ObservableList<TOAssignment> getAss() {

    ObservableList<TOAssignment> assignment = FXCollections.observableArrayList();
    List<TOAssignment> temp = AssignmentController.getAssignments();

    for (TOAssignment current : temp) {
      assignment.add(current);


    }
    return assignment;

  }

}
