package org.javafling.pokerenlighter.gui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.JLabel;

/**
 *
 * @author Murzea Radu
 */
public class JLabelLink extends JLabel
{
	private String URL;
	
	private static final String HREF_START = "<a href=\"";
    private static final String HREF_CLOSED = "\">";
    private static final String HREF_END = "</a>";
    private static final String HTML = "<html>";
    private static final String HTML_END = "</html>";
	
	public JLabelLink (String URL, String text)
	{
		super (text);
		
		this.URL = URL;
		
		if (isBrowsingSupported())
		{
			setText (linkIfy ());
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			addMouseListener(new LinkMouseListener());
		}
	}

	private boolean isBrowsingSupported()
	{
		return Desktop.isDesktopSupported () ? Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) : false;
	}

    private String linkIfy()
    {
		StringBuilder sb = new StringBuilder ();
		sb.append (HTML);
		sb.append (HREF_START);
		sb.append (URL);
		sb.append (HREF_CLOSED);
		sb.append (getText ());
		sb.append (HREF_END);
		sb.append (HTML_END);
		
		return sb.toString ();
	}
	
	private class LinkMouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			try
			{
				Desktop.getDesktop().browse(new URI(URL));
			}
			catch (Exception e) {  }
		}
	}
}
