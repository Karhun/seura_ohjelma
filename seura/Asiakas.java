package seura;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.IntKentta;
import kanta.JonoKentta;
import kanta.Kentta;
import kanta.Tietue;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

//import static kanta.HetuTarkistus.*;

/**
 * Seuran asiakas joka osaa huolehtia tunnusNRo:staan.
 * 
 * @author Anette Karhu
 * @version 2.7.2016
 *
 */
public class Asiakas implements Cloneable, Tietue {

	private Kentta[] kentat = {
			new IntKentta("id"),
			new JonoKentta("nimi"),
			new JonoKentta("henkilotunnus "),
			new JonoKentta("osoite"),
			new JonoKentta("postinumero "),
			new JonoKentta("kaupunki")
	};
	
	private static int seuraavaNro = 1;


	/**
	 * @author Anette
	 * @version 28.7.2016
	 * Luokka joka vertaa kahta asiakasta keskenään
	 */
	public static class Vertailija implements Comparator<Asiakas> {

		private final int kenttanro;
		
		/**
		 * Alustetaan vertailija vertailemaan tietyn kentän perusteella
		 * @param k vertailtavan kentän indeksi
		 */
		public Vertailija(int k) {
			this.kenttanro = k;
		}
		
		/**
		 * Verrataan kahta asiakasta keskenän
		 * @param a1 eka verrattava asiakas
		 * @param a2 toinen verrattava asiakas
		 * @return < 0 jos a1 < a2, == 0 jos a1 == a2 ja muuten >0
		 */
		@Override
		public int compare(Asiakas a1, Asiakas a2) {
			String s1 = a1.getAvain(kenttanro);
			String s2 = a2.getAvain(kenttanro);
			return s1.compareTo(s2);
		}
	}

	/**
	 * Pääohjelma asiakas-luokalle
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
	    Asiakas pekka = new Asiakas(), minna = new Asiakas();
	    pekka.rekisteroi();
	    minna.rekisteroi();
        
        pekka.vastaaPekkaPouta();
        pekka.tulosta(System.out);

        minna.vastaaPekkaPouta();
        minna.tulosta(System.out);

        minna.vastaaPekkaPouta();
        minna.tulosta(System.out);
	}
	
	/**
	 * Alustetaan asiakkaan merkkijono-attribuutti tyhjiksi
	 * joinoiksi ja tunnusnro = 0.
	 */
	public Asiakas() {
		// On toistaiseksi vain olemassa
	}

	/**
	 * Rekisteröi asiakkaan rekisterinumeron
	 * @return asiakkaan uusi tunnusNro
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas1 = new Asiakas();
	 * asiakas1.getTunnusNro() === 0;
	 * asiakas1.rekisteroi();
	 * Asiakas asiakas2 = new Asiakas();
	 * asiakas2.rekisteroi();
	 * int n1 = asiakas1.getTunnusNro();
	 * int n2 = asiakas2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
	public int rekisteroi() {
		return setTunnusNro(seuraavaNro);
	}
	
	/**
	 * Palauttaa asiakkaan kenttien lukumäärän
	 * @return kenttien lukumäärä
	 */
	@Override
	public int getKenttia() {
	//	return 6;
		return kentat.length;
	}
	
	/**
	 * Eka kenttä joka on miellekäs kysyttäväksi
	 * @return ekan kentän indeksi
	 */
	@Override
	public int ekaKentta() {
		return 1;
	}
	
	/**
	 * @return jäsenen nimi
	 */
	public String getNimi() {
		return anna(1);
	}

	/**
	 * Alustaa esimerkin laittamalla  tiedot kenttiin
	 */
	public void vastaaPekkaPouta() {

		aseta(1, "Pekka Pouta "); // + rand(1, 20)
		aseta(2, "123-12345");
		aseta(3, "Liikemiehentie 6");
		aseta(4, "12345");
		aseta(5, "Viheri");
	
	}
	
	/**
	 * Tulostetaan asiakkaan tiedot
	 * @param os tietovirta joka tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Tulostaa asiakkaan tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		int pisin = 0;
		for (Kentta kentta : kentat) {
			if (kentta.getKysymys().length() > pisin)
				pisin = kentta.getKysymys().length();
		}
		
		for (Kentta kentta : kentat) {
			out.println(Mjonot.fmt(kentta.getKysymys(), -pisin - 1) + ": " + kentta.toString());
		}
	}

	/**
	 * Palauttaa asiakkaan tunnusNron
	 * @return asiakkaan tunnusNro
	 */
	public int getTunnusNro() {
		return ((IntKentta)(kentat[0])).getValue();
	}
	
	/**
	 * 
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin aiempi suurin luku.
	 * @param nr asetettava tunnusnumero
	 */
	private int setTunnusNro(int nr) {
		IntKentta k = ((IntKentta) (kentat[0]));
		k.setValue(nr);
		if (nr >= seuraavaNro) seuraavaNro = nr + 1;
		return k.getValue();
	}
	
