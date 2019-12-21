package fxSeura;
	
import javafx.application.Application;
import javafx.application.Platform;
//import javafx.application.Platform;
import javafx.stage.Stage;
import seura.Seura;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * Pääohjelma asiasrekisterin käynnistämiseksi
 * @author Anette Karhu
 * @version 2.6.2016
 *
 */
public class SeuraMain extends Application {
	@Override
	public void start(Stage primaryStage) {
	    try {
	        FXMLLoader ldr = new FXMLLoader(getClass().getResource("Asiakasrekisteri.fxml"));
	    final Pane root = (Pane)ldr.load();
	    final SeuraGUIController seuraCtrl = (SeuraGUIController)ldr.getController(); 
	        
	        final Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Seura"); 
	           
	        primaryStage.setOnCloseRequest((event) -> {
	            if ( !seuraCtrl.voikoSulkea() ) event.consume(); 
	        });
	        
	        Seura seura = new Seura();
	        seuraCtrl.setSeura(seura);
	       // seuraCtrl.avaa(); 
	        primaryStage.show();
	        Application.Parameters params = getParameters();
            if ( params.getRaw().size() > 0 )
                seuraCtrl.lueTiedosto(params.getRaw().get(0));
            else
                if ( !seuraCtrl.avaa() ) Platform.exit();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Käynnistää käyttöliittymän
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
