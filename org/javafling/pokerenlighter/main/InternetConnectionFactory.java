package org.javafling.pokerenlighter.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


/**
 * Contains factory methods for creating InternetConnection objects.
 * 
 * @author Radu Murzea
 * 
 * @version 1.0
 */
public class InternetConnectionFactory
{
	/**
	 * Creates a direct internet connection (no proxy) using the URL provided as parameter.
	 * 
	 * @param URLName the URL to which to connect
	 * 
	 * @return an InternetConnection object which can be used to retrieve the content at the specified URL.
	 * 
	 * @throws NullPointerException if the URL is null.
	 * @throws IllegalArgumentException if the URL is an empty String.
	 * @throws UnsupportedOperationException if the protocol of the URL is https (currently not supported).
	 * @throws IOException if there was an error while creating the InternetConnection object or if the
	 * URL has syntactic errors.
	 * 
	 * @since 1.0
	 */
	public static InternetConnection createDirectConnection (String URLName) throws IOException
	{
		if (URLName.isEmpty ())
		{
			throw new IllegalArgumentException ();
		}
		
		URL url = new URL (URLName);
		
		if (url.getProtocol ().equals ("https"))
		{
			throw new UnsupportedOperationException ();		
		}
		
		return new InternetConnection (url);
	}
	
	/**
	 * Creates an InternetConnection object that is using a proxy connection to retrieve the content
	 * from the specified URL.
	 * 
	 * @param URLName the URL to which to connect
	 * @param proxyHost the host of the proxy
	 * @param proxyPort the port of the proxy
	 * 
	 * @return an InternetConnection object which can be used to retrieve the content at the specified URL.
	 * 
	 * @throws NullPointerException if the URL or the proxy host are null.
	 * @throws UnsupportedOperationException if the protocol of the URL is https (currently not supported).
	 * @throws IllegalArgumentException if URL is an empty String or if proxyHost is an empty String or if
	 * the port is invalid (valid ports are unsigned 16-bit integers) or if proxyHost is incompatible with HTTP.
	 * @throws IOException if there was an error while creating the InternetConnection object or if the
	 * URL has syntactic errors.
	 * 
	 * @since 1.0
	 */
	public static InternetConnection createConnectionWithProxy (String URLName,
																String proxyHost,
																int proxyPort)
																throws IOException
	{
		if (URLName == null || proxyHost == null)
		{
			throw new NullPointerException ();
		}
		
		if (URLName.isEmpty () || proxyHost.isEmpty () || proxyPort < 1 || proxyPort > 65535)
		{
			throw new IllegalArgumentException ();
		}
		
		URL url = new URL (URLName);
		
		if (url.getProtocol ().equals ("https"))
		{
			throw new UnsupportedOperationException ();		
		}
		
		Proxy proxy = new Proxy (Proxy.Type.HTTP, new InetSocketAddress (proxyHost, proxyPort));
		
		return new InternetConnection (url, proxy);
	}
}
