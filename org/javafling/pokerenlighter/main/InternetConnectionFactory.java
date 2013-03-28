package org.javafling.pokerenlighter.main;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;


/**
 *
 * @author Murzea Radu
 */
public class InternetConnectionFactory
{
	public static InternetConnection createDirectConnection (String URLName)
	{
		if (URLName.isEmpty ())
		{
			throw new IllegalArgumentException ();
		}
		
		URL url = createURL (URLName);
		
		if (url.getProtocol ().equals ("https"))
		{
			throw new UnsupportedOperationException ();		
		}
		
		return new InternetConnection (url);
	}
	
	public static InternetConnection createConnectionWithProxy (String URLName, String proxyHost, int proxyPort)
	{
		if (URLName.isEmpty () || proxyHost.isEmpty () || proxyPort < 1 || proxyPort > 65535)
		{
			throw new IllegalArgumentException ();
		}
		
		URL url = createURL (URLName);
		
		if (url.getProtocol ().equals ("https"))
		{
			throw new UnsupportedOperationException ();		
		}
		
		Proxy proxy = createProxy (proxyHost, proxyPort);
		
		return new InternetConnection (url, proxy);
	}
	
	private static URL createURL (String URLName)
	{
		URL url = null;
		try
		{
			url = new URL (URLName);
		}
		catch (MalformedURLException ex)
		{
			ex.printStackTrace ();
		}
		
		return url;
	}
	
	private static Proxy createProxy (String proxyHost, int proxyPort)
	{
		return new Proxy (Proxy.Type.HTTP, new InetSocketAddress (proxyHost, proxyPort));
	}
}
