package fxSeura;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import seura.Varaus;

/**
 * TableView:t‰ varten kenno jolla voidaan muokata harrastusta.
 * Tieto otetaan editorista heti kun n‰pp‰in p‰‰stet‰‰n ylˆs jolloin
 * saadaan samanlainen k‰ytˆs kuin itse j‰senen editoinnissa.
 * 
 * @author vesal
 * @version 9.1.2016
 *
 */
public class VarausCell extends TableCell<Varaus, String> {
    
    /**
     * Rajapinta funktiolle, joka asettaa arvon harrastukselle 
     */
    interface VarausAsetus {
        /**
         * Asettaa arvon harrastukselle
         * @param var harrastus jolle arvo asetetaan
         * @param kenttaNro mihin kentt‰‰n
         * @param uusiarvo mik‰ on uusi arvo
         * @return null jos ok, muuten virhe
         */
        public String aseta(Varaus var, int kenttaNro, String uusiarvo);
    }

    
    /**
     * Alustetaan TableView n‰ytt‰m‰‰n varauksia.
     * @param tableVaraukset valmiiksi luotu TableView
     * @param asettaja luokka joka suorittaa asetuksen. Mik‰li t‰t‰ ei anneta
     * asettaa itse vastaavaan paikkaan.
     */
    public static void alustaVarausTable(TableView<Varaus> tableVaraukset, VarausAsetus asettaja) {
    	Varaus apuVaraus = new Varaus();
        
        int eka = apuVaraus.ekaKentta();
        int kenttia = apuVaraus.getKenttia();
        
        for (int k=eka; k<kenttia; k++) {
            TableColumn<Varaus, String> tc = new TableColumn<>(apuVaraus.getKysymys(k));

            final int kenttaNro = k;
            tc.setCellFactory(column -> new VarausCell(kenttaNro, asettaja));
            tc.setCellValueFactory((rivi) -> {
                //String s = ("0000"+rivi.getValue().anna(kenttaNro));
                //return new SimpleStringProperty(s.substring(s.length()-4));
                String s = rivi.getValue().getAvain(kenttaNro);
                if ( s.length() > 0 && s.charAt(0) == ' ') s = s.replace(' ','0'); // newChar) s = '!' + s.substring(1); 
                return new SimpleStringProperty(s);
            });

            tc.setPrefWidth(90);
            tc.setMaxWidth(300);
            
            tableVaraukset.getColumns().add(tc);
            tableVaraukset.setTableMenuButtonVisible(true);
        }
        
        ObservableList<Varaus> data = FXCollections.observableArrayList();
        tableVaraukset.setItems(data);
        tableVaraukset.setEditable(true);
    }


    /**
     * Aktivoidaan viimeisin solu muokkaamista varten
     * @param table mist‰ taulukosta
     */
    public static void editLast(TableView<Varaus> table) {
        int r = table.getItems().size()-1;
        if ( r < 0 ) return;
        @SuppressWarnings("unchecked")
        TableColumn<Varaus, String> col = (TableColumn<Varaus, String>)table.getColumns().get(0);
        table.requestFocus();
        table.getSelectionModel().select(r, col);
        // Platform.runLater(() -> table.edit(0, col));
        table.edit(r-1, col); 
        //table.edit(table.getFocusModel().getFocusedCell().getRow(), col); 
        //table.getSelectionModel().
        
    }

    
    /**
     * Palautetaan valitun rivin kohdalla oleva harrastus
     * @param table talukko josta etsit‰‰n
     * @return harrastus kohdalla
     */
    public static Varaus getSelected(TableView<Varaus> table) {
        return table.getFocusModel().getFocusedItem();
    }

    
    private TextField textField;
    private int kenttaNro;
    private VarausAsetus asettaja;

    
    /**
     * Alustetaan kentt‰
     * @param kenttaNro monettako kentt‰‰ kenno edustaa
     * @param asettaja luokka joka suottaa arvon asettamisen.
     */
    public VarausCell(int kenttaNro, VarausAsetus asettaja) {
        this.kenttaNro = kenttaNro;
        this.asettaja = asettaja;
    }


