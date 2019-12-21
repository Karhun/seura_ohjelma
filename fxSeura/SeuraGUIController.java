package fxSeura;


import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
//import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import seura.Asiakas;
import seura.SailoException;
import seura.Seura;
//import seura.VarauksenKohde;
import seura.Varaus;

/**
 * Luokka k‰yttˆliittym‰n tapahtumien hoitamiseksi.
 * 
 * @author Anette Karhu
 * @version 9.6.2016
 *
 */
public class SeuraGUIController implements Initializable{
	
	@FXML private ComboBoxChooser cbKentat;
	@FXML private TextField hakuehto;
    @FXML private ScrollPane panelAsiakas;
    @FXML private GridPane gridAsiakas;
    @FXML private ListChooser chooserAsiakkaat;
    @FXML private StringGrid<Varaus> tableVaraukset;
    //@FXML private Label labelVirhe;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
	
	 /**
     * K‰sitell‰‰n uuden asiakkaan lis‰‰minen
     */
    @FXML private void handleUusiAsiakas() {
       // Dialogs.showMessageDialog("Ei osata viel‰ lis‰t‰ asiakasta");
    	uusiAsiakas();
    }
    
    @FXML private void handleAvaa() {
        avaa();
    }
    
    /**
     * K‰sitell‰‰n tallennusk‰sky
     */
    @FXML private void handleTallenna() {
        tallenna(true);
    }    
  
    /**
     * K‰sitell‰‰n lopetusk‰sky
     */
    @FXML private void handleLopeta() {
        tallenna(false);
        Platform.exit();
    }
   
    /**
     * K‰sitell‰‰n tulostaminen
     * @throws SailoException jos lis‰‰minen ei onnistu
     */
    @FXML private void handleTulosta() throws SailoException {
       	//Dialogs.showMessageDialog("Viel‰ ei osata tulostaa");
    	TulostusController tulostusCtrl = TulostusController.tulosta(null);
        tulostaValitut(tulostusCtrl.getTextArea());
     } 
    
    /**
     * K‰sitell‰‰n hakuehto 
     */
    @FXML private void handleHakuehto() {
    	//String ehto = hakuehto.getText();
    	//if ( ehto.isEmpty() ) naytaVirhe(null);
        hae(0);
    }


	/**
     * Asiakkaan poisto rekisterist‰
     */
    @FXML private void handlePoistaAsiakas() {
    	//Dialogs.showMessageDialog("Viel‰ ei osata poistaa asiakasta");
    	poistaAsiakas();
    }
    
    /**
     * Varauksen lis‰ys asiakkaalle
     * @throws SailoException jos lis‰‰minen ei onnistu
     */
    @FXML private void handleLisaaVaraus() throws SailoException {
    	//Dialogs.showMessageDialog("Viel‰ ei osata lis‰t‰ varausta");
    	lisaaVaraus();
    }
    
    /**
     * Varauksen poisto asiakkaalta
     */
    @FXML private void handlePoistaVaraus() {
    	//Dialogs.showMessageDialog("Viel‰ ei osata poistaa varausta");
    	poistaVaraus();
    }
    

	/**
     * Tietojen n‰ytt‰minen.
     */
    @FXML private void handleTietoja() {
       //Dialogs.showMessageDialog("Ei osata viel‰ tietoja");
    ModalController.showModal(SeuraGUIController.class.getResource("AboutView.fxml"), "Seura", null, "");
    }
    
    //============================================================
    private Asiakas apuasiakas = new Asiakas();
    private TextField[] edits;
    private String seurannimi = "himoliikkujat";   
    private Seura seura;
    private ListView<Asiakas> listAsiakkaat = new ListView<Asiakas>();
    private ObservableList<Asiakas> listdataAsiakkaat = FXCollections.observableArrayList();
    private EditoitavaAsiakas editoitavaAsiakas = new EditoitavaAsiakas();
    private TableView<Varaus> tableViewVaraukset = new TableView<>();
    
   // private TextArea areaAsiakas = new TextArea();
 // private static Varaus apuvaraus = new Varaus();
	//private int kentta;
    //private Asiakas asiakasKohdalla;
    
    
    /**
     * Luokka jossa on tallessa editoitavaksi otettu tai uusi asiakas
     * ja sen alkuper‰inen arvo.  Alkuper‰isest‰ tehd‰‰n aluksi klooni
     * ja kloonia muokataan.  Sitten voidaan verrata tarvittaessa ett‰
     * onko editointia tapahtunut ja tarvitaanko tallennusta.
     * Ja jos ei haluta tallennusta, niin ei tarvita mit‰‰n undo, koska
     * muokkaus on koskenut kopiota.  
     */
    private class EditoitavaAsiakas {
        private Asiakas editoitava;
        private Asiakas alkuperainen;
     
