package seura;

	import java.net.URL;
	import java.util.ResourceBundle;

	import fi.jyu.mit.fxgui.Dialogs;
	import fi.jyu.mit.fxgui.ModalController;
	import fi.jyu.mit.fxgui.ModalControllerInterface;
	import fi.jyu.mit.ohj2.Mjonot;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.scene.Node;
	import javafx.scene.control.Label;
	import javafx.scene.control.ScrollPane;
	import javafx.scene.control.TextField;
	import javafx.scene.layout.GridPane;
	import javafx.stage.Stage;
	

	/**
	 * Kysyt‰‰n j‰senen tiedot luomalla sille uusi dialogi
	 * 
 * @author Anette Karhu 
 * @version 2.7.2016
	 *
	 */
	public class AsiakasDialogController implements ModalControllerInterface<Asiakas>,Initializable  {

	    @FXML private ScrollPane panelAsiakas;
	    @FXML private GridPane gridAsiakas;
	    @FXML private Label labelVirhe;

	    @Override
	    public void initialize(URL url, ResourceBundle bundle) {
	        alusta();  
	    }
	    
	    @FXML private void handleOK() {
	        if ( asiakasKohdalla != null && asiakasKohdalla.getNimi().trim().equals("") ) {
	            naytaVirhe("Nimi ei saa olla tyhj‰");
	            return;
	        }
	        ModalController.closeStage(labelVirhe);
	    }

	    
	    @FXML private void handleCancel() {
	    	asiakasKohdalla = null;
	        ModalController.closeStage(labelVirhe);
	    }

	// ========================================================    
	    private Asiakas asiakasKohdalla;
	    private static Asiakas apuasiakas = new Asiakas(); // J‰sen jolta voidaan kysell‰ tietoja.
	    private TextField[] edits;
	    private int kentta = 0;
	    

	    /**
	     * Luodaan GridPaneen asiakkaan tiedot
	     * @param gridAsiakas mihin tiedot luodaan
	     * @return luodut tekstikent‰t
	     */
	    public static TextField[] luoKentat(GridPane gridAsiakas) {
	    	gridAsiakas.getChildren().clear();
	        TextField[] edits = new TextField[apuasiakas.getKenttia()];
	        
	        for (int i=0, k = apuasiakas.ekaKentta(); k < apuasiakas.getKenttia(); k++, i++) {
	            Label label = new Label(apuasiakas.getKysymys(k));
	            gridAsiakas.add(label, 0, i);
	            TextField edit = new TextField();
	            edits[k] = edit;
	            edit.setId("e"+k);
	            gridAsiakas.add(edit, 1, i);
	        }
	        return edits;
	    }
	    

	    /**
	     * Palautetaan komponentin id:st‰ saatava luku
	     * @param obj tutkittava komponentti
	     * @param oletus mik‰ arvo jos id ei ole kunnollinen
	     * @return komponentin id lukuna 
	     */
	    public static int getFieldId(Object obj, int oletus) {
	        if ( !( obj instanceof Node)) return oletus;
	        Node node = (Node)obj;
	        return Mjonot.erotaInt(node.getId().substring(1),oletus);
	    }
	    
	    
	    /**
	     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
	     * yksi iso tekstikentt‰, johon voidaan tulostaa j‰senten tiedot.
	     */
	    protected void alusta() {
	        edits = luoKentat(gridAsiakas);
	        
	        for (TextField edit : edits)
	            if ( edit != null )
	                edit.setOnKeyReleased( e -> kasitteleMuutosAsiakkaaseen((TextField)(e.getSource())));
	        
	    }
	    
	    
	    @Override
	    public void setDefault(Asiakas oletus) {
	        asiakasKohdalla = oletus;
	        naytaAsiakas(edits, asiakasKohdalla);
	    }

	    
	    @Override
	    public Asiakas getResult() {
	        return asiakasKohdalla;
	    }
	    
	    
	    private void setKentta(int kentta) {
	        this.kentta = kentta;
	    }
	    
	    
	    /**
	     * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	     */
	    @Override
	    public void handleShown() {
	        kentta = Math.max(apuasiakas.ekaKentta(), Math.min(kentta, apuasiakas.getKenttia()-1));
	        edits[kentta].requestFocus();
	    }
	    
	    
	    private void naytaVirhe(String virhe) {
	        if ( virhe == null || virhe.isEmpty() ) {
	            labelVirhe.setText("");
	            labelVirhe.getStyleClass().removeAll("virhe");
	            return;
	        }
	        labelVirhe.setText(virhe);
	        labelVirhe.getStyleClass().add("virhe");
	    }

	    
	    /**
	     * K‰sitell‰‰n asiakkaaseen tullut muutos
	     * @param edit muuttunut kentt‰
	     */
	    protected void kasitteleMuutosAsiakkaaseen(TextField edit) {
	        if (asiakasKohdalla == null) return;
	        int k = getFieldId(edit,apuasiakas.ekaKentta());
	        String s = edit.getText();
	        String virhe = null;
	        virhe = asiakasKohdalla.aseta(k,s); 
	        if (virhe == null) {
	            Dialogs.setToolTipText(edit,"");
	            edit.getStyleClass().removeAll("virhe");
	            naytaVirhe(virhe);
	        } else {
	            Dialogs.setToolTipText(edit,virhe);
	            edit.getStyleClass().add("virhe");
	            naytaVirhe(virhe);
	        }
	    }
	    
	    
	    /**
	     * N‰ytet‰‰n asiakastiedot TextField komponentteihin
	     * @param edits taulukko TextFieldeist‰ johon n‰ytet‰‰n
	     * @param asiakas n‰ytett‰v‰ asiakas
	     */
	    public static void naytaAsiakas(TextField[] edits, Asiakas asiakas) {
	        if (asiakas == null) return;
	        for (int k = asiakas.ekaKentta(); k < asiakas.getKenttia(); k++) {
	            edits[k].setText(asiakas.anna(k));
	        }
	    }
	    
	    
	    /**
	     * Luodaan asiakkaan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
	     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
	     * @param oletus mit‰ dataan n‰ytet‰‰n oletuksena
	     * @param kentta mik‰ kentt‰ saa fokuksen kun n‰ytet‰‰n
	     * @return null jos painetaan Cancel, muuten t‰ytetty tietue
	     */
	    public static Asiakas kysyAsiakas(Stage modalityStage, Asiakas oletus, int kentta) {
	        return ModalController.<Asiakas, AsiakasDialogController>
	        		showModal(
	        		AsiakasDialogController.class.getResource("AsiakasDialogView.fxml"),
	                "Seura",
	                modalityStage, oletus,
	                ctrl -> ctrl.setKentta(kentta) 
	                
	                );

	}

	}

