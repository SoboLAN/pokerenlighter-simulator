package org.javafling.pokerenlighter.main;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Murzea Radu
 */
public class MD5Hasher
{
	public static String hash (String content)
	{
		if (content == null)
		{
			return null;
		}
	
		MessageDigest md = null;

		try
		{
			md = MessageDigest.getInstance ("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}

		md.reset ();

		try
		{
			md.update (content.getBytes ("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
		
		byte[] digest = md.digest ();
		BigInteger bigint = new BigInteger (1, digest);
		String hashtext = bigint.toString (16);

		while (hashtext.length () < 32)
		{
			hashtext = "0" + hashtext;
		}

		return hashtext;
	}
}
