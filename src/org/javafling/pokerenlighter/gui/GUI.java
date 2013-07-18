package org.javafling.pokerenlighter.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;
import org.javafling.pokerenlighter.simulation.Range;
import org.javafling.pokerenlighter.simulation.SimulationExport;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult;
import org.javafling.pokerenlighter.simulation.Simulator;

/** Main GUI (Graphical User Interface) class.
 *
 * @author Radu Murzea
 */
public final class GUI implements PropertyChangeListener
{
	private static GUI _instance;
	
	private static final int MAX_PLAYERS = 7;
	
	private String windowTitle = "Poker Enlighter";
	private String blankCardPath = "images/blank.card.gif";
	
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
	private JComboBox<Integer> playerIDBox;
	private JComboBox<String> handTypeBox, variationBox;
	private JButton selectButton, startButton, stopButton, //saveProfileButton, loadProfileButton,
			exportButton, viewGraphButton;
	private JSpinner playersCount;
	private JCheckBox enableFlop, enableTurn, enableRiver;
	private JLabel flopCard1, flopCard2, flopCard3, turnCard, riverCard;
	private JProgressBar progressBar;
	
	private PlayerProfile[] holdemProfiles, omahaProfiles, omahaHiLoProfiles;
	private Card[] holdemCommunityCards, omahaCommunityCards, omahaHiLoCommunityCards;
	
	private Simulator simulator;

	public static GUI getGUI (PEDictionary lang)
	{
		if (_instance == null)
		{
			_instance = new GUI (lang);
		}

		return _instance;
	}
	
	public static GUI getGUI ()
	{
		return _instance;
	}
	
	private GUI (PEDictionary language)
	{
		dictionary = language;
		
		holdemProfiles = new PlayerProfile[MAX_PLAYERS];
		omahaProfiles = new PlayerProfile[MAX_PLAYERS];
		omahaHiLoProfiles = new PlayerProfile[MAX_PLAYERS];
		
		holdemCommunityCards = new Card[5];
		omahaCommunityCards = new Card[5];
		omahaHiLoCommunityCards = new Card[5];

		mainframe = new JFrame (windowTitle);
		mainframe.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		mainframe.setIconImage (Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("images/ace_spades_icon.jpg")));
		
		MenuBar menuBar = new MenuBar (mainframe, dictionary);
		mainframe.setJMenuBar (menuBar.getMenuBar ());
		mainframe.setLayout (new BorderLayout ());
		
		customPanel = createCustomPanel ();
		mainframe.add (customPanel, BorderLayout.CENTER);
		
		statusBar = new StatusBar (dictionary.getValue ("statusbar.ready"));
		mainframe.add (statusBar, BorderLayout.SOUTH);
		
		newSimulation ();
		
		setChoicesTableContent ();

		mainframe.pack ();
	}

	public void setLocation (int x, int y)
	{
		mainframe.setLocation (x, y);
	}
	
	public void setLocationToCenterOfScreen ()
	{
		mainframe.setLocationRelativeTo (null);
	}
	
	public void setVisible (boolean on)
	{
		mainframe.setVisible (on);
	}
	
	public void setResizable (boolean on)
	{
		mainframe.setResizable (on);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		switch (evt.getPropertyName ())
		{
			case "progress":
				int newValue = (Integer) evt.getNewValue ();
				progressBar.setValue (newValue);

				//simulator is done, so re-enable stuff
				if (newValue == 100)
				{
					setGUIElementsDone (false);

					setResultsTableContent ();
				}
				break;
			case "state":
				String state = (String) evt.getNewValue ();
				if (state.equals ("cancelled"))
				{
					setGUIElementsDone (true);
				}
				break;
		}
	}
	
	public void newSimulation ()
	{
		if (simulator != null)
		{
			if (simulator.isRunning ())
			{
				return;
			}
		}
		
		simulator = null;
		
		for (int i = 0; i < MAX_PLAYERS; i++)
		{
			holdemProfiles[i] = new PlayerProfile (HandType.RANDOM, null, null);
			omahaProfiles[i] = new PlayerProfile (HandType.RANDOM, null, null);
			omahaHiLoProfiles[i] = new PlayerProfile (HandType.RANDOM, null, null);
		}
		
		for (int i = 0; i < 5; i++)
		{
			holdemCommunityCards[i] = null;
			omahaCommunityCards[i] = null;
			omahaHiLoCommunityCards[i] = null;
		}
		
		playersCount.setValue (Integer.valueOf (2));
		adjustAvailablePlayerIDs ();
		
		variationBox.setSelectedIndex (0);
		
		setCommunityCardsContent ();
		
		setResultsTableContent ();
		
		setChoicesTableContent ();
		
		stopButton.setEnabled (false);
		viewGraphButton.setEnabled (false);
		exportButton.setEnabled (false);
		
		progressBar.setValue (0);
		
		statusBar.setText (dictionary.getValue ("statusbar.ready"));
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
	
	private JPanel createPlayersPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		GUIUtilities.setBorder (panel, dictionary.getValue ("title.handoptions"), TitledBorder.LEFT);

		JPanel topPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		topPanel.add (new JLabel (dictionary.getValue ("label.handoptions.player")));
		
		Integer[] IDs = new Integer[MAX_PLAYERS];
		for (int i = 0; i < IDs.length; i++)
		{
			IDs[i] = i + 1;
		}
		playerIDBox = new JComboBox<> (IDs);
		playerIDBox.setEditable (false);
		topPanel.add (playerIDBox);
		
		topPanel.add (new JLabel (dictionary.getValue ("label.handoptions.handtype")));
		
		String[] handBoxOptions = {dictionary.getValue ("combobox.handoptions.random"),
								dictionary.getValue ("combobox.handoptions.range"),
								dictionary.getValue ("combobox.handoptions.exactcards")};

		handTypeBox = new JComboBox<> (handBoxOptions);
		handTypeBox.setEditable (false);
		handTypeBox.addItemListener (new HandTypeItemListener ());
		topPanel.add (handTypeBox);
		
		selectButton = new JButton (dictionary.getValue ("button.handoptions.chooserange"));
		topPanel.add (selectButton);
		selectButton.addActionListener (new SelectButtonListener ());
		
		panel.add (topPanel, BorderLayout.NORTH);
		
		/*JPanel importPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		saveProfileButton = new JButton ("Save Profile");
		importPanel.add (saveProfileButton);
		
		loadProfileButton = new JButton ("Load Profile");
		importPanel.add (loadProfileButton);
	
		panel.add (importPanel, BorderLayout.CENTER);*/

		choicesPanel = createChoicesPanel ();
		panel.add (choicesPanel, BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel createChoicesPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
	
		String[] titles = {dictionary.getValue ("table.handoptions.player"),
							dictionary.getValue ("table.handoptions.handtype"),
							dictionary.getValue ("table.handoptions.selection")};
		
		Object[][] rows = new String[MAX_PLAYERS][3];
		for (int i = 0; i < getCurrentPlayerCount (); i++)
		{
			rows[i][0] = Integer.toString (i + 1);
		}
		for (int i = getCurrentPlayerCount (); i < MAX_PLAYERS; i++)
		{
			rows[i][0] = " ";
		}

		DefaultTableModel model = new DefaultTableModel (rows, titles)
		{
			@Override
			public boolean isCellEditable (int row, int column)
			{
				return false;
			}
		};

		choicesTable = new JTable (model);
		
		choicesTable.getColumnModel ().setColumnSelectionAllowed (false);
		choicesTable.getTableHeader ().setReorderingAllowed (false);
		
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
	
	private JPanel createPlayersVariationPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2));
		
		GUIUtilities.setBorder (panel, dictionary.getValue ("title.prefs.general"), TitledBorder.LEFT);
		
		JPanel nrPlayersPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
	
		SpinnerNumberModel playersModel = new SpinnerNumberModel (2, 2, MAX_PLAYERS, 1);		
		playersCount = new JSpinner (playersModel);
		((JSpinner.DefaultEditor) playersCount.getEditor ()).getTextField ().setEditable (false);
		playersCount.addChangeListener (new PlayerCountSpinnerListener ());

		nrPlayersPanel.add (new JLabel (dictionary.getValue ("label.general.nrplayers")));
		nrPlayersPanel.add (playersCount);
		
		JPanel variationPanel = new JPanel (new FlowLayout (FlowLayout.RIGHT));
		
		variationBox = new JComboBox<> (new String[]{"Texas Hold'em", "Omaha", "Omaha Hi/Lo"});
		variationBox.addItemListener (new PokerTypeListener ());
		variationBox.setEditable (false);
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
		
		flopCard1 = new JLabel ();
		flopCard2 = new JLabel ();
		flopCard3 = new JLabel ();
		
		panel.add (enableFlop);
		panel.add (flopCard1);
		panel.add (flopCard2);
		panel.add (flopCard3);
	
		enableTurn = new JCheckBox (dictionary.getValue ("label.community.turn"));
		
		turnCard = new JLabel ();
		
		panel.add (enableTurn);
		panel.add (turnCard);

		enableRiver = new JCheckBox (dictionary.getValue ("label.community.river"));
		
		riverCard = new JLabel ();
		
		panel.add (enableRiver);
		panel.add (riverCard);
		
		flopCard1.addMouseListener (new CommunityCardsListener (Street.FLOP));
		flopCard2.addMouseListener (new CommunityCardsListener (Street.FLOP));
		flopCard3.addMouseListener (new CommunityCardsListener (Street.FLOP));
		turnCard.addMouseListener (new CommunityCardsListener (Street.TURN));
		riverCard.addMouseListener (new CommunityCardsListener (Street.RIVER));
		
		return panel;
	}
	
	private JPanel createProgressPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.LEFT, 10, 10));

		GUIUtilities.setBorder (panel, dictionary.getValue ("title.controls"), TitledBorder.LEFT);
				
		startButton = new JButton (dictionary.getValue ("button.controls.start"));
		startButton.addActionListener (new StartSimulationListener ());
		
		stopButton = new JButton (dictionary.getValue ("button.controls.stop"));
		stopButton.addActionListener (new StopSimulationListener ());

		progressBar = new JProgressBar (0, 100);
		progressBar.setPreferredSize (new Dimension (220, 20));		
		progressBar.setStringPainted (true);

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
		
		String[] titles = {dictionary.getValue ("table.results.player"),
							dictionary.getValue ("table.results.wins"),
							dictionary.getValue ("table.results.loses"),
							dictionary.getValue ("table.results.ties")
		};
		
		Object[][] rows = new String[MAX_PLAYERS][4];
		
		for (int i = 0; i < MAX_PLAYERS; i++)
		{
			rows[i][0] = " ";
		}
		
		DefaultTableModel model = new DefaultTableModel (rows, titles)
		{
			@Override
			public boolean isCellEditable (int row, int column)
			{
				return false;
			}
		};
		
		resultsTable = new JTable (model);
	
		resultsTable.getTableHeader ().setReorderingAllowed (false);
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
		
		exportButton = new JButton ("Export to XML");
		exportButton.addActionListener (new ResultXMLListener ());
		viewGraphButton = new JButton (dictionary.getValue ("button.results.graph"));
		viewGraphButton.addActionListener (new ViewGraphListener ());
		
		buttonsPanel.add (viewGraphButton);
		buttonsPanel.add (exportButton);

		panel.add (buttonsPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private class StartSimulationListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			simulator = new Simulator (getSelectedVariation (), OptionsContainer.getOptionsContainer ().getRounds ());
			simulator.addPropertyChangeListener (_instance);
			simulator.setUpdateInterval (10);
			
			try
			{
				setPlayers ();

				setCommunityCards ();
				
				simulator.start ();
			}
			catch (Exception ex)
			{
				GUIUtilities.showErrorDialog (mainframe,
											"The simulator encountered an error: " + ex.getMessage (),
											"Simulator Error");
				return;
			}

			setGUIElementsRunning ();
		}
		
		private void setPlayers ()
		{
			for (int i = 0; i < getCurrentPlayerCount (); i++)
			{
				if (getSelectedVariation () == PokerType.TEXAS_HOLDEM)
				{
					simulator.addPlayer (holdemProfiles[i]);
				}
				else if (getSelectedVariation () == PokerType.OMAHA)
				{
					simulator.addPlayer (omahaProfiles[i]);
				}
				else if (getSelectedVariation () == PokerType.OMAHA_HILO)
				{
					simulator.addPlayer (omahaHiLoProfiles[i]);
				}
			}
		}

		private void setCommunityCards ()
		{
			Card[] flop = new Card[3];
			Card turnCard = null;
			Card riverCard = null;
			if (getSelectedVariation () == PokerType.TEXAS_HOLDEM)
			{
				flop[0] = holdemCommunityCards[0];
				flop[1] = holdemCommunityCards[1];
				flop[2] = holdemCommunityCards[2];
				turnCard = holdemCommunityCards[3];
				riverCard = holdemCommunityCards[4];
			}
			else if (getSelectedVariation () == PokerType.OMAHA)
			{
				flop[0] = omahaCommunityCards[0];
				flop[1] = omahaCommunityCards[1];
				flop[2] = omahaCommunityCards[2];
				turnCard = omahaCommunityCards[3];
				riverCard = omahaCommunityCards[4];
			}
			else if (getSelectedVariation () == PokerType.OMAHA_HILO)
			{
				flop[0] = omahaHiLoCommunityCards[0];
				flop[1] = omahaHiLoCommunityCards[1];
				flop[2] = omahaHiLoCommunityCards[2];
				turnCard = omahaHiLoCommunityCards[3];
				riverCard = omahaHiLoCommunityCards[4];
			}
			
			if (enableFlop.isSelected () && flop[0] != null)
			{
				simulator.setFlop (flop);
			}
			if (enableTurn.isSelected () && turnCard != null)
			{
				simulator.setTurn (turnCard);
			}
			if (enableRiver.isSelected () && riverCard != null)
			{
				simulator.setRiver (riverCard);
			}
		}
	}
	
	private class StopSimulationListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			simulator.stop ();
		}
	}
	
	private class CommunityCardsListener extends MouseAdapter
	{
		private Street street;
		
		public CommunityCardsListener (Street street)
		{
			this.street = street;
		}
		
		private Card[] getSelectedCommunityCards (PokerType selectedPokerType)
		{
			Card[] selectedCommunityCards = null;

			int nrCards = (street == Street.FLOP) ? 3 : 1;
			int position = (street == Street.FLOP ) ? 0 : (street == Street.TURN ? 3 : 4);
			if (selectedPokerType == PokerType.TEXAS_HOLDEM)
			{
				for (int i = position; i < position + nrCards; i++)
				{
					if (holdemCommunityCards[i] == null)
					{
						return null;
					}
				}

				selectedCommunityCards = new Card[nrCards];
				System.arraycopy (holdemCommunityCards, position, selectedCommunityCards, 0, nrCards);
			}
			else if (selectedPokerType == PokerType.OMAHA)
			{
				for (int i = position; i < position + nrCards; i++)
				{
					if (omahaCommunityCards[i] == null)
					{
						return null;
					}
				}
				
				selectedCommunityCards = new Card[nrCards];
				System.arraycopy (omahaCommunityCards, position, selectedCommunityCards, 0, nrCards);
			}
			else if (selectedPokerType == PokerType.OMAHA_HILO)
			{
				for (int i = position; i < position + nrCards; i++)
				{
					if (omahaHiLoCommunityCards[i] == null)
					{
						return null;
					}
				}
				
				selectedCommunityCards = new Card[nrCards];
				System.arraycopy (omahaHiLoCommunityCards, position, selectedCommunityCards, 0, nrCards);
			}
			
			return selectedCommunityCards;
		}
		
		@Override
		public void mousePressed (MouseEvent e)
		{
			if (! enableFlop.isEnabled ())
			{
				return;
			}
			
			if ((street == Street.FLOP && ! enableFlop.isSelected ()) ||
				(street == Street.TURN && ! enableTurn.isSelected ()) ||
				(street == Street.RIVER && ! enableRiver.isSelected ()))
			{
				return;
			}

			PokerType selectedPokerType = getSelectedVariation ();
			
			ArrayList<Card> usedCards = getUsedCards (selectedPokerType);
			
			Card[] selectedCommunityCards = getSelectedCommunityCards (selectedPokerType);
			
			int nrCards = (street == Street.FLOP) ? 3 : 1;

			CardsDialog cd = new CardsDialog (mainframe, nrCards, selectedCommunityCards, usedCards);
			cd.setLocationRelativeTo (mainframe);
			cd.setVisible (true);
				
			Card[] newCards = cd.getCards ();
				
			if (newCards != null)
			{
				updateCommunityCards (selectedPokerType, newCards);
			
				setCommunityCardsContent ();
			}
		}
		
		private void updateCommunityCards (PokerType selectedPokerType, Card[] newCards)
		{
			if (selectedPokerType == PokerType.TEXAS_HOLDEM)
			{
				if (street == Street.FLOP)
				{
					holdemCommunityCards[0] = newCards[0];
					holdemCommunityCards[1] = newCards[1];
					holdemCommunityCards[2] = newCards[2];
				}
				else if (street == Street.TURN)
				{
					holdemCommunityCards[3] = newCards[0];
				}
				else if (street == Street.RIVER)
				{
					holdemCommunityCards[4] = newCards[0];
				}
			}
			else if (selectedPokerType == PokerType.OMAHA)
			{
				if (street == Street.FLOP)
				{
					omahaCommunityCards[0] = newCards[0];
					omahaCommunityCards[1] = newCards[1];
					omahaCommunityCards[2] = newCards[2];
				}
				else if (street == Street.TURN)
				{
					omahaCommunityCards[3] = newCards[0];
				}
				else if (street == Street.RIVER)
				{
					omahaCommunityCards[4] = newCards[0];
				}
			}
			else if (selectedPokerType == PokerType.OMAHA_HILO)
			{
				if (street == Street.FLOP)
				{
					omahaHiLoCommunityCards[0] = newCards[0];
					omahaHiLoCommunityCards[1] = newCards[1];
					omahaHiLoCommunityCards[2] = newCards[2];
				}
				else if (street == Street.TURN)
				{
					omahaHiLoCommunityCards[3] = newCards[0];
				}
				else if (street == Street.RIVER)
				{
					omahaHiLoCommunityCards[4] = newCards[0];
				}
			}
		}
	}

	private void setChoicesTableContent ()
	{
		PokerType gameType = getSelectedVariation ();
		
		int nrPlayersToFill = getCurrentPlayerCount ();
		
		TableModel model = choicesTable.getModel ();
		
		for (int i = 0; i < nrPlayersToFill; i++)
		{
			model.setValueAt (Integer.toString (i + 1), i, 0);
			
			PlayerProfile profile = null;
			if (gameType == PokerType.TEXAS_HOLDEM)
			{
				profile = holdemProfiles[i];
			}
			else if (gameType == PokerType.OMAHA)
			{
				profile = omahaProfiles[i];
			}
			else if (gameType == PokerType.OMAHA_HILO)
			{
				profile = omahaHiLoProfiles[i];
			}
			
			if (profile.getHandType () == HandType.RANDOM)
			{
				model.setValueAt (dictionary.getValue ("table.handoptions.handtype.random"), i, 1);
				model.setValueAt (" ", i, 2);
			}
			else if (profile.getHandType () == HandType.RANGE)
			{
				model.setValueAt (dictionary.getValue ("table.handoptions.handtype.range"), i, 1);
				model.setValueAt (Integer.toString (profile.getRange ().getPercentage ()) + " %", i, 2);
			}
			else if (profile.getHandType () == HandType.EXACTCARDS)
			{
				model.setValueAt (dictionary.getValue ("table.handoptions.handtype.exactcards"), i, 1);
				StringBuilder sb = new StringBuilder ();
				Card[] c = profile.getCards ();
				for (Card card : c)
				{
					sb.append (card.toString ());
				}
				model.setValueAt (sb.toString (), i, 2);
			}
		}
		
		for (int i = nrPlayersToFill; i < MAX_PLAYERS; i++)
		{
			model.setValueAt (" ", i, 0);
			model.setValueAt (" ", i, 1);
			model.setValueAt (" ", i, 2);
		}
	}
	
	private void setCommunityCardsContent ()
	{
		PokerType gameType = getSelectedVariation ();
		
		Card[] currentCommunityCards = null;
		
		if (gameType == PokerType.TEXAS_HOLDEM)
		{
			currentCommunityCards = holdemCommunityCards;
		}
		else if (gameType == PokerType.OMAHA)
		{
			currentCommunityCards = omahaCommunityCards;
		}
		else if (gameType == PokerType.OMAHA_HILO)
		{
			currentCommunityCards = omahaHiLoCommunityCards;
		}
		
		if (currentCommunityCards[0] != null)
		{
			enableFlop.setSelected (true);
			flopCard1.setIcon (new ImageIcon (getClass ().getResource (
									"images/cards/" + currentCommunityCards[0].toString () + ".gif")));
			flopCard2.setIcon (new ImageIcon (getClass ().getResource (
									"images/cards/" + currentCommunityCards[1].toString () + ".gif")));
			flopCard3.setIcon (new ImageIcon (getClass ().getResource (
									"images/cards/" + currentCommunityCards[2].toString () + ".gif")));
		}
		else
		{
			enableFlop.setSelected (false);
			flopCard1.setIcon (new ImageIcon (getClass ().getResource (blankCardPath)));
			flopCard2.setIcon (new ImageIcon (getClass ().getResource (blankCardPath)));
			flopCard3.setIcon (new ImageIcon (getClass ().getResource (blankCardPath)));
		}
		if (currentCommunityCards[3] != null)
		{
			enableTurn.setSelected (true);
			turnCard.setIcon (new ImageIcon (getClass ().getResource (
									"images/cards/" + currentCommunityCards[3].toString () + ".gif")));
		}
		else
		{
			enableTurn.setSelected (false);
			turnCard.setIcon (new ImageIcon (getClass ().getResource (blankCardPath)));
		}
		if (currentCommunityCards[4] != null)
		{
			enableRiver.setSelected (true);
			riverCard.setIcon (new ImageIcon (getClass ().getResource (
									"images/cards/" + currentCommunityCards[4].toString () + ".gif")));
		}
		else
		{
			enableRiver.setSelected (false);
			riverCard.setIcon (new ImageIcon (getClass ().getResource (blankCardPath)));
		}
	}
	
	private void setResultsTableContent ()
	{
		TableModel model = resultsTable.getModel ();
		
		if (simulator == null)
		{
			for (int i = 0; i < MAX_PLAYERS; i++)
			{
				model.setValueAt (" ", i, 0);
				model.setValueAt (" ", i, 1);
				model.setValueAt (" ", i, 2);
				model.setValueAt (" ", i, 3);
			}
			
			return;
		}
		
		SimulationFinalResult result = simulator.getResult ();
		int nrPlayersToFill = result.getNrOfPlayers ();

		for (int i = 0; i < nrPlayersToFill; i++)
		{
			model.setValueAt (Integer.toString (i + 1), i, 0);

			model.setValueAt (result.getFormattedWinPercentage (i) + " %", i, 1);
			model.setValueAt (result.getFormattedLosePercentage (i) + " %", i, 2);
			model.setValueAt (result.getFormattedTiePercentage (i) + " %", i, 3);
		}
		
		for (int i = nrPlayersToFill; i < MAX_PLAYERS; i++)
		{
			model.setValueAt (" ", i, 0);
			model.setValueAt (" ", i, 1);
			model.setValueAt (" ", i, 2);
			model.setValueAt (" ", i, 3);
		}
	}
	
	private void setGUIElementsRunning ()
	{		
		handTypeBox.setEnabled (false);
		variationBox.setEnabled (false);
		playerIDBox.setEnabled (false);
		viewGraphButton.setEnabled (false);
		exportButton.setEnabled (false);
		startButton.setEnabled (false);
		stopButton.setEnabled (true);
		playersCount.setEnabled (false);
		enableFlop.setEnabled (false);
		enableTurn.setEnabled (false);
		enableRiver.setEnabled (false);
		
		mainframe.setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
		
		StringBuilder sb = new StringBuilder ();
		sb.append (dictionary.getValue ("statusbar.running"));
		sb.append (" - ");
		sb.append (getCurrentPlayerCount ());
		sb.append (" Players");
		sb.append (" - ");
		sb.append (OptionsContainer.getOptionsContainer ().getRounds ());
		sb.append (" Rounds");
		sb.append (" - ");
		sb.append (simulator.getNrOfThreads ());
		sb.append (" Threads");
		
		statusBar.setText (sb.toString ());
	}
	
	private void setGUIElementsDone (boolean stopped)
	{
		handTypeBox.setEnabled (true);
		variationBox.setEnabled (true);
		exportButton.setEnabled (true);
		playerIDBox.setEnabled (true);
		startButton.setEnabled (true);
		stopButton.setEnabled (false);
		playersCount.setEnabled (true);
		enableFlop.setEnabled (true);
		enableTurn.setEnabled (true);
		enableRiver.setEnabled (true);
		
		mainframe.setCursor (Cursor.getDefaultCursor ());

		if (! stopped)
		{
			long duration = simulator.getResult ().getDuration ();
			double durationSeconds = duration / 1000.0;
			DecimalFormat df = new DecimalFormat ();
			df.setMaximumFractionDigits (2);
			df.setMinimumFractionDigits (2);

			statusBar.setText (dictionary.getValue ("statusbar.done") +
								" (" +
								df.format (durationSeconds) + " " +
								dictionary.getValue ("statusbar.seconds") + ")");
			
			viewGraphButton.setEnabled (true);
		}
		else
		{
			statusBar.setText (dictionary.getValue ("statusbar.stopped"));
			viewGraphButton.setEnabled (false);
		}
	}
	
	private class PlayerCountSpinnerListener implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			setChoicesTableContent ();
			
			adjustAvailablePlayerIDs ();
		}
	}
	
	private class ViewGraphListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			ResultChartDialog rcd = new ResultChartDialog (mainframe,
														"Simulation Results Graph",
														simulator.getResult ());
			rcd.setLocationRelativeTo (mainframe);
			rcd.setVisible (true);
		}
	}
	
	private class SelectButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			PokerType selectedPokerType = getSelectedVariation ();
			HandType selectedHandType = getSelectedHandType ();
			int selectedPlayer = playerIDBox.getSelectedIndex ();
			
			if (selectedHandType == HandType.RANGE)
			{
				if (selectedPokerType == PokerType.TEXAS_HOLDEM)
				{
					RangeDialog rd = new RangeDialog (mainframe, holdemProfiles[selectedPlayer].getRange ());
			
					rd.setLocationRelativeTo (mainframe);
					rd.setVisible (true);
					
					Range newRange = rd.getRange ();
					
					if (! rd.isCancelled ())
					{
						if (newRange.getPercentage () == 100)
						{
							holdemProfiles[selectedPlayer] = new PlayerProfile (HandType.RANDOM, null, null);
							
							setChoicesTableContent ();
						}
						else if (newRange.getPercentage () != 0)
						{
							holdemProfiles[selectedPlayer] = new PlayerProfile (HandType.RANGE, newRange, null);
							
							setChoicesTableContent ();
						}
					}			
				}
			}
			else if (selectedHandType == HandType.EXACTCARDS)
			{
				ArrayList<Card> usedCards = getUsedCards (selectedPokerType);

				Card[] playerCards = null;
				if (selectedPokerType == PokerType.TEXAS_HOLDEM)
				{
					playerCards = holdemProfiles[selectedPlayer].getCards ();
				}
				else if (selectedPokerType == PokerType.OMAHA)
				{
					playerCards = omahaProfiles[selectedPlayer].getCards ();
				}
				else if (selectedPokerType == PokerType.OMAHA_HILO)
				{
					playerCards = omahaHiLoProfiles[selectedPlayer].getCards ();
				}
				
				int nrOfRequestedCards = selectedPokerType == PokerType.TEXAS_HOLDEM ? 2 : 4;
				
				CardsDialog cd = new CardsDialog (mainframe, nrOfRequestedCards, playerCards, usedCards);
				cd.setLocationRelativeTo (mainframe);
				cd.setVisible (true);
				
				Card[] selectedCards = cd.getCards ();
				
				if (selectedCards != null)
				{
					PlayerProfile newProfile = new PlayerProfile (HandType.EXACTCARDS, null, selectedCards);
					
					if (selectedPokerType == PokerType.TEXAS_HOLDEM)
					{
						holdemProfiles[selectedPlayer] = newProfile;
					}
					else if (selectedPokerType == PokerType.OMAHA)
					{
						omahaProfiles[selectedPlayer] = newProfile;
					}
					else if (selectedPokerType == PokerType.OMAHA_HILO)
					{
						omahaHiLoProfiles[selectedPlayer] = newProfile;
					}
					
					setChoicesTableContent ();
				}
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
			else if (selectedHandType == HandType.RANDOM)
			{
				PokerType gameType = getSelectedVariation ();
				
				int player = playerIDBox.getSelectedIndex ();
				
				if (gameType == PokerType.TEXAS_HOLDEM)
				{
					holdemProfiles[player] = new PlayerProfile (HandType.RANDOM, null, null);
				}
				else if (gameType == PokerType.OMAHA)
				{
					omahaProfiles[player] = new PlayerProfile (HandType.RANDOM, null, null);
				}
				else if (gameType == PokerType.OMAHA_HILO)
				{
					omahaHiLoProfiles[player] = new PlayerProfile (HandType.RANDOM, null, null);
				}

				setChoicesTableContent ();
			}
		}
	}
	
	private class PokerTypeListener implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			setChoicesTableContent ();
			
			setCommunityCardsContent ();
		}
	}
	
	private class ResultXMLListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fc = new JFileChooser ();
			int returnVal = fc.showSaveDialog (mainframe);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile ();
				try (BufferedWriter bw = new BufferedWriter (new FileWriter (file)))
				{
					bw.write (SimulationExport.getResultXMLString (simulator.getResult ()));
				}
				catch (Exception ex)
				{
					GUIUtilities.showErrorDialog (mainframe, "Could not save the result", "Save Error");
				}
			}
		}
	}
	
	private void adjustAvailablePlayerIDs ()
	{
		playerIDBox.removeAllItems ();
		
		for (int i = 1; i <= getCurrentPlayerCount (); i++)
		{
			playerIDBox.addItem (new Integer (i));
		}
	}
	
	private int getCurrentPlayerCount ()
	{
		//playersCount might not yet be created (depends on the order of creation of panels)
		if (playersCount == null)
		{
			return 2;
		}
		else
		{
			Integer c = (Integer) playersCount.getValue ();
		
			return c.intValue ();
		}
	}

	private ArrayList<Card> getUsedCards (PokerType selectedVariation)
	{
		ArrayList<Card> usedCards = new ArrayList<> ();

		Card[] cards;
		if (selectedVariation == PokerType.TEXAS_HOLDEM)
		{
			for (int i = 0; i < MAX_PLAYERS; i++)
			{
				if (holdemProfiles[i].getHandType () != HandType.EXACTCARDS)
				{
					continue;
				}
				
				cards = holdemProfiles[i].getCards ();
				usedCards.add (cards[0]);
				usedCards.add (cards[1]);
			}
			
			for (int i = 0; i < 5; i++)
			{
				if (holdemCommunityCards[i] != null)
				{
					usedCards.add (holdemCommunityCards[i]);
				}
			}
		}
		else if (selectedVariation == PokerType.OMAHA)
		{
			for (int i = 0; i < MAX_PLAYERS; i++)
			{
				if (omahaProfiles[i].getHandType () != HandType.EXACTCARDS)
				{
					continue;
				}
				
				cards = omahaProfiles[i].getCards ();
				usedCards.add (cards[0]);
				usedCards.add (cards[1]);
				usedCards.add (cards[2]);
				usedCards.add (cards[3]);
			}
			
			for (int i = 0; i < 5; i++)
			{
				if (omahaCommunityCards[i] != null)
				{
					usedCards.add (omahaCommunityCards[i]);
				}
			}
		}
		else if (selectedVariation == PokerType.OMAHA_HILO)
		{
			for (int i = 0; i < MAX_PLAYERS; i++)
			{
				if (omahaHiLoProfiles[i].getHandType () != HandType.EXACTCARDS)
				{
					continue;
				}
				
				cards = omahaHiLoProfiles[i].getCards ();
				usedCards.add (cards[0]);
				usedCards.add (cards[1]);
				usedCards.add (cards[2]);
				usedCards.add (cards[3]);
			}
			
			for (int i = 0; i < 5; i++)
			{
				if (omahaHiLoCommunityCards[i] != null)
				{
					usedCards.add (omahaHiLoCommunityCards[i]);
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
}
