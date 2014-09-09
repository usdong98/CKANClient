package com.sayit.ckan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ckan.resource.impl.Dataset;
import org.ckan.resource.impl.Resource;
import org.ckan.result.impl.DatasetSearchResult;
import org.ckan.result.list.impl.DatasetSearchList;

import com.google.gson.Gson;

public class FirstExam {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FirstExam ckan = new FirstExam();
		
		// TODO Auto-generated method stub
		String apiKey = "39c89141-8771-4e06-b595-67a5ae9abd37"; //usdong98 사용자
		int port = 5000;
		String searchDatasetTxt = "car";
		//org.ckan.Client client = new org.ckan.Client(new org.ckan.Connection("http://datahub.io"),apiKey);
		//org.ckan.Client client = new org.ckan.Client(new org.ckan.Connection("http://data.gg.go.kr", 80), apiKey);
		org.ckan.Client client = new org.ckan.Client(new org.ckan.Connection("http://122.199.152.135", port), apiKey);

        Dataset ds = null;
        DatasetSearchResult sr = null;
        try
        {
            sr = client.searchDatasets(searchDatasetTxt);  //[WOW] 그룹과 조직 id로 조회 가능
        }
        catch(Exception cke)
        {
            System.out.println("[WOW]" + cke);  
        }
        DatasetSearchList dsl = sr.result;
        List results = dsl.results;
        Iterator iterator = results.iterator();
        while(iterator.hasNext())
        {
            ds = (Dataset) iterator.next();
            System.out.println(ds);
            
            ckan.getResource(ds.getResources());
            
        }

//        try
//        {
//            ds = client.debugThis().getDataset(searchDatasetTxt);
//        }
//        catch(CKANException cke)
//        {
//            System.out.println(cke);
//        }
//        System.out.println(ds);

	}

	private void getResource(List<Resource> resources) {
		for (Resource res : resources) {
			System.out.println(res.getFormat());
			System.out.println(res.getHash());
			System.out.println(res.getDescription());
			System.out.println(res.getUrl());
			if(res.getFormat().equals("CSV")) {
				this.getData(res.getUrl());
			}
		}
		//Resource 포맷과 URL을 알 수 있으므로 차크 또는 Grid오픈 소스를 활용하여 표현할 수 있다.
	}
	
	private void getData(String url){
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		
		HttpResponse response;
		try {
			response = client.execute(request);
			
			//BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
			//한글꺠짐 방지
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"euc-kr"));
			
			StringBuffer result = new StringBuffer();
			String line = null;
			while((line = rd.readLine()) != null) {
				result.append(line + "\n");
			}
			System.out.println(result.toString());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
