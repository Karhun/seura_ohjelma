package seura;

import java.io.File;
//import java.io.File;
import java.util.Collection;
import java.util.List;



/**
 * Seura-luokka joka huolehtii asiakkaista. P‰‰osin kaikki metodit
 * vain "v‰litt‰j‰metodeja" j‰senistˆˆn.
 * 
 * @author Anette Karhu
 * @version 2.7.2016
 */
public class Seura {
	private Asiakkaat asiakkaat = new Asiakkaat();
	private Varaukset varaukset = new Varaukset();
	private VarauksenKohteet varauksenKohteet = new VarauksenKohteet();
	

	/**
	 * @param tunnusNro viitenumero jonka mukaan poistetaan
	 * @return montako asiakasta poistettiin
	 */
	public int poista(int tunnusNro) {
		 int ret = asiakkaat.poista(tunnusNro); 
	        varaukset.poista(tunnusNro); 
	        return ret;		
	}

	/**
	 * Poistaa varauksen.
	 * @param varaus poistettava varaus
	 */
	public void poistaVaraus(Varaus varaus) {
		varaukset.poista(varaus);
		
	}

	/**
	 * Lis‰t‰‰n uusi asiakas seuraan
	 * @param asiakas lis‰tt‰v‰ asiakas
	 * @throws SailoException jos lis‰‰minen ei onnistu
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Seura seura = new Seura();
	 * Asiakas pekka = new Asiakas(), minna = new Asiakas();
	 * seura.lisaa(pekka);
	 * seura.lisaa(minna);
	 * seura.lisaa(pekka);
	 * Collection<Asiakas> loytyneet = seura.etsi("",-1);
	 * Iterator<Asiakas> it = loytyneet.iterator();
	 * it.next() === pekka;
	 * it.next() === minna;
	 * it.next() === pekka;
	 * </pre>
	 */
	public void lisaa(Asiakas asiakas) throws SailoException {
		asiakkaat.lisaa(asiakas);
	}

	/**
	 * @param asiakas lis‰tt‰v‰n asiakkaan viite
	 * @throws SailoException jos tietorakenne on jo t‰ynn‰
	 */
	public void korvaaTaiLisaa(Asiakas asiakas) throws SailoException {
		asiakkaat.korvaaTaiLisaa(asiakas);
		
	}
    /**
     * Lis‰t‰‰n uusi varaus seuraan
     * @param var lis‰tt‰v‰ varaus 
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Varaus var) throws SailoException {
        varaukset.lisaa(var);
    }
    
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien asiakkaiden viitteet 
     * @param hakuehto hakuehto  
     * @param k etsitt‰v‰n kent‰n indeksi  
     * @return tietorakenteen lˆytyneist‰ asiakkaista 
     * @throws SailoException Jos jotakin menee v‰‰rin
     */ 
    public Collection<Asiakas> etsi(String hakuehto, int k) throws SailoException { 
        return asiakkaat.etsi(hakuehto, k); 
    } 
    
    
    /**
     * Haetaan kaikki asiakkaiden varaukset
     * @param asiakas asiakas jolle varauksia haetaan
     * @return tietorakenne jossa viiteet lˆydettyihin varauksiin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.util.*;
     * #THROWS SailoException 
     *  Seura seura = new Seura();
     *  Asiakas pekka = new Asiakas(), anni = new Asiakas(), matti = new Asiakas();
     *  pekka.rekisteroi(); anni.rekisteroi(); matti.rekisteroi();
     *  int id1 = pekka.getTunnusNro();
     *  int id2 = anni.getTunnusNro();
     *  Varaus sulkkis = new Varaus(id1); seura.lisaa(sulkkis);
     *  Varaus pesis = new Varaus(id1); seura.lisaa(pesis);
     *  Varaus squash = new Varaus(id2); seura.lisaa(squash);
     *  Varaus l‰tk‰ = new Varaus(id2); seura.lisaa(l‰tk‰);
     *  Varaus futis = new Varaus(id2); seura.lisaa(futis);
     *   #THROWS SailoException
     *  List<Varaus> loytyneet;
     *  loytyneet = seura.annaVaraukset(matti);
     *  loytyneet.size() === 0; 
     *  loytyneet = seura.annaVaraukset(pekka);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == sulkkis === true;
     *  loytyneet.get(1) == pesis === true;
     *  loytyneet = seura.annaVaraukset(anni);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == squash === true;
     * </pre> 
     */
    public List<Varaus> annaVaraukset(Asiakas asiakas) throws SailoException {
        return varaukset.annaVaraukset(asiakas.getTunnusNro());
    }
    

    /**  
     * Laitetaan varaukset muuttuneeksi, niin pakotetaan tallentamaan.  
     */  
	public void setVarausMuutos() {
		varaukset.setMuutos();  
		
	}

    /**
    * Asettaa tiedostojen perusnimet
    * @param nimi uusi nimi
    */
   public void setTiedosto(String nimi) {
       File dir = new File(nimi);
       dir.mkdirs();
       String hakemistonNimi = "";
       if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
       asiakkaat.setTiedostonPerusNimi(hakemistonNimi + "nimet");
       varaukset.setTiedostonPerusNimi(hakemistonNimi + "varaukset");
   }

    /**
     * Lis‰t‰‰n uusi varauksenKohde seuraan
     * @param varauksenKohde lis‰tt‰v‰ varauksenKohde
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(VarauksenKohde varauksenKohde) throws SailoException {
    	varauksenKohteet.lisaa(varauksenKohde);
    }
    
	/**
	 * Palauttaa seuran j‰senien m‰‰r‰n.
	 * @return asiakkaiden lukum‰‰r‰
	 
	public int getAsiakkaita() {
		return asiakkaat.getLkm();
	}

	/**
	 * Palauttaa seuran varauksien m‰‰r‰n.
	 * @return varauksien lukum‰‰r‰
	
	public int getVarauksia() {
		return varaukset.getLkm();
	}
	 */
	/**
	 * Palauttaa seuran varauksenkohteiden m‰‰r‰n.
	 * @return varauksenkohteiden lukum‰‰r‰
	 
	public int getVarauksenKohteita() {
		return asiakkaat.getLkm();
	}*/
	
    /**
     * Palauttaa i:n asiakkaan
     * @param i monesko asiakas palautetaan
     * @return viite i:teen asiakkaaseen
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     
    public Asiakas annaAsiakas(int i) throws IndexOutOfBoundsException {
        return asiakkaat.anna(i);
    } */



