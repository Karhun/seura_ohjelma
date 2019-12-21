package seura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Seuran varauksenkohteet, joka osaa mm. lisätä uuden varauksenkohteen
 * 
 * @author Anette Karhu
 * @version 8.7.2016
 *
 */
public class VarauksenKohteet implements Iterable<VarauksenKohde>{

	private boolean muutettu = false;
	private String tiedostonPerusNimi = "varauksenkohteet";
	
	private final Collection<VarauksenKohde> alkiot = new ArrayList<VarauksenKohde>();
	  
	 /**
     * Varaustenkohteiden alustaminen
     */
    public VarauksenKohteet() {
        // toistaiseksi ei tarvitse tehdä mitään
    }
    
	/**
	 * Ottaa uuden varauksenkohteen tietorakenteeseen.
	 * @param varauksenKohde lisättävä varauksenkohde
	 */
	public void lisaa(VarauksenKohde varauksenKohde) {
		alkiot.add(varauksenKohde);
		muutettu = true;
	}
    
	
	/**
	 * Lukee listat oletustiedostosta
	 * @throws SailoException jos joidenkin listojen lisääminen
	 * ei onnistu
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}
	
	
	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallentamiseen
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}

	/**
	 * Lukee varauksenkohteet tiedostosta
	 * @param nimi luettavan tiedoston nimi
	 * @throws SailoException jos joidenkin varauksenkohteiden lisääminen ei onnistu
 	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #import java.io.File;
	 * VarauksenKohteet varkohteet = new VarauksenKohteet();  
	 * VarauksenKohde varkoh = new VarauksenKohde();  ;
	 *  String tiedNimi = "testi";
	 *  File ftied = new File(tiedNimi+".dat");
	 *  ftied.delete();
	 * varkohteet.lisaa(varkoh);
	 * varkohteet.tallenna();
	 */
	public void lueTiedostosta(String nimi) throws SailoException {
		setTiedostonPerusNimi(nimi);
		try (BufferedReader br = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi;
	        while((rivi = br.readLine()) != null ) {
	              rivi = rivi.trim();
	              if ("".equals(rivi) || rivi.charAt(0)  == ';') continue;
	              VarauksenKohde varauksenKohde = new VarauksenKohde();
	              varauksenKohde.parse(rivi);
	              lisaa(varauksenKohde);
	       }
	       muutettu = false;
	       } catch ( FileNotFoundException e ) {
	                throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea" );
	       } catch (IOException e ) {
	                throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	       }
	}

		/**
		 * Asettaa tiedoston perusnimen
		 * @param tied tallennustiedoston perusnimi
		 */
		public void setTiedostonPerusNimi(String tied) {
			tiedostonPerusNimi = tied;
		}
		
		/**
		 * Laitetaan muutos, jolloin pakotetaan tallentamaan.
		 */
		public void setMuutos() {
			muutettu = true;
		}
		 
	/**
	 *  Poistaa kaikki tietyn asiakkaan tilaukset
	 * @param asiakasNro  viite siihen, minkä asiakkaan tilaukset poistetaan
	 * @return montako poistettiin
	 */
	public int poista(int asiakasNro) {
		int n = 0;
		for (Iterator<VarauksenKohde> it = alkiot.iterator(); it.hasNext(); ) {
			VarauksenKohde til = it.next();
			if (til.getVarausNro() == asiakasNro) {
				it.remove();
				n++;
			}
		}
        if (n > 0) muutettu = true;
        return n;
	}
	
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
          return tiedostonPerusNimi + ".dat";
       }
    
	/**
	 * Palauttaa varakopiotiedoston nimen.
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}
	
	/**
	 * Tallentaa varauksenkohteet tiedostoon. 
	 * @throws SailoException jos tallennus epäonnistuu
	 */
	public void tallenna() throws SailoException {
        if ( !muutettu ) return; 
 		 
 		        File fbak = new File(getBakNimi()); 
 		        File ftied = new File(getTiedostonNimi()); 
 		        fbak.delete(); 
 		        ftied.renameTo(fbak);
 		 
 		        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) { 
 		          
 		            for (VarauksenKohde varauksenKohde : this) { 
 		                fo.println(varauksenKohde.toString()); 
 		            } 
 		        } catch ( FileNotFoundException ex ) { 
 		            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea"); 
 		        } catch ( IOException ex ) { 
 		            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia"); 
 		        } 
 		 
 		        muutettu = false; 
 		    } 
    
	/**
	 * Haetaan kaikki varauksenkohteet
	 * @param tunnusNro asiakkaan viitenumero jolle varauksia haetaan
     * @return tietorakenne jossa on viitteet löydettyihin varauksiin
	 */
	public List<VarauksenKohde> annaVarauksenKohteet(int tunnusNro) {
		List<VarauksenKohde> loydetyt = new ArrayList<VarauksenKohde>();
		for (VarauksenKohde varauksenKohde : alkiot) {
			if (varauksenKohde.getVarausNro() == tunnusNro) loydetyt.add(varauksenKohde);
		}
		return loydetyt;
	}
	
	/**Poistaa valitun varauksenKohteen
	 * @param varauksenKohde poistettava varauksenKohde
	 * @return tosi jos löytyi poistettava varauksenKohde
	 */
	public boolean poista(VarauksenKohde varauksenKohde) {
		boolean ret = alkiot.remove(varauksenKohde);
		if (ret) muutettu = true;
		return ret;
		
	}
	
	/**
	 * Iteraattori VarauksenKohteiden läpikäymiseen
	 * @return VarauksenKohdeiteraattori
	 */
    @Override
	public Iterator<VarauksenKohde> iterator() {
    	return alkiot.iterator();
    }
    
    /**
     * Kokeillaan varauksenkohteet-luokkaa
     * @param args ei käytössä
     */
	public static void main(String[] args) {
		VarauksenKohteet varauksenKohteet = new VarauksenKohteet();
		VarauksenKohde tenniskentta1 = new VarauksenKohde();
		tenniskentta1.vastaaTenniskentta(1);
						
		VarauksenKohde tenniskentta2 = new VarauksenKohde();
		tenniskentta2.vastaaTenniskentta(2);
		
		VarauksenKohde tenniskentta3 = new VarauksenKohde();
		tenniskentta3.vastaaTenniskentta(3);
		
		VarauksenKohde tenniskentta4 = new VarauksenKohde();
		tenniskentta4.vastaaTenniskentta(4);
		
		varauksenKohteet.lisaa(tenniskentta1);
		varauksenKohteet.lisaa(tenniskentta2);
		varauksenKohteet.lisaa(tenniskentta3);
		varauksenKohteet.lisaa(tenniskentta4);
		
		System.out.println("============= Suhteet testi =================");
			
		List<VarauksenKohde> varauksenKohteet2 = varauksenKohteet.annaVarauksenKohteet(1);
		for (VarauksenKohde varauksenKohde : varauksenKohteet2) {
			System.out.println(varauksenKohde.getTunnusNro() + " ");
			varauksenKohde.tulosta(System.out);
		}
	}

}
