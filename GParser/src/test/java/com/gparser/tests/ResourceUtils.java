package com.gparser.tests;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * General utils for tests, regarding resources.
 * Created by Gilad Ber on 5/14/14.
 */
public class ResourceUtils
{
	public static String getResourceLocation(String baseLocation) throws UnsupportedEncodingException
	{
		URL originalUrl = ResourceUtils.class.getClassLoader().getResource(baseLocation);
		assert originalUrl != null;
		return URLDecoder.decode(originalUrl.getFile(), "UTF-8");
	}
}
