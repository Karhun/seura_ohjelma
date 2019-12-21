package fxSeura;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Anette
 * @version 1.8.2016
 *
 */
public class SeuranNimiController implements ModalControllerInterface<String>{
	  @FXML private TextField textVastaus;
	    private String vastaus = null;

	    
	    @FXML private void handleOK() {
	        vastaus = textVastaus.getText();
	        ModalController.closeStage(textVastaus);
	    }

	    
	    @FXML private void handleCancel() {
	        ModalController.closeStage(textVastaus);
	    }


	    @Override
	    public String getResult() {
	        return vastaus;
	    }

	    
	    @Override
	    public void setDefault(String oletus) {
	        textVastaus.setText(oletus);
	    }

	    
	    /**
	     * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	     */
	    @Override
	    public void handleShown() {
	        textVastaus.requestFocus();
	    }
	    
	    
	    /**
	     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
	     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
	     * @param oletus mit‰ nime‰ n‰ytet‰‰n oletuksena
	     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
	     */
	    public static String kysyNimi(Stage modalityStage, String oletus) {
	        return ModalController.showModal(
	                SeuranNimiController.class.getResource("SeuranNimiView.fxml"),
	                "Seura",
	                modalityStage, oletus);
	    }
	}
