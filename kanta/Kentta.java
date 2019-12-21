package kanta;

/**
 * Rajapinta kentälle
 * @author Anette Karhu
 * @version 28.7.2016
 */
public interface Kentta extends Cloneable, Comparable<Kentta> {

	/**
	 * Korvataan aiempi toString metodi omalla
	 * @return haluttu kenttä merkkijonona
	 */
	@Override
	String toString();

	/**
	 * @return kentän kysymys
	 */
	String getKysymys();

	/**
	 * Asettaa kentän sisällön ottamalla tiedot merkkijonosta.
	 * @param jono
	 * @return null jos ok, muutoin virheteksti
	 */
	String aseta(String jono);

	/**
	 * @return kentän avain
	 */
	String getAvain();
	
	/**
	 * @return kloonin halutun kentän sisällöstä
	 * @throws CloneNotSupportedException heittää poikkeuksen jos kloonaus ei onnistu
	 */
	Kentta clone() throws CloneNotSupportedException ;
	
	/**
	 * @param k mistä kentästä
	 * @return vaakasuuntainen sijainti kentälle
	 */
	int getSijainti(int k);
}
