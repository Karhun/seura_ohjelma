package kanta;

import javax.swing.SwingConstants;

/**
 * @author Anette Karhu
 * @version 28.7.2016
 */
public abstract class PerusKentta implements Kentta {
	private final String kysymys;
	
	/**
	 * Alustetaan kentt‰ kysymyksen tiedoilla
	 * @param kysymys haluttu kysymys
	 */
	public PerusKentta(String kysymys) {
		this.kysymys = kysymys;
	}

	/**
	 * Vertailee kahta oliota kesken‰‰n
	 */
	@Override
	public int compareTo(Kentta k) {
		return getAvain().compareTo(k.getAvain());
	}

	/**
	 * @return kentt‰‰ vastaava kysymys
	 * @see kanta.Kentta#getKysymys()
	 */
	@Override
	public String getKysymys() {
		return kysymys;
	}

	/**
	 * @param jono josta otetaan kent‰n arvo
	 * @see kanta.Kentta#aseta(String)
	 */
	@Override
	public abstract String aseta(String jono);

	/**
	 * Palauttaa kent‰n tiedot vertailtavana merkkijonona
	 * @return vertailtava merkkijono kent‰st‰
	 */
	@Override
	public String getAvain() {
		return toString().toUpperCase();
	}

	/**
	 * @return vaakasuuntainen sijainti kent‰lle
	 */
	public int getSijainti() {
		return SwingConstants.LEFT;
	}
	
	/**
	 * @return syv‰kopio oliosta
	 */
	@Override
	public Kentta clone() throws CloneNotSupportedException {
		return (Kentta) super.clone();
	}

}
