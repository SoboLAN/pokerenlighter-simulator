package org.javafling.pokerenlighter.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.javafling.pokerenlighter.main.MD5Hasher;

/**
 *
 * @author Radu Murzea
 * 
 * @version 1.1
 */
public class PEDictionary
{	
	private static PEDictionary _instance;
	
	private static final String dictionaryEN = "config/dict.en";
	private static final String dictionaryRO = "config/dict.ro";
	
	private static final String englishMD5 = "b5a9d9840ff11a74b04fc27dbe4bbd2a";
	private static final String romanianMD5 = "0c90187261c3d703cdf9f2fa52d2e191";

	private HashMap<String, String> words;
	
	private Language currentLanguage;
	
	private static final Map<String, String> defaults;
	static
	{
		//in time, adjust the initial capacity as the number of keys grows or shrinks
		Map<String, String> myMap = new HashMap<> (128);
		myMap.put ("menubar.file", "File");
		myMap.put ("menubar.file.exit", "Exit");
		myMap.put ("menubar.file.prefs", "Preferences");
		myMap.put ("menubar.file.newsim", "New Simulation");
		myMap.put ("menubar.help", "Help");
		myMap.put ("menubar.help.about", "About");
		myMap.put ("menubar.help.checkupdate", "Check for Update");
		myMap.put ("title.general", "General Settings");
		myMap.put ("title.handoptions", "Hand Options");
		myMap.put ("title.communitycards", "Community Cards");
		myMap.put ("title.controls", "Controls");
		myMap.put ("title.results", "Results");
		myMap.put ("title.prefs", "Preferences");
		myMap.put ("title.prefs.general", "General Options");
		myMap.put ("title.prefs.simulation", "Simulation");
		myMap.put ("title.prefs.graph", "Bar Graph Options");
		myMap.put ("title.prefs.wins", "Wins");
		myMap.put ("title.prefs.loses", "Loses");
		myMap.put ("title.prefs.ties", "Ties");
		myMap.put ("label.general.nrplayers", "Number of Players:");
		myMap.put ("label.general.pokertype", "Poker Type:");
		myMap.put ("label.handoptions.player", "Player:");
		myMap.put ("label.handoptions.handtype", "Hand Type:");
		myMap.put ("label.community.flop", "Flop:");
		myMap.put ("label.community.turn", "Turn:");
		myMap.put ("label.community.river", "River:");
		myMap.put ("label.controls.progress", "Progress:");
		myMap.put ("label.prefs.general.language", "Language:");
		myMap.put ("label.prefs.general.needrestart", "(needs restart)");
		myMap.put ("label.prefs.general.lookandfeel", "Look and Feel:");
		myMap.put ("label.prefs.simulation.rounds", "Rounds per Simulation:");
		myMap.put ("label.prefs.graph.red", "Red:");
		myMap.put ("label.prefs.graph.green", "Green:");
		myMap.put ("label.prefs.graph.blue", "Blue:");
		myMap.put ("label.prefs.graph.colorcode", "Color Code:");
		myMap.put ("label.prefs.graph.displaygraph", "Display Percentage Label");
		myMap.put ("combobox.handoptions.exactcards", "Exact Cards");
		myMap.put ("combobox.handoptions.random", "Random");
		myMap.put ("combobox.handoptions.range", "Range");
		myMap.put ("combobox.prefs.english", "English");
		myMap.put ("combobox.prefs.romanian", "Romanian");
		myMap.put ("button.handoptions.chooserange", "Choose Range");
		myMap.put ("button.handoptions.choosecards", "Choose Cards");
		myMap.put ("button.controls.start", "Start");
		myMap.put ("button.controls.stop", "Stop");
		myMap.put ("button.results.graph", "View Graph");
		myMap.put ("button.prefs.ok", "OK");
		myMap.put ("button.prefs.cancel", "Cancel");
		myMap.put ("button.prefs.apply", "Apply");
		myMap.put ("table.handoptions.player", "Player");
		myMap.put ("table.handoptions.selection", "Selection");
		myMap.put ("table.handoptions.handtype", "Hand Type");
		myMap.put ("table.handoptions.handtype.range", "Range");
		myMap.put ("table.handoptions.handtype.exactcards", "Exact Cards");
		myMap.put ("table.handoptions.handtype.random", "Random");
		myMap.put ("table.results.player", "Player");
		myMap.put ("table.results.handtype", "Hand Type");
		myMap.put ("table.handoptions.handtype.range", "Range");
		myMap.put ("table.handoptions.handtype.exactcards", "Exact Cards");
		myMap.put ("table.handoptions.handtype.random", "Random");
		myMap.put ("table.results.wins", "Wins");
		myMap.put ("table.results.loses", "Loses");
		myMap.put ("table.results.ties", "Ties");
		myMap.put ("statusbar.ready", "Ready");
		myMap.put ("statusbar.running", "Running");
		myMap.put ("statusbar.onethread", "thread");
		myMap.put ("statusbar.multiplethreads", "threads");
		myMap.put ("statusbar.rounds", "rounds");
		myMap.put ("statusbar.done", "Done");
		myMap.put ("statusbar.seconds", "Stopped");
		myMap.put ("statusbar.stopped", "seconds");
		defaults = Collections.unmodifiableMap (myMap);
	}

	public static PEDictionary forLanguage (Language language) throws IOException
	{
		if (_instance == null)
		{
			_instance = new PEDictionary (language);
		}
		
		return _instance;
	}
	
	private PEDictionary (Language language) throws IOException
	{
		if (language != Language.ENGLISH && language != Language.ROMANIAN)
		{
			throw new IllegalArgumentException ("invalid language");
		}

		words = new HashMap<> ((int) (defaults.size () * 1.4));
		currentLanguage = language;

		loadConfiguration ();
	}

	private void loadConfiguration () throws IOException
	{
		Properties prop = new Properties ();
		
		String path = "";
		
		switch (currentLanguage)
		{
			case ENGLISH: path = dictionaryEN; break;
			case ROMANIAN: path = dictionaryRO; break;
		}

		//using utf-8 to support basically any language
		try (InputStreamReader input = new InputStreamReader (new FileInputStream (path), Charset.forName ("utf-8")))
		{
			prop.load (input);
		}
		
		String[] keys = defaults.keySet ().toArray (new String[0]);
		Arrays.sort (keys);
		if (existsAllKeys (prop, keys) && isMD5Match (prop, keys))
		{
			for (int i = 0; i < keys.length; i++)
			{
				words.put (keys[i], prop.getProperty (keys[i]));
			}
		}
		//else use defaults
		else
		{
			for (int i = 0; i < keys.length; i++)
			{
				words.put (keys[i], defaults.get (keys[i]));
			}
		}
	}
	
	private boolean existsAllKeys (Properties properties, String[] keys)
	{
		for (int i = 0; i < keys.length; i++)
		{
			if (properties.getProperty (keys[i]) == null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isMD5Match (Properties properties, String[] keys)
	{
		String currentMD5 = null;
		switch (currentLanguage)
		{
			case ENGLISH: currentMD5 = englishMD5; break;
			case ROMANIAN: currentMD5 = romanianMD5; break;
		}

		StringBuilder allValues = new StringBuilder ();
		
		for (int i = 0; i < keys.length; i++)
		{
			allValues.append (properties.getProperty (keys[i]));
		}
		
		String computedMD5 = MD5Hasher.hash (allValues.toString ());
		
		return currentMD5.equals (computedMD5);
	}
	
	public String getValue (String key)
	{
		return words.get (key);
	}
	
	//used for when the hashes need updating
	/*public static void main (String[] args) throws Exception
	{
		Properties prop = new Properties ();

		//using utf-8 to support basically any language
		try (InputStreamReader input = new InputStreamReader (new FileInputStream (dictionaryEN), Charset.forName ("utf-8")))
		{
			prop.load (input);
		}
		
		String[] keys = defaults.keySet ().toArray (new String[0]);
		Arrays.sort (keys);
		
		StringBuilder allValues = new StringBuilder ();
		
		for (int i = 0; i < keys.length; i++)
		{
			allValues.append (prop.getProperty (keys[i]));
		}
		
		System.out.println (MD5Hasher.hash (allValues.toString ()));
	}*/
}