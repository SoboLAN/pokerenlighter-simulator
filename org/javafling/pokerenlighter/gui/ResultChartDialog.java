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
import org.javafling.pokerenlighter.simulation.SimulationWorkerResult;
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
	private SimulationWorkerResult result;
	
	public ResultChartDialog (JFrame parent, String title, SimulationWorkerResult result)
	{
		super (parent, title, true);
		
		setLayout (new BorderLayout ());
		setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		
		this.result = result;
		
		ChartPanel chartPanel = buildChart (title);
		add (chartPanel, BorderLayout.CENTER);
		
		JButton btn = new JButton ("OK");
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
		//for 6 players: 7 x 30 pixels space zone and 6 x 85 pixels bar zone
		//so formula will be:
		//width = 85 x nr_players + 30 x (nr_players + 1)
		//additionally, 60 pixels should be added for accomodating metadata on the side
		//
		//final formula: 115 x nr_players + 90
		
		int width = 115 * result.getNrOfPlayers () + 90;
	
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
		
		categoryDataSet.addValue (27.9, "Wins", "Player 1");
		categoryDataSet.addValue (27.9, "Wins", "Player 2");
		categoryDataSet.addValue (43.1, "Wins", "Player 3");
		categoryDataSet.addValue (21.3, "Wins", "Player 4");
		categoryDataSet.addValue (21.3, "Wins", "Player 5");
		categoryDataSet.addValue (9.3, "Wins", "Player 6");
		
		categoryDataSet.addValue (74.2, "Loses", "Player 1");
		categoryDataSet.addValue (74.2, "Loses", "Player 2");
		categoryDataSet.addValue (36.1, "Loses", "Player 3");
		categoryDataSet.addValue (81.5, "Loses", "Player 4");
		categoryDataSet.addValue (81.5, "Loses", "Player 5");
		categoryDataSet.addValue (91.8, "Loses", "Player 6");
		
		categoryDataSet.addValue (4.7, "Ties", "Player 1");
		categoryDataSet.addValue (4.7, "Ties", "Player 2");
		categoryDataSet.addValue (2.2, "Ties", "Player 3");
		categoryDataSet.addValue (1.8, "Ties", "Player 4");
		categoryDataSet.addValue (1.8, "Ties", "Player 5");
		categoryDataSet.addValue (5.3, "Ties", "Player 6");
		
		return categoryDataSet;        
    }
	
	@Override
	public void actionPerformed (ActionEvent e)
	{
		dispose ();
	}
}