    /** 
     * @param id j‰senen id, jota haetaan 
     * @return j‰sen jolla on valittu id 
     */ 
    public Asiakas annaAsiakasId(int id) { 
        return asiakkaat.annaId(id); 
    } 
    
  
    
   
    /**
     * Lukee seuran tiedot tiedostosta
    // * @param nimi jota k‰ytet‰‰n lukemisessa
     * @param nimi jota k‰ytet‰‰n luettaessa
     * @throws SailoException jos lukeminen ep‰onnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Seura seura = new Seura();
     *  Asiakas pekka = new Asiakas(); pekka.vastaaPekkaPouta(); pekka.rekisteroi();
     *  Asiakas minna = new Asiakas(); minna.vastaaPekkaPouta(); minna.rekisteroi();
     *  String hakemisto = "testi";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto +"/yhteystiedot.dat");
     *  File fstied = new File(hakemisto+"/varaukset.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fstied.delete();
     *  seura.lueTiedostosta(hakemisto); 
     *  seura.lisaa(pekka);
     *  seura.lisaa(minna);
     *  seura.tallenna();
     *  seura = new Seura();
     *  seura.lueTiedostosta(hakemisto);
     *  Collection<Asiakas> kaikki = seura.etsi("",-1); 
     *  Iterator<Asiakas> it = kaikki.iterator();
     *  it.next() === pekka;
     *  it.next() === minna;
     *  it.hasNext() === true;
     *  seura.lisaa(minna);
     *  seura.tallenna();
     *  ftied.delete()  === false;
     *  File fbak = new File(hakemisto +"/varaukset.bak");
     *  fbak.delete() === false;
     *  dir.delete() === false;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException { //(String nimi) 
        asiakkaat = new Asiakkaat(); 
        varaukset = new Varaukset();
        varauksenKohteet = new VarauksenKohteet();

        setTiedosto(nimi);
        asiakkaat.lueTiedostosta();
        varaukset.lueTiedostosta();
        varauksenKohteet.lueTiedostosta();
    }


      /**
     * Tallentaa seuran tiedot tiedostoon
     * @throws SailoException jos tallentamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            asiakkaat.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        
        try {
            varaukset.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        
        try {
            varauksenKohteet.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
         
    /** 
     * @return seuran koko nimi 
     */ 
    public String getNimi() { 
        return asiakkaat.getKokoNimi(); 
    } 

    
   /**
    * kokeillaan kerho luokkaa
	* @param args ei k‰ytˆss‰
	*/
	public static void main(String args[]) {
		Seura seura = new Seura();
		
		try {
			         Asiakas pekka = new Asiakas(), anni = new Asiakas();
		             pekka.rekisteroi();
		             pekka.vastaaPekkaPouta();
		             anni.rekisteroi();
		             anni.vastaaPekkaPouta();
		 
		             seura.lisaa(pekka);
		             seura.lisaa(anni);
		 
		             int id1 = pekka.getTunnusNro();
		             int id2 = anni.getTunnusNro();
		             Varaus tennis = new Varaus(id1); tennis.vastaaTenniskentta(id1); seura.lisaa(tennis);
		             Varaus sulkkis = new Varaus(id1); sulkkis.vastaaTenniskentta(id1); seura.lisaa(sulkkis);
		             Varaus squash = new Varaus(id2); squash.vastaaTenniskentta(id2); seura.lisaa(squash);
		             Varaus pesis = new Varaus(id2); pesis.vastaaTenniskentta(id2); seura.lisaa(pesis);
		             Varaus l‰tk‰ = new Varaus(id2); l‰tk‰.vastaaTenniskentta(id2); seura.lisaa(l‰tk‰);
		             
		             System.out.println("============= Seuran testi =================");
		             Collection<Asiakas> asiakkaat = seura.etsi("", -1);
		             int i = 0;
		             for (Asiakas asiakas: asiakkaat) {
		                 System.out.println("Asiakas paikassa: " + i);
		                 asiakas.tulosta(System.out);
		                 List<Varaus> loytyneet = seura.annaVaraukset(asiakas);
		                 for (Varaus varaus : loytyneet)
		                     varaus.tulosta(System.out);
		                 i++;
		             }
		             
		} catch (SailoException ex) {
		    System.out.println(ex.getMessage());             
		             
	}
}


	/**
	 * Asetetataan tieto ett‰ asiakas muuttunut.
	
	public void setMuutosAsiakkaaseen() {
		asiakkaat.setMuutos();	
	} */


	/**
	 * Poistaa asiakkaista ja varauksista ne joilla on nro.
	 * @param asiakas poistettava asiakas
	 * @return montako asiakasta poistettiin
	 
	public int poista(Asiakas asiakas) {
        int id = asiakas.getTunnusNro();
        int ret = asiakkaat.poista(id); 
        varaukset.poista(id); 
        return ret; 		
	}
*/




	/**
	 * @param asiakas lis‰tt‰v‰n asiakkaan viite
	 * @throws SailoException jos tietorakenne jo t‰ynn‰
	 
	public void annaTaiLisaaVarauksenKohde(Asiakas asiakas) throws SailoException {
		asiakkaat.annaTaiLisaaVarauksenKohde(asiakas);
	}
*/

/**
 * @param kohde varauksenkohde
 * @return kohteen
 
public VarauksenKohde annaTaiLisaaVarauksenKohde(String kohde) {

	return null;
} */

}
