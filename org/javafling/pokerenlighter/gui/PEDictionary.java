package org.javafling.pokerenlighter.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public class PEDictionary
{	
	private static PEDictionary _instance;
	
	private static final String dictionaryEN = "config/dict.en";
	private static final String dictionaryRO = "config/dict.ro";

	private HashMap<String, String> words;
	
	private Language currentLanguage;

	//list of all keys
	private String[] keys =
	{
		"menubar.file",
		"menubar.file.exit",
		"menubar.file.checkupdate",
		"menubar.file.prefs",
		"menubar.help",
		"menubar.help.about",
		"title.general",
		"title.handoptions",
		"title.communitycards",
		"title.controls",
		"title.results",
		"title.prefs",
		"title.prefs.general",
		"title.prefs.simulation",
		"title.prefs.graph",
		"title.prefs.wins",
		"title.prefs.loses",
		"title.prefs.ties",
		"label.general.nrplayers",
		"label.general.pokertype",
		"label.handoptions.player",
		"label.handoptions.handtype",
		"label.community.flop",
		"label.community.turn",
		"label.community.river",
		"label.controls.progress",
		"label.prefs.general.language",
		"label.prefs.general.needrestart",
		"label.prefs.general.lookandfeel",
		"label.prefs.simulation.rounds",
		"label.prefs.graph.red",
		"label.prefs.graph.green",
		"label.prefs.graph.blue",
		"label.prefs.graph.colorcode",
		"label.prefs.graph.displaygraph",
		"combobox.handoptions.exactcards",
		"combobox.handoptions.random",
		"combobox.handoptions.range",
		"combobox.prefs.english",
		"combobox.prefs.romanian",
		"button.handoptions.chooserange",
		"button.handoptions.choosecards",
		"button.controls.start",
		"button.controls.stop",
		"button.results.graph",
		"button.prefs.ok",
		"button.prefs.cancel",
		"button.prefs.apply",
		"table.handoptions.player",
		"table.handoptions.selection",
		"table.handoptions.handtype",
		"table.handoptions.handtype.range",
		"table.handoptions.handtype.exactcards",
		"table.handoptions.handtype.random",
		"table.results.player",
		"table.results.handtype",
		"table.handoptions.handtype.range",
		"table.handoptions.handtype.exactcards",
		"table.handoptions.handtype.random",
		"table.results.wins",
		"table.results.loses",
		"table.results.ties",
		"statusbar.ready",
		"statusbar.running",
		"statusbar.onethread",
		"statusbar.multiplethreads",
		"statusbar.rounds",
		"statusbar.done"
	};
	
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

		words = new HashMap<> ((int) (keys.length * 1.4));
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
		
		for (int i = 0; i < keys.length; i++)
		{
			String word = prop.getProperty (keys[i]);
			
			if (word == null)
			{
				throw new RuntimeException ("word " + keys[i] + " not found");
			}
			
			words.put (keys[i], word);
		}
	}
	
	public String getValue (String key)
	{
		return words.get (key);
	}
}