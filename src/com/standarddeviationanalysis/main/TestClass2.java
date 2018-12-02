package com.standarddeviationanalysis.main;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestClass2 {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
    HttpClient client = HttpClients.createDefault();
    HttpUriRequest request = new HttpGet("https://www.nseindia.com/live_market/dynaContent/live_watch/option_chain/optionKeys.jsp?segmentLink=17&instrument=OPTSTK&symbol=ACC&date=27DEC2018");
    HttpResponse response = client.execute(request);
    System.out.println(EntityUtils.toString(response.getEntity()));
	}
    		

}
