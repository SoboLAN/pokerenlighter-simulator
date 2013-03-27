package org.javafling.pokerenlighter.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;

/**
 *
 * @author Murzea Radu
 */
public class GUI
{
	public static final int SCREEN_CENTER = 5;
	
	private static GUI _instance;
	
	private String windowTitle = "Poker Enlighter 2.0 Alpha";
	
	private PEDictionary dictionary;
	
	private JFrame mainframe;

	private JPanel customPanel;
	private JPanel playersPanel;
	private JPanel progressPanel;
	private JPanel choicesPanel;
	private JPanel generalChoicesPanel;
	private JPanel communityPanel;
	private JPanel resultsPanel;
	
	private StatusBar statusBar;
	
	private JTable choicesTable, resultsTable;
	private JComboBox playerIDBox, handTypeBox, variationBox;
	private JButton selectButton, saveProfileButton, loadProfileButton, startButton, stopButton,
					exportButton, viewGraphButton;
	private JSpinner playersCount;
	private JCheckBox enableFlop, enableTurn, enableRiver;
	private JLabel flopCard1, flopCard2, flopCard3, turnCard, riverCard;
	private JProgressBar progressBar;
	
	private PlayerProfile[][] playersInfo;
	
	public static GUI getGUI (PEDictionary lang)
	{
		if (_instance == null)
		{
			_instance = new GUI (lang);
		}

		return _instance;
	}
	
	private GUI (PEDictionary language)
	{
		dictionary = language;
		
		playersInfo = new PlayerProfile[3][7];
		
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				playersInfo[j][i] = new PlayerProfile (HandType.RANDOM);
			}
		}

		mainframe = new JFrame (windowTitle);
		mainframe.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		mainframe.setIconImage (Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("images/ace_spades_icon.jpg")));
		
		MenuBar menuBar = new MenuBar (mainframe);
		mainframe.setJMenuBar (menuBar.getMenuBar ());
		mainframe.setLayout (new BorderLayout ());
		
		customPanel = createCustomPanel ();
		mainframe.add (customPanel, BorderLayout.CENTER);
		
		statusBar = new StatusBar ("Running - Texas Hold'em - 4 Players - 90,000 Rounds - 3 threads");
		mainframe.add (statusBar, BorderLayout.SOUTH);
		
		mainframe.pack ();
	}
	
	/** Sets the location of the Window on the screen relative to
	* the top-left corner.
	*
	* @param x the number of pixels to move to the right
	*
	* @param y the number of pixels to move to the bottom
	*/
	public void setLocation (int x, int y)
	{
		mainframe.setLocation (x, y);
	}
	
	public void setLocation (int location)
	{
		if (location == SCREEN_CENTER)
		{
			mainframe.setLocationRelativeTo (null);
		}
	}
	
	public void setVisible (boolean on)
	{
		mainframe.setVisible (on);
	}
	
	public void setResizable (boolean on)
	{
		mainframe.setResizable (on);
	}
	
	private JPanel createCustomPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());

		generalChoicesPanel = createPlayersVariationPanel ();
		panel.add (generalChoicesPanel, BorderLayout.NORTH);

		JPanel middlePanel = new JPanel (new BorderLayout ());
		
		playersPanel = createPlayersPanel ();
		middlePanel.add (playersPanel, BorderLayout.NORTH);
		
		communityPanel = createCommunityPanel ();
		middlePanel.add (communityPanel, BorderLayout.CENTER);
		
		progressPanel = createProgressPanel ();
		middlePanel.add (progressPanel, BorderLayout.SOUTH);

		panel.add (middlePanel, BorderLayout.CENTER);
		
		resultsPanel = createResultsPanel ();
		panel.add (resultsPanel, BorderLayout.SOUTH);
	
		return panel;
	}
	
	private JPanel createChoicesPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
	
		String[] titles = {dictionary.getValue ("table.handoptions.player"),
							dictionary.getValue ("table.handoptions.handtype"),
							dictionary.getValue ("table.handoptions.selection")};
		
		Object[][] rows = new String[][]
		{
			{"1", "Range", "16.6 %"},
			{"2", "Range", "12.3 %"},
			{"3", "Exact Cards", "KcKd"},
			{"4", "Random", ""},
			{"5", "Random", ""},
			{"6", "Exact Cards", "As4h"},
			{"", "", "", "", ""}
		};
		
		choicesTable = new JTable (rows, titles);
		
		choicesTable.getColumnModel ().setColumnSelectionAllowed (false);
		
		for (int i = 0; i < 3; i++)
		{
			TableColumn col = choicesTable.getColumnModel ().getColumn (i);
			
			DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer ();
			
			dtcr.setHorizontalAlignment (SwingConstants.CENTER);
			
			col.setCellRenderer (dtcr);
		}

		panel.add (choicesTable.getTableHeader (), BorderLayout.PAGE_START);
		panel.add (choicesTable, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createPlayersPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		GUIUtilities.setBorder (panel, dictionary.getValue ("title.handoptions"), TitledBorder.LEFT);

		JPanel topPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		topPanel.add (new JLabel (dictionary.getValue ("label.handoptions.player")));
		
		playerIDBox = new JComboBox (new Integer[]{1, 2, 3, 4, 5, 6, 7});
		topPanel.add (playerIDBox);
		
		topPanel.add (new JLabel (dictionary.getValue ("label.handoptions.handtype")));
		
		String[] handBoxOptions = {dictionary.getValue ("combobox.handoptions.random"),
								dictionary.getValue ("combobox.handoptions.range"),
								dictionary.getValue ("combobox.handoptions.exactcards")};

		handTypeBox = new JComboBox (handBoxOptions);
		handTypeBox.addItemListener (new HandTypeItemListener ());
		topPanel.add (handTypeBox);
		
		selectButton = new JButton (dictionary.getValue ("button.handoptions.chooserange"));
		topPanel.add (selectButton);
		selectButton.addActionListener (new SelectButtonListener ());
		
		panel.add (topPanel, BorderLayout.NORTH);
		
		JPanel importPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		saveProfileButton = new JButton ("Save Profile");
		importPanel.add (saveProfileButton);
		
		loadProfileButton = new JButton ("Load Profile");
		importPanel.add (loadProfileButton);
		
		panel.add (importPanel, BorderLayout.CENTER);

		choicesPanel = createChoicesPanel ();
		panel.add (choicesPanel, BorderLayout.SOUTH);

		return panel;
	}

	
	private JPanel createPlayersVariationPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2));
		
		GUIUtilities.setBorder (panel, dictionary.getValue ("title.prefs.general"), TitledBorder.LEFT);
		
		JPanel nrPlayersPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
	
		SpinnerNumberModel playersModel = new SpinnerNumberModel (2, 2, 7, 1);		
		playersCount = new JSpinner (playersModel);

		nrPlayersPanel.add (new JLabel (dictionary.getValue ("label.general.nrplayers")));
		nrPlayersPanel.add (playersCount);
		
		JPanel variationPanel = new JPanel (new FlowLayout (FlowLayout.RIGHT));
		
		variationBox = new JComboBox (new String[]{"Texas Hold'em", "Omaha", "Omaha Hi/Lo"});
		variationBox.addItemListener (new PokerTypeListener ());
		variationPanel.add (new JLabel (dictionary.getValue ("label.general.pokertype")));
		variationPanel.add (variationBox);
		
		panel.add (nrPlayersPanel);
		panel.add (variationPanel);
		
		return panel;
	}
	

	private JPanel createCommunityPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));

		GUIUtilities.setBorder (panel, dictionary.getValue ("title.communitycards"), TitledBorder.LEFT);
		
		enableFlop = new JCheckBox (dictionary.getValue ("label.community.flop"));
		
		flopCard1 = new JLabel (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
		flopCard2 = new JLabel (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
		flopCard3 = new JLabel (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
		
		panel.add (enableFlop);
		panel.add (flopCard1);
		panel.add (flopCard2);
		panel.add (flopCard3);
	
		enableTurn = new JCheckBox (dictionary.getValue ("label.community.turn"));
		
		turnCard = new JLabel (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
		
		panel.add (enableTurn);
		panel.add (turnCard);

		enableRiver = new JCheckBox (dictionary.getValue ("label.community.river"));
		
		riverCard = new JLabel (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
		
		panel.add (enableRiver);
		panel.add (riverCard);
		
		return panel;
	}
	
	private JPanel createProgressPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.LEFT, 10, 10));

		GUIUtilities.setBorder (panel, dictionary.getValue ("title.controls"), TitledBorder.LEFT);
				
		startButton = new JButton (dictionary.getValue ("button.controls.start"));
		
		stopButton = new JButton (dictionary.getValue ("button.controls.stop"));

		progressBar = new JProgressBar (0, 100);
		progressBar.setPreferredSize (new Dimension (220, 20));		
		progressBar.setStringPainted (true);
		progressBar.setValue (0);

		panel.add (startButton);
		panel.add (stopButton);
		panel.add (new JLabel (dictionary.getValue ("label.controls.progress")));
		panel.add (progressBar);
		
		return panel;
	}
	
	private JPanel createResultsPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		GUIUtilities.setBorder (panel, dictionary.getValue ("title.results"), TitledBorder.CENTER);
		
		String[] titles = {dictionary.getValue ("table.handoptions.player"),
							dictionary.getValue ("table.handoptions.handtype"),
							dictionary.getValue ("table.results.wins"),
							dictionary.getValue ("table.results.loses"),
							dictionary.getValue ("table.results.ties")
		};
		
		Object[][] rows = new String[][]
		{
			{"1", "Range", "23.9 %", "74.2 %", "1.7 %"},
			{"2", "Range", "23.9 %", "74.2 %", "1.7 %"},
			{"3", "Exact Cards", "43.1 %", "36.1 %", "1.2 %"},
			{"4", "Random", "18.3 %", "81.5 %", "0.8 %"},
			{"5", "Random", "18.3 %", "81.5 %", "0.8 %"},
			{"6", "Exact Cards", "7.3 %", "91.8 %", "2.3 %"},
			{"", "", "", "", ""}
		};
		
		resultsTable = new JTable (rows, titles);
		
//		DefaultTableModel tableModel = new DefaultTableModel ()
//		{
//			@Override
//			public boolean isCellEditable (int row, int column)
//			{
//				return false;
//			}
//		};
//		DefaultTableColumnModel columnModel = new DefaultTableColumnModel ()
//		{
//			@Override
//			public void moveColumn (int columnIndex, int newIndex) {}
//		};
//		
//		resultsTable.setColumnModel (columnModel);
//		resultsTable.setModel (tableModel);
		
		resultsTable.getColumnModel ().setColumnSelectionAllowed (false);
		
		for (int i = 0; i < 4; i++)
		{
			TableColumn col = resultsTable.getColumnModel ().getColumn (i);
			
			DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer ();
			
			dtcr.setHorizontalAlignment (SwingConstants.CENTER);
			
			col.setCellRenderer (dtcr);
		}

		panel.add (resultsTable.getTableHeader (), BorderLayout.PAGE_START);
		panel.add (resultsTable, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel (new FlowLayout (FlowLayout.CENTER, 5, 5));
		
		exportButton = new JButton (dictionary.getValue ("button.results.export"));
		viewGraphButton = new JButton (dictionary.getValue ("button.results.graph"));
		
		buttonsPanel.add (exportButton);
		buttonsPanel.add (viewGraphButton);
		
		panel.add (buttonsPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private ArrayList<Card> getUsedCards (PokerType selectedVariation)
	{
		ArrayList<Card> usedCards = new ArrayList<> ();
				
		for (int i = 0; i < 7; i++)
		{					
			Card[] cards = playersInfo[selectedVariation.ordinal ()][i].getCards ();
					
			if (cards == null)
			{
				continue;
			}
				
			for (int j = 0; j < cards.length; j++)
			{
				if (cards[j] != null)
				{
					usedCards.add (cards[j]);
				}
			}
		}
				
		return usedCards;
	}
	
	private PokerType getSelectedVariation ()
	{
		int selectedVariation = variationBox.getSelectedIndex ();

		switch (selectedVariation)
		{
			case 0: return PokerType.TEXAS_HOLDEM;
			case 1: return PokerType.OMAHA;
			case 2: return PokerType.OMAHA_HILO;
			default: return null;
		}
	}
	
	private HandType getSelectedHandType ()
	{
		int selectedType = handTypeBox.getSelectedIndex ();
		
		switch (selectedType)
		{
			case 0: return HandType.RANDOM;
			case 1: return HandType.RANGE;
			case 2: return HandType.EXACTCARDS;
			default: return null;
		}
	}
	
	private class SelectButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			PokerType selectedPokerType = getSelectedVariation ();
			HandType selectedHandType = getSelectedHandType ();
			
			if (selectedHandType == HandType.RANGE)
			{
				if (selectedPokerType == PokerType.TEXAS_HOLDEM)
				{
					RangeDialog rd = new RangeDialog (mainframe);
			
					rd.setLocationRelativeTo (mainframe);
					rd.setVisible (true);
				}
			}
			else if (selectedHandType == HandType.EXACTCARDS)
			{
				ArrayList<Card> usedCards = getUsedCards (selectedPokerType);
				
				Card[] playerCards = playersInfo[selectedPokerType.ordinal ()][playerIDBox.getSelectedIndex ()].getCards ();
				
				CardsDialog cd = new CardsDialog (mainframe, selectedPokerType, playerCards, usedCards);
				
				cd.setLocationRelativeTo (mainframe);
				cd.setVisible (true);
			}
		}
	}
	
	private class HandTypeItemListener implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange () == ItemEvent.DESELECTED)
			{
				return;
			}
			
			HandType selectedHandType = getSelectedHandType ();
	
			if (selectedHandType == HandType.RANGE)
			{
				selectButton.setText (dictionary.getValue ("button.handoptions.chooserange"));
			}
			else if (selectedHandType == HandType.EXACTCARDS)
			{
				selectButton.setText (dictionary.getValue ("button.handoptions.choosecards"));
			}
		}
	}
	
	private class PokerTypeListener implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			
		}	
	}
}
