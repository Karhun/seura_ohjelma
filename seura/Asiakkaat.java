package seura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Seuran asiakkaat joka osaa mm. lisätä uuden asiakkaan.
 * 
 * @author Anette Karhu
 * @version 2.7.2016
 */
public class Asiakkaat implements Iterable<Asiakas> {
	
	private int MAX_ASIAKKAITA = Integer.MAX_VALUE;
    private boolean muutettu = false; 
	private int lkm = 0;
	private Asiakas[] alkiot = new Asiakas[5];
	private String tiedostonPerusNimi = "Yhteystiedot"; 
	private String kokoNimi = "";

	/**
	 * Alustaa asiakkaat
	 */
	public Asiakkaat() {
		// On vain olemassa.
	}
	
	/**
	 * Muodostaja jolle maksimikoko voidaan asettaa
	 * @param koko asiakkaiden maxkoko
	 */
	public Asiakkaat(int koko) {
		MAX_ASIAKKAITA = koko;
		alkiot = new Asiakas[koko];
	}
	
    /**
       * Lisää uuden asiakkaan tietorakenteeseen.  Ottaa asiakkaan omistukseensa.
       * @param asiakas lisättävän asiakkaan viite.  Huom tietorakenne muuttuu omistajaksi
       * @throws SailoException jos tietorakenne on jo täynnä
       * @example
       * <pre name="test">
       * #THROWS SailoException 
       * Asiakkaat asiakkaat = new Asiakkaat();
       * Asiakas pekka = new Asiakas(), anni = new Asiakas();
       * asiakkaat.getLkm() === 0;
       * asiakkaat.lisaa(pekka); asiakkaat.getLkm() === 1;
       * asiakkaat.lisaa(anni); asiakkaat.getLkm() === 2;
       * asiakkaat.lisaa(pekka); asiakkaat.getLkm() === 3;
       * asiakkaat.anna(0) === pekka;
       * asiakkaat.anna(1) === anni;
       * asiakkaat.anna(2) === pekka;
       * asiakkaat.anna(1) == pekka === false;
       * asiakkaat.anna(1) == anni === true;
       * asiakkaat.anna(3) === pekka; #THROWS IndexOutOfBoundsException 
       * asiakkaat.lisaa(pekka); asiakkaat.getLkm() === 4;
       * asiakkaat.lisaa(pekka); asiakkaat.getLkm() === 5;
       * asiakkaat.lisaa(pekka);  
       * </pre>
       */
	public void lisaa(Asiakas asiakas) throws SailoException {
		if( lkm >= MAX_ASIAKKAITA ) throw new SailoException("Liikaa alkioita");
		if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
		alkiot[lkm] = asiakas;
		lkm++;
		muutettu = true;
	}
	

	/**
	 * poistaa asiakkaan jolla on valittu tunnusnumero 
	 * @param id  poistettavan asiakkaan id.
	 * @return 1 jos poistettiin, 0 jos ei löydy 
	 */
	public int poista(int id) {
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1;
	}
	

	/**
	 * @param asiakas lisättävän asiakkaan viite.
	 * @throws SailoException  jos tietorakenne on jo täynnä
	 */
	public void korvaaTaiLisaa(Asiakas asiakas) throws SailoException {
	       int id = asiakas.getTunnusNro();
	        for (int i = 0; i < lkm; i++) {
	            if ( alkiot[i].getTunnusNro() == id ) {
	                alkiot[i] = asiakas;
	                muutettu = true;
	                return;
	            }
	        }
	        lisaa(asiakas);
	} 
	
	
	/**
	 * Palauttaa viitteen i:nteen asiakkaaseen.
	 * @param i monnenneko asiakkaan viite halutaan
	 * @return viite asiakkaaseen, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei sallitulla alueella
	 */
	protected Asiakas anna(int i) throws IndexOutOfBoundsException {
		if ( i < 0 || lkm <= i ) throw new IndexOutOfBoundsException ("Laiton indeksi: " + i);
		return alkiot[i];
		
	}
	
	
   /**
   * Lukee asiakkaat tiedostosta.
   * @param tied tiedoston perusnimi
   * @throws SailoException jos lukeminen epäonnistuu
   *
   * @example
   * <pre name="test">
   * #THROWS SailoException 
   * #import java.io.File;
   * 
   *  Asiakkaat asiakkaat = new Asiakkaat();
   *  Asiakas pekka = new Asiakas(), minna = new Asiakas();
   *  pekka.vastaaPekkaPouta();
   *  minna.vastaaPekkaPouta();
   *  String hakemisto = "testikelmit";
   *  String tiedNimi = hakemisto+"/nimet";
   *  File ftied = new File(tiedNimi+".dat");
   *  File dir = new File(hakemisto);
   *  dir.mkdir();
   *  ftied.delete();
   *  asiakkaat.lueTiedostosta(tiedNimi); #THROWS SailoException
   *  asiakkaat.lisaa(pekka);
   *  asiakkaat.lisaa(minna);
   *  asiakkaat.tallenna();
   *  asiakkaat = new Asiakkaat();            // Poistetaan vanhat luomalla uusi
   *  asiakkaat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
   *  Iterator<Asiakas> i = asiakkaat.iterator();
   *  i.next() === pekka;
   *  i.next() === minna;
   *  i.hasNext() === false;
   *  asiakkaat.lisaa(minna);
   *  asiakkaat.tallenna();
   *  ftied.delete() === true;
   *  File fbak = new File(tiedNimi+".bak");
   *  fbak.delete() === true;
   *  dir.delete() === true;
   * </pre>
   */
  public void lueTiedostosta(String tied) throws SailoException {
  	setTiedostonPerusNimi(tied);
	    try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
	    	kokoNimi = fi.readLine();
	        if ( kokoNimi == null ) throw new SailoException("Seuran nimi puuttuu");
	        String rivi = fi.readLine();
	        if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
	            	while ( (rivi = fi.readLine()) != null ) {
	                rivi = rivi.trim();
	                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
	                Asiakas asiakas = new Asiakas();
	                asiakas.parse(rivi); 
	                lisaa(asiakas);
	            }
	            muutettu = false;
	        } catch ( FileNotFoundException e ) {
	            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	        } catch ( IOException e ) {
	            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	        }
	    }
	

	/**
	 * Luetaan aikaisemmin annetun nimisestä tiedostosta.
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
        if ( !muutettu ) return; 
 		 
 		        File fbak = new File(getBakNimi()); 
 		        File ftied = new File(getTiedostonNimi()); 
 		        fbak.delete(); 
 		        ftied.renameTo(fbak);
 		 
 		        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) { 
 		        	fo.println(getKokoNimi());
 		        	fo.println(alkiot.length);
 		            for (Asiakas asiakas : this) { 
 		                fo.println(asiakas.toString()); 
 		            } 
 		        } catch ( FileNotFoundException ex ) { 
 		            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea"); 
 		        } catch ( IOException ex ) { 
 		            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia"); 
 		        } 
 		 
 		        muutettu = false; 
 		    } 
	
	  /**
     * Palauttaa seuran koko nimen
     * @return seuran koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
	/**
	 * Palauttaa seuran asiakkaiden lukumäärän.
	 * @return asiakkaiden lukumäärä
	 */
	public int getLkm() {
		return lkm;
	}
	  
	
	/**
	 * Palauttaa tiedoston nimen jota käytetään tallennukseen
	 * @return tallennettavan tiedoston nimi
	 */
	private String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}

	
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
	public void setTiedostonPerusNimi(String nimi) {
		tiedostonPerusNimi = nimi;
		
	}
	
	
	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return getTiedostonPerusNimi() + ".dat";
	}

	
	/**
	 * Palauttaa varakopiotiedoston nimen.
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}
	

    /**
     * Luokka asiakkaiden iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Asiakkaat asiakkaat = new Asiakkaat();
     * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas();
     * aku1.rekisteroi(); aku2.rekisteroi();
     *
     * asiakkaat.lisaa(aku1); 
     * asiakkaat.lisaa(aku2); 
     * asiakkaat.lisaa(aku1); 
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Asiakas asiakas:asiakkaat)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+asiakas.getTunnusNro());           
     * 
     * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Asiakas>  i=asiakkaat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Asiakas asiakas = i.next();
     *   ids.append(" "+asiakas.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Asiakas>  i=asiakkaat.iterator();
     * i.next() == aku1  === true;
     * i.next() == aku2  === true;
     * i.next() == aku1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class AsiakkaatIterator implements Iterator<Asiakas> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa asiakasta
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä asiakkaita
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }

        /**
         * Annetaan seuraava asiakas
         * @return seuraava asiakas
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Asiakas next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
        

    /**
     * Palautetaan iteraattori asiakkaista.
     * @return asiakas iteraattori
     */
    @Override
    public Iterator<Asiakas> iterator() {
        return new AsiakkaatIterator();
    }


    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien asiakkaiden viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä asiakkaista 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Asiakkaat asiakkaat = new Asiakkaat(); 
     *   Asiakas jasen1 = new Asiakas(); jasen1.parse("1|Ankka Aku|030201-115H|Ankkakuja 6|"); 
     *   Asiakas jasen2 = new Asiakas(); jasen2.parse("2|Ankka Tupu||030552-123B|"); 
     *   Asiakas jasen3 = new Asiakas(); jasen3.parse("3|Susi Sepe|121237-121V||131313|Perämetsä"); 
     *   Asiakas jasen4 = new Asiakas(); jasen4.parse("4|Ankka Iines|030245-115V|Ankkakuja 9"); 
     *   Asiakas jasen5 = new Asiakas(); jasen5.parse("5|Ankka Roope|091007-408U|Ankkakuja 12"); 
     *   asiakkaat.lisaa(jasen1); asiakkaat.lisaa(jasen2); asiakkaat.lisaa(jasen3); asiakkaat.lisaa(jasen4); asiakkaat.lisaa(jasen5);
     * </pre> 
     */ 
    public Collection<Asiakas> etsi(String hakuehto, int k) { 
        String ehto = "*";
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;
        int hk = k;
        if ( k < 0 ) hk = 1;
        List<Asiakas> loytyneet = new ArrayList<Asiakas>(); 
        for (Asiakas asiakas : this) { 
            if (WildChars.onkoSamat(asiakas.anna(hk), ehto)) 
            	loytyneet.add(asiakas);  
        } 
        Collections.sort(loytyneet, new Asiakas.Vertailija(k));
        return loytyneet; 
    }
        /**
         * @param id tunnusnumero, jonka mukaan etsitään 
         * @return  asiakas jolla etsittävä id tai null 
         */
        public Asiakas annaId(int id) { 
            for (Asiakas asiakas : this) { 
                if (id == asiakas.getTunnusNro()) return asiakas; 
            } 
            return null; 
        } 

    	
        /** 
         * Etsii asiakkaan id:n perusteella 
         * @param id tunnusnumero, jonka mukaan etsitään 
         * @return löytyneen asiakkaan indeksi tai -1 jos ei löydy 
         * <pre name="test"> 
         * #THROWS SailoException  
         * Asiakkaat asiakkaat = new Asiakkaat(); 
         * Asiakas aku1 = new Asiakas(), aku2 = new Asiakas(), aku3 = new Asiakas(); 
         * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
         * int id1 = aku1.getTunnusNro(); 
         * asiakkaat.lisaa(aku1); asiakkaat.lisaa(aku2); asiakkaat.lisaa(aku3); 
         * asiakkaat.etsiId(id1+1) === 1; 
         * asiakkaat.etsiId(id1+2) === 2; 
         * </pre> 
         */ 
        public int etsiId(int id) { 
            for (int i = 0; i < lkm; i++) 
                if (id == alkiot[i].getTunnusNro()) return i; 
            return -1; 
        }


	/**
	 * Kokeiluohjelma asiakkaille
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Asiakkaat asiakkaat = new Asiakkaat();

		Asiakas pekka = new Asiakas();
		Asiakas anni = new Asiakas();
		pekka.rekisteroi();
		pekka.vastaaPekkaPouta();
		
		anni.rekisteroi();
		anni.vastaaPekkaPouta();
		
		try {
			asiakkaat.lisaa(pekka);
			asiakkaat.lisaa(anni);
			
			System.out.println("================= Jäsenet testi =============");
			
		/*	for (int i =0; i < asiakkaat.getLkm(); i++) {
				Asiakas asiakas = asiakkaat.anna(i);
				System.out.println("Asiakas nro: " + i);
				asiakas.tulosta(System.out); */
		    int i = 0;
            for (Asiakas asiakas: asiakkaat) {
                System.out.println("Asiakas nro: " + i++);
                asiakas.tulosta(System.out);
			}
			
		} catch (SailoException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * muutos, jonka jälkeen pakotetaan tallentamaan.
	
	public void setMuutos() {
		muutettu = true;
	}
 */


	/**
	 * @param asiakas lisättävän asiakkaan viite
	 * @throws SailoException jos tietorakenne jo täynnä
	 
	public void annaTaiLisaaVarauksenKohde(Asiakas asiakas) throws SailoException {
		int id= asiakas.getTunnusNro();
		for (int i=0 ; i< lkm; i++) {
			if (alkiot[i].getTunnusNro()== id) {
				alkiot[i] = asiakas;
				muutettu = true;
				return;
			} 
		}
		lisaa(asiakas);
	}
	*/

}