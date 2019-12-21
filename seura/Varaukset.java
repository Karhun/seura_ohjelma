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
 * Seuran varaukset, joka osaa mm. lisätä uuden varauksen.
 * 
 * @author Anette Karhu 
 * @version 2.7.2016
 */
public class Varaukset implements Iterable<Varaus> {

    private boolean muutettu = false;
    private String tiedostonPerusNimi = "varaukset";
   private int lkm = 0;

    
    /** Taulukko varauksista */
    private final Collection<Varaus> alkiot = new ArrayList<Varaus>();
    private Varaus[] varausAlkiot = new Varaus[alkiot.size()];

    /**
     * Varauksien alustaminen
     */
    public Varaukset() {
    	//ei tee vielä mitään.
    }

    /**
     * muunnetaan varausalkiot.
     */
    public void muunna() {
    	alkiot.toArray(varausAlkiot);
    } 
    
    /**
     * Lisää uuden varauksen tietorakenteeseen ja ottaa varauksen omistukseensa.
     * @param var lisättävä varaus.  Huom tietorakenne muuttuu omistajaksi
     * @example
     * <pre name="test">
     * Varaukset varaukset2 = new Varaukset();
     * Varaus paikka1 = new Varaus(); 
     * Varaus paikka2 = new Varaus();
     * varaukset2.getLkm() === 0;
     * varaukset2.lisaa(paikka2); varaukset2.getLkm() === 1;
     * varaukset2.lisaa(paikka1); varaukset2.getLkm() === 2;
     * varaukset2.lisaa(paikka1); varaukset2.getLkm() === 3;
     * varaukset2.lisaa(paikka1); varaukset2.getLkm() === 4;
     * </pre>
     */
    public void lisaa(Varaus var) {
        alkiot.add(var);
        muutettu = true;
    }

    
    /**
     * Poistaa valitun varauksen
     * @param varaus poistettava varaus
     * @return tosi jos löytyi poistettava varaus
     * 
     */
    public boolean poista(Varaus varaus) {
    	boolean ret = alkiot.remove(varaus);
    	if (ret) muutettu = true;
    	return ret;
    }
	
    
    /**
     * Poistaa kaikki tietyn asiakkaan varaukset
     * @param asiakasNro viite siihen, minkä asiakkaan varaukset poistetaan
     * @return montako poistettiin
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * #import java.io.File; 
     *  Varaukset varaukset = new Varaukset(); 
     *  Varaus pitsi21 = new Varaus(); pitsi21.vastaaTenniskentta(2); 
     *  Varaus pitsi11 = new Varaus(); pitsi11.vastaaTenniskentta(1); 
     *  Varaus pitsi22 = new Varaus(); pitsi22.vastaaTenniskentta(2);  
     *  Varaus pitsi12 = new Varaus(); pitsi12.vastaaTenniskentta(1);  
     *  Varaus pitsi23 = new Varaus(); pitsi23.vastaaTenniskentta(2);  
     *  varaukset.lisaa(pitsi21); 
     *  varaukset.lisaa(pitsi11); 
     *  varaukset.lisaa(pitsi22); 
     *  varaukset.lisaa(pitsi12); 
     *  varaukset.lisaa(pitsi23); 
     *  varaukset.poista(2) === 3;  varaukset.getLkm() === 2; 
     *  varaukset.poista(3) === 0;  varaukset.getLkm() === 2; 
     *  List<Varaus> h = varaukset.annaVaraukset(2); 
     *  h.size() === 0;  
     *  h = varaukset.annaVaraukset(1); 
     *  h.get(0) === pitsi11; 
     *  h.get(1) === pitsi12; 
     * </pre> 
     */
    public int poista(int asiakasNro) {
    	int n = 0;
    	for (Iterator<Varaus> it = alkiot.iterator(); it.hasNext(); ) {
    		Varaus var = it.next();
    		if (var.getAsiakasNro() == asiakasNro) {
    			it.remove();
    			n++;
    		}
    	}
    	if (n > 0) muutettu = true;
    	return n;
    }
    
	
    /**
     * Lukee jäsenistön tiedostosta.  
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
    	setTiedostonPerusNimi(tied);
	    try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {	    		        
	        String rivi;
	        
	        while ( (rivi = fi.readLine()) != null ) {
	                rivi = rivi.trim();
	                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
	                Varaus var = new Varaus();
	                var.parse(rivi); 
	                lisaa(var);
	            }
	            muutettu = false;
	        } catch ( FileNotFoundException e ) {
	            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	        } catch ( IOException e ) {
	            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	        }
    }
    

	/**
	 *  Jos tiedosto annettu ju aikaisemmin, luetaan aikaisemmin annetun nimisestä tiedostosta.
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());		
	}


	
    /**
     * Tallentaa asiakkaat tiedostoon.  
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
     	if ( !muutettu) return;
    	
    	File fbak = new File(getBakNimi());
    	File ftied = new File(getTiedostonNimi());
    	fbak.delete();
    	ftied.renameTo(fbak);
    	
    	try (PrintWriter fi = new PrintWriter (new FileWriter (ftied.getCanonicalFile()))) {
    		for (Varaus var : this) {
				fi.println(var.toString());	
    		}			
    	} catch ( FileNotFoundException e) {
    		throw new SailoException (" Tiedosto " + ftied.getName() + " ei aukea");
    	} catch  (IOException ex) {
    		throw new SailoException (" Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
    	}
    	
    	muutettu = false;
    }
	

    /**
     * Palauttaa seuran mahdollisten varauksien lukumäärän
     * @return varausten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }

	/**
	 * Palauttaa tiedoston nimen jota käytetään tallennukseen
	 * @return tallennettavan tiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}
	
	
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param varaukset tallennustiedoston perusnimi
     */
	public void setTiedostonPerusNimi(String varaukset) {
		tiedostonPerusNimi = varaukset;
		
	}
	

	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}

 
    /**
	 * Palauttaa varmuuskopiotiedoston nimen.
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return "varaukset"  + ".bak";
	}


    /**
     * Iteraattori kaikkien varausten läpikäymiseen
     * @return varausiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Varaukset varaukset = new Varaukset();
     *  Varaus pitsi21 = new Varaus(2); varaukset.lisaa(pitsi21);
     *  Varaus pitsi11 = new Varaus(1); varaukset.lisaa(pitsi11);
     *  Varaus pitsi22 = new Varaus(2); varaukset.lisaa(pitsi22);
     *  Varaus pitsi12 = new Varaus(1); varaukset.lisaa(pitsi12);
     *  Varaus pitsi23 = new Varaus(2); varaukset.lisaa(pitsi23);
     * 
     *  Iterator<Varaus> i2=varaukset.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Varaus var:varaukset ) { 
     *    var.getAsiakasNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Varaus> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki asiakas varaukset
     * @param tunnusNro asiakkaan tunnusnumero jolle varauksia haetaan
     * @return tietorakenne jossa viiteet löydettyihin varauksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Varaukset varaukset = new Varaukset();
     *  Varaus pitsi21 = new Varaus(2); varaukset.lisaa(pitsi21);
     *  Varaus pitsi11 = new Varaus(1); varaukset.lisaa(pitsi11);
     *  Varaus pitsi22 = new Varaus(2); varaukset.lisaa(pitsi22);
     *  Varaus pitsi12 = new Varaus(1); varaukset.lisaa(pitsi12);
     *  Varaus pitsi23 = new Varaus(2); varaukset.lisaa(pitsi23);
     *  Varaus pitsi51 = new Varaus(5); varaukset.lisaa(pitsi51);
     *  
     *  List<Varaus> loytyneet;
     *  loytyneet = varaukset.annaVaraukset(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = varaukset.annaVaraukset(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = varaukset.annaVaraukset(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == pitsi51 === true;
     * </pre> 
     */
    public List<Varaus> annaVaraukset(int tunnusNro) {
        List<Varaus> loydetyt = new ArrayList<Varaus>();
        for (Varaus var : alkiot)
            if (var.getAsiakasNro() == tunnusNro) loydetyt.add(var);
        return loydetyt;
    }
	
    /**  
     * Laitetaan muutos, jolloin pakotetaan tallentamaan.    
     */  
    public void setMuutos() {  
        muutettu = true;  
    } 
    

 
    
    /**
     * Korvaa varauksen tietorakenteessa ja ottaa asiakkaan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva varaus, jos ei löydy,
     * niin lisätään uutena varauksena.
     * @param varaus lisättävän varauksen viite.
     * @throws SailoException jos tietorakenne on ongelmia
   */
    public void korvaaTaiLisaa(Varaus varaus) throws SailoException {
    	muunna();
    	int id = varaus.getTunnusNro();
    	for ( int i = 0; i < lkm; i++) {
    		if (varausAlkiot[i].getTunnusNro() == id) {
    			varausAlkiot[i] = varaus;
    			muutettu = true;
    			return;
    		}
    	}
    	lisaa(varaus);  	
    }  
    
    /**
	 * Testiohjelma varauksille
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
        Varaukset varaukset = new Varaukset();
        Varaus tenniskenttä = new Varaus();
        tenniskenttä.vastaaTenniskentta(2);
        Varaus sulkkis = new Varaus();
        sulkkis.vastaaTenniskentta(1);
        Varaus pesis = new Varaus();
        pesis.vastaaTenniskentta(2);
        Varaus squahs = new Varaus();
        squahs.vastaaTenniskentta(2);

        varaukset.lisaa(tenniskenttä);
        varaukset.lisaa(sulkkis);
        varaukset.lisaa(pesis);
        varaukset.lisaa(sulkkis);
        varaukset.lisaa(squahs);

        System.out.println("============= Varaukset testi =================");

        List<Varaus> varaukset2 = varaukset.annaVaraukset(1);

        for (Varaus var : varaukset2) {
            System.out.print(var.getAsiakasNro() + " ");
            var.tulosta(System.out);
        }

    

	}

}