        public EditoitavaAsiakas() { editoitava = null; }
        
        /**
         * Asetetaan editoitava asiakas.  Jos on olemassa entinen editoiva
         * niin pit‰‰ tarkistaa onko se muuttunut ja pit‰‰kˆ se tallentaa.
         * @param asiakas uusi editoitava asiakas
         * @param saakoHakea saako samalla hakea listan uudelleen 
         * @return asetettu editoitava j‰sen, voi olla myˆs null
         */
        public boolean tallennaJaAseta(Asiakas asiakas, boolean saakoHakea) {
            boolean oliko = tarkistaMuutos(saakoHakea);
            
            this.alkuperainen = asiakas;
            if ( asiakas == null ) editoitava = null;
            else try {
                this.editoitava = alkuperainen.clone();
            } catch (CloneNotSupportedException e) {
               // pit‰isi tulla aina Jasen
            }
            return oliko;
        }
        
        
        private boolean tarkistaMuutos(boolean saakoHakea) {
            if ( editoitava == null ) return  false;
            if ( !muuttunut() ) return false;
            if ( !Dialogs.showQuestionDialog("Asiakkaan tiedot muuttuneet", "Tallennetaanko?", "Kyll‰", "Ei") )
               return false; 
            tallenna(saakoHakea);
            editoitava = null;
            return true;
        }
       

