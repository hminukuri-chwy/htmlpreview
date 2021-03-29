package com.chewy.util;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class Strings {
  /**
   * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
   * <p>
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is null, empty or whitespace
   */
  public static boolean isBlank(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(cs.charAt(i)) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
   * <p>
   * <pre>
   * StringUtils.isNotBlank(null)      = false
   * StringUtils.isNotBlank("")        = false
   * StringUtils.isNotBlank(" ")       = false
   * StringUtils.isNotBlank("bob")     = true
   * StringUtils.isNotBlank("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is not empty and not null and not whitespace
   */
  public static boolean isNotBlank(final CharSequence cs) {
    return !isBlank(cs);
  }

  public static String replaceNewLines(String input) {
    return input.replace("\n","\r");
  }
}
