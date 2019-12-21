package seura;

import java.io.OutputStream;
//import java.io.PrintStream;
import java.io.PrintWriter;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.IntKentta;
import kanta.JonoKentta;
import kanta.Kentta;
import kanta.Tietue;

/**
 * Varaus joka osaa mm. itse huolehtia tunnus_nro:staan.
 *
 * @author Anette Karhu
 * @version 2.7.2016
 */
public class Varaus implements Cloneable, Tietue {
  
	private Kentta[] kentat = {
			new IntKentta("tunnusNro"),
			new JonoKentta("asiakasNro"),
			new JonoKentta("laji "),
			new JonoKentta("varauksenPvm"),
			new JonoKentta("lisatietoja "),
	};
	
/*	private int tunnusNro;
    private int asiakasNro;
    private String laji;
    private String varauksenPvm;
    private String lisatietoja;
*/	

    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan varaus.
     */
    public Varaus() {
        // Viel‰ ei tarvita mit‰‰n
    }
    
	
	 private IntKentta getAsiakasNroKentta() {
		 return (IntKentta)(kentat[0]);
	 }
	 

	    /**
	     * Alustetaan tietyn asiakkaan varaus.  
	     * @param tunnusNro asiakkaan viitenumero 
	     */
	    public Varaus(int tunnusNro) {
	    	getAsiakasNroKentta().setValue(tunnusNro);
	    }
	    
	@Override
	public int getKenttia() {
		return kentat.length;
	}

	/**
	* @param k mik‰ kentt‰ halutaan
	* @return k:s kentt‰
	*/
	public Kentta getKentta(int k) {
		return kentat[k];
	}
	
	
	@Override
	public int ekaKentta() {
		return 2;
	}
	
/*	private IntKentta getTunnusNroKentta() {
		return (IntKentta)(kentat[0]);
	} */


	/**
	 * Palauttaa k:ta asiakkaan kentt‰‰ vastaavan kysymyksen
	 * @param k kuinka mones kent‰n kysymys palautetaan
	 * @return k:netta kentt‰‰ vastaava kysymys
	 */
	@Override
	public String getKysymys(int k) {
		try {
			return kentat[k].getKysymys();
		} catch (Exception e) {
			return "?";
		}
	}

	/**
	 * @return kaikkien n‰ytett‰vien kysymysten otsikot merkkionotaulukkona
	 */
	public String[] getOtsikot() {
		int n = getKenttia() - ekaKentta();
		String[] otsikot = new String[n];
		for (int i = 0, k = ekaKentta(); i < n; i++, k++) {
			otsikot[i] = getKysymys(k);
		}
		return otsikot;
	}
	

	@Override
	public String anna(int k) {
		try {
			return kentat[k].toString();
		} catch (Exception e) {
			return "";
		}
	}
	
    /**
     * Antaa k:n kent‰n sis‰llˆn avain-merkkijonona
     * jonka perusteella voi lajitella
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     *
     * @example
     * <pre name="test">
     *   Varaus var = new Varaus();
     *   var.parse("   2   |  10  |   tennis  | 21.3.2015 | 2-3 ");
     *   var.getAvain(0) === "         2";
     *   var.getAvain(1) === "        10";
     *   var.getAvain(2) === "tennis";
     *   var.getAvain(3) === "      21.3.2015";
     *   var.getAvain(20) === "";
     * </pre>
     */
    public String getAvain(int k) {
        try {
            return kentat[k].getAvain();
        } catch (Exception ex) {
            return "";
        }
    }

	@Override
	public String aseta(int k, String jono) {
		try {
			String virhe = kentat[k].aseta(jono.trim());
			if (virhe == null && k == 0) setTunnusNro(getTunnusNro());
			if (virhe == null) return virhe;
			return getKysymys(k) + ": " + virhe;
		} catch (Exception e) {
			return "Virhe: " + e.getMessage();
		}
	}
	
	@Override
	public Tietue clone() throws CloneNotSupportedException {
			Varaus uusi;
			uusi = (Varaus) super.clone();
			uusi.kentat = kentat.clone();
			for (int k = 0; k < getKenttia(); k++)
	            uusi.kentat[k] = kentat[k].clone();
			return uusi;
	}
	
