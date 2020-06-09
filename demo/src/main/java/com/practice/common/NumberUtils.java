package com.practice.common;

public class NumberUtils {
	public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(str);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
