/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Scanner;

/**
 *
 * @author marti
 */
public class Inputter {

	public static String inputPatternStr(String message, String pattern) {
		Scanner scanner;
		boolean flag = false;
		String response = "";
		while (!flag) {
			scanner = new Scanner(System.in);
			if (response.matches(pattern)) {
				flag = true;
			} else if (response.isEmpty()) {
				System.out.println(message);
				response = scanner.nextLine().trim();
			} else {
				System.err.println("Wrong format!");
				response = scanner.nextLine().trim();
			}
		}
		return response;
	}

	public static String inputNotBlankStr(String msg) {
		Scanner scanner;
		boolean flag = false;
		String response = "";
		while (!flag) {
			System.out.println(msg);
			scanner = new Scanner(System.in);
			response = scanner.nextLine().trim();
			if (!response.isEmpty()) {
				flag = true;
			} else {
				System.err.println("Cannot Be Blank!");
			}
		}
		return response;
	}

	public static short inputShort(String msg) {
		Scanner scanner;
		boolean flag = false;
		Short response = 0;
		String inputData;
		while (!flag) {
			try {
				System.out.println(msg);
				scanner = new Scanner(System.in);
				inputData = scanner.nextLine();
				response = Short.parseShort(inputData);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter only number");
			}
		}
		return response;
	}

	public static boolean inputBoolean(String message) {
		Scanner scanner;
		boolean flag = false;
		boolean response = false;
		String dataInput;
		while (!flag) {
			try {
				System.out.println(message);
				scanner = new Scanner(System.in);
				dataInput = scanner.nextLine();
				response = Boolean.parseBoolean(dataInput);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter true/false only");
			}
		}
		return response;
	}

	public static long inputLong(String message) {
		Scanner scanner;
		boolean flag = false;
		long response = 0;
		String dataInput;
		while (!flag) {
			try {
				System.out.println(message);
				scanner = new Scanner(System.in);
				dataInput = scanner.nextLine();
				response = Long.parseLong(dataInput);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter only number");
			}
		}
		return response;
	}

	public static int inputInteger(String message) {
		Scanner scanner;
		boolean flag = false;
		int response = 0;
		String dataInput;
		while (!flag) {
			try {
				System.out.println(message);
				scanner = new Scanner(System.in);
				dataInput = scanner.nextLine();
				response = Integer.parseInt(dataInput);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter only number");
			}
		}
		return response;
	}

	public static float inputFloat(String message) {
		Scanner scanner;
		boolean flag = false;
		float response = 0;
		String dataInput;
		while (!flag) {
			try {
				System.out.println(message);
				scanner = new Scanner(System.in);
				dataInput = scanner.nextLine();
				response = Float.parseFloat(dataInput);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter only number");
			}
		}
		return response;
	}

	public static double inputDouble(String message) {
		Scanner scanner;
		boolean flag = false;
		double response = 0;
		String dataInput;
		while (!flag) {
			try {
				System.out.println(message);
				scanner = new Scanner(System.in);
				dataInput = scanner.nextLine();
				response = Double.parseDouble(dataInput);
				flag = true;
			} catch (NumberFormatException numberException) {
				System.err.println("Invalid input. Please enter only number");
			}
		}
		return response;
	}
}
