package com.packt.cookbook.libraries.common;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Utility class for work with strings
 */
public class StringHelper {

	/**
	 * White space
	 */
	public static final String SPACE = " ";

	/**
	 * CRLF characters
	 */
	public static final String CRLF = "\r\n";

	private static final String HEXES = "0123456789ABCDEF";
	private static final Pattern NORMALIZE_PATTERN = Pattern.compile("[\\t\\r\\n\\s]+");

	private static String DEF_COMMENT_LANG = "ru";

	private static final Pattern MASKED_PATTERN = Pattern.compile(".*\\*{3,}.*");

	/**
	 * Hide constructor for utility class
	 */
	private StringHelper() {
	}

	/**
	 * Converts byte array to hex string
	 * Length of result string x2 of array size
	 * Hex letters will be in uppercase
	 *
	 * @param raw byte array
	 * @return hex string
	 */
	public static String getHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4))
					.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	/**
	 * Align value to right, adding fillChar at left
	 * for example, alignNumber(102, 5, '0') returns 00102
	 * Value must be >= 0
	 *
	 * @param value    value
	 * @param width    min width
	 * @param fillChar char for filling
	 * @return
	 */
	public static String alignNumber(long value, int width, char fillChar) {
		String valS = String.valueOf(value);
		if (value < 0 || valS.length() >= width) {
			return valS;
		}
		char[] valChars = valS.toCharArray();
		char[] result = new char[width];
		Arrays.fill(result, fillChar);
		System.arraycopy(valChars, 0, result, width - valChars.length, valChars.length);
		return new String(result);
	}

	/**
	 * Concatenates tokens specified using separator(may be null)
	 *
	 * @param separator - tokens separator or null
	 * @param tokens    - tokens to concatenate
	 * @return resulting string
	 */
	public static String concat(String separator, Object... tokens) {
		if (tokens == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object token : tokens) {
			sb.append(token.toString());
			if (separator != null) {
				sb.append(separator);
			}
		}
		return (separator == null) ? sb.toString() : sb.substring(0, sb.length() - separator.length());
	}

	/**
	 * Return null if empty string specified
	 *
	 * @param s - string for processing
	 * @return null if string is empty or original string
	 */
	public static String emptyToNull(String s) {
		return isEmpty(s) ? null : s;
	}

	/**
	 * Returns first non-empty (not NULL and length > 0) token from array specified, NULL if not found
	 *
	 * @param tokens - array of tokens
	 * @return first non-empty token or null if not found
	 */
	public static String firstNonEmpty(String... tokens) {
		if (tokens == null) {
			return null;
		}
		for (String s : tokens) {
			if (!isEmpty(s)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * @param s - string to test
	 * @return true if string is null or empty string (without white spaces)
	 */
	public static boolean isEmpty(String s) {
		return (s == null) || s.isBlank();
	}

	/**
	 * Normalize given string, e.g. transform string to single-line, contains separators only as one-symbol white spaces
	 *
	 * @param source - source string to normalize
	 * @return normalized string
	 */
	public static String normalize(String source) {
		if (source == null) {
			return source;
		}
		String result = source.trim();
		if (result.isEmpty()) {
			return result;
		}
		return NORMALIZE_PATTERN.matcher(result).replaceAll(SPACE);
	}

	/**
	 * Builds string which contains specified fragment {count} times
	 *
	 * @param s     - fragment for repeat
	 * @param count - amount of fragments
	 * @return resulting string
	 */
	public static String repeat(String s, int count) {
		return s.repeat(count);
	}

	/**
	 * Safely get length of string specified
	 *
	 * @param s - string to get length for
	 * @return length of string specified or 0 if string is null
	 */
	public static int safeLength(String s) {
		return (s != null) ? s.length() : 0;
	}

	/**
	 * Safely trim given string
	 *
	 * @param s - string to trimming
	 * @return trimmed string if not null, null otherwise
	 */
	public static String safeTrim(String s) {
		return (s != null) ? s.trim() : null;
	}

	/**
	 * Cuts string if length greater than maxLen
	 *
	 * @param s      string
	 * @param maxLen max length of string
	 * @return original or cutted string
	 */
	public static String cutLongString(String s, int maxLen) {
		if (maxLen < 0) {
			throw new IllegalArgumentException("maxLen is negative");
		}
		if (s == null) {
			return s;
		}
		if (s.length() <= maxLen) {
			return s;
		}
		if (maxLen == 0) {
			return "";
		}
		return s.substring(0, maxLen);
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str   string to split
	 * @param delim delimiter
	 * @return list of string
	 */
	public static List<String> quickSplit(String str, String delim) {
		return quickSplit(str, delim, ArrayList::new);
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str         string to split
	 * @param delim       delimiter
	 * @param colProducer collection supplier
	 * @return list of string
	 */
	public static <C extends Collection<? super String>> C quickSplit(String str, String delim, Supplier<C> colProducer) {
		C result = colProducer.get();
		int posI = 0;
		int delimLen = delim.length();
		while (posI < str.length()) {
			int posDelim = str.indexOf(delim, posI);
			if (posDelim < 0) {
				result.add(str.substring(posI));
				break;
			} else {
				if (posDelim == posI) {
					result.add("");
					posI += delimLen;
				} else {
					result.add(str.substring(posI, posDelim));
					posI = posDelim + delimLen;
				}
			}
		}
		return result;
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str   string to split
	 * @param delim delimiter
	 * @return list of string
	 */
	public static List<String> quickSplit(String str, char delim) {
		return quickSplit(str, delim, ArrayList::new);
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str         string to split
	 * @param delim       delimiter
	 * @param colProducer collection supplier
	 * @return list of string
	 */
	public static <C extends Collection<? super String>> C quickSplit(String str, char delim, Supplier<C> colProducer) {
		C result = colProducer.get();
		int posI = 0;
		while (posI < str.length()) {
			int posDelim = str.indexOf(delim, posI);
			if (posDelim < 0) {
				result.add(str.substring(posI));
				break;
			} else {
				if (posDelim == posI) {
					result.add("");
					posI++;
				} else {
					result.add(str.substring(posI, posDelim));
					posI = posDelim + 1;
				}
			}
		}
		return result;
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str   string to split
	 * @param delim delimiter
	 * @return list of string
	 */
	public static List<String> quickSplitAndTrim(String str, String delim) {
		return quickSplitAndTrim(str, delim, ArrayList::new);
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str         string to split
	 * @param delim       delimiter
	 * @param colProducer collection supplier
	 * @return list of string
	 */
	public static <C extends Collection<? super String>> C quickSplitAndTrim(String str, String delim, Supplier<C> colProducer) {
		C result = colProducer.get();
		int posI = 0;
		int delimLen = delim.length();
		while (posI < str.length()) {
			int posDelim = str.indexOf(delim, posI);
			if (posDelim < 0) {
				result.add(safeTrim(str.substring(posI)));
				break;
			} else {
				if (posDelim == posI) {
					result.add("");
					posI += delimLen;
				} else {
					result.add(safeTrim(str.substring(posI, posDelim)));
					posI = posDelim + delimLen;
				}
			}
		}
		return result;
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str   string to split
	 * @param delim delimiter
	 * @return list of string
	 */
	public static List<String> quickSplitAndTrim(String str, char delim) {
		return quickSplitAndTrim(str, delim, ArrayList::new);
	}

	/**
	 * Split string by delimiter, much quicker than String.split
	 *
	 * @param str         string to split
	 * @param delim       delimiter
	 * @param colProducer collection supplier
	 * @return list of string
	 */
	public static <C extends Collection<? super String>> C quickSplitAndTrim(String str, char delim, Supplier<C> colProducer) {
		C result = colProducer.get();
		int posI = 0;
		while (posI < str.length()) {
			int posDelim = str.indexOf(delim, posI);
			if (posDelim < 0) {
				result.add(safeTrim(str.substring(posI)));
				break;
			} else {
				if (posDelim == posI) {
					result.add("");
					posI++;
				} else {
					result.add(safeTrim(str.substring(posI, posDelim)));
					posI = posDelim + 1;
				}
			}
		}
		return result;
	}

	/**
	 * Function for transliteration
	 *
	 * @param str
	 * @return
	 */

//	public static String toTranslit(String str) {
//		return Translit.toTranslit(str);
//	}

	/**
	 * Checks if string has only digits
	 * It doesn't work for negative or fractional numbers
	 * Most faster than regexp or Long.parseLong
	 *
	 * @param str string
	 * @return true if str consist only from digits
	 */
	public static boolean isNumber(String str) {
		int size = str.length();
		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return size > 0;
	}

	/**
	 * Checks if string has only digits
	 * It works for negative or fractional numbers
	 * Most faster than regexp or Long.parseLong
	 *
	 * @param str string
	 * @return true if str consist only from digits
	 */
	public static boolean isNegativeNumber(String str) {
		str = normalize(str);
		int size = str.length();
		if ('-' != str.charAt(0)) {
			return isNumber(str);
		}
		for (int i = 1; i < size; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return size > 0;
	}

	/**
	 * Checks if string contains hex number
	 * 00020108176692973050026537669201C0F99C3B - true
	 * 0041aca52e6bb603510051e8a928af02800114 - true
	 * AEGspS5rtgNRAFHoqSivAoABFA== - false
	 *
	 * @param str string to check
	 * @return true if string contains hex number
	 */
	public static boolean isHexNumber(String str) {
		int size = str.length();
		for (int i = 0; i < size; i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c) ||
					(c >= 'A' && c <= 'F') ||
					(c >= 'a' && c <= 'f')) {
				continue;
			}
			return false;
		}
		return size > 0;
	}

	/**
	 * Converts collection to string with ',' separator
	 *
	 * @param list collection
	 * @return string
	 */
	public static String listToString(Collection list) {
		return listToString(list, ',');
	}

	/**
	 * Converts collection to string with specified separator
	 *
	 * @param list      collection
	 * @param separator separator
	 * @return string
	 */
	public static String listToString(Collection list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(o);
		}
		return sb.toString();
	}

	/**
	 * Converts collection to string with specified separator
	 *
	 * @param list      collection
	 * @param separator char separator
	 * @return string
	 */
	public static String listToString(Collection list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(o);
		}
		return sb.toString();
	}

	/**
	 * Make simple string from MultilangualString or simple string, using default language ru
	 *
	 * @param str string or MultilangualString in xml format
	 * @return simple string
	 */
//	public static String prepareMultiString(String str) {
//		if (StringHelper.isEmpty(str)) {
//			return "";
//		}
//		MultilangualString s = MultilangualString.fromString(str);
//		return s == null ? "" : s.getDefaultValue(DEF_COMMENT_LANG);
//	}

	/**
	 * Returns hash code of String
	 * <p>Equals to hashCode of String in Java 1.6.
	 * Used to not depend from Java implementation.</p>
	 *
	 * @param str string
	 * @return hash code
	 */
	public static int stringHashCode(String str) {
		if (str.length() == 0) {
			return 0;
		}
		int h = 0;
		for (int i = 0; i < str.length(); i++) {
			h = 31 * h + str.charAt(i);
		}
		return h;
	}

	/**
	 * Replace all oldChar to newChar
	 * faster than String.replaceAll(",", ".")
	 *
	 * @param srcString source string
	 * @param oldChar   char to search
	 * @param newChar   char to replace
	 * @return string
	 */
	public static String replaceChars(String srcString, char oldChar, char newChar) {
		if (srcString == null || srcString.isEmpty()) {
			return srcString;
		}
		char[] chars = null;
		for (int i = 0; i < srcString.length(); i++) {
			if (srcString.charAt(i) == oldChar) {
				if (chars == null) {
					chars = new char[srcString.length()];
					srcString.getChars(0, srcString.length(), chars, 0);
				}
				chars[i] = newChar;
			}
		}
		return (chars != null ? new String(chars) : srcString);
	}

	/**
	 * Cut string to maxLen if str.length() > maxLen
	 * and replace chars in middle with ...
	 *
	 * @param str    source string
	 * @param maxLen max length, must be greater than 4
	 * @return source string or cutted string
	 */
	public static String cutStringToLen(String str, int maxLen) {
		if (maxLen <= 4) {
			throw new IllegalArgumentException("maxLen must be greater than 4");
		}
		if (str == null || str.length() <= maxLen) {
			return str;
		}
		StringBuilder sb = new StringBuilder(maxLen);
		int beginCount = (maxLen - 3) / 2;
		int endCount = maxLen - beginCount - 3;
		sb.append(str, 0, beginCount);
		sb.append("...");
		sb.append(str, str.length() - endCount, str.length());
		return sb.toString();
	}

	/**
	 * Joins elements from iterable using separator ","
	 *
	 * @param iterable iterable
	 * @return joined String or null if iterable is null
	 */
	public static String join(Iterable<?> iterable) {
		return join(iterable, ",");
	}

	/**
	 * Joins elements from iterable using separator
	 *
	 * @param iterable  iterable
	 * @param separator separator character to use, null means empty string ''
	 * @return joined String or null if iterable is null
	 */
	public static String join(Iterable<?> iterable, String separator) {
		if (iterable == null) {
			return null;
		}
		if (separator == null) {
			separator = "";
		}
		return StreamSupport.stream(iterable.spliterator(), false)
				.map(String::valueOf)
				.collect(Collectors.joining(separator));
	}

	/**
	 * Is masked boolean.
	 *
	 * @param value the value
	 * @return the boolean
	 */
	public static boolean isMasked(String value) {
		Matcher m = MASKED_PATTERN.matcher(value);
		return m.matches();
	}

//f
}