	/**
	 * Palauttaa asiakkaan tiedot merkkijonona, jota voidaan tallettaa tiedostoon.
	 * @return asiakas tolppaerotettuna merkkijonona
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas = new Asiakas();
	 * asiakas.parse("3|Pouta Pekka|030201-111C|Liikemiehentie 6|Viheri");
	 * asiakas.toString() === "3|Pouta Pekka|030201-111C|Liikemiehentie 6|Viheri|";
	 * </pre>
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		String erotin = "";
		for (int k = 0; k < getKenttia(); k++ ) {
			sb.append(erotin);
			sb.append(anna(k));
			erotin = "|";
		}
		return sb.toString();
	}
	
	/**
	 * Selvittää asiakkaan tiedot paaluerotetusta merkkijonosta ja pitää
	 * huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
	 * @param rivi josta asiakkaan tiedot otetaan
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas = new Asiakas();
	 * asiakas.parse("1|Pouta Pekka|030201-111C");
	 * asiakas.getTunnusNro() === 1;
	 * asiakas.toString().startsWith("1|Pouta Pekka|030201-111C|") === true;
	 * 
	 * asiakas.rekisteroi();
	 * int n = asiakas.getTunnusNro();
	 * asiakas.parse(""+(n+20));
	 * asiakas.rekisteroi();
	 * asiakas.getTunnusNro() === n+20+1;
	 * </pre>
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		for (int k = 0; k < getKenttia(); k++ ) {
			aseta(k, Mjonot.erota(sb, '|'));
		}
	}

	@Override
	public boolean equals(Object asiakas) {
		if (asiakas instanceof Asiakas) return equals((Asiakas)asiakas);
		return false;
	}
	
	/**
	 * Tutkii onko asiakkaan tiedot samat kuin parametrina tuodun asiakkaan tiedot
	 * @param asiakas asiakas johon verrataan
	 * @return true jos kaikki tiedot samat, muuten false
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas1 = new Asiakas();
	 * asiakas1.parse("3    |Pouta Pekka    |030201-111C    |Isokatu 33       |Keskusta");
	 * Asiakas asiakas2 = new Asiakas();
	 * asiakas2.parse("2         |Saijamo  |Kaija       |0554609201          |Jebou 13 B |Viheri");
	 * Asiakas asiakas3 = new Asiakas();
	 * asiakas3.parse("3    |Pouta Pekka    |030201-111C    |Isokatu 33       |Keskusta");
	 * 
	 * asiakas1.equals(asiakas3) === true;
	 * asiakas3.equals(asiakas1) === true;
	 * asiakas1.equals(asiakas2) === false;
	 * </pre>
	 */
	public boolean equals(Asiakas asiakas) {
		if (asiakas == null) return false;
		for (int k = 0; k < getKenttia(); k++) {
			if (!anna(k).equals(asiakas.anna(k))) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return getTunnusNro();
	}
	
	/**
	 * Antaa kentän k sisällön merkkijonona
	 * @param k monesko kentän sisältö
	 * @return kentän sisältö merkkijonona 
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas = new Asiakas();
	 * asiakas.parse("1|Pouta Pekka|");
	 * asiakas.anna(0) === "1";
	 * asiakas.anna(1) === "Pouta Pekka";
	 * </pre>
	 */
	@Override
	public String anna(int k) {
		try {
			return kentat[k].toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Antaa k:n kentän sisällön avain-merkkijonona
	 * jonka perusteella voi lajitella
	 * @param k moneenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	public String getAvain(int k) {
		try {
			return kentat[k].getAvain();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Palauttaa k:ta asiakkaan kenttää vastaavan kysymyksen
	 * @param k kuinka mones kentän kysymys palautetaan
	 * @return k:netta kenttää vastaava kysymys
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
	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
	 * @param k kuinka monennen kentän arvo lasketaan
	 * @param jono jonoa joka asetetaan kentän arvoksi
	 * @return null jos asettaminen onnistuu
	 * @example
	 * <pre name="test">
	 * Asiakas asiakas = new Asiakas();
	 * asiakas.aseta(1, "Pekka Pouta") === null;
	 * asiakas.aseta(2, "1234-12345") === null;
	 * asiakas.aseta(3, "jebou 1") === null;
	 * asiakas.aseta(4, "Viheri") === null;
	 * </pre>
	 */
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
	
	/**
	 * Tehdään identtinen klooni asiakkaasta
	 * @return Object kloonattu asiakas
	 * @example
	 * <pre name="test">
	 * #THROWS CloneNotSupportedException
	 * Asiakas asiakas = new Asiakas();
	 * asiakas.parse(" 1| Pekka Pouta ");
	 * Asiakas kopio = asiakas.clone();
	 * kopio.toString() === asiakas.toString();
	 * kopio.toString().equals(asiakas.toString()) === true;
	 * </pre>
	 */
	@Override
	public Asiakas clone() throws CloneNotSupportedException {
		Asiakas uusi;
		uusi = (Asiakas) super.clone();
		uusi.kentat = kentat.clone();
		for (int k = 0; k < getKenttia(); k++)
            uusi.kentat[k] = kentat[k].clone();
		return uusi;
	}
	
	/**
	 * @return kaikkien näytettävien kysymysten otsikot merkkionotaulukkona
	 * @example
	 * <pre name="test">
	 * #import java.util.Arrays;
	 * Asiakas asiakas = new Asiakas();
	 * Arrays.toString(asiakas.getOtsikot())  =R= "\\[nimi, henkilotunnus.*]";
	 * </pre>
	 */
	public String[] getOtsikot() {
		int n = getKenttia() - ekaKentta();
		String[] otsikot = new String[n];
		for (int i = 0, k = ekaKentta(); i < n; i++, k++) {
			otsikot[i] = getKysymys(k);
		}
		return otsikot;
	}
}