package fxSeura;

import fi.jyu.mit.fxgui.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Tulostuksen hoitava luokka
 * @author Anette Karhu
 * @version 2.7.2016
 */
public class TulostusController implements ModalControllerInterface<String> {

	@FXML TextArea tulostusAlue;
	
    /**
     * K‰sitell‰‰n tulostusk‰sky
     */
    @FXML private void handleTulosta() {
    	Dialogs.showMessageDialog("Osaan myˆhemmin tulostaa");
    }

    /**
     * K‰sitell‰‰n ok-k‰sky
     */
    @FXML private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }
    
 
    @Override
    public void setDefault(String oletus) {
    //if (oletus == null) return;
    tulostusAlue.setText(oletus);
    }

 
	@Override
	public String getResult() {
		return null;
	}

	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		// teen jotain myˆhemmin
	}
	
	
	/**
	 * Tulostusalueen k‰sittely
	 * @return tulostusalue johon tiedot tulostetaan
	 */
	public TextArea getTextArea() {
		return tulostusAlue;
	}

	/**
	 * N‰ytt‰‰ tulostuslueella tekstin.
	 * @param tulostus tulostettava teksti
	 * @return kontrolleri jolta pyydet‰‰n list‰‰ tietoa
	 */
	public static TulostusController tulosta(String tulostus) {
		TulostusController tulostusCtrl = 
				(TulostusController) ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"), 
				"Tulostus", tulostus);
		return tulostusCtrl;
	}
	
 }