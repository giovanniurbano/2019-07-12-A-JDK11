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
    	txtResult.clear();
    	boxFood.getItems().clear();
    	txtResult.appendText("Creazione grafo...\n");
    	String p = this.txtPorzioni.getText();
    	try {
    		int porzioni = Integer.parseInt(p);
    		if(porzioni < 1) {
    			this.txtResult.appendText("Inserire un intero maggiore di 0!");
        		return;
    		}
    		String msg = this.model.creaGrafo(porzioni);
    		this.txtResult.appendText(msg);
    		
    		this.boxFood.getItems().addAll(this.model.getVertici());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire un intero!");
    		return;
    	}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...\n");
    	Food f = this.boxFood.getValue();
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}	
    	if(f == null) {
    		this.txtResult.appendText("Scegliere un cibo!");
    		return;
    	}
    	List<Adiacenza> topFive = this.model.getViciniCalorie(f, 5);
    	for(Adiacenza a : topFive) {
    		this.txtResult.appendText(a.getF2() + " - " + a.getPeso() + "\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...\n");
    	Food f = this.boxFood.getValue();
    	
    	if(this.model.getGrafo() == null) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}	
    	if(f == null) {
    		this.txtResult.appendText("Scegliere un cibo!");
    		return;
    	}
    	
    	String k = this.txtK.getText();
    	try {
    		int stazioni = Integer.parseInt(k);
    		if(stazioni < 1 || stazioni > 10) {
    			this.txtResult.appendText("Inserire un intero compreso tra 1 e 10!");
        		return;
    		}
    		Simulator sim = new Simulator(this.model, stazioni, f);
    		sim.run();
    		this.txtResult.appendText("Cibi preparati: " + sim.getnCibi());
    		this.txtResult.appendText("\nTempo impiegato: " + sim.getTempoImpiegato());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire un intero!");
    		return;
    	}
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