		public boolean tallennaJaTyhjenna(boolean saakoHakea) { return tallennaJaAseta(null, saakoHakea); } 
        private boolean muuttunut() {  return !editoitava.equals(alkuperainen);  }
        public Asiakas getEditoitava() {  return editoitava; }
        public boolean onkoKetaan() { return editoitava != null;  }
        public void poista() { editoitava = null; }
    }
    
    
    /**
     * Luokka jolla hoidellaan miten asiakas n‰ytet‰‰n listassa
     */
    public static class CellAsiakas extends ListCell<Asiakas> {
        @Override protected void updateItem(Asiakas item, boolean empty) {
            super.updateItem(item, empty); 
            setText(empty ? "" : item.getNimi());
        }
    }

    
    /**
     *Tekee tarvittavat muut alustukset, tyhjennetaan GridPanen 
     * ja luodaan tilalle uudet labelit ja editit. Varaustaulukko vaihdetaan oikeita
     * varauksia sis‰lt‰v‰ksi.
     */
    private void alusta() {
        listAsiakkaat.setItems(listdataAsiakkaat);
        gridAsiakas.getChildren().clear();
        
        edits = new TextField[apuasiakas.getKenttia()];
 
        ObservableList<String> kentat = cbKentat.getItems();
        kentat.clear();
        
        for (int i=0, k = apuasiakas.ekaKentta(); k < apuasiakas.getKenttia(); k++, i++) {
            String otsikko = apuasiakas.getKysymys(k);
            kentat.add(otsikko);
            Label label = new Label(otsikko);
            gridAsiakas.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;            
            final int kk = k;
            edit.setOnKeyReleased( e -> kasitteleMuutosAsiakkaaseen(kk,(TextField)(e.getSource())));
            gridAsiakas.add(edit, 1, i);
        }
        cbKentat.getSelectionModel().select(0);
        
        BorderPane parent = (BorderPane)chooserAsiakkaat.getParent();
        listAsiakkaat.setPrefHeight(chooserAsiakkaat.getPrefHeight());
        listAsiakkaat.setPrefWidth(chooserAsiakkaat.getPrefWidth());
        parent.setCenter(listAsiakkaat);
        listAsiakkaat.setCellFactory( p -> new CellAsiakas() );
        listAsiakkaat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ( newValue == null ) {
                if ( !editoitavaAsiakas.onkoKetaan() ) naytaAsiakas(null); 
                return;
            }
            if ( !editoitavaAsiakas.tallennaJaTyhjenna(false) ) { naytaAsiakas(newValue); return; }
            Platform.runLater( () -> { // koska listan muuttumisen aikana ei saa muuttaa listaa
                hae(0);
                naytaAsiakas(newValue);
            });
         });

        parent = (BorderPane)tableVaraukset.getParent();
        parent.setCenter(tableViewVaraukset);
        tableViewVaraukset.setPlaceholder(new Label("Ei viel‰ varauksia"));
        tableVaraukset = null;
        VarausCell.alustaVarausTable(tableViewVaraukset, (var, knro, arvo) -> {
            String virhe = var.aseta(knro, arvo);
            naytaVirhe(virhe);
            if ( virhe == null ) seura.setVarausMuutos();
            return virhe;
        });
	}
    
    
    private void naytaVirhe(String virhe) {
    	if ( virhe == null || virhe.isEmpty() ) {		
   // 		labelVirhe.setText("");
  //  		labelVirhe.getStyleClass().removeAll("virhe");
    		return;
    	}
 //   	labelVirhe.setText(virhe);
 //   	labelVirhe.getStyleClass().add("virhe");
	}
    
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
	/**
     * Alustaa seuran lukemalla sen valitun nimisest‰ tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstin‰
     */
    protected String lueTiedosto(String nimi) {
        seurannimi = nimi;
        setTitle("Seura - " +seurannimi);
        try {
            seura.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }

	/**
	 * Kysyt‰‰n tiedoston nimi ja luetaan se
	 * @return true jos onnistui, false jos ei             
	 */
	public boolean avaa() {
		  editoitavaAsiakas.tallennaJaTyhjenna(false); // jos on muokattu, niin tallennetaan ensin
	        String uusinimi = SeuranNimiController.kysyNimi(null, seurannimi);
	        if (uusinimi == null) return false;
	        lueTiedosto(uusinimi);
	        return true;
	}
    
    /**
     * Tallettaa nykyisen mahdollisesti muutetun asiakkaan ja sitten koko tiedoston
     * @param saakoHakea saako samalla hakea listan uudelleen
     * @return null jos menee hyvin, muuten virheteksti
     */
    public String tallenna(boolean saakoHakea) {
        try {
            if ( editoitavaAsiakas.onkoKetaan() ) {
                Asiakas editoitava = editoitavaAsiakas.getEditoitava();
                editoitavaAsiakas.poista();
                seura.korvaaTaiLisaa(editoitava);
                final int jnro = editoitava.getTunnusNro();
                if ( saakoHakea ) hae(jnro);
            } 
            seura.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    

    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkaa sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
    	editoitavaAsiakas.tallennaJaTyhjenna(false);
        tallenna(false);
        return true;
    }
    
    /**
     * N‰ytt‰‰ listasta valitun asiakkaan tiedot
     */
	private void naytaAsiakas(Asiakas asiakas) {
        for (int k = apuasiakas.ekaKentta(); k < apuasiakas.getKenttia(); k++) {
            String arvo = "";
            if ( asiakas != null ) arvo = asiakas.anna(k);
            TextField edit = edits[k];
            edit.setText(arvo);
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
        }
        naytaVaraukset(asiakas);
        gridAsiakas.setVisible(asiakas != null); 
        naytaVirhe(null);
    }

    
    /**
     * Hakee asiakkaan tiedot listaan
     * @param tunnusNro asiakkaan numero, joka aktivoidaan haun j‰lkeen
     */
    private void hae(int tunnusNro) {
        int jnro = tunnusNro; // jnro j‰senen numero, joka aktivoidaan haun j‰lkeen
        if ( jnro <= 0 ) {
        	Asiakas kohdalla = getAsiakasKohdalla();
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro();
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apuasiakas.ekaKentta();
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        
        naytaVirhe(null);
        
        listdataAsiakkaat.clear();
        listAsiakkaat.setItems(null);
        
        int index = 0;
        Collection<Asiakas> asiakkaat;
        try {
            asiakkaat = seura.etsi(ehto, k);
        	int i = 0;
        	for (Asiakas asiakas:asiakkaat) {
        	    if (asiakas.getTunnusNro() == jnro) index = i;
        	    listdataAsiakkaat.add(asiakas);
        	    i++;
            }
        } catch (SailoException ex) {
        	Dialogs.showMessageDialog("Asiakkaan hakemisessa ongelmia! " + ex.getMessage());
        }
        listAsiakkaat.setItems(listdataAsiakkaat);
        listAsiakkaat.getSelectionModel().select(index); 
    }
    
    
 	/**
      * K‰sitell‰‰n asiakkaaseen tullut muutos
      * @param k mit‰ kentt‰‰ muutos koskee
      * @param edit muuttunut kentt‰
      */
     protected void kasitteleMuutosAsiakkaaseen(int k, TextField edit) {
         if (!editoitavaAsiakas.onkoKetaan()) editoitavaAsiakas.tallennaJaAseta(getAsiakasKohdalla(),true);
         if (!editoitavaAsiakas.onkoKetaan()) return; // ei ole j‰sent‰ jota muokataan

         Asiakas editoitava = editoitavaAsiakas.getEditoitava();
         String s = edit.getText();
         String virhe = null;
         virhe = editoitava.aseta(k,s); 
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
      * @return listasta valittu kohdalla oleva asiakas
      */
 	private Asiakas getAsiakasKohdalla() {
 		return listAsiakkaat.getSelectionModel().getSelectedItem();
 	}
    
	   
 /**
  * Luo uuden asiakkaan jota aletaan editoimaan
  */
 private void uusiAsiakas() {
     Asiakas uusi = new Asiakas();
     uusi.rekisteroi(); 
     editoitavaAsiakas.tallennaJaAseta(uusi, true);
     listAsiakkaat.getSelectionModel().clearSelection();
     naytaAsiakas(uusi);
   /*  uusi.vastaaPekkaPouta(); 
     try {
         seura.lisaa(uusi);
     } catch (SailoException e) {
         Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
         return;
     }
     //hae(uusi.getTunnusNro());
     listdataAsiakkaat.add(uusi);
     int n = listAsiakkaat.getItems().size();
     listAsiakkaat.getSelectionModel().select(n-1); */
 }
 
    /**
     * Tekee uuden tyhj‰n varauksen editointia varten
     * @throws SailoException jos lis‰‰minen ei onnistu
     */
    public void lisaaVaraus() throws SailoException {
        // JOptionPane.showMessageDialog(null, "Viel‰ ei osata lis‰t‰ harrastusta!" );
    	Asiakas asiakasKohdalla = getAsiakasKohdalla();
        if ( asiakasKohdalla == null ) return; 
        Varaus var = new Varaus(asiakasKohdalla.getTunnusNro());
        var.rekisteroi(); 
        
     //  String kohde = "Tenniskentta";
      //  VarauksenKohde k = seura.annaTaiLisaaVarauksenKohde(kohde);        
      //  var.vastaaTenniskentta(asiakasKohdalla.getTunnusNro()); //t‰h‰n varauksenkohteen viel‰ 
        
        try {
        seura.lisaa(var); 
        } catch (SailoException e) {
        	Dialogs.showMessageDialog("Ongelmia varauksen lis‰‰misess‰ " + e.getMessage());
        }
        naytaVaraukset(asiakasKohdalla);
        VarausCell.editLast(tableViewVaraukset);
        //hae(asiakasKohdalla.getTunnusNro());         
    }
    

    /**
     * N‰ytet‰‰n varaukset taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
     * lis‰t‰‰n siihen kaikki varaukset
     * @param asiakas asiakas, jonka varaukset n‰ytet‰‰n
     */ 
    private void naytaVaraukset(Asiakas asiakas) {
      //  tableVaraukset.clear();
      //  if ( asiakas == null ) return;
    	
        ObservableList<Varaus> items = tableViewVaraukset.getItems();
        items.clear();
        if ( asiakas == null ) return;
        
        List<Varaus> varaukset; 
        try {
            varaukset = seura.annaVaraukset(asiakas);
            if ( varaukset.size() == 0 ) return;
           // for (Varaus var: varaukset)
            	items.addAll(varaukset);
                //naytaVaraus(var);
        } catch (SailoException e) {
        	naytaVirhe(e.getMessage());
        }
         
      
    }
    
	/** 
     * @param seura Seura jota k‰ytet‰‰n t‰ss‰ k‰yttˆliittym‰ss‰ 
     */ 
    public void setSeura(Seura seura){ 
        this.seura = seura; 
       // naytaAsiakas(); 
    } 
    
    
    /**
     * Poistetaan varaustaulukosta valitulla kohdalla oleva varaus. 
     */
    private void poistaVaraus() {
        int rivi = tableViewVaraukset.getFocusModel().getFocusedCell().getRow();
        if ( rivi < 0 ) return;
        Varaus varaus = VarausCell.getSelected(tableViewVaraukset);
        if ( varaus == null ) return;
        seura.poistaVaraus(varaus);
        naytaVaraukset(getAsiakasKohdalla());        
        int varauksia = tableViewVaraukset.getItems().size(); 
        if ( rivi >= varauksia ) rivi = varauksia -1;
        tableViewVaraukset.getFocusModel().focus(rivi);
        tableViewVaraukset.getSelectionModel().select(rivi);
		
	}
        

    /*
     * Poistetaan listalta valittu asiakas
     */
    private void poistaAsiakas() {
    	Asiakas asiakas = getAsiakasKohdalla();
        if ( asiakas == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko asiakas: " + asiakas.getNimi(), "Kyll‰", "Ei") )
            return;
        seura.poista(asiakas.getTunnusNro());
        int index = listAsiakkaat.getSelectionModel().getSelectedIndex();
        hae(0);
        listAsiakkaat.getSelectionModel().select(index);
    }
    
    
	/**
     * Palautetaan komponentin id:st‰ saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mik‰ arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
    
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    } */
 
        	

    
	/**
     * N‰ytt‰‰ listasta valitun asiakkaan tiedot, tilap‰isesti yhteen isoon edit-kentt‰‰n
     * @throws SailoException 
     
    private void naytaAsiakas() throws SailoException {
        asiakasKohdalla = listAsiakkaat.getSelectionModel().getSelectedItem();        
        if (asiakasKohdalla == null) return;
        
        areaAsiakas.setText("");
        //try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaAsiakas)) {
        	//asiakasKohdalla.tulosta(os);
        	//this.tulosta(os, asiakasKohdalla);
       // laitaAsiakas();
       // naytaVaraukset(apuasiakas);
        AsiakasDialogController.naytaAsiakas(edits, asiakasKohdalla);
        naytaVaraukset(asiakasKohdalla);
        	 } */
        	 
    /*
	private void laitaAsiakas() {
        if (asiakasKohdalla == null) return;
        for (int k = apuasiakas.ekaKentta(); k < apuasiakas.getKenttia(); k++) {
            edits[k].setText(asiakasKohdalla.anna(k));
        }
	} */

	

   
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstin‰ 
     
      // Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi viel‰");
    private String tallenna() { 
    	try { 
    		seura.tallenna(); 
    		return null; 
    	} catch (SailoException ex) { 
    		Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage()); 
    		return ex.getMessage(); 
    	} 
    }
    */
    


   
 

    
	/**
	 * Lis‰‰ varaukselle asiakkaan id:n mukaisen sijainnin.
	 * @param id varauksen id
	 * @throws SailoException jos lis‰‰minen ei onnistu
	 
	protected void lisaaSijainti(int id) throws SailoException {
		Varaus lisaaVaraus = new Varaus(id);
		lisaaVaraus.vastaaTenniskentta(id);
		seura.lisaa(lisaaVaraus);
	} */



    /**
     * Lis‰t‰‰n yhden varauksen tiedot taulukkoon.  
     * @param var Varaus joka n‰ytet‰‰n
     
	private void naytaVaraus(Varaus var) {
        int kenttia = var.getKenttia();
        String[] rivi = new String[kenttia-var.ekaKentta()];
        for (int i=0, k=var.ekaKentta(); k < kenttia; i++, k++)
            rivi[i] = var.anna(k);
        tableVaraukset.add(var,rivi);		
	}
*/

    
    /**
     * Tulostaa asiakkaan tiedot
     * @param os tietovirta johon tulostetaan
     * @param asiakas tulostettava asiakas
     * 
     */
    public void tulosta(PrintStream os, final Asiakas asiakas) {
        os.println("----------------------------------------------");
        asiakas.tulosta(os);
        os.println("----------------------------------------------");
      //  List<Varaus> varaukset = seura.annaVaraukset(asiakas);  
      //  for (Varaus var:varaukset)  
      //      var.tulosta(os);     
        tulostaVaraukset(os, asiakas);
    }
    
    
    private void tulostaVaraukset(PrintStream os, final Asiakas asiakas) {
        try {
            List<Varaus> varaukset = seura.annaVaraukset(asiakas);
            for (Varaus var:varaukset) 
                var.tulosta(os);     
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Varausten hakemisessa ongelmia! " + ex.getMessage());
        }     
	}

	/**
     * Tulostaa listassa olevat asiakkaat tekstialueeseen
     * @param text tulostaa
     */
    public void tulostaValitut(TextArea text) {
       try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Valitut asiakkaat");
            for (Asiakas asiakas:listdataAsiakkaat) {
                tulosta(os, asiakas);
                os.println("\n\n");
            }   
       
        
    }
    }
}


    
    
