package org.javafling.pokerenlighter.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

/** Provides basic functionality for retrieving content from a URL (typically HTML).
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public class InternetConnection
{
	private URL url;
	private Proxy proxy;
	private HttpURLConnection connection;
	private String content;
	
	public InternetConnection (URL url)
	{
		this.url = url;
		createConnection ();
	}
	
	public InternetConnection (URL url, Proxy proxy)
	{
		this.url = url;
		this.proxy = proxy;
		createConnection ();
	}
	
	private void createConnection ()
	{
		connection = null;
		try
		{
			if (proxy == null)
			{
				connection = (HttpURLConnection) url.openConnection ();
			}
			else
			{
				connection = (HttpURLConnection) url.openConnection (proxy);
			}
			connection.setRequestMethod ("GET");
			connection.setRequestProperty ("User Agent", "Mozilla/4.0 (compatible; JVM)");
			connection.setRequestProperty ("Pragma", "no-cache");
		}
		catch (IOException ex)
		{
			ex.printStackTrace ();
		}
	}
	
	/** It will perform the actual connection and return the contents.
	 * <br />
	 * Note that the content is cached after the first call of this method. Subsequent calls
	 * will not make another HTTP request; instead, it will return the cached content. In order to force
	 * this method to make another connection, you must clear the cache. You can do this by
	 * calling the clearContent() method.
	 * <br />
	 * NOTE: This method assumes that the content retrieved will be encoded as UTF-8, so it will treat it
	 * as such.
	 * <br />
	 * WARNING: Depending on the Internet connection and the length of the content, it may take a while
	 * for this method to return the content. In most situations, it should not exceed 2000 ms.
	 * Therefore, it is advised to call this on a separate/dedicated thread.
	 * 
	 * @return the content found at the URL of this connection. If there is a problem while retrieving
	 * the content, null is returned.
	 */
	public String getContent ()
	{
		if (content != null)
		{
			return content;
		}

		InputStreamReader is;
		try
		{
			connection.connect ();
			
			InputStream input = connection.getResponseCode () == 200 ? connection.getInputStream () : connection.getErrorStream ();
			is = new InputStreamReader (input, Charset.forName ("UTF-8"));
		}
		catch (Exception e)
		{
			return null;
		}
		
		StringBuilder sb = new StringBuilder ();

		try (BufferedReader reader = new BufferedReader (is))
		{
			String line;
			while ((line = reader.readLine ()) != null)
			{
				sb.append (line);
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			connection.disconnect ();
		}
		
		content = sb.toString ();
		return content;
	}
	
	public void clearContent ()
	{
		content = null;
	}
}
