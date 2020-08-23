package com.lite.core.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Util {
	
	protected static TimeZone defaultTimeZone = null;
	protected static SimpleDateFormat defaultDateFormatter = null;
	public static long milliscondsForDay = 86400000L;

	/**
	 * Parse Date, return null if failed
	 * @param text
	 * @return
	 */
	public static Date tryParseDate(String text, SimpleDateFormat formatter) {
		if (text == null)
			return null;
		try {
			return formatter.parse(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * parse Date, return null if failed
	 * @param pattern e.g. yyyy-MM-dd HH:mm:ss
	 * @param timeZone e.g. IST/CST/GMT
	 * @return
	 */
	public static Date tryParseDate(String text,String pattern, String timeZone) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return tryParseDate(text, dateFormatter);
	}
	
	/**
	 * Format Date
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, SimpleDateFormat formatter) {
		return date == null ? "" : formatter.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param format e.g. yyyy-MM-dd HH:mm:ss
	 * @param timeZone e.g. GMT,IST,Asia/Shanghai
	 * @return
	 */
	public static String formatDate(Date date, String format, String timeZone) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatDate(date, formatter);
	}

	/**
	 * Get date delta
	 */
	public static Date getDate(Date date, String delta, int sign) {
		if (date == null)
			date = new Date();
		return new Date(date.getTime() + Util.decodeTime(delta) * sign);
	}
	
	/**
	 * Create and init value for array
	 * @param array
	 * @param val
	 */
	public static <T> T[] arrayCreate(Class<T> klass, int len, T val) {
		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(klass, len);
		for (int i=0; i<array.length; i++) {
			array[i] = val;
		}
		return array;
	}
	
	/**
	 * Init value for array
	 * @param array
	 * @param val
	 */
	public static <T> void arrayInit(T[] array, T val) {
		for (int i=0; i<array.length; i++) {
			array[i] = val; 
		}
	}
	
	/**
	 * Whole array to string, join with delimiter
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static <T> String arrayJoin(T[] array, String delimiter) {
		return arrayJoin(array, 0, array.length, delimiter, null);
	}
	
	/**
	 * Whole array to string, join with delimiter
	 * @param array
	 * @param delimiter
	 * @return
	 */
	public static <T> String arrayJoin(T[] array, String delimiter, String fillForNull) {
		return arrayJoin(array, 0, array.length, delimiter, fillForNull);
	}
	
	/**
	 * Array to string, with offset and length, join with delimiter
	 * @param array
	 * @param offset
	 * @param len
	 * @param delimiter
	 * @return
	 */
	public static <T> String arrayJoin(T[] array, int offset, int len, String delimiter, String fillForNull) {
		if (array == null || array.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i=offset; i<offset+len && i<array.length; i++) {
			if (i > offset)
				sb.append(delimiter);
			if (array[i] != null)
				sb.append(array[i]);
			else if (fillForNull != null)
				sb.append(fillForNull);
		}
		return sb.toString();
	}

	/**
	 * Trim the string array
	 */
	public static void arrayTrim(String[] array) {
		if (Util.isNullOrEmpty(array))
			return;
		for (int i=0; i<array.length; i++) {
			if (Util.isNullOrEmpty(array[i]))
				continue;
			array[i] = array[i].replaceFirst("^[ \\s\\t\\r\\n]+", "").replaceFirst("[ \\s\\t\\r\\n]$", "");
		}
	}
	
	/**
	 * Search val in array and return index, return -1 if not found
	 * @param array
	 * @param val
	 * @return
	 */
	public static <T> int arraySearch(T[] array, Object val) {
		if (array == null || array.length == 0)
			return -1;
		for (int i=0; i<array.length; i++) {
			if (val == null ? array[i] == null : val.equals(array[i]))
				return i;
		}
		return -1;
	}

	/**
	 * Concat multiple arrays
	 * @param first
	 * @param rest
	 * @return
	 */
	public static <T> T[] arrayConcat(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			if (array == null)
				continue;
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			if (array == null)
				continue;
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	/**
	 * Parse int, return null if failed
	 * @param text
	 * @return
	 */
	public static Integer tryParseInt(String text) {
		try {
			return Integer.parseInt(text.replaceAll(",", "").replaceFirst("\\.0+$", ""));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Integer tryParseInt(String text, boolean roundFloat) {
		try {
			Double num = tryParseDouble(text);
			if (num == null)
				return null;
			else
				return (int)Math.round(num);
				
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Parse long, return null if failed
	 * @param text
	 * @return
	 */
	public static Long tryParseLong(String text) {
		try {
			return Long.parseLong(text.replaceAll(",", ""));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parse Float, return null if failed
	 * @param text
	 * @return
	 */
	public static Float tryParseFloat(String text) {
		try {
			return text.endsWith("%") ? 
					Float.parseFloat(text.substring(0, text.length() - 1).replaceAll(",", "")) / 100 : 
					Float.parseFloat(text.replaceAll(",", ""));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Parse Double, return null if failed
	 * @param text
	 * @return
	 */
	public static Double tryParseDouble(String text) {
		try {
			return text.endsWith("%") ? 
					Double.parseDouble(text.substring(0, text.length() - 1).replaceAll(",", "")) / 100 : 
					Double.parseDouble(text.replaceAll(",", ""));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Double tryParseDouble(Object ojb, double defaultValue) {
		if (ojb == null)
			return defaultValue;
		else if (ojb instanceof Integer) {
			return 0.0 + (Integer)ojb;
		} else if (ojb instanceof Double)
			return (Double)ojb;
		else if (ojb instanceof String) {
			return (Double)notEmptyOtherwise(tryParseDouble((String)ojb), defaultValue);
		} else
			return defaultValue;
	}

	/**
	 * Format double to string, keep integer format if possible
	 * @param val
	 * @param decimalPlaces
	 * @return
	 */
	public static String formatDouble(Double val, int decimalPlaces) {
		return formatDouble(val, decimalPlaces, null);
	}
	
	/**
	 * Format double to string, keep integer format if possible
	 * @param val
	 * @param decimalPlaces
	 * @return
	 */
	public static String formatDouble(Double val, int decimalPlaces, String valForNull) {
		if (val == null)
			return valForNull;
		if (val == val.longValue())
	        return String.format("%d", val.longValue());
	    else
	        return String.format("%." + decimalPlaces + "f", val);
	}
	
	/**
	 * Round double to decimal places
	 */
	public static double roundDouble(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * Whether object is null or empty
	 * @param val
	 * @return
	 */
	public static boolean isNullOrEmpty(Object val) {
		return null == val || (val instanceof String) && ((String)val).isEmpty() || 
				(val.getClass().isArray()) && Array.getLength(val) == 0 ||
				(val instanceof Collection<?>) && ((Collection<?>)val).isEmpty() ||
				(val instanceof Map<?,?>) && ((Map<?,?>)val).isEmpty();
	}
	
	/**
	 * Whether object is not empty
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(Object val) {
		return !isNullOrEmpty(val);
	}
	
	/**
	 * return val if not empty, otherwise return val2
	 * @param val
	 * @param val2
	 * @return
	 */
	public static Object notEmptyOtherwise(Object val, Object val2) {
		return isNotEmpty(val) ? val : val2;
	}
	/**
	 * return val1 if not null, otherwise return val2
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static <T> T notNullOtherwise(T val1, T val2) {
		return null != val1 ? val1 : val2;
	}
	
	/**
	 * Whether a equals b
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		return (a == null) ? (b == null) : a.equals(b);
	}
	
	/**
	 * Compare, treat null as smaller 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<? super T>> int compare(T a, T b) {
		return compare(a, b, true, true);
	}

	/**
	 * Compare, treat null as smaller 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<? super T>> int compare(T a, T b, boolean asc, boolean nullToTop) {
		if (a == null) {
			return (b == null) ? 0 : (nullToTop ? -1 : 1);
		} else {
			return (b == null) ? (nullToTop ? 1 : -1) : (asc ? 1 : -1) * a.compareTo(b);
		}
	}

	/**
	 * Compare two object
	 */
	@SuppressWarnings("unchecked")
	public static int compareObject(Object a, Object b, boolean asc, boolean nullToTop) {
		return Util.compare((Comparable)a, (Comparable)b, asc, nullToTop);
	}
	
	/**
	 * String to camel case
	 * @param init
	 * @return
	 */
	public static String toCamelCase(String str, boolean capitalizeFirst, boolean lowerTheRest) {
	    if (str == null)
	        return null;

	    StringBuilder sb = new StringBuilder(str.length());

	    for (final String word : str.split("[ \\_\\-]")) {
	    	if (word.isEmpty())
	    		continue;
            sb.append(sb.length() == 0 && !capitalizeFirst ? word.substring(0, 1).toLowerCase() : word.substring(0, 1).toUpperCase());
            sb.append(lowerTheRest ? word.substring(1).toLowerCase() : word.substring(1));
	    }

	    return sb.toString();
	}

	/**
	 * String to snake case
	 * @param init
	 * @return
	 */
	public static String toSnakeCase(String str, boolean underscoreBeforeNonLetter) {
	    if (str == null)
	        return null;

	    StringBuilder sb = new StringBuilder();
	    int state = 0; // 0 uppercase, 1 lowercase, 2 nonletter
	    char c;
	    for (int i=0; i<str.length(); i++) {
	    	c = str.charAt(i);
	    	if (Character.isUpperCase(c)) {
	    		if (state != 0 && sb.length() > 0)
	    			sb.append('_');
	    		state = 0;
	    	} else if (Character.isLowerCase(c)) {
	    		state = 1;
	    	} else {
	    		if (state != 2 && underscoreBeforeNonLetter && sb.length() > 0)
	    			sb.append('_');
	    		state = 2;
	    	}
	    	sb.append(Character.toLowerCase(c));
	    }

	    return sb.toString();
	}
	
	/**
	 * To all capitalized string
	 * @param str
	 * @return
	 */
	public static String toAllCapital(String str) {
	    if (str == null)
	        return null;

	    StringBuilder sb = new StringBuilder();
	    int state = 0; // 0 uppercase, 1 lowercase, 2 nonletter
	    char c;
	    for (int i=0; i<str.length(); i++) {
	    	c = str.charAt(i);
	    	if (Character.isUpperCase(c)) {
	    		if (state != 0 && sb.length() > 0)
	    			sb.append(' ');
	    		state = 0;
	    	} else if (Character.isLowerCase(c)) {
	    		state = 1;
	    	} else {
	    		if (state != 2 && sb.length() > 0)
	    			sb.append(' ');
	    		state = 2;
	    	}
	    	sb.append(sb.length() == 0 ? Character.toUpperCase(c) : c);
	    }

	    return sb.toString();
	}

	/**
	 * To all capitalized string, without deleting characters
	 */
	public static String toAllCapitalSimple(String str) {
	    if (str == null)
	        return null;
	    char[] chars = str.toCharArray();
	    boolean isFirst = true;
	    for (int i=0; i<chars.length; i++) {
	    	if (Character.isLetterOrDigit(chars[i])) {
	    		if (isFirst)
		    		chars[i] = Character.toUpperCase(chars[i]);
	    		else
		    		chars[i] = Character.toLowerCase(chars[i]);
	    		isFirst = false;
	    	} else {
	    		isFirst = true;
	    	}
	    }
	    return new String(chars);
	}
	
	/**
	 * Key value pair
	 * @param <K>
	 * @param <V>
	 */
	public static class Pair<K, V>
	{
	    public K key;
	    public V value;

	    public Pair(K key, V value)
	    {
	        this.key = key;
	        this.value = value;
	    }
	    
	    public String toString() {
	    	return "" + key + "=" + value;
	    }
	}

	/**
	 * HashMap to sorted list, asc or desc, truncate to length
	 * @param hash
	 * @param desc
	 * @param len
	 * @return
	 */
	public static <T> List<Util.Pair<T, Double>> sortHashMap(Map<T, Double> hash, Boolean desc, int offset, int len) {
		LinkedList<Util.Pair<T, Double>> list = new LinkedList<Util.Pair<T, Double>>();
		for (Map.Entry<T, Double> entry : hash.entrySet()) {
			list.add(new Util.Pair<T, Double>(entry.getKey(), entry.getValue()));
		}
		list.sort(new Comparator<Util.Pair<T, Double>>() {
			@Override
			public int compare(Util.Pair<T, Double> p1, Util.Pair<T, Double> p2){
	            return p2.value.compareTo(p1.value) * (desc ? 1 : -1);
	        }
		});
		return list.size() <= len ? list : list.subList(offset, offset + len);
	}
	
	/**
	 * LashSet to sorted list, asc or desc, truncate to length
	 * @param hash
	 * @param desc
	 * @param len
	 * @return
	 */
	public static <T extends Comparable<T>> List<T> sortHashSet(HashSet<T> set, Boolean desc, int offset, int len) {
		LinkedList<T> list = new LinkedList<T>();
		list.addAll(set);
		list.sort(new Comparator<T>() {
			@Override
			public int compare(T p1, T p2) {
	            return p2.compareTo(p1) * (desc ? 1 : -1);
	        }
		});
		return list.size() <= len ? list : list.subList(offset, offset + len);
	}

	/**
	 * Format HashMap to string
	 * @param map
	 * @param sb
	 * @param prefix
	 * @param delimiter
	 * @param postfix
	 */
	public static <T,P> void toFormattedString(HashMap<T,P> map, StringBuilder sb, String prefix, String delimiter, String postfix) {
		P val;
		for (Map.Entry<T, P> entry : new TreeMap<T, P>(map).entrySet()) {
			sb.append(prefix).append(entry.getKey()).append(delimiter);
			if ((val = entry.getValue()) != null)
				sb.append(val);
			sb.append(postfix);
		}
    }

	/**
	 * Tokenize text with default delimiters
	 * @param text
	 * @return
	 */
	public static String[] tokenizeText(String text) {
		return tokenizeText(text, "[^a-zA-Z0-9\\.\\-\\_\\~\\>\\/]+");
	}

	/**
	 * Tokenize text with default delimiters
	 * @param text
	 * @param delimiter
	 * @return
	 */
	public static String[] tokenizeText(String text, String delimiter) {
		if (text == null)
			return new String[] {};
		text = text.replaceFirst("^" + delimiter, "").replaceFirst(delimiter + "$", "");
		if (text.isEmpty())
			return new String[] {};
		return text.split(delimiter);
	}

	/**
	 * Tokenize for tag (allow #)
	 */
	public static String[] tokenizeTextForTag(String text) {
		return tokenizeText(text, "[^a-zA-Z0-9\\.\\-\\_\\~\\>\\/\\#\\(\\)]+");
	}
	
	/**
	 * Split by removing all redundancy
	 * charsOrStrs: [xyz] or (xx|yy)
	 */
	public static String[] splitText(String text, String charsOrStrs) {
		if (Util.isNullOrEmpty(text) || Util.isNullOrEmpty(charsOrStrs))
			return new String[0];
		String[] tokens = text.split(charsOrStrs + "+");
		List<String> resList = new ArrayList<String>(tokens.length);
		for (String t : tokens) {
			if (Util.isNotEmpty(t))
				resList.add(t);
		}
		return resList.toArray(new String[resList.size()]);
	}
	
	/**
	 * Shorten text, consider unicodes
	 */
	public static String shortenText(String text, int len, boolean maybeFullWidth) {
		if (Util.isNullOrEmpty(text))
			return text;
		if (!maybeFullWidth)
			return text.length() <= len ? text : text.substring(0, len);
		byte[] bytes = text.getBytes();
		if (bytes.length <= len)
			return text;
		return new String(bytes, 0, len);
	}
	
	/**
	 * Build regex if all keyword exists
	 * @param keywords
	 * @return
	 */
	public static String regexIfAllKeywordsExists(String[] keywords) {
	    StringBuilder sb = new StringBuilder("^");

	    for (String keyword : keywords) {
	        //sb.append("(?=.*\\b");
	    	sb.append("(?=.*");
	        sb.append(keyword);
	        //sb.append("\\b)");
	        sb.append(")");
	    }
	    sb.append(".*");
	    //sb.append(".*$");

	    return sb.toString();
	}

    /**
     * Parse the input custom filter regex, into list of name/regex pair
     * @return
     */
    public static List<Util.Pair<String, String>> parseCustomFilters(String customFilters) {
    	List<Util.Pair<String, String>> list = new ArrayList<Util.Pair<String, String>>();
    	if (Util.isNullOrEmpty(customFilters))
    		return list;
    	String[] tokens = customFilters.split("(\\(\\(|\\)\\))");
    	String attr, regex;
    	for (int i=0; i<tokens.length-1; i+=2) {
    		attr = tokens[i].trim();
    		regex = tokens[i+1];
    		if (attr.isEmpty() || regex.isEmpty())
    			continue;
    		list.add(new Util.Pair<String, String>(attr, regex));
    	}
    	return list;
    }
	
	/**
	 * Make HTML link tag
	 * @param imageUrl
	 * @return
	 */
	public static String makeHtmlLinkTag(String url, String content) {
    	return isNullOrEmpty(content) ? "" : String.format("<a href=\"%s\" target=\"_blank\">%s</a>", url, content);
	}
	
	/**
	 * Make HTML img tag
	 * @param imgUrl
	 * @param styleClass
	 * @return
	 */
	public static String makeHtmlImgTag(String imgUrl, String styleClass) {
		return makeHtmlImgTag(imgUrl, styleClass, "");
	}
	
	/**
	 * Make HTML img tag
	 * @param imgUrl
	 * @param styleClass
	 * @return
	 */
	public static String makeHtmlImgTag(String imgUrl, String styleClass, String noImageStr) {
		return Util.isNullOrEmpty(imgUrl) ? noImageStr : 
			String.format("<img src=\"%s\" alt=\"Cannot load\" class=\"%s\">", imgUrl, styleClass);
	}

	/**
	 * Make HTML select tag
	 */
	public static String makeHtmlSelectTag(String id, String name, boolean isMultiple, Integer multipleSize, String[] values, String[] texts, String[] colors, String[] selectedValues) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<select id=\"%s\" name=\"%s\" %s %s>", id, name, isMultiple ? "multiple=\"multiple\"" : "", multipleSize != null ? "size=\"" + multipleSize + "\"" : ""));
		for (int i=0; i<values.length; i++) {
			sb.append(String.format("<option value=\"%s\" %s %s>%s</option>", values[i], colors == null ? "" : "style=\"color:" + colors[i] + "\"", 
					Util.arraySearch(selectedValues, values[i]) >= 0 ? "selected" : "", texts[i]));
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	/**
	 * Make HTML radio tag
	 */
	public static String makeHtmlRadioTag(String name, String[] ids, String[] values, String[] texts, String[] styleClasses, String checkedValue, String styleClass) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<values.length; i++) {
			sb.append(String.format("%s<input type=\"radio\" name=\"%s\" %s value=\"%s\" %s %s>%s%s",
					texts == null ? "" : "<label " + (Util.isNotEmpty(styleClass) ? "class=\"" + styleClass + "\"" : "") + ">",
					name, ids == null ? "" : "id=\"" + ids[i] + "\"", values[i],
					styleClasses == null ? "" : "class=\"" + styleClasses[i] + "\"", 
					values[i].equals(checkedValue) ? "checked" : "", texts == null ? "" : texts[i],
					texts == null ? "" : "</label>"));
		}
		return sb.toString();
	}
	
	/**
	 * Shorten text and show in tooltip if necessary
	 */
	public static String htmlAddTooltip(String text, int maxLen) {
		if (Util.isNullOrEmpty(text) || text.length() <= maxLen)
			return text;
		return String.format("<span title=\"%s\">%s......</span>", htmlEscape(text), text.substring(0, maxLen)); 
	}
	
	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioNullOrAsc(String val) {
		return val == null || "Asc".equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioNullOrDesc(String val) {
		return val == null || "Desc".equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioNullOrInt(String val, Integer iVal) {
		return val == null || ("" + iVal).equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioAsc(String val) {
		return "Asc".equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioDesc(String val) {
		return "Desc".equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioNullOrNone(String val) {
		return val == null || "None".equals(val);
	}

	/**
	 * A workaround for HTML radio (don't use "" value)
	 * @param val
	 * @return
	 */
	public static boolean isHtmlRadioNullOrChart(String val) {
		return val == null || "Chart".equals(val);
	}
	
	/**
	 * A workaround for Thymeleaf equal method
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isThymeleafEqual(String a, String b) {
		return a.equals(b);
	}

	/**
	 * A workaround for Thymeleaf empty method
	 * @param a
	 * @param b
	 * @return
	 */
	public static Object thymeleafNotEmptyOtherwise(Object a, Object b) {
		return a == null || ("" + a).isEmpty() ? b : a;
	}

	/**
	 * Make html paging
	 * @param total
	 * @param limit
	 * @param currPageNo
	 * @param hiddenPageNoId
	 * @param formName
	 * @return
	 */
	public static String makeHtmlPaging(int total, int limit, int currPageNo, String hiddenPageNoId, String formName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"pagination\" style=\"margin:5px 0px 0px 5px\">");
		
		int remainder = total % limit;
		int pageCount = (remainder == 0) ? (total / limit) : ((total / limit) + 1);
		String itemStr = "<li class='%s'><a href='#' onclick=\"document.getElementById('" + hiddenPageNoId + "').value=%d; return ezSubmitForm('" + formName + "');\">%s</a></li>";

		int minPageNo = Math.max(currPageNo - 1, 1);
		int maxPageNo = Math.min(currPageNo + 1, pageCount);
		
		if (1 < currPageNo)
			sb.append(String.format(itemStr, "page-item", currPageNo - 1, "&#60;"));
		if (1 < minPageNo)
			sb.append(String.format(itemStr, "page-item", 1, "" + 1));
		if (1 == minPageNo - 2)
			sb.append(String.format(itemStr, "page-item", 2, "" + 2));
		else if (1 < minPageNo - 2)
			sb.append(String.format(itemStr, "page-item page-last-separator disabled", currPageNo, "..."));
		
		for (int i=minPageNo; i<=maxPageNo; i++) {
			sb.append(String.format(itemStr, i == currPageNo ? "active" : "page-item", i, "" + i));
		}
		
		if (maxPageNo < pageCount - 2)
			sb.append(String.format(itemStr, "page-item page-last-separator disabled", currPageNo, "..."));
		else if (maxPageNo == pageCount - 2)
			sb.append(String.format(itemStr, "page-item", pageCount - 1, "" + (pageCount - 1)));
		if (maxPageNo < pageCount)
			sb.append(String.format(itemStr, "page-item", pageCount, "" + pageCount));
		if (currPageNo < pageCount)
			sb.append(String.format(itemStr, "page-item", currPageNo + 1, "&#62;"));
		
		sb.append("</ul>");
		return sb.toString();
	}
	
	/**
	 * Print to console
	 */
	public static void p(Object obj) {
		System.out.println("" + obj);
	}

	/**
	 * Make permalink with all params
	 * @param baseUrl
	 * @param allParams
	 * @return
	 */
	public static String makePermalink(String baseUrl, MultiValueMap<String,String> allParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append("?");
		String key;
		List<String> valueList;
		for (Map.Entry<String,List<String>> entry : allParams.entrySet()) {
			if (isNotEmpty(key = entry.getKey()) && !"currPageNo".equals(key) && isNotEmpty(valueList = entry.getValue())) {
				for (String val : valueList) {
					if (Util.isNullOrEmpty(val))
						continue;
					//if (lineBreakToComma)
					//	val = val.replaceAll("[\r\n]+", ",");
					//if ("customFilters".equals(key) || "orderByMethod".equals(key))
					//	val = URLEncoder.encode(val);
					sb.append(key + "=" + URLEncoder.encode(val) + "&");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert all params into a key (for caching)
	 */
	public static String makeParamsKey(MultiValueMap<String,String> allParams) {
		if (isNullOrEmpty(allParams))
			return "";
		List<String> keyList = new ArrayList<String>(allParams.keySet());
		Collections.sort(keyList);
		List<String> valList;
		StringBuilder sb = new StringBuilder();
		for (String key : keyList) {
			sb.append("" + key + "==");
			if ((valList = allParams.get(key)) == null)
				continue;
			Collections.sort(valList);
			sb.append(arrayJoin(valList.toArray(), ",,"));
			sb.append("&&");
		}
		return sb.toString();
	}
	
	/**
	 * Escape for html
	 */
	public static String htmlEscape(String str) {
		return str == null ? "" : HtmlUtils.htmlEscape(str);
	}
	
	/**
	 * Encode url value
	 */
	public static String urlEncode(String value) {
		return value == null ? "" : URLEncoder.encode(value);
	}

	/**
	 * Parse JSON string to map, return null on error
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> jsonStrToMap(String jsonStr) {
		if (isNullOrEmpty(jsonStr))
			return new HashMap<String, Object>();
		try {
			return new ObjectMapper().readValue(jsonStr, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> Map<String, T> jsonStrToMap(String jsonStr, Class<T> cls) {
		if (isNullOrEmpty(jsonStr))
			return new HashMap<String, T>();
		try {
			return new ObjectMapper().readValue(jsonStr, new TypeReference<Map<String, T>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <K,V> Map<K, V> jsonStrToMap(String jsonStr, Class<V> value, Class<K> key) {
		if (isNullOrEmpty(jsonStr))
			return new HashMap<K, V>();
		try {
			return new ObjectMapper().readValue(jsonStr, new TypeReference<Map<K, V>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Format map to JSON string
	 */
	public static String mapToJsonStr(Map<String, Object> map) {
		if (isNullOrEmpty(map))
			return "";
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Format object to JSON string
	 */
	public static String objToJsonStr(Object obj) {
		if (isNullOrEmpty(obj))
			return "";
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> List<T> jsonStrToListObject(String jsonStr, Class<T> cls) {
		if (isNullOrEmpty(jsonStr))
			return null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, cls);
			 
			return mapper.readValue(jsonStr, listType);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> Map<String, T> jsonStrToMapObject(String jsonStr, Class<T> cls){
		if (isNullOrEmpty(jsonStr))
			return null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory factory;
			MapType type;

			factory = TypeFactory.defaultInstance();
			type    = factory.constructMapType(HashMap.class, String.class, cls);
			//TypeReference<HashMap<String, T>> typeRef = new TypeReference<HashMap<String, T>>() {};
			 
			return mapper.readValue(jsonStr, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T jsonStrToObject(String jsonStr, Class<T> cls) {
		if (isNullOrEmpty(jsonStr))
			return null;
		try {
			return new ObjectMapper().readValue(jsonStr, cls);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T mapToObject(Map<String, Object> map, Class<T> cls) {
		return jsonStrToObject(mapToJsonStr(map),cls);
	}
	
	public static String mapListToJson(List<Map<String, Object>> mapList) {
		if (isNullOrEmpty(mapList))
			return "";
		try {
			return new ObjectMapper().writeValueAsString(mapList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> List<T> mapListToObjectList(List<Map<String, Object>> mapList, Class<T> cls) {
		return jsonStrToListObject(mapListToJson(mapList), cls);
	}

	/**
	 * Get color string by value between 0-1
	 */
	public static String colorByValue(double val, Color minColor, Color maxColor) {
		return colorByValue(val, val, val, minColor, maxColor);
	}

	/**
	 * Get color string by R G B values
	 */
	public static String colorByValue(double rVal, double gVal, double bVal, Color minColor, Color maxColor) {
		return String.format("#%02X%02X%02X", 
				minColor.getRed() + (int)((maxColor.getRed() - minColor.getRed()) * rVal),
				minColor.getGreen() + (int)((maxColor.getGreen() - minColor.getGreen()) * gVal),
				minColor.getBlue() + (int)((maxColor.getBlue() - minColor.getBlue()) * bVal));
	}

	/**
	 * Get items in both list
	 */
	public static <T> List<T> listJoin(List<T> aList, List<T> bList) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(bList);
		LinkedList<T> list = new LinkedList<T>();
		for (T t : aList) {
			if (set.contains(t))
				list.add(t);
		}
		return list;
	}

	/**
	 * Get items in a but not in b list
	 */
	public static <T> List<T> listUnJoin(List<T> aList, List<T> bList) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(bList);
		LinkedList<T> list = new LinkedList<T>();
		for (T t : aList) {
			if (!set.contains(t))
				list.add(t);
		}
		return list;
	}

	/** 
	 * @return intersection of aList and bList
	 */
	public static ArrayList<String> getIntersection(Collection<String> aList, Collection<String> bList) {
		if (aList == null || bList == null)
			return null;
		else if (aList.isEmpty() || bList.isEmpty())
			return null;
		else {
			ArrayList<String> intersection = new ArrayList<String>();			
			for (String str1 : aList) {
				for (String str2 : bList) {
					if (str1.equals(str2))
						intersection.add(str2);
				}
			}
			return intersection;
		}
	}
	
	public static boolean isExistsIntersection(String[] aList, String[] bList) {
		if (aList == null || bList == null)
			return false;
		else if (aList.length == 0 || bList.length == 0)
			return false;
		else {			
			for (String str1 : aList) {
				for (String str2 : bList) {
					if (str1.equals(str2))
						return true;
				}
			}
			return false;
		}
	}
	
	public static boolean isExistsIntersection(String[] aList, Collection<String> bList) {
		if (aList == null || bList == null)
			return false;
		else if (aList.length == 0 || bList.size() == 0)
			return false;
		else {			
			for (String str1 : aList) {
				for (String str2 : bList) {
					if (str1.equals(str2))
						return true;
				}
			}
			return false;
		}
	}
	
	public static boolean isExistsIntersection(Collection<String> aList, Collection<String> bList) {
		if (aList == null || bList == null)
			return false;
		else if (aList.size() == 0 || bList.size() == 0)
			return false;
		else {			
			for (String str1 : aList) {
				for (String str2 : bList) {
					if (str1.equals(str2))
						return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Write content to file
	 */
	public static boolean writeToFile(String filename, String content) {
		try (PrintWriter out = new PrintWriter(filename)) {
		    out.print(content);
		    return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Thread sleep
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get milliseconds from time string like "30m", can be "W", "D", "h", "m", "s", "ms"
	 */
	public static Long decodeTime(String str) {
		if (isNullOrEmpty(str))
			return null;
		if (str.endsWith("ms"))
			return tryParseLong(str.substring(0, str.length() - 2));
		switch (str.charAt(str.length() - 1)) {
			case 'M': return tryParseLong(str.substring(0, str.length() - 1)) * 30 * 86400000L; //TODO not accurate, only for sales stat usage
			case 'W': return tryParseLong(str.substring(0, str.length() - 1)) * 7 * 86400000L;
			case 'D': return tryParseLong(str.substring(0, str.length() - 1)) * 86400000L;
			case 'h': return tryParseLong(str.substring(0, str.length() - 1)) * 3600000L;
			case 'm': return tryParseLong(str.substring(0, str.length() - 1)) * 60000L;
			case 's': return tryParseLong(str.substring(0, str.length() - 1)) * 1000L;
			default: return null;
		}
	}

	/**
	 * Map columns and values to hash
	 */
	public static <T, P> void columnsAndValuesToHash(Map<T, P> hash, T[] columns, P[] values) {
		for (int i=0; i<columns.length; i++) {
			if (i >= values.length)
				break;
			hash.put(columns[i], values[i]);
		}
	}

	/**
	 * Map columns and values to hash
	 */
	public static <T, P> void columnsAndValuesToHash(Map<T, P> hash, List<T> columns, List<P> values) {
		for (int i=0; i<columns.size(); i++) {
			if (i >= values.size())
				break;
			hash.put(columns.get(i), values.get(i));
		}
	}

	/**
	 * Get weighted average, null value is treated as some value or skipped, return null if all values are skipped
	 */
	public static Double weightedAverage(Double[] values, Double[] weights, Double valueForNull) {
		Double sum = 0D;
		Double sumWeights = 0D;
		Double val;
		for (int i=0; i<values.length; i++) {
			if ((val = values[i]) == null && (val = valueForNull) == null)
				continue;
			sum += val * weights[i];
			sumWeights += weights[i];
		}
		return sumWeights > 0D ? sum / sumWeights : null;
	}

	/**
	 * Convert list to map
	 */
	public static <A, B> HashMap<A, B> listToMap(List<Pair<A, B>> list) {
		HashMap<A, B> map = new HashMap<A, B>();
		for (Pair<A, B> pair : list) {
			map.put(pair.key, pair.value);
		}
		return map;
	}

	/**
	 * Convert array to map
	 */
	public static <T> Map<T, T> arrayToMap(T[] array) {
		HashMap<T, T> map = new HashMap<T, T>();
		for (int i=0; i<array.length; i+=2) {
			map.put(array[i], array[i+1]);
		}
		return map;
	}

	/**
	 * Convert array to map
	 */
	public static <T> Map<T, T[]> arrayToMap(T[][] array) {
		HashMap<T, T[]> map = new HashMap<T, T[]>();
		for (int i=0; i<array.length; i+=2) {
			map.put(array[i][0], array[i+1]);
		}
		return map;
	}

	/**
	 * Convert array to map
	 */
	public static <T> Set<T> arrayToSet(T[] array) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(Arrays.asList(array));
		return set;
	}
	
	/**
	 * Append a string multiple times to a stringbuilder
	 */
	public static void appendMulti(StringBuilder sb, String str, int cnt) {
		for (int i=0; i<cnt; i++) {
			sb.append(str);
		}
	}
	
	/**
	 * Get field values from objects
	 * @param objectList
	 * @param fieldName
	 * @param fieldClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T,F> List<F> getFieldValueList(List<T> objectList, String fieldName, Class<F> fieldClass) {
		if (Util.isNullOrEmpty(objectList))
			return new ArrayList<F>();
		List<F> fieldValueList = new ArrayList<F>(objectList.size());
		try {
			for (T t : objectList) {
				fieldValueList.add((F)t.getClass().getDeclaredField(fieldName).get(t));
			}
		} catch (Exception e) {
			return new ArrayList<F>();
		}
		return fieldValueList;
	}

	/**
	 * Make map from array
	 * @param keyValArray
	 * @return
	 */
	public static HashMap<String, Object> makeMap(Object[][] keyValArray) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (Util.isNullOrEmpty(keyValArray))
			return map;
		for (Object[] keyVal : keyValArray) {
			if (Util.isNullOrEmpty(keyVal) || Util.isNullOrEmpty(keyVal[0]))
				continue;
			map.put((String)keyVal[0], keyVal[1]);
		}
		return map;
	}

	/**
	 * Increment for count map
	 */
	public static void inc(Map<String, Integer> cntMap, String key, Integer change) {
		if (cntMap == null || change == null)
			return;
		cntMap.put(key, cntMap.getOrDefault(key, 0) + change);
	}

	/**
	 * Increment for count map
	 */
	public static void inc(Map<String, Double> cntMap, String key, Double change) {
		if (cntMap == null || change == null)
			return;
		cntMap.put(key, cntMap.getOrDefault(key, 0D) + change);
	}

	public static double addWithArith(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.add(b2).doubleValue();  
    }
	public static double addWithArith(double v1, double v2, int scale) {
		return roundWithArith(addWithArith(v1, v2), scale);
	}
	public static double subWithArith(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.subtract(b2).doubleValue();  
    }
	public static double subWithArith(double v1, double v2, int scale) {
		return roundWithArith(subWithArith(v1, v2), scale);
	}
	public static double mulWithArith(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.multiply(b2).doubleValue();  
    }
	public static double mulWithArith(double v1, double v2, int scale) {
		return roundWithArith(mulWithArith(v1, v2), scale);
	}
	public static double divWithArith(double v1, double v2) {  
        return divWithArith(v1, v2, 10);  
    } 
	public static double divWithArith(double v1, double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(  
                    "The   scale   must   be   a   positive   integer   or   zero");  
        }  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }
	public static double roundWithArith(double v, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(  
                    "The   scale   must   be   a   positive   integer   or   zero");  
        }  
        BigDecimal b = new BigDecimal(Double.toString(v));  
        BigDecimal one = new BigDecimal("1");  
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }
	public static boolean doubleEquals(Double v1, Double v2, double epslon) {
		if (v1 == null)
			return v2 == null;
		if (v2 == null)
			return false;
		return Math.abs(v1 - v2) < epslon;
	}
	
	private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
	public static boolean isInteger(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
        return pattern.matcher(str).matches();  
  }
	/**
	 * 
	 * @return get a uuid that has been remove "-", leng of it is 32
	 */
	public static String getGuid32() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Zip data
	 */
	public static byte[] zip(byte[] bytes) {
		if (bytes == null)
			return null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			try (ZipOutputStream zos = new ZipOutputStream(bos)) {
				zos.putNextEntry(new ZipEntry("0"));
				zos.write(bytes);
				zos.closeEntry();
			} catch (Exception e) {
				System.out.println("Error when zipping: " + e.getMessage());
				return null;
			}
			return bos.toByteArray();
		} catch (Exception e) {
			System.out.println("Error when zipping: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Unzip data
	 */
	public static byte[] unzip(byte[] bytes) {
		if (bytes == null)
			return null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes))) {
				zis.getNextEntry();
				byte[] buffer = new byte[1024];
				int offset = -1;
				while ((offset = zis.read(buffer)) != -1) {
					bos.write(buffer, 0, offset);
				}
			} catch (Exception e) {
				System.out.println("Error when unzipping: " + e.getMessage());
				return null;
			}
			return bos.toByteArray();
		} catch (Exception e) {
			System.out.println("Error when unzipping: " + e.getMessage());
			return null;
		}
	}

	private static Random _random = new Random(new Date().getTime());
	/**
	 * Get random
	 */
	public static Random getRandom() {
		return _random;
	}

    /**
     * Import file, save to metas table, _id = filename, value = zipped content
     */
    public static Pair<String, byte[]> parseTextFile(MultipartFile file, boolean toBytes, boolean toZip) throws Exception {
    	if (file == null || file.getSize() <= 0)
    		return null;
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
    		StringBuilder sb = new StringBuilder();
    		String line;
    		while ((line = br.readLine()) != null) {
    			sb.append(line);
    			sb.append("\r\n");
    		}
    		
    		String content = sb.toString();
    		byte[] data = null;
    		if (toBytes) {
    			data = content.getBytes();
    			if (toZip) {
    				data = zip(data);
    	    		if (data == null)
    	    			throw new Exception("Failed to zip the content");
    			}
    		}
    		
    		return new Pair<String, byte[]>(content, data);
    		
    	} catch (Exception e) {
    		throw new Exception("" + e.getMessage());
    	}
    }

	/**
	 * Map double values to color strings
	 * @param negColor #E88361
	 * @param zeroColor  #FFFFFF
	 * @param posColor #43AED1
	 */
	public static String[] mapValueToColor(Double[] values, Color negColor, Color zeroColor, Color posColor) {
		if (values == null)
			return null;
		String[] colors = new String[values.length];
		Util.arrayInit(colors, null);
		if (values.length == 0)
			return colors;

		Double maxVal = null;
		for (Double v : values) {
			if (v == null)
				continue;
			if (maxVal == null || Math.abs(v) > maxVal)
				maxVal = Math.abs(v);
		}
		if (maxVal == null)
			return colors;
		if (maxVal < Double.MIN_NORMAL) {
			Util.arrayInit(colors,
					String.format("#%02X%02X%02X", zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue()));
			return colors;
		}

		for (int i = 0; i < values.length; i++) {
			if (values[i] == null)
				continue;
			colors[i] = colorByValue(Math.abs(values[i]) / maxVal, zeroColor, values[i] < 0 ? negColor : posColor);
		}
		return colors;
	}

	/**
	 * Make HTML checkbox parent
	 */
	protected static final String htmlCheckboxParent = "<input type=\"checkbox\" id=\"cbParent\" id=\"cbParent\" onclick=\"clickCheckboxParent('cbParent', 'cbChild')\">";
	public static String makeHtmlCheckboxParent() {
        return htmlCheckboxParent;
	}
	
	/**
	 * Make HTML checkbox child
	 */
	protected static final String htmlCheckboxChild = "<input type=\"checkbox\" name=\"cbChild\" value=\"%s\">";
	public static String makeHtmlCheckboxChild(String value) {
		return String.format(htmlCheckboxChild, value);
	}
	
	/**
	 * upper case the first letter of the character before each space and lower case the rest letter,
	 * e.g. "  deep 2#  $  center f  $" will be transfer to "Deep 2# $ Center F"
	 */
	public static String upperCaseText(String text) {
		if (StringUtils.isBlank(text))
			return "";
		StringBuilder result = new StringBuilder(text.length());
		String[] arr = text.split(" ");
		for (String subStr : arr) {
			if (StringUtils.isNotBlank(subStr)) {
				subStr = subStr.trim();
				result.append(" ");
				result.append(subStr.substring(0, 1).toUpperCase());
				if (subStr.length() > 1)
					result.append(subStr.substring(1).toLowerCase());
			}
		}
        
        return result.substring(1);
    }

	public static boolean removeExistFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * delete all sub files and it self
	 * @param file
	 * @return
	 */
	public static boolean delAllSubFilesAndItSelf(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            //File[] files = file.listFiles();
            for (File subFile : file.listFiles()) {
            	delAllSubFilesAndItSelf(subFile);
            }
        }
        return file.delete();
    }

	/**
     * unzip file or directorys
     */
    public static void unzipFile(File srcFile, String destDirPath) throws RuntimeException {
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + " is not exists");
        }
 
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    if(!targetFile.getParentFile().exists()){
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();

                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if(zipFile != null){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * upzip file or directory
     * @param inputStream 
     * @param destDirPath
     */
	public static boolean unzipFile(InputStream inputStream, String destDirPath) {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream(inputStream);
			ZipEntry zipEntry = null;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					File dir = new File(destDirPath + "/" + zipEntry.getName());
					if (!dir.exists()) {
						dir.mkdirs();
					}
				} else {
					String filename = new String(destDirPath + "/" + zipEntry.getName());
					FileOutputStream out = new FileOutputStream(filename);
					byte bytes[] = new byte[1024];
					int n;
					while ((n = zipInputStream.read(bytes, 0, 1024)) != -1) {
						out.write(bytes, 0, n);
					}
					out.close();
					out = null;
					bytes = null;
				}
			}
			return true;
		} catch (IOException ioexception) {
			System.out.println("Error to upzipFile " + ioexception);
			return false;
		} finally {
			if (null != zipInputStream) {
				try {
					zipInputStream.close();
				} catch (IOException e) {
					System.out.println("Error to upzipFile " + e);
				}
			}
		}
	}

	/**
	 * transfer file to byte[]
	 * @return
	 * @throws IOException
	 */
	public static byte[] fileToByteArray(File file) throws IOException {
		if (!file.exists()) {
			throw new IOException(file.getName());
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
		java.io.BufferedInputStream in = null;
		try {
			in = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				bos.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * get current work directory
	 * @return System.getProperty("user.dir")
	 */
	public static String getApplicationDirectory() {
		return System.getProperty("user.dir");
	}

	public static boolean createDirectoryIfNotExists(String filePath) {
		if (!Files.exists(Paths.get(filePath))){
			try {
				Files.createDirectories(Paths.get(filePath));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
