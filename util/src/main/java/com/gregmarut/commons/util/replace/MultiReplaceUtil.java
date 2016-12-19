package com.gregmarut.commons.util.replace;

import com.gregmarut.commons.util.KeyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class that assists in finding and replacing of strings for multiple values. Multiple strings will be returned for each combinations of values.
 *
 * @author Greg Marut
 */
public class MultiReplaceUtil
{
	/**
	 * The given text input will be searched for all instances of the keyValues and a new string will be returned with all possible combinations of the keys and values
	 *
	 * @param text
	 * @param keyValues
	 * @return
	 */
	public static List<String> replaceAll(final String text, final KeyValue<String, List<String>>... keyValues)
	{
		//add the keyvalues to a map
		Map<String, List<String>> keyValuesMap = new HashMap<String, List<String>>();
		
		//for each of the key values items in the array
		for (KeyValue<String, List<String>> keyValue : keyValues)
		{
			keyValuesMap.put(keyValue.getKey(), keyValue.getValue());
		}
		
		return replaceAll(text, keyValuesMap);
	}
	
	/**
	 * The given text input will be searched for all instances of the keyValues and a new string will be returned with all possible combinations of the keys and values
	 *
	 * @param text
	 * @param keyValuesMap
	 * @return
	 */
	public static List<String> replaceAll(final String text, final Map<String, List<String>> keyValuesMap)
	{
		//holds the list of strings to return
		final List<String> results = new ArrayList<String>();
		
		//for each of the entry sets in the map
		for (Map.Entry<String, List<String>> keyValue : keyValuesMap.entrySet())
		{
			//build a regex pattern for this key and apply the mather to the input
			final String regex = Pattern.quote(keyValue.getKey());
			Matcher matcher = Pattern.compile(regex).matcher(text);
			
			//check to see if a regex result was found in this string
			if (matcher.find())
			{
				//for each of the values in this entry
				for (String value : keyValue.getValue())
				{
					final String result = matcher.replaceAll(value);
					results.addAll(replaceAll(result, keyValuesMap));
				}
				
				return results;
			}
		}
		
		//add this input to the results
		results.add(text);
		
		return results;
	}
}
