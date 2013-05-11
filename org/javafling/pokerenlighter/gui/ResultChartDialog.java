package org.javafling.pokerenlighter.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public final class ResultChartDialog extends JDialog implements ActionListener
{
	private SimulationFinalResult result;
	
	public ResultChartDialog (JFrame parent, String title, SimulationFinalResult result)
	{
		super (parent, title, true);
		
		setLayout (new BorderLayout ());
		setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		
		this.result = result;
		
		ChartPanel chartPanel = buildChart (title);
		add (chartPanel, BorderLayout.CENTER);
		
		JButton btn = new JButton ("Close");
		btn.addActionListener (this);
		JPanel btnPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		btnPanel.add (btn);
		
		add (btnPanel, BorderLayout.SOUTH);
		
		pack ();
	}
	
	private ChartPanel buildChart (String title)
	{
		DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createBarChart(title,					//title of the chart
													"Players",					//X axis label
													"Percentage",				//Y axis label
													dataset,					//data
													PlotOrientation.VERTICAL,	//orientation (vert/horiz)
													true,						//whether or not legend is required
													true,						//generate tooltips ?
													false);						//generate URLs ?
		
		CategoryPlot plot = (CategoryPlot) chart.getPlot ();
		
		setBarColors (plot);

		if (OptionsContainer.getOptionsContainer ().getShowGraphLabels ())
		{
			setBarLabels (plot);
		}

        ChartPanel chartPanel = new ChartPanel(chart);
		
        //set size
		//for 6 players: 7 x 30 pixels space zone and 6 x 130 pixels bar zone
		//so formula will be:
		//width = 130 x nr_players + 30 x (nr_players + 1)
		//additionally, 60 pixels should be added for accomodating metadata on the side
		//
		//final formula: 160 x nr_players + 90
		
		int width = 160 * result.getNrOfPlayers () + 90;
	
        chartPanel.setPreferredSize(new Dimension(width, 450));
		
		return chartPanel;
	}
	
	//sets the colors of the bars according to the values found in the options file
	private void setBarColors (CategoryPlot plot)
	{
		BarRenderer renderer = (BarRenderer) plot.getRenderer ();
		
		OptionsContainer options = OptionsContainer.getOptionsContainer ();
				
		Color winsColor = new Color (options.getWinsRedValue (),
									options.getWinsGreenValue (),
									options.getWinsBlueValue ());
		Color losesColor = new Color (options.getLosesRedValue (),
									options.getLosesGreenValue (),
									options.getLosesBlueValue ());
		Color tieColor = new Color (options.getTiesRedValue (),
									options.getTiesGreenValue (),
									options.getTiesBlueValue ());
		
		renderer.setSeriesPaint (0, winsColor);
		renderer.setSeriesPaint (1, losesColor);
		renderer.setSeriesPaint (2, tieColor);
	}
	
	//display the values of the bars as a label on top of them
	private void setBarLabels (CategoryPlot plot)
	{
		CategoryItemRenderer categoryRenderer = plot.getRenderer ();
		
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator ("{2} %",
																					new DecimalFormat ("0.0"));

		categoryRenderer.setSeriesItemLabelGenerator (0, generator);
		categoryRenderer.setSeriesItemLabelGenerator (1, generator);
		categoryRenderer.setSeriesItemLabelGenerator (2, generator);

		categoryRenderer.setSeriesItemLabelsVisible (0, true);
		categoryRenderer.setSeriesItemLabelsVisible (1, true);
		categoryRenderer.setSeriesItemLabelsVisible (2, true);
	}
	
	private DefaultCategoryDataset createDataset()
	{
        DefaultCategoryDataset categoryDataSet = new DefaultCategoryDataset();
		
		DecimalFormat formatter = new DecimalFormat ();
		formatter.setMaximumFractionDigits (1);
		
		for (int i = 0; i < result.getNrOfPlayers (); i++)
		{
			StringBuilder playerLabel = new StringBuilder ("Player " + Integer.toString (i + 1));
			playerLabel.append (" (");
			
			HandType handType = result.getPlayer (i).getHandType ();
			if (handType == HandType.EXACTCARDS)
			{
				Card[] playerCards = result.getPlayer (i).getCards ();
				
				for (Card playerCard : playerCards)
				{
					playerLabel.append (playerCard.toString ());
				}
			}
			else if (handType == HandType.RANDOM)
			{
				playerLabel.append ("Random");
			}
			else if (handType == HandType.RANGE)
			{
				playerLabel.append (Integer.toString (result.getPlayer (i).getRange ().getPercentage ()));
				playerLabel.append ("% Range");
			}
			
			playerLabel.append (")");
			
			categoryDataSet.addValue (Double.parseDouble (formatter.format (result.getWinPercentage (i))),
									"Wins",
									playerLabel.toString ());

			categoryDataSet.addValue (Double.parseDouble (formatter.format (result.getLosePercentage (i))),
									"Loses",
									playerLabel.toString ());
			
			categoryDataSet.addValue (Double.parseDouble (formatter.format (result.getTiePercentage (i))),
									"Ties",
									playerLabel.toString ());
		}
		
		return categoryDataSet;        
    }
	
	@Override
	public void actionPerformed (ActionEvent e)
	{
		dispose ();
	}
}
