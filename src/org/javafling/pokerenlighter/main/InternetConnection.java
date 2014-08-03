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
 * @author Radu Murzea
 * 
 * @version 1.0
 */
public class InternetConnection
{
    private URL url;
    private Proxy proxy;
    private HttpURLConnection connection;
    
    //container for caching the content
    private String content;
    
    /**
     * Creates an InternetConnection object. The URL must be a valid one.
     * @param url the URL from which to retrieve the content.
     * @throws NullPointerException if the parameter url is null.
     * @throws IOException if there is a problem making the connection
     */
    public InternetConnection(URL url) throws IOException
    {
        if (url == null) {
            throw new NullPointerException("url can not be null");
        }
        
        this.url = url;
        createConnection();
    }
    
    /**
     * Creates and InternetConnection object which uses a proxy to retrieve the content.
     * @param url the URL from which to retrieve the content.
     * @param proxy the proxy through which to route the connection.
     * @throws NullPointerException if any of the parameters are null.
     * @throws IOException if there is a problem making the connection
     */
    public InternetConnection(URL url, Proxy proxy) throws IOException
    {
        if (url == null || proxy == null) {
            throw new NullPointerException();
        }

        this.url = url;
        this.proxy = proxy;
        createConnection();
    }
    
    //creates the actual connection.
    private void createConnection() throws IOException
    {
        if (proxy == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection(proxy);
        }
            
        //some HTTP headers are necessary
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User Agent", "Mozilla/4.0 (compatible; JVM)");
        connection.setRequestProperty("Pragma", "no-cache");
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
     * 
     * @since 1.0
     */
    public String getContent()
    {
        if (content != null) {
            return content;
        }

        InputStreamReader is;
        try {
            connection.connect();
            
            InputStream input = connection.getResponseCode() == 200
                ? connection.getInputStream()
                : connection.getErrorStream();
            is = new InputStreamReader(input, Charset.forName("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(is)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return null;
        } finally {
            connection.disconnect();
        }
        
        content = sb.toString();
        return content;
    }
    
    /**
     * Cleares the cached content from this object. Calling getContent () after calling this method
     * will trigger another HTTP Request.
     * @since 1.0
     */
    public void clearContent()
    {
        content = null;
    }
}
