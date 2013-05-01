package org.javafling.pokerenlighter.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public class PreferencesDialog
{
	private OptionsContainer options;
	
	private JFrame parent;
	
	private JDialog optionsdialog;
	
	private JPanel languagePanel, lnfPanel, roundsPanel, graphPanel, buttonsPanel;

	private JComboBox languageBox, lnfBox;
	
	private JSlider roundsSlider;
	private JTextField roundsText, winsCode, losesCode, tiesCode;
	private JCheckBox displayLabelCheckBox;
	private JSpinner[][] colorSpinners;
	private JLabel winsColorLabel, losesColorLabel, tiesColorLabel;
	
	private JButton okButton, cancelButton, applyButton;
	
	private int[][] colorValues;
	
	public PreferencesDialog (JFrame parent)
	{
		this.parent = parent;
		
		options = OptionsContainer.getOptionsContainer ();
	}
	
	public void display ()
	{
		optionsdialog = new JDialog (parent, "Preferences", true);
		optionsdialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
		
		languagePanel = createLanguagePanel ();
		lnfPanel = createlnfPanel ();
		roundsPanel = createRoundsPanel ();
		graphPanel = createGraphPanel ();
		buttonsPanel = createButtonsPanel ();
		
		JPanel mainPanel = new JPanel (new BorderLayout ());
		
		JPanel northPanel = new JPanel (new GridLayout (1, 2));
		
		northPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"General Options",
															TitledBorder.LEFT,
															TitledBorder.TOP));
		
		northPanel.add (languagePanel);
		northPanel.add (lnfPanel);
		
		mainPanel.add (northPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel (new BorderLayout ());
		centerPanel.add (roundsPanel, BorderLayout.NORTH);
		centerPanel.add (graphPanel, BorderLayout.CENTER);
		
		mainPanel.add (centerPanel, BorderLayout.CENTER);
		mainPanel.add (buttonsPanel, BorderLayout.SOUTH);

		optionsdialog.setContentPane (mainPanel);
		optionsdialog.pack ();
		optionsdialog.setResizable (false);

		optionsdialog.setLocationRelativeTo (parent);
		optionsdialog.setVisible (true);
	}
	
	private JPanel createLanguagePanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		panel.add (new JLabel ("Language:"));

		languageBox = new JComboBox (options.getAvailableLanguages ());
		languageBox.setSelectedItem (options.getLanguage ());
		languageBox.addItemListener (new GeneralChangeListener ());
		
		panel.add (languageBox);
		
		panel.add (new JLabel ("(needs restart)"));
		
		return panel;
	}
	
	private JPanel createlnfPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.RIGHT));
		
		panel.add (new JLabel ("Look and Feel:"));

		lnfBox = new JComboBox (options.getAvailableLookAndFeels ());
		lnfBox.setSelectedItem (options.getLookAndFeel ());
		lnfBox.addItemListener (new GeneralChangeListener ());
		
		panel.add (lnfBox);
		
		panel.add (new JLabel ("(needs restart)"));
		
		return panel;
	}
	
	private JPanel createRoundsPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"Simulation",
															TitledBorder.LEFT,
															TitledBorder.TOP));
		
		panel.add (new JLabel ("Rounds per Simulation:"));
		
		DecimalFormat df = new DecimalFormat ();
		df.setGroupingSize (3);
		
		roundsText = new JTextField (df.format (options.getRounds ()), 6);
		roundsText.setEditable (false);
		
		panel.add (roundsText);
		
		roundsSlider = new JSlider (JSlider.HORIZONTAL, 60, 200, options.getRounds () / 1000);
		
		roundsSlider.setMinorTickSpacing (5);
		roundsSlider.setMajorTickSpacing (20);
		roundsSlider.setPaintLabels (true);
		roundsSlider.setPaintTicks (true);
		
		roundsSlider.setPreferredSize (new Dimension (300, 50));
		
		roundsSlider.addChangeListener (new SliderChangeListener ());
		
		panel.add (roundsSlider);
		
		return panel;
	}
	
	private JPanel createGraphPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"Bar Graph Options",
															TitledBorder.CENTER,
															TitledBorder.TOP));
		
		JPanel colorsPanel = new JPanel (new GridLayout (3, 1));
		
		colorValues = new int[3][3];
		colorValues[0][0] = options.getWinsRedValue ();
		colorValues[0][1] = options.getWinsGreenValue ();
		colorValues[0][2] = options.getWinsBlueValue ();
		colorValues[1][0] = options.getLosesRedValue ();
		colorValues[1][1] = options.getLosesGreenValue ();
		colorValues[1][2] = options.getLosesBlueValue ();
		colorValues[2][0] = options.getTiesRedValue ();
		colorValues[2][1] = options.getTiesGreenValue ();
		colorValues[2][2] = options.getTiesBlueValue ();
		
		colorSpinners = new JSpinner[3][3];
		
		//start of WINS panel
		
		JPanel winsPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		winsPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"Wins",
															TitledBorder.LEFT,
															TitledBorder.TOP));
	
		SpinnerNumberModel winsRedModel = new SpinnerNumberModel (colorValues[0][0], 0, 255, 1);
		colorSpinners[0][0] = new JSpinner (winsRedModel);
		((DefaultEditor) colorSpinners[0][0].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel winsGreenModel = new SpinnerNumberModel (colorValues[0][1], 0, 255, 1);
		colorSpinners[0][1] = new JSpinner (winsGreenModel);
		((DefaultEditor) colorSpinners[0][1].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel winsBlueModel = new SpinnerNumberModel (colorValues[0][2], 0, 255, 1);
		colorSpinners[0][2] = new JSpinner (winsBlueModel);
		((DefaultEditor) colorSpinners[0][2].getEditor ()).getTextField ().setEditable (false);
		
		winsPanel.add (new JLabel ("Red:"));
		winsPanel.add (colorSpinners[0][0]);
		winsPanel.add (new JLabel ("Green:"));
		winsPanel.add (colorSpinners[0][1]);
		winsPanel.add (new JLabel ("Blue:"));
		winsPanel.add (colorSpinners[0][2]);
		
		winsCode = new JTextField (6);
		winsCode.setText (Integer.toHexString (colorValues[0][0]).toUpperCase () +
						Integer.toHexString (colorValues[0][1]).toUpperCase () +
						Integer.toHexString (colorValues[0][2]).toUpperCase ());
		winsCode.setEditable (false);
		
		winsPanel.add (new JLabel ("Color Code:"));
		winsPanel.add (winsCode);

		winsColorLabel = new JLabel (" ");
		winsColorLabel.setOpaque (true);
		winsColorLabel.setPreferredSize (new Dimension (40, 30));
		winsColorLabel.setBackground (new Color (38, 206, 70));
		
		winsPanel.add (winsColorLabel);
		
		colorsPanel.add (winsPanel);
		
		//end of WINS panel
		
		//start of LOSES panel
		
		JPanel losesPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		losesPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"Loses",
															TitledBorder.LEFT,
															TitledBorder.TOP));

		SpinnerNumberModel losesRedModel = new SpinnerNumberModel (colorValues[1][0], 0, 255, 1);
		colorSpinners[1][0] = new JSpinner (losesRedModel);
		((DefaultEditor) colorSpinners[1][0].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel losesGreenModel = new SpinnerNumberModel (colorValues[1][1], 0, 255, 1);
		colorSpinners[1][1] = new JSpinner (losesGreenModel);
		((DefaultEditor) colorSpinners[1][1].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel losesBlueModel = new SpinnerNumberModel (colorValues[1][2], 0, 255, 1);
		colorSpinners[1][2] = new JSpinner (losesBlueModel);
		((DefaultEditor) colorSpinners[1][2].getEditor ()).getTextField ().setEditable (false);
		
		losesPanel.add (new JLabel ("Red:"));
		losesPanel.add (colorSpinners[1][0]);
		losesPanel.add (new JLabel ("Green:"));
		losesPanel.add (colorSpinners[1][1]);
		losesPanel.add (new JLabel ("Blue:"));
		losesPanel.add (colorSpinners[1][2]);
		
		losesCode = new JTextField (6);
		losesCode.setText (Integer.toHexString (colorValues[1][0]).toUpperCase () +
						Integer.toHexString (colorValues[1][1]).toUpperCase () +
						Integer.toHexString (colorValues[1][2]).toUpperCase ());
		losesCode.setEditable (false);
		
		losesPanel.add (new JLabel ("Color Code:"));
		losesPanel.add (losesCode);

		losesColorLabel = new JLabel (" ");
		losesColorLabel.setOpaque (true);
		losesColorLabel.setPreferredSize (new Dimension (40, 30));
		losesColorLabel.setBackground (new Color (220, 45, 47));
		
		losesPanel.add (losesColorLabel);
		
		colorsPanel.add (losesPanel);
		
		//end of LOSES panel
		
		//start of TIES panel
		
		JPanel tiesPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		tiesPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
															"Ties",
															TitledBorder.LEFT,
															TitledBorder.TOP));

		SpinnerNumberModel tiesRedModel = new SpinnerNumberModel (colorValues[2][0], 0, 255, 1);
		colorSpinners[2][0] = new JSpinner (tiesRedModel);
		((DefaultEditor) colorSpinners[2][0].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel tiesGreenModel = new SpinnerNumberModel (colorValues[2][1], 0, 255, 1);
		colorSpinners[2][1] = new JSpinner (tiesGreenModel);
		((DefaultEditor) colorSpinners[2][1].getEditor ()).getTextField ().setEditable (false);
		
		SpinnerNumberModel tiesBlueModel = new SpinnerNumberModel (colorValues[2][2], 0, 255, 1);
		colorSpinners[2][2] = new JSpinner (tiesBlueModel);
		((DefaultEditor) colorSpinners[2][2].getEditor ()).getTextField ().setEditable (false);
		
		tiesPanel.add (new JLabel ("Red:"));
		tiesPanel.add (colorSpinners[2][0]);
		tiesPanel.add (new JLabel ("Green:"));
		tiesPanel.add (colorSpinners[2][1]);
		tiesPanel.add (new JLabel ("Blue:"));
		tiesPanel.add (colorSpinners[2][2]);
		
		tiesCode = new JTextField (6);
		tiesCode.setText (Integer.toHexString (colorValues[2][0]).toUpperCase () +
						Integer.toHexString (colorValues[2][1]).toUpperCase () +
						Integer.toHexString (colorValues[2][2]).toUpperCase ());
		tiesCode.setEditable (false);
		
		tiesPanel.add (new JLabel ("Color Code:"));
		tiesPanel.add (tiesCode);
		
		Color tiesColor = new Color (70, 94, 91);
		
		tiesColorLabel = new JLabel (" ");
		tiesColorLabel.setOpaque (true);
		tiesColorLabel.setPreferredSize (new Dimension (40, 30));
		tiesColorLabel.setBackground (tiesColor);
		
		tiesPanel.add (tiesColorLabel);
		
		colorsPanel.add (tiesPanel);
		
		//end of TIES panel
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				colorSpinners[i][j].addChangeListener (new SpinnerChangeListener (i, j));
			}
		}
		
		panel.add (colorsPanel, BorderLayout.CENTER);
		
		JPanel percentagePanel = new JPanel (new FlowLayout (FlowLayout.LEFT));

		displayLabelCheckBox = new JCheckBox ();
		displayLabelCheckBox.setSelected (true);
		displayLabelCheckBox.addItemListener (new GeneralChangeListener ());
		
		percentagePanel.add (displayLabelCheckBox);
		
		percentagePanel.add (new JLabel ("Display Percentage Label"));
		
		panel.add (percentagePanel, BorderLayout.SOUTH);
	
		return panel;
	}
	
	private JPanel createButtonsPanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		okButton = new JButton ("OK");
		cancelButton = new JButton ("Cancel");		
		applyButton = new JButton ("Apply");
		
		applyButton.setEnabled (false);
		
		panel.add (okButton);
		panel.add (cancelButton);
		panel.add (applyButton);
		
		cancelButton.addActionListener (new ActionListener ()
		{
			@Override
			public void actionPerformed (ActionEvent a)
			{
				optionsdialog.dispose ();
			}
		});
		
		applyButton.addActionListener (new ButtonSaveListener (false));
		okButton.addActionListener (new ButtonSaveListener (true));

		return panel;
	}
	
	private class ButtonSaveListener implements ActionListener
	{
		private boolean dispose;
		
		public ButtonSaveListener (boolean dispose)
		{
			this.dispose = dispose;
		}
		
		@Override
		public void actionPerformed (ActionEvent e)
		{
			options.setLanguage (languageBox.getSelectedIndex ());
			options.setLookAndFeel (lnfBox.getSelectedIndex ());
			options.setRounds (roundsSlider.getValue ());
			options.setWinsRed (colorValues[0][0]);
			options.setWinsGreen (colorValues[0][1]);
			options.setWinsBlue (colorValues[0][2]);
			options.setLosesRed (colorValues[1][0]);
			options.setLosesGreen (colorValues[1][1]);
			options.setLosesBlue (colorValues[1][2]);
			options.setTiesRed (colorValues[2][0]);
			options.setTiesGreen (colorValues[2][1]);
			options.setTiesBlue (colorValues[2][2]);
			options.setDisplayLabel (displayLabelCheckBox.isSelected ());
			
			options.saveConfiguration ();
			
			if (dispose)
			{
				optionsdialog.dispose ();
			}
			else
			{
				applyButton.setEnabled (false);
			}
		}
	}
	
	private class SliderChangeListener implements ChangeListener
	{
		private DecimalFormat formatter;
		
		public SliderChangeListener ()
		{
			formatter = new DecimalFormat ();
			formatter.setGroupingSize (3);
		}
		
		@Override
		public void stateChanged(ChangeEvent e)
		{
			int newValue = 1000 * roundsSlider.getValue ();
			
			roundsText.setText (formatter.format (newValue));
			
			applyButton.setEnabled (true);
		}		
	}
	
	private class SpinnerChangeListener implements ChangeListener
	{
		private int row, column;
		
		public SpinnerChangeListener (int row, int column)
		{
			this.row = row;
			this.column = column;
		}
		
		@Override
		public void stateChanged (ChangeEvent e)
		{
			colorValues[row][column] = (Integer) colorSpinners[row][column].getValue ();
			
			switch (row)
			{
				case 0:
				{
					winsCode.setText (Integer.toHexString (colorValues[0][0]).toUpperCase () +
									Integer.toHexString (colorValues[0][1]).toUpperCase () +
									Integer.toHexString (colorValues[0][2]).toUpperCase ());
					
					winsColorLabel.setBackground (new Color (colorValues[0][0],
															colorValues[0][1],
															colorValues[0][2]));
					
					break;
				}
				case 1:
				{
					losesCode.setText (Integer.toHexString (colorValues[1][0]).toUpperCase () +
									Integer.toHexString (colorValues[1][1]).toUpperCase () +
									Integer.toHexString (colorValues[1][2]).toUpperCase ());
					
					losesColorLabel.setBackground (new Color (colorValues[1][0],
															colorValues[1][1],
															colorValues[1][2]));
					
					break;
				}
				case 2:
				{
					tiesCode.setText (Integer.toHexString (colorValues[2][0]).toUpperCase () +
									Integer.toHexString (colorValues[2][1]).toUpperCase () +
									Integer.toHexString (colorValues[2][2]).toUpperCase ());
					
					tiesColorLabel.setBackground (new Color (colorValues[2][0],
															colorValues[2][1],
															colorValues[2][2]));
					
					break;
				}
			}
			
			applyButton.setEnabled (true);
		}
	}

	private class GeneralChangeListener implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			applyButton.setEnabled (true);
		}
	}
}
