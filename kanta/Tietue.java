package kanta;

/**
 * @author Anette Karhu 
 * @version 2.7.2016
 */
public interface Tietue {

	/**
	 * @return tietueen kenttien lukumäärä
	 * @example
	 * <pre name="test">
	 * #import Seura.Varaus;
	 * Varaus til = new Varaus();
	 * til.getKenttia() === 4;
	 * </pre>
	 */
	public abstract int getKenttia();
	
	/**
	 * @param k minkä kentän kysymys halutaan
	 * @param s valitun kentän kysymysteksti
	 * @return null jos ok, muutoin virheteksti
	 */
	public abstract String aseta(int k, String s);
	
	/**
	 * @return ensimmäinen käyttäjän syötettävän kentän indeksi
	 * @example
	 * <pre name="test">
	 * #import Seura.Varaus;
	 * Varaus til = new Varaus();
	 * til.ekaKentta() === 1;
	 * </pre>
	 */
	public abstract int ekaKentta();
	
	/**
	 * @param k minkä kentän kysymys halutaan
	 * @return valitun kentän kysymysteksti
	 * @example
	 * <pre name="test">
	 * #import kotiinkuljetus.Varaus;
	 * Varaus til = new Varaus();
	 * til.getKysymys(1) === "Aika";
	 * </pre>
	 */
	public abstract String getKysymys(int k);
	
	
	/**
	 * Asetetaan valitun kentän sisältö. Mikäli asettaminen onnistuu, 
	 * palautetaan null ja muutoin virheteksti
	 * @param k minkä kentän sisältö asetetaan
	 * @return null jos ok, muuten virheteksti
	 * @example
	 * <pre name="test">
	 * #import Seura.VarauksenKohde;
	 * Varaus var = new Varaus();
	 * var.parse("1       | Tenniskenttä ");
	 * var.anna(0) === "1";
	 * var.anna(1) === "Tenniskenttä";
	 * </pre>
	 */
	public abstract String anna(int k);
	
	/**
	 * Tehdään identtinen klooni tietueesta
	 * @return kloonattu tietue
	 * @throws CloneNotSupportedException jos kloonausta ei tueta
	 */
	public abstract Tietue clone() throws CloneNotSupportedException;
	
	/**
	 * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoo
	 * @return tietue tolppaeroteltuna merkkijonona
	 */
	@Override
	public abstract String toString();
}
