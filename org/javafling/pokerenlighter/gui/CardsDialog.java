package org.javafling.pokerenlighter.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.javafling.pokerenlighter.combination.Card;

/**
 *
 * @author Murzea Radu
 */
public class CardsDialog extends JDialog
{
	private JLabel[][] cardsLabels;
	private JLabel[] selectedLabels;
	
	private boolean[][] labelStates;
	private boolean[] selectedStates;
	
	private Card[] selectedCards;
	
	private JButton okButton;
	
	public CardsDialog (JFrame parent, int nrOfRequestedCards, Card[] alreadySelectedCards, ArrayList<Card> usedCards)
	{
		super (parent, "Cards Dialog", true);
		
		setResizable (false);
		setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		
		//order is important here !!: createButtonsPanel must be BEFORE createSelectedPanel.
		//this is because createSelectedPanel will try to modify selected state of OK button,
		//so the button must exist
		JPanel buttonsPanel = createButtonsPanel ();
		JPanel cardsPanel = createCardsPanel (usedCards);
		JPanel selectedPanel = createSelectedPanel (nrOfRequestedCards, alreadySelectedCards);

		JPanel content = new JPanel (new BorderLayout ());
		content.add (cardsPanel, BorderLayout.NORTH);
		content.add (selectedPanel, BorderLayout.CENTER);
		content.add (buttonsPanel, BorderLayout.SOUTH);
		
		setContentPane (content);
		
		pack ();
	}
	
	private JPanel createCardsPanel (ArrayList<Card> usedCards)
	{
		labelStates = new boolean[4][13];
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
			{
				labelStates[i][j] = true;
			}
		}
		
		for (Card c : usedCards)
		{
			int rank = c.getRank () - 2;
			
			int color = 0;
			
			if (c.getColor () == 'c')
			{
				color = 0;
			}
			else if (c.getColor () == 'd')
			{
				color = 1;
			}
			else if (c.getColor () == 'h')
			{
				color = 2;
			}
			else if (c.getColor () == 's')
			{
				color = 3;
			}
			
			labelStates[color][rank] = false;
		}

		JPanel panel = new JPanel (new GridLayout (4, 13, 5, 5));
		
		GUIUtilities.setBorder (panel, "Available Cards", TitledBorder.LEFT);

		cardsLabels = new JLabel[4][13];
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
			{
				if (! labelStates[i][j])
				{
					cardsLabels[i][j] = new JLabel (
								new ImageIcon (
								getClass ().getResource (
								"images/blank.card.gif")));
				}
				else
				{
					char color = '.';

					switch (i)
					{
						case 0: color = 'c'; break;
						case 1: color = 'd'; break;
						case 2: color = 'h'; break;
						case 3: color = 's'; break;
					}

					cardsLabels[i][j] = new JLabel (
									new ImageIcon (
									getClass ().getResource (
									"images/cards/" + Card.getCharCard (j + 2) + color + ".gif")));
				}
				
				cardsLabels[i][j].addMouseListener (new MainLabelListener (i, j));
				
				panel.add (cardsLabels[i][j]);
			}
		}
		
		return panel;
	}
	
	private JPanel createSelectedPanel (int nrOfRequestedCards, Card[] alreadySelectedCards)
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		panel.add (new JLabel ("Selected Cards:"));
		
		selectedLabels = new JLabel[nrOfRequestedCards];
		selectedStates = new boolean[nrOfRequestedCards];
		
		selectedCards = checkCardsState (alreadySelectedCards) ? alreadySelectedCards : new Card[nrOfRequestedCards];
		
		for (int i = 0; i < nrOfRequestedCards; i++)
		{
			if (alreadySelectedCards == null)
			{
				selectedLabels[i] = new JLabel (
									new ImageIcon (
									getClass ().getResource ("images/blank.card.gif")));
			}
			else
			{				
				selectedLabels[i] = new JLabel (
									new ImageIcon (
									getClass ().getResource (
									"images/cards/" + alreadySelectedCards[i].toString () + ".gif")));
				
				selectedStates[i] = true;
			}
			
			selectedLabels[i].addMouseListener (new SelectedLabelListener (i));
				
			panel.add (selectedLabels[i]);
		}
		
		if (alreadySelectedCards != null)
		{
			okButton.setEnabled (true);
		}
		
		return panel;
	}
	
	private JPanel createButtonsPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		okButton = new JButton ("OK");
		JButton cancelButton = new JButton ("Cancel");
		
		cancelButton.addActionListener (new CancelListener ());
		okButton.addActionListener (new OKListener ());
		
		okButton.setEnabled (false);
		
		panel.add (okButton);
		panel.add (cancelButton);
		
		return panel;
	}
	
	public Card[] getCards ()
	{
		return checkCardsState (selectedCards) ? selectedCards : null;
	}
	
	private boolean checkCardsState (Card[] cards)
	{
		if (cards == null)
		{
			return false;
		}
		
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i] == null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private class CancelListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectedCards = null;
			
			dispose ();
		}
	}
	
	private class OKListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispose ();
		}
	}
	
	private class MainLabelListener extends MouseAdapter
	{
		private int row, column;
	
		MainLabelListener (int row, int column)
		{
			this.row = row;
			this.column = column;
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			//if card is unavailable, there's nothing to do
			if (! labelStates[row][column])
			{
				return;
			}
			
			for (int i = 0; i < selectedStates.length; i++)
			{
				//find the first empty spot in the row of the selected cards
				if (! selectedStates[i])
				{
					cardsLabels[row][column].setIcon (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
					labelStates[row][column] = false;
					
					char color = '.';
					switch (row)
					{
						case 0: color = 'c'; break;
						case 1: color = 'd'; break;
						case 2: color = 'h'; break;
						case 3: color = 's'; break;
					}
					
					Card newCard = new Card (column + 2, color);
					
					selectedLabels[i].setIcon (new ImageIcon (getClass ().getResource (
												"images/cards/" + newCard.toString () + ".gif")));
					
					selectedStates[i] = true;
					selectedCards[i] = newCard;
					
					break;
				}
			}
			
			checkCompleteness ();
		}
		
		private void checkCompleteness ()
		{
			boolean checkComplete = true;
			
			for (int i = 0; i < selectedStates.length; i++)
			{
				if (! selectedStates[i])
				{
					checkComplete = false;
					break;
				}
			}
			
			if (checkComplete)
			{
				okButton.setEnabled (true);
			}
		}
	}
	
	private class SelectedLabelListener extends MouseAdapter
	{
		private int column;
	
		SelectedLabelListener (int column)
		{
			this.column = column;
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			if (! selectedStates[column])
			{
				return;
			}
			
			Card existingCard = selectedCards[column];
			
			int row = 0;
			
			if (existingCard.getColor () == 'c')
			{
				row = 0;
			}
			else if (existingCard.getColor () == 'd')
			{
				row = 1;
			}
			else if (existingCard.getColor () == 'h')
			{
				row = 2;
			}
			else if (existingCard.getColor () == 's')
			{
				row = 3;
			}
			
			int bigColumn = existingCard.getRank () - 2;

			selectedLabels[column].setIcon (new ImageIcon (getClass ().getResource ("images/blank.card.gif")));
			selectedStates[column] = false;
			
			cardsLabels[row][bigColumn].setIcon (new ImageIcon (getClass ().getResource (
												"images/cards/" + existingCard.toString () + ".gif")));
			labelStates[row][bigColumn] = true;
			selectedCards[column] = null;
			
			okButton.setEnabled (false);
		}
	}
}
