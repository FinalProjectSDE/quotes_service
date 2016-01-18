package introsde.motivationalphrases.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import introsde.motivationalphrases.model.Phrase;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.StringReader;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Path("/phrase")
public class PhraseCollectionResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Path("/random")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Phrase getRandomPhrase() {
		int source1_pages_number = 4;
		Random random = new Random();
		int random_page = showRandomInteger(1, (source1_pages_number-1), random);
		String source_backup = "http://www.stands4.com/services/v2/quotes.php?uid=4725&tokenid=BoDSnUkk32raC1gq&searchtype=random";
		//String source1 = "http://www.quoteland.com/topic/Motivational-Quotes/232/?pg=" + random_page;
		String sourceHTML = null;
		Phrase quote = new Phrase();
		try {
			quote.setText(Stands4parser(source_backup));
		} catch (Exception e) {
			System.out.println("ERROR: It was impossible to retrieve the HTML page: " + source_backup);
			System.out.println("Well, let's try another service");
			e.printStackTrace();
			/*try {
				sourceHTML = readHTMLfromPage(source1);
				quote.setText(parseQuotes(sourceHTML));
			} catch (Exception e1) {
				System.out.println("Today without new quotes! Work hard and be yourself! " + source1);
				e1.printStackTrace();
			}*/
		}
		System.out.println(quote.getText());
		
	return quote;	
	}

	// html retriever
	public String readHTMLfromPage(String url) throws Exception {
		URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        String inputLine;
        String outputHTML = "";
        while ((inputLine = in.readLine()) != null)
        	outputHTML+=inputLine;
        in.close();
        return outputHTML;
	}
	

	public String parseQuotes(String html) {
		int quotes_number = 25;
		Random random = new Random();
		int random_quote = showRandomInteger(1, quotes_number, random);
		Document doc = Jsoup.parse(html);
		Element row = doc.select("body > table:nth-child(5) > tbody "
				+ "> tr > td:nth-child(2) > table:nth-child(1) "
				+ "> tbody > tr:nth-child(4) > td:nth-child(2) "
				+ "> table > tbody > tr:nth-child(1) > td:nth-child(1) "
				+ "> font > table:nth-child(12) > tbody > "
				+ "tr:nth-child("+ ((random_quote*2)-1) +") > td:nth-child(2)").first();
		Elements quoteElements = row.select(">font");
		return quoteElements.first().text() + "\n" + quoteElements.get(1).text();
	}
	
	// specific parser for 
	public String Stands4parser(String url) throws Exception {
		String quote ="";
		String author ="";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to: " + url);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		// parsing xml strings
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			InputSource is;
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(response.toString()));
            org.w3c.dom.Document doc = builder.parse(is);
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            quote = (String) xpath.evaluate("/results/result/quote", doc, XPathConstants.STRING);
            author = (String) xpath.evaluate("/results/result/author", doc, XPathConstants.STRING);
          
		return quote + "(c) "+ author;
	}
	
	
	//number generator
	private static int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    long range = (long)aEnd - (long)aStart + 1;
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	 }
}
