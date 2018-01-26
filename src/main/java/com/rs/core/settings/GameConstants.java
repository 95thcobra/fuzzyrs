package com.rs.core.settings;

import java.math.BigInteger;

public final class GameConstants {

    public static final String DATA_PATH = "./data";
    public static final String CACHE_PATH = DATA_PATH + "/cache/";
    public static final String ERROR_OUTPUT = DATA_PATH + "/logs/err.txt";
	public static final String CONSOLE_OUTPUT = DATA_PATH + "/logs/out.txt";

	/**
	 * General client and server settings.
	 */
	public static final int PORT_ID = 43594;
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 718;
    public static final int CUSTOM_CLIENT_BUILD = 5424672;

	public static final int MAX_ITEMS = 30000;

	public static final int MAX_STARTER_AMOUNT = 3;
	/**
     * World settings
     */
	public static final int WORLD_CYCLE_TIME = 600; // the speed of worldtask in ms
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	/**
	 * Game constants
	 */
	public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72 };
	public static final String GRAB_SERVER_TOKEN = "hAJWGrsaETglRjuwxMwnlA/d5W6EgYWx";
	public static final int[] GRAB_SERVER_KEYS = { 1441, 78700, 44880, 39771,
			363186, 44375, 0, 16140, 7316, 271148, 810710, 216189, 379672,
			454149, 933950, 21006, 25367, 17247, 1244, 1, 14856, 1494, 119,
			882901, 1818764, 3963, 3618 };
	// an exeption(grab server has his own keyset unlike rest of client)
	public static final BigInteger GRAB_SERVER_PRIVATE_EXPONENT = new BigInteger(
			"95776340111155337321344029627634178888626101791582245228586750697996713454019354716577077577558156976177994479837760989691356438974879647293064177555518187567327659793331431421153203931914933858526857396428052266926507860603166705084302845740310178306001400777670591958466653637275131498866778592148380588481");
	public static final BigInteger GRAB_SERVER_MODULUS = new BigInteger(
			"119555331260995530494627322191654816613155476612603817103079689925995402263457895890829148093414135342420807287820032417458428763496565605970163936696811485500553506743979521465489801746973392901885588777462023165252483988431877411021816445058706597607453280166045122971960003629860919338852061972113876035333");
	public static final BigInteger PRIVATE_EXPONENT = new BigInteger(
			"90587072701551327129007891668787349509347630408215045082807628285770049664232156776755654198505412956586289981306433146503308411067358680117206732091608088418458220580479081111360656446804397560752455367862620370537461050334224448167071367743407184852057833323917170323302797356352672118595769338616589092625");
	public static final BigInteger MODULUS = new BigInteger(
			"102876637271116124732338500663639643113504464789339249490399312659674772039314875904176809267475033772367707882873773291786014475222178654932442254125731622781524413208523465520758537060408541610254619166907142593731337618490879831401461945679478046811438574041131738117063340726565226753787565780501845348613");
    public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000;
    public static final int AIR_GUITAR_MUSICS_COUNT = 50;
    public static final int QUESTS = 183;
    public static final int PLAYERS_LIMIT = 2000;
    public static final int LOCAL_PLAYERS_LIMIT = 250;
    public static final int LOCAL_NPCS_LIMIT = 250;
    /**
     * If the use of the managment server is enabled.
     */
    public static boolean MANAGMENT_SERVER_ENABLED = true;
    /**
	 * Donator settings
	 */
	public static String[] DONATOR_ITEMS = { "halo", "flaming skull",
			"third-age", "eye of the", "donator cape", "ring of stone" };
	public static String[] RARE_DROPS = { "pernix", "torva", "virtus",
			"bandos", "subjugation", "akrisae", "saradomin", "zamorak",
			"spirit shield", "dragon claws", "chaotic", "armadyl", "thok",
			"blisterwood", "korasi", "vigour", "golden", "primal", "keenblade",
			"dragonic", "drygore", "coins" };

	private GameConstants() {

	}
}