    /** 
     * Oma asetus, joka asettaa joko kutsumalla annettua asettajaa tai harrastuksen omaa
     * @param var mille harrastukselle asetetaan
     * @param knro mihin kentt‰‰n
     * @param uusiarvo mik‰ arvo
     * @return null jos ei virhett‰, muuten virhe
     */
    protected String aseta(Varaus var, int knro, String uusiarvo) {
        String arvo = "";
        if ( uusiarvo != null ) arvo = uusiarvo;
        if ( asettaja != null ) return asettaja.aseta(var, knro, arvo);
        if ( var == null ) return "Ei varausta";
        return var.aseta(kenttaNro, arvo);

    }
    
    
    /**
     * @return antaa kohdalla olevan harrastuksen
     */
    protected Varaus getObject() {
        @SuppressWarnings("unchecked")
        TableRow<Varaus> row = getTableRow();
        if ( row == null ) return null;
        Varaus var = row.getItem();
        return var;
    }


    /**
     * @return antaa kohdalla olevan harrastuksen kent‰n sis‰llˆn merkkijonona
     */
    protected String getObjectItem() {
    	Varaus var = getObject();
        if ( var == null ) return getItem();
        return var.anna(kenttaNro);
    }


    @Override
    public void startEdit() {
        if ( isEmpty()) return;
        super.startEdit();
        createTextField();
        setText(null);
        setGraphic(textField);
        textField.setText(getObjectItem());
        Platform.runLater(() -> textField.requestFocus());
        textField.selectAll();
    }


    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getObjectItem());
        setGraphic(null);
    }


    @Override
    public void updateItem(String itm, boolean empty) {
    	Varaus var = getObject();
        String item = itm;
        if ( var != null ) item = var.anna(kenttaNro);


        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setStyle("");
            setGraphic(null);
            return;
        } 
        if (isEditing()) {
            if (textField != null) textField.setText(getObjectItem());
            setText(null);
            setGraphic(textField);
            return;
        }              
        setText(getObjectItem());
        setGraphic(null);
    }


    @SuppressWarnings("unchecked")
    private void createTextField() {
        if ( textField != null ) return;
        textField = new TextField();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener( (arg0, arg1,arg2) -> {
            if (!arg2) {
                commitEdit(textField.getText());
            }
        });
        textField.setOnAction(e -> commitEdit(textField.getText()));
        textField.setOnKeyReleased(e -> {
            if ( e == null ) return;
            if ( textField == null ) return;
            if ( kenttaNro == 0 ) return;
            Varaus var = getObject();
            String s = textField.getText();
            String virhe = aseta(var, kenttaNro, s);
            if ( virhe != null ) textField.setStyle("-fx-background-color: red");
            else textField.setStyle("");
        });
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                // commitEdit(textField.getText());
                cancelEdit();
                t.consume();
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                t.consume();
            } else if (t.getCode() == KeyCode.TAB) {
                // commitEdit(textField.getText()); // ei tarvii kun talletetaan muutenkin
                cancelEdit();
                t.consume();
                // if ( getObject() != null ) return;
                TableView<Varaus> table = getTableView();
                if ( t.isShiftDown() )
                    table.getFocusModel().focusLeftCell();
                else 
                    table.getFocusModel().focusRightCell();
                
                table.edit(getTableRow().getIndex(), table.getFocusModel().getFocusedCell().getTableColumn());
               // Platform.runLater(() -> table.edit(getTableRow().getIndex(), getTableColumn())); // TODO: ei toimi???
                // startEdit();
                /*
                        TableColumn<Harrastus, String> nextColumn = getNextColumn(!t.isShiftDown());
                        if (nextColumn != null) {
                        }
                 */
            }
        });
    }




 

}

    