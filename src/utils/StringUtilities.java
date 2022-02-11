/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 *
 * @author marti
 */
public class StringUtilities {

	public static String generateRepeatedString(String target, int times) {
		Objects.requireNonNull(target);
		String response = "";
		while (times > 0) {
			response = response + target;
			times--;
		}
		return response;
	}

	public static String toCamelCase(String target) {
		Objects.requireNonNull(target);
		String response = "";
		target = target.toLowerCase();
		String[] split = target.split("\\s");
		for (String e : split) {
			response += e.substring(0, 1).toUpperCase() + e.substring(1) + " ";
		}
		return response.trim();
	}

	public static String toPretty(String target) {
		Objects.requireNonNull(target);
		String doubleRegex = "\\d{1,15}\\.\\d{1,15}";
		String longRegex = "\\d{1,18}";
		String response = "";
		String pattern = "###,###.###";
		if (target.matches(doubleRegex)) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			double value = Double.parseDouble(target);
			response = decimalFormat.format(value);
		} else if (target.matches(longRegex)) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			double value = Long.parseLong(target);
			response = decimalFormat.format(value);
		} else  {
			response = StringUtilities.toCamelCase(target);
		} 
		return response;
	}
	/*public static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }*/
}
