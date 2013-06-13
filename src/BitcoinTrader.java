import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.mtgox.api.ApiKeys;
import com.mtgox.api.MtGox;


public class BitcoinTrader {
	
	private static String infile = "resources/sam.apikeys";
	private static String encoding = "UTF-8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.setProperty("javax.net.ssl.trustStore","resources/ssl/mtgox.jks");
		System.setProperty("javax.net.ssl.trustStorePassword","password");
		
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
		trade.setPrintHTTPResponse(true);
		
		try {
			while (true) {
				System.out.println(trade.getLastPriceUSD());
				Thread.sleep(30000);
			}
		} catch (Exception e) {
			
		}
	}

}
