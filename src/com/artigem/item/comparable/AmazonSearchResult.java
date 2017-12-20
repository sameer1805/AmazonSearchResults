package com.artigem.item.comparable;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 */
public class AmazonSearchResult {

   
	 /*
     * Your AWS Associate tag.
     */
    private static final String AWS_ASSOCIATE_TAG = "3circlecrm-20";//"artigem1-20";
	
	/*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAIHGTNPW74ULNGTHA";//"AKIAIQDOMUIA7A7YW75Q";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "93mvrtM3t2U1gUlW/2rjHPj7rUo0VIhjwRBL79iG";//"SqgGtYyAUc6ZmcrYdp/6DUj4P4TfcoF14yVjmeR0";

    /*
     * Use the end-point according to the region you are interested in.
     */
    private static final String ENDPOINT = "webservices.amazon.com";
    
    /*
     * Use a sample search key for results
     */
    private static final String SEARCH_KEYWORDS ="macbook air";

    public static void main(String[] args) {

        /*
         * Set up the signed requests helper.
         */
        SignedRequestsHelper helper;

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
        params.put("SearchIndex", "All");
        params.put("Keywords", SEARCH_KEYWORDS);//"izod shirt");
        params.put("ResponseGroup", "Images,ItemAttributes,Offers,OfferSummary");

        requestUrl = helper.sign(params);

        System.out.println("Signed URL: \"" + requestUrl + "\"");
        
        /*String title = fetchTitle(requestUrl);
        System.out.println("Signed Title is \"" + title + "\"");
        System.out.println();*/
        
        String jsonResponse = fetchItemDetailsInJSON(requestUrl);
        System.out.println("Signed JSON response is \""+jsonResponse+"\"");

       
    }
    
    public String getResultsFromAmazon(String searchKey) {
        System.out.println("AmazonSearchResult.getResultsFromAmazon() >>> "+searchKey);
        /*
         * Set up the signed requests helper.
         */
        SignedRequestsHelper helper;

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"\"}";
        }

        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
        params.put("AssociateTag", AWS_ASSOCIATE_TAG);
        params.put("SearchIndex", "All");
        params.put("Keywords", searchKey);
        params.put("ResponseGroup", "Images,ItemAttributes,Offers,OfferSummary");

        requestUrl = helper.sign(params);

        System.out.println("Signed URL: \"" + requestUrl + "\"");
        
        /*String title = fetchTitle(requestUrl);
        System.out.println("Signed Title is \"" + title + "\"");
        System.out.println();*/
        
        String jsonResponse = fetchItemDetailsInJSON(requestUrl);
        System.out.println("Signed JSON response is \""+jsonResponse+"\"");
        return jsonResponse;

    }
    
    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
    }
    
    
    /*
     * Utility function to fetch the response from the service and extract the
     * information about items in JSON format from the XML.
     */
    private static String fetchItemDetailsInJSON(String requestUrl) {
    	StringBuffer resultString = new StringBuffer();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            System.out.println("xml response ..."); System.out.println(doc.toString());
            String resultvalidNode = doc.getElementsByTagName("IsValid").item(0).getTextContent();
            String totalResults = doc.getElementsByTagName("TotalResults").item(0).getTextContent();
                    resultString.append("{\"success\":\""+resultvalidNode+"\", ");
            		resultString.append("\"totalResults\": \""+totalResults+"\", "); 
            		resultString.append("\"items\": [");
            NodeList allItems = doc. getElementsByTagName("Item");
            if(allItems != null && allItems.getLength() > 0){
            	
            	for (int i = 0; i < allItems.getLength(); i++) {
            		System.out.println("item # "+i);
            		Node itemNode = allItems.item(i);
            		
            		if (itemNode.getNodeType() == Node.ELEMENT_NODE) {

            		Element item = (Element) itemNode;
            		
            		Element itemAttribute = (Element)(item.getElementsByTagName("ItemAttributes").item(0));
            		
            		resultString.append("{");
            		resultString.append("\"description\":\""+ itemAttribute.getElementsByTagName("Title").item(0).getTextContent().replace('"',' ') +"\",");
            		resultString.append("\"brand\":\""+ itemAttribute.getElementsByTagName("Brand").item(0).getTextContent() +"\",");
            		try{
            		resultString.append("\"model\":\""+ itemAttribute.getElementsByTagName("Model").item(0).getTextContent()  +"\",");
            		}catch(NullPointerException e){
            			resultString.append("\"model\":\"N/A\",");
            		}
            		
            		try{
            		resultString.append("\"price\":\""+ ((Element)itemAttribute.getElementsByTagName("ListPrice").item(0)).getElementsByTagName("FormattedPrice").item(0).getTextContent()  +"\",");
            		}catch(NullPointerException e){
            			try{
            			resultString.append("\"price\":\""+ ((Element)((Element)(item.getElementsByTagName("OfferSummary").item(0))).getElementsByTagName("LowestNewPrice").item(0)).getElementsByTagName("FormattedPrice").item(0).getTextContent()  +"\",");
            			} catch(NullPointerException ne){
            				resultString.append("\"price\":\"N/A\",");
            			}	
            		}
            		
            		
            		resultString.append("\"imageURL\":\""+ ((Element)(item.getElementsByTagName("MediumImage").item(0))).getElementsByTagName("URL").item(0).getTextContent()+"\","); //can fetch the "SmallImage"
            		resultString.append("\"buyURL\":\""+item.getElementsByTagName("DetailPageURL").item(0).getTextContent()+"\"");
            		
            		resultString.append("}");
            		
            		}
            		
            		if(i < (allItems.getLength()-1)){
            			resultString.append(",");
            		}
            	}
            }
            
            resultString.append("]}");
            return resultString.toString();               
            
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
        //return title;
    }

}
