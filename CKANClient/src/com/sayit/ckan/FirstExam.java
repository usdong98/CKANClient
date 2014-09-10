package com.sayit.ckan;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ckan.Client;
import org.ckan.resource.impl.Dataset;
import org.ckan.resource.impl.Resource;
import org.ckan.result.impl.DatasetSearchResult;
import org.ckan.result.list.impl.DatasetSearchList;

import com.sayit.utils.convert.csv.CSV2JSON;

public class FirstExam {
//	private static String ckanUrl = "http://data.gg.go.kr";
//	private static int port = 80;
	private static String ckanUrl = "http://122.199.152.135";
	private static int port = 5000;
	private static String apiKey = "d49edc6f-ac84-4576-a107-ee62afbce903";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FirstExam ckan = new FirstExam();
		String searchDatasetTxt = null;
		org.ckan.Client client = null;
		
		// TODO Auto-generated method stub
		
		
		//client = new org.ckan.Client(new org.ckan.Connection("http://datahub.io"),apiKey);
		client = new org.ckan.Client(new org.ckan.Connection(FirstExam.ckanUrl, FirstExam.port), FirstExam.apiKey);
		//searchDatasetTxt = "administration";
		searchDatasetTxt = "test002";
		
		
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
            ckan.getResource(client, ds.getResources());
            break;
            
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

	private void getResource(Client client, List<Resource> resources) {
		for (Resource res : resources) {
			System.out.println("RESOURCE ID : " + res.getId());
			System.out.println("RESOURCE FORMAT : " + res.getFormat());
			System.out.println("RESOURCE HASH : " + res.getHash());
			System.out.println("RESOURCE DESC : " + res.getDescription());
			System.out.println("RESOURCE URL : " + res.getUrl());
			//Resource 포맷과 URL을 알 수 있으므로 차크 또는 Grid오픈 소스를 활용하여 표현할 수 있다.
			if(res.getFormat().equals("CSV")) {
				this.getUrlData(res.getUrl());
				this.getAPIData(res.getId());
			}
		}
	}
	
	private void getAPIData(String id) {
		//curl -X GET "http://122.199.152.135:5000/api/3/action/datastore_search?resource_id=6005a2af-2351-45fe-a0e4-d63d35c0da6d"
		//http://docs.ckan.org/en/latest/maintaining/datastore.html?highlight=datastore_search#ckanext.datastore.logic.action.datastore_search
		String url = FirstExam.ckanUrl + ":" + FirstExam.port + "/api/action/datastore_search?sort=&resource_id=" + id;
		
		//참조  - http://docs.ckan.org/en/latest/api/index.html (찾기 : resource_show)
		//Return the metadata of a resource.
		//String url = FirstExam.ckanUrl + ":" + FirstExam.port + "/api/3/action/resource_show?id=" + id;
		
		//url = FirstExam.ckanUrl + ":" + FirstExam.port + "/api/3/action/resource_view_reorder?id=" + id;
		
		
		System.out.println("URL : " + url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		//request.addHeader("Authorization", FirstExam.apiKey);
		
		HttpResponse response;
		try {
			response = client.execute(request);
			
			//한글꺠짐 방지
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"euc-kr"));

			StringBuffer result = new StringBuffer();
			String line = null;
			while((line = rd.readLine()) != null) {
				result.append(line + "\n");
			}
			System.out.println(result.toString());
			
			rd.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getUrlData(String url){
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		
		HttpResponse response;
		try {
			response = client.execute(request);
			
			//한글꺠짐 방지
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"euc-kr"));

			this.CSV2Json(rd);
			rd.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void CSV2Json(BufferedReader reader) {
		CSV2JSON c2j = new CSV2JSON();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			List list = c2j.readAll(reader);
			c2j.toJson(list, bos);
			System.out.println(bos.toString());
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Gson gson = new Gson();
		//System.out.println(gson.toJson(c2j));
		
	}
}
