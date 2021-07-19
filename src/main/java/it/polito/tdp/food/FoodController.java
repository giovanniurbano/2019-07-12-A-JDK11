/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Simulator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.boxFood.getItems().clear();
    	this.txtResult.clear();
    	String ps = this.txtPorzioni.getText();
    	try {
    		int p = Integer.parseInt(ps);
    		if(p < 0) {
    			this.txtResult.setText("Inserire un intero > 0!");
        		return;
    		}
    		String msg = this.model.creaGrafo(p);
    		this.txtResult.appendText(msg);
    		
    		this.boxFood.getItems().addAll(this.model.getVertici());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.setText("Inserire un intero!");
    		return;
    	}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	if(this.model.getGrafo() == null) {
    		this.txtResult.setText("Creare grafo!");
    		return;
    	}
    	Food f = this.boxFood.getValue();
    	if(f == null) {
    		this.txtResult.setText("Scegliere un cibo!");
    		return;
    	}
    	List<Adiacenza> bestFive = this.model.getBestFive(f);
    	this.txtResult.setText("Cibi con calorie congiunte massime:");
    	for(Adiacenza a : bestFive)
    		this.txtResult.appendText("\n" + a.getF2() + " - " + a.getPeso());	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
