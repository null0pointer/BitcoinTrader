import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
			
			while (true) {
				double price = trade.getLastPriceUSD();
				Date date = new Date();
				String dateString = dateFormat.format(date);
				System.out.println("$" + price + " at " + dateString);
				writer.write("\"" + dateString + "\", \"" + price + "\"\n");
				writer.flush();
				Thread.sleep(15000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
