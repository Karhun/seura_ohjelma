package kanta;

/**
 * @author Anette Karhu
 * @version 28.7.2016
 */
public class JonoKentta extends PerusKentta {
	String jono = "";

	/**
	 * Alustetaan kysymyksellä
	 * @param kysymys joka esitetään kenttää kysyttäessä
	 */
	public JonoKentta(String kysymys) {
		super(kysymys);
	}
	
	/**
	 * @return kentän sisältö
	 */
	@Override
	public String toString() {
		return jono;
	}

	/**
	 * @param mjono merkkijono joka asetetaan kentän arvoksi
	 * @see kanta.PerusKentta#aseta(String)
	 */
	@Override
	public String aseta(String mjono) {
		this.jono = mjono;
		return null;
	}

	@Override
	public int getSijainti(int k) {
		return 0;
	}

}
