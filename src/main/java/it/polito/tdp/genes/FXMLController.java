/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnContaArchi"
    private Button btnContaArchi; // Value injected by FXMLLoader

    @FXML // fx:id="btnRicerca"
    private Button btnRicerca; // Value injected by FXMLLoader

    @FXML // fx:id="txtSoglia"
    private TextField txtSoglia; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doContaArchi(ActionEvent event) {
    	double s;
    	try {
    		s = Integer.parseInt(txtSoglia.getText());
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.\n");
    		e.printStackTrace();
    		return;
    	}
    	if (s<model.getPesoMin() || s>model.getPesoMax()) {
    		txtResult.setText("Inserire un valore compreso tra "+model.getPesoMin()+" e "+model.getPesoMax()+"\n");
    		return;
    	}
    	txtResult.appendText("Soglia: "+s+" --> Maggiori "+model.getMaggiori(s)+", minori "+model.getMinori(s)+"\n");

    }

    @FXML
    void doRicerca(ActionEvent event) {
    	txtResult.clear();
    	double s;
    	try {
    		s = Integer.parseInt(txtSoglia.getText());
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserire un valore numerico.\n");
    		e.printStackTrace();
    		return;
    	}
    	if (s<model.getPesoMin() || s>model.getPesoMax()) {
    		txtResult.setText("Inserire un valore compreso tra "+model.getPesoMin()+" e "+model.getPesoMax()+"\n");
    		return;
    	}
    	List<Integer> cammino = model.cercaLista(s);
    	for (Integer c : cammino) 
    		txtResult.appendText(c+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnContaArchi != null : "fx:id=\"btnContaArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSoglia != null : "fx:id=\"txtSoglia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		model.creaGrafo();
		txtResult.setText("Grafo creato: "+model.nVertici()+" vertici, "+model.nArchi()+" archi\n");
		txtResult.appendText("Peso minimo = "+model.getPesoMin()+", peso massimo = "+model.getPesoMax()+"\n");
		
	}
}
