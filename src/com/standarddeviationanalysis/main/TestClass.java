package com.standarddeviationanalysis.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestClass {

	public static void main(String[] args) throws ParseException, ClientProtocolException, IOException {
		HttpUriRequest httpRequest = new HttpGet("https://www.nseindia.com/content/fo/fo_secban.csv");
		String response = EntityUtils.toString(HttpClients.createDefault().execute(httpRequest).getEntity());
		System.out.println(response);
		String arr = response.split(":")[1];

		// System.out.println(arr);
		// String[] newArr = arr.split(",");
		// System.out.println("First element: " + newArr[0]);
		// Pattern p = Pattern.compile("/^[A-Za-z]+$/");
		// Matcher m = p.matcher(arr);
		// while(m.find())
		// { System.out.println("Inside loop");
		// System.out.println(m.group());
		// }
		String[] newArr = arr.split("\n");

		// System.out.println("First Element: " + newArr[1]);
		// for(int i=0;i<newArr.length;i++)
		// {
		// System.out.println(newArr[i]);
		// }
		List<String> bannedSecurities = new ArrayList<>();
		for (int i = 0; i < newArr.length; i++) {
			if (newArr[i] != null && newArr[i].length() != 0 && newArr[i] != " " && newArr[i].contains(",")) {
				// System.out.println("Security: " + newArr[i].length());
				bannedSecurities.add(newArr[i].split(",")[1].trim());
			}
		}

		System.out.println("Banned securities: ");
		for (String security : bannedSecurities) {
			System.out.println(security);
		}
	}

}
