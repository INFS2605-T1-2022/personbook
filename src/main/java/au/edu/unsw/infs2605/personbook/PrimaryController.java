package au.edu.unsw.infs2605.personbook;

import java.time.DateTimeException;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class PrimaryController {
    
    @FXML public ListView<Person> myListView;
    @FXML public Label lblCaseNotes;
    @FXML public Button btnNew;
    @FXML public Button btnCancel;
    @FXML public Button btnSave;
    @FXML public Button btnNewCaseNote;
    @FXML public Button btnCancelNewCaseNote;
    @FXML public Button btnSaveCaseNote;
    @FXML public ToggleButton btnView;
    @FXML public ToggleButton btnEdit;
    @FXML public TextField txtFullName;
    @FXML public TextField txtBdayDay;
    @FXML public TextField txtBdayMonth;
    @FXML public TextField txtBdayYear;
    @FXML public TextArea txtCaseNotes;
    @FXML public CheckBox chkPersonal;
    @FXML public CheckBox chkBusiness;
    @FXML public ChoiceBox<CaseNote> choiceBoxForCaseNotes;
    
    public ObservableList<Person> people = FXCollections.observableArrayList(HelperForData.generateSamplePersonRecords());
    
    public Person currentlySelectedPerson;
    public CaseNote currentlySelectedCaseNote;
    public boolean changesMadeToPersonRecord;
    
    // prevent userDidSelectCaseNote() from executing when switching between person records.
    public boolean suppressCaseNoteListener = false;

    public void initialize() {
        this.setupButtonIcons();
        this.setupSampleData();
        
        // TIP-08: ToggleGroup
        ToggleGroup tgp = new ToggleGroup();
        tgp.getToggles().add(btnView);
        tgp.getToggles().add(btnEdit);
        
        // some buttons only visibile under very specific conditions
        HelperForJavafx.setNodesHidden(new Node[]{btnSave, btnCancelNewCaseNote, btnSaveCaseNote}, true);
        
        // since View is selected by default
        this.setPersonDetailsEditMode(false);
        
        myListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> userDidSelectListItem(newValue));
        choiceBoxForCaseNotes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> userDidSelectCaseNote(newValue));
    }
    //
    // HANDLE USER ACTIONS
    //

    @FXML
    private void userDidSelectListItem(Person selectedPerson) {
        HelperForPersonGUI.populatePersonDetails(selectedPerson, txtFullName, txtBdayDay, txtBdayMonth, txtBdayYear, chkPersonal, chkBusiness);
        
        // process case notes
        this.suppressCaseNoteListener = true;
        selectedPerson.sortCaseNotes();
        choiceBoxForCaseNotes.setItems(selectedPerson.getCaseNotes());
        this.suppressCaseNoteListener = false;
        
        // respond to case notes processing
        boolean caseNotesWereAdded = (selectedPerson.getCaseNotes().size() > 0);
        choiceBoxForCaseNotes.setDisable(!caseNotesWereAdded);
        txtCaseNotes.setDisable(!caseNotesWereAdded);
        if (caseNotesWereAdded) {
            choiceBoxForCaseNotes.getSelectionModel().select(0);
            this.userDidSelectCaseNote(choiceBoxForCaseNotes.getSelectionModel().getSelectedItem());
        } else {
            txtCaseNotes.setText("");
            this.currentlySelectedCaseNote = null;
        }
        
        // by default, cannot edit case notes
        HelperForJavafx.setTextboxEditable(txtCaseNotes, false);
        
        // reset the change tracker
        this.changesMadeToPersonRecord = false;
        
        // finally, update the selection tracker
        this.currentlySelectedPerson = selectedPerson;
    }
    
    @FXML
    private void userDidSelectCaseNote(CaseNote selectedCaseNote) {
        if (!suppressCaseNoteListener) {
            // if save button was visible from before, it should be invisible now
            HelperForJavafx.setNodeHidden(btnSaveCaseNote, true);

            // populate case note text
            txtCaseNotes.setText(selectedCaseNote.getCaseNoteText());

            // finally, update the selection tracker
            this.currentlySelectedCaseNote = selectedCaseNote;
        }
    }
    
    
    @FXML
    private void userDidClickNew() {
        Person person = new Person();
        this.people.add(person);
        myListView.getSelectionModel().select(person);
        
        btnEdit.setSelected(true);
        this.userDidClickEdit();
    }
    
    
    @FXML
    private void userDidAddNewCaseNote() {
        txtCaseNotes.setText("");
        txtCaseNotes.requestFocus(); // TIP-09: requestFocus()
        
        this.setCaseNoteEditMode(true);
    }
    
    @FXML
    private void userDidCancelNewCaseNote() {
        this.setCaseNoteEditMode(false);
        
        if (null != currentlySelectedCaseNote) {
            this.userDidSelectCaseNote(currentlySelectedCaseNote);
        } else {
            txtCaseNotes.setText("");
        }
    }
    
    @FXML
    private void userDidClickCancel() {
        this.setPersonDetailsEditMode(false);
        
        // discard any changes made, by re-loading the selected person.
        this.userDidSelectListItem(this.currentlySelectedPerson);
        
        if (this.currentlySelectedPerson.isNewContactNotYetSaved()) {
            myListView.getItems().remove(currentlySelectedPerson);
        }
    }
    
    @FXML
    private void userDidClickEdit() {
        this.setPersonDetailsEditMode(true);
    }
    
    @FXML
    private void userDidClickSave() {
        // TIP-10: Outsourcing to HelperForPersonGUI
        boolean couldSaveSuccessfully = HelperForPersonGUI.updatePersonDetails(this.currentlySelectedPerson, txtFullName, txtBdayDay, txtBdayMonth, txtBdayYear, chkPersonal, chkBusiness);
            
        if (couldSaveSuccessfully) {
            this.setPersonDetailsEditMode(false);
            myListView.refresh(); // show the new name already
        }
    }
    @FXML
    private void userDidClickSaveCaseNote() {
        CaseNote newCaseNote = new CaseNote();
        newCaseNote.setCaseNoteText(txtCaseNotes.getText());
        this.currentlySelectedPerson.addCaseNote(newCaseNote);
        this.currentlySelectedPerson.sortCaseNotes();
        
        this.setCaseNoteEditMode(false);
        
        // change selection to new case note
        choiceBoxForCaseNotes.setDisable(false); // there is now at least 1 case note, so this should not be disabled
        choiceBoxForCaseNotes.getSelectionModel().select(newCaseNote);
        this.userDidSelectCaseNote(newCaseNote);
    }
    
    @FXML
    private void userDidUpdatePersonDetails() {
        HelperForJavafx.disableButtonIfTextIsBlank(btnSave, txtFullName.getText());
    }
    @FXML
    private void userDidUpdateActiveCaseNote() {
        HelperForJavafx.disableButtonIfTextIsBlank(btnSaveCaseNote, txtCaseNotes.getText());
    }
    
    //
    // FUNCTIONALITIES
    //
    
    private void setupButtonIcons() {
        HelperForJavafx.setupIconForButton(btnNew, "Farm-Fresh_add.png");
        HelperForJavafx.setupIconForButton(btnNewCaseNote, "Farm-Fresh_add.png");
        HelperForJavafx.setupIconForButton(btnEdit, "Farm-Fresh_pencil.png");
        HelperForJavafx.setupIconForButton(btnView, "Farm-Fresh_vcard.png");
        HelperForJavafx.setupIconForButton(btnSave, "Farm-Fresh_diskette.png");
        HelperForJavafx.setupIconForButton(btnSaveCaseNote, "Farm-Fresh_diskette.png");
        HelperForJavafx.setupIconForButton(btnCancel, "Farm-Fresh_delete.png");
        HelperForJavafx.setupIconForButton(btnCancelNewCaseNote, "Farm-Fresh_delete.png");
    }
    
    private void setupSampleData() {
        // add all the things
        myListView.setItems(this.people);
        
        // select the first person by default
        // we can assume there is a first person since we just populated above!
        myListView.getSelectionModel().select(0);
        this.userDidSelectListItem(this.people.get(0));
    }
    
    private void setPersonDetailsEditMode(boolean isEditMode) {
        HelperForJavafx.setTextboxesEditable(new TextField[]{txtFullName, txtBdayDay, txtBdayMonth, txtBdayYear}, isEditMode);
        HelperForJavafx.setNodesHidden(new Node[]{btnCancel, btnSave}, !isEditMode);
        
        // set disable/enable for nodes
        Node[] nodesToDisableIffEditing = {btnNewCaseNote, btnView, btnEdit, myListView};
        Node[] nodesToEnableIffEditing = {chkPersonal, chkBusiness};
        HelperForJavafx.setNodesDisabled(nodesToDisableIffEditing, isEditMode);
        HelperForJavafx.setNodesDisabled(nodesToEnableIffEditing, !isEditMode);
        
        // behaviour only required when entering edit mode
        txtFullName.requestFocus();
        
        // behaviour only required when exiting edit mode
        if (!isEditMode) {
            System.out.println("!isEditMode");
            btnSave.setDisable(true);  // disable save button for next edit mode
            btnView.setSelected(true);
        }
    }
    
    private void setCaseNoteEditMode(boolean isEditMode) {
        // change which buttons are visible
        HelperForJavafx.setNodesHidden(new Node[]{choiceBoxForCaseNotes, btnNewCaseNote}, isEditMode);
        HelperForJavafx.setNodesHidden(new Node[]{btnCancelNewCaseNote, btnSaveCaseNote}, !isEditMode);
        
        // user should not be allowed to move between persons when in case note edit mode
        myListView.setDisable(isEditMode);
        txtCaseNotes.setDisable(!isEditMode);
        
        
        if (!isEditMode) { 
            btnSaveCaseNote.setDisable(true);
        }
        
        HelperForJavafx.setTextboxEditable(txtCaseNotes, true);
    }
}