	/**
	 * Apumetodi jonka avulla saadaan t‰ytetty‰ testiarvot Varaukselle.
	 * @param id viite asiakkaaseen kenen varauksesta on kyse
	 */
	public void vastaaTenniskentta(int id) {
		//aseta(0, "" + id);
		aseta(1, "" + id);
		aseta(2, "" + "Tenniskentt‰");
		aseta(3, "" + "21.3.2016");
		aseta(4, "" + "12-13");
	}
		
    /**
     * Tulostetaan asiakkaan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
    	//tulosta(new PrintStream(os, true));
    	tulosta(new PrintWriter(os,true)); 
    }
    
	/**
	 * Tulostetaan varauksen tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	private void tulosta(PrintWriter out) {
		/*int pisin = 0;
		for (Kentta kentta : kentat) {
			if (kentta.getKysymys().length() > pisin)
				pisin = kentta.getKysymys().length();
		}
		
		for (Kentta kentta : kentat) {
			out.println(Mjonot.fmt(kentta.getKysymys(), -pisin - 1) + ": " + kentta.toString());
		} */
	    String erotin = "";
        for (int k=ekaKentta(); k<getKenttia(); k++) {
            out.print(erotin + anna(k));
            erotin = " ";
        }
        out.println();
    
	}
    
    /**
     * Antaa varaukselle seuraavan rekisterinumeron.
     * @return varauksen uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Varaus tennis = new Varaus();
     *   tennis.getTunnusNro() === 0;
     *   tennis.rekisteroi();
     *   Varaus sulkkis = new Varaus();
     *   sulkkis.rekisteroi();
     *   int n1 = tennis.getTunnusNro();
     *   int n2 = sulkkis.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
    	return setTunnusNro(seuraavaNro);
    }


    /**
     * Palautetaan varauksen oma id
     * @return varauksen id
     */
    public int getTunnusNro() {
    	//return getTunnusNroKentta().getValue();
    	return  ((IntKentta)(kentat[1])).getValue(); 
    }


    /**
     * Palautetaan kenelle asiakkaalle varaus kuuluu
     * @return asiakkaan id
     */
    public int getAsiakasNro() {
    	return getAsiakasNroKentta().getValue();
    }

    /**
     * Palauttaa varauksen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return varaus tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     * Varaus varaus = new Varaus();
     * varaus.parse(" 2 | 1 | Tenniskentt‰ | 21.3.2016 | 12-13 ");
     * varaus.toString() === "2|1|Tenniskentt‰|21.3.2016|12-13";
     * </pre>
     */
    @Override
	public String toString() {
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
     * Selvitt‰‰ varauksen tiedot | erotellusta merkkijonosta
     * Pit‰‰ huolen ett‰ seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta varauksen tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Varaus varaus = new Varaus();
     *   varaus.parse(" 2 | 1 | Tenniskentt‰ | 21.3.2016 | 12-13 ");
     *   varaus.getTunnusNro() === 2;
     *   varaus.toString().startsWith("2|1|Tenniskentt‰|21.3.2016|12-13") === true; 
     *
     *   varaus.rekisteroi();
     *   int n = varaus.getTunnusNro();
     *   varaus.parse(""+(n+20));      
     *   varaus.rekisteroi();           
     *   varaus.getTunnusNro() === n+20+1;
     *   varaus.toString() === "" + (n+20+1) + "|1|Tenniskentt‰|21.3.2016|12-13";
     *     
     * </pre>
     */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		for (int k = 0; k < getKenttia(); k++) {
		aseta(k, Mjonot.erota(sb, '|'));
		}
	      		
	}

	 /**
     * Asettaa tunnusnumeron ja samalla varmistaa ett‰
     * seuraava numero on aina suurempi kuin t‰h‰n menness‰ suurin.
     * @param nr asetettava tunnusnumero
     */
    private int setTunnusNro(int nr) {
    	IntKentta k = ((IntKentta)(kentat[0]));
    	k.setValue(nr);
    	if (nr >= seuraavaNro) seuraavaNro = nr + 1;
    	return k.getValue();
    }

	
	 @Override
	    public boolean equals(Object obj) {
	        if ( obj == null ) return false;
	        return this.toString().equals(obj.toString());
	    }
	 

	    @Override
	    public int hashCode() {
	        return getTunnusNro();
	    }


    
	/**
     * Testiohjelma Varaukselle
     * @param args ei k‰ytˆss‰
     */
	public static void main(String[] args) {
	     Varaus var = new Varaus();
	     var.vastaaTenniskentta(1);
	     var.rekisteroi();
	     var.tulosta(System.out);
	}

}
