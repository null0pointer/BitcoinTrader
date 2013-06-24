import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import com.mtgox.api.ApiKeys;
import com.mtgox.api.MtGox;


public class BitcoinTrader {
	
	private static String infile = "resources/sam.apikeys";
	private static String outfile = "resources/output.csv";
	private static String encoding = "UTF-8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String apiKey = "";
		String secretKey = "";
		
		try {
			Scanner scanner = new Scanner(new FileInputStream(infile), encoding);
			apiKey = scanner.nextLine();
			secretKey = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ApiKeys keys = new ApiKeys(secretKey, apiKey);
		
		MtGox trade = new MtGox(keys);
		trade.setPrintHTTPResponse(false);
		
		try {
			File f = new File(outfile);
			boolean outfileExists = f.exists();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile, true));
			
			if (!outfileExists) {
				writer.write("\"date\", \"price\"\n");
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			double previousPrice = 0;
			
			while (true) {
				double price = trade.getLastPriceUSD();
				
				if (price != previousPrice) {
					Date date = new Date();
					String dateString = dateFormat.format(date);
					System.out.println("$" + price + " at " + dateString);
					writer.write("\"" + dateString + "\", \"" + price + "\"\n");
					writer.flush();
					previousPrice = price;
				} else {
					System.out.println("$" + price + " is same as previous.");
				}
				
				Thread.sleep(15000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cleanDataFile(String filename) {
		try {
			Scanner scanner = new Scanner(new FileInputStream(filename), encoding);
			
			ArrayList<String> outputLines = new ArrayList<String>();
			outputLines.add(scanner.nextLine());
			
			double previousPrice = 0;
			
			int totalLines = 0;
			int removedLines = 0;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				double price = Double.parseDouble(line.split(",")[1].replaceAll("\"", ""));
				if (price != previousPrice) {
					outputLines.add(line);
					System.out.println(line);
					previousPrice = price;
				} else {
					removedLines++;
				}
				totalLines++;
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
			
			for (int i = 0; i < outputLines.size(); i++) {
				writer.write(outputLines.get(i));
			}
			
			writer.flush();
			writer.close();
			
			System.out.println("Removed " + (((double)removedLines / (double)totalLines) * 100) + "% of lines (" + removedLines + "/" + totalLines + ")");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
