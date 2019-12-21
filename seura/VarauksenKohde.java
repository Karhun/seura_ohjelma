package seura;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.IntKentta;
import kanta.Kentta;
import kanta.JonoKentta;

/**
 * Varauksenkohde joka osaa mm. itse huolehtia tunnus_nro:staan.
 * 
 * @author Anette Karhu
 * @version 8.7.2016
 * */
public class VarauksenKohde {
    
	private Kentta kentat[] = {
			new IntKentta("tunnusNro"),
			new IntKentta ("kohdeNro"),
			new JonoKentta("laji"),
			
	};
	//private int kohdeNro;
	//private String laji;
	
	private static int seuraavaNro = 1;
	
    /**
     * Luo uuden varauksenkohteen.
     */
    public VarauksenKohde() {
        // Viel� ei tarvita mit��n
    }
    
    
    /**
     * Alustetaan tietty VarauksenKohde
     * @param string VarauksenKohteen viitenumero
     
    public  VarauksenKohde(String string) {
    	 getVarausNroKentta().setValue(string);
    }
    */
    /**
     * @return ensimm�isen sijoitettavan kent�n indeksi
     */
    public int ekaKentta() {
    	return 2;
    }
    
    /**
     * 
     * @return tunnusnumeron arvo
     */
    private IntKentta getTunnusNroKentta() {
    	return (IntKentta)(kentat[0]);
    }
    

	/**
	 * @return varausnumeron arvo
	 */
	private IntKentta getVarausNroKentta() {
		return (IntKentta)(kentat[1]);
	}
    
	/**
	 * @return kaikkien kenttien lukum��r�
	 * 
	 */
	public int getKenttia() {
		return 3;
	}
	
	/**
	 * Antaa k:n kent�n sis�ll�n avain-merkkijonona
	 * jonka perusteella voi lajitella	 
	 * @param k monenko kent�n sis�lt� palautetaan
	 * @return kent�n sis�lt� merkkijonona
	 */
	public String getAvain(int k) {
		try {
			return kentat[k].getAvain();
		 } catch (Exception e) {
			 return "";
		}
	}
		
	
    /**
     * Apumetodi jonka avulla saadaan t�ytetty� testiarvot VarauksenKohteelle.
     * @param nro viite varaukseen mist� varauksenkohteesta on kyse
     */
	public void vastaaTenniskentta(int nro) {
		//kohdeNro = nro;
		//laji = "Tenniskentt�";
		aseta(0, "2");
		aseta(2, "Tennis");
		aseta(1, "" + nro);
		aseta(0, "1");
	}
	

	/**
	 * Asettaa merkkijonon tiettyyn kentt��n
	 * @param i kent�n numero
	 * @param string kent�n tiedot
	 * @return merkkijono kentiss�
	 */
	public String aseta(int i, String string) {
		try {
			String virhe = kentat[i].aseta(string.trim());
			if (virhe == null && i == 0) setKohdeNro(getTunnusNro());
			if (virhe == null) return virhe;
			return getKysymys(i) + ": " + virhe;
		} catch (Exception e) {
			return "Virhe: " + e.getMessage();
		}
	}


	/**
	 * 
	 * @param i mink� kentan kysymys halutaan
	 * @return  valitun kent�n kysymysteksti
	 */
	private String getKysymys(int i) {
		try {
			return kentat[i].getKysymys();
		} catch (Exception e) {
			return "?";
		}
	}


	/**
	 * Tulostetaan varauksen tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		String erotin = "";
		for (int k = ekaKentta(); k < getKenttia(); k++) {
			out.println(erotin + anna(k));
			 erotin = " "; 
		}
		out.println(); 
	}

	/**
	 * Annetaan haluttu kent�n arvo
	 * @param k kent�n indeksi
	 * @return kent�n arvo
	 */
    private String anna(int k) {
		try {
			return kentat[k].toString();
		}catch (Exception e) {
			return "";
		}
	}

    /**
     * Tulostetaan varauksenkohteen tiedot
     * @param os tietovirta mihin tulostetaan
     */
    public void tulosta(OutputStream os) {
    	tulosta(new PrintStream(os));
    }

	
	/**
	 * Asettaa varauksenkohteelle seuraavan rekisterinumeron.
	 * @return varauksenkohteen id
	 * 	 * @example
	 * <pre name="test">
	 * VarauksenKohde varkoh = new VarauksenKohde();
	 * varkoh.getTunnusNro() === 0;
	 * varkoh.numeroi();
	 * VarauksenKohde varkoh2 = new VarauksenKohde();
	 * varkoh2.numeroi();
	 * int n1 = varkoh.getTunnusNro();
	 * int n2 = varkoh2.getTunnusNro();
	 * n1 === n2-1
	 */
	public int numeroi() {
		return setKohdeNro(seuraavaNro);
	}
	
	/**
	 * Asettaa kohdenumeron ja varmistaa ett� seuraava id on suurempi kuin aiemmat.
	 * @param id varauksenkohteen id
	 * @return asetettu kohdenumero.
	 * 
	 */
	private int setKohdeNro(int id) {
		IntKentta k = ((IntKentta)(kentat[0]));
		k.setValue(id);
		if (id >= seuraavaNro) seuraavaNro = id + 1;
		return k.getValue();
	}


	/**
	 * Muutetaan varauksenkohteen tiedot merkkijonoksi:
	 * @return varauksen kohde tolppaeroteltuna merkkijonona
	 * "kohdeNro|laji|, esimerkiksi
	 * "4|Tenniskentt�"
	 */
	@Override
	public String toString() {
		//return kohdeNro + "|" + laji ;
		StringBuffer sb = new StringBuffer("");
		String erotin = "";
		for (int k = 0; k < getKenttia(); k++) {
			sb.append(erotin);
			sb.append(anna(k));
			erotin = "|";
		}
		return sb.toString();
	}
	
	
	/**
	 * Luetaan varauksenkohteen tiedot merkeill� "|" erotellusta
	 * merkkijonosta. 
	 * @param rivi luettava rivi
	 */
	public void parse(String rivi) {
		//String[] osat = rivi.split("\\|");
		//kohdeNro = Integer.parseInt(osat[0].trim());
		//if (kohdeNro >= seuraavaNro) seuraavaNro = kohdeNro +1;
		//laji = osat[1].trim();
		StringBuffer sb = new StringBuffer(rivi);
		for (int k = 0; k < getKenttia(); k++) {
			aseta(k, Mjonot.erota(sb, '|'));
		}
	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return this.toString().equals(obj.toString());
	}
	
	@Override
	public int hashCode() {
		return getTunnusNro();
	}
	

	/**
     * 
	 * @return kohdeNro
	 */
	public int getVarausNro() {
		return getVarausNroKentta().getValue();
	}

	/**
	 * Palautetaan varauksenkohteen oma tunnusNro
	 * @return varauksenkohteen tunnusNro
	 */
	public int getTunnusNro() {
        return getTunnusNroKentta().getValue();
	}

	
	/**
	 * P��ohjelma varauksenkohteelle
	 * @param args ei k�yt�ss�
	 */
	public static void main(String[] args) {
		VarauksenKohde tenniskentta = new VarauksenKohde();
		tenniskentta.vastaaTenniskentta(1);
		tenniskentta.tulosta(System.out);

	}

}


