package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="comboStato"
    private ComboBox<Country> comboStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	String anno= txtAnno.getText();
    	int annoI=0;
    	try {
    		annoI=Integer.parseInt(anno);	
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Devi inserire un numero");
    	}
    	
    	if(annoI>=1816 && annoI<=2016) {
    		this.model.creaGrafo(annoI);
    		txtResult.appendText("Grafo creato con " + this.model.numeroVertici() +" vertici e " +this.model.numeroArchi()+" archi\n");
    	}else {
    		txtResult.appendText("Devi inserire un numero compreso tra 1860 e 2016");
    	}

		txtResult.appendText("Il numero di componenti connesse è: "+ this.model.numComponentiConnesse()+"\n");
    	//for(Border b: this.model.getBorder(annoI)) {
		for(Country c: model.getGrafo().vertexSet()) {
    		txtResult.appendText(c.getStateName()+" - " + this.model.gradoVertice(c)+"\n");
    	}
    	
    	comboStato.getItems().addAll(this.model.getCountry());

    }

    @FXML
    void doTrovaVicini(ActionEvent event) {
    	txtResult.clear();
    	
    	Country box=comboStato.getValue();
    	
    	txtResult.appendText("I vicini sono: \n");
    	
    	for(Country c: this.model.trovaVicini(box)) {
    		txtResult.appendText(c.toString()+"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert comboStato != null : "fx:id=\"comboStato\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
