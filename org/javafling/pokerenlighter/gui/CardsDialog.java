package org.javafling.pokerenlighter.gui;

import org.javafling.pokerenlighter.combination.Card;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Murzea Radu
 */
public class CardsDialog extends JDialog
{
	private JLabel[][] cardsLabels;
	private JLabel[] selectedLabels;
	
	private int requestedCards;
	
	public CardsDialog (JFrame parent, int nrOfRequestedCards)
	{
		super (parent, "Cards Dialog", true);
		
		requestedCards = nrOfRequestedCards;
		
		setResizable (false);
		setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
		
		JPanel cardsPanel = createCardsPanel ();
		JPanel selectedPanel = createSelectedPanel ();
		JPanel buttonsPanel = createButtonsPanel ();
		
		JPanel content = new JPanel (new BorderLayout ());
		content.add (cardsPanel, BorderLayout.NORTH);
		content.add (selectedPanel, BorderLayout.CENTER);
		content.add (buttonsPanel, BorderLayout.SOUTH);
		
		setContentPane (content);
		
		pack ();
	}
	
	private JPanel createCardsPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (4, 13, 5, 5));
		
		GUIUtilities.setBorder (panel, "Available Cards", TitledBorder.LEFT);

		cardsLabels = new JLabel[4][13];
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
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
				
				panel.add (cardsLabels[i][j]);
			}
		}
		
		return panel;
	}
	
	private JPanel createSelectedPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		panel.add (new JLabel ("Selected Cards:"));
		
		selectedLabels = new JLabel[requestedCards];
		
		for (int i = 0; i < requestedCards; i++)
		{
			selectedLabels[i] = new JLabel (
								new ImageIcon (
								getClass ().getResource ("images/blank.card.gif")));
			
			panel.add (selectedLabels[i]);
		}
		
		return panel;
	}
	
	private JPanel createButtonsPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		JButton okButton = new JButton ("OK");
		JButton cancelButton = new JButton ("Cancel");
		
		cancelButton.addActionListener (new CancelButtonListener ());
		
		panel.add (okButton);
		panel.add (cancelButton);
		
		return panel;
	}
	
	private class CancelButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispose ();
		}
	}
}
