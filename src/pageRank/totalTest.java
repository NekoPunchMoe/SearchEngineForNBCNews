package pageRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class totalTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		totalTest.printDir();
	}
	
	public static void testForUrl() throws IOException {
		String target = "http://localhost:8983/solr/myexample/select?indent=on&q=_text_:Brexit&sort=pageRankFile%20desc&wt=json";
		URL url = new URL(target);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = br.readLine();
		while(line != null) {
			System.out.println(line);
			line = br.readLine();
		}
	}
	
	public static void testForMap() throws IOException {
		HashMap<String, String> idUrlMap = new HashMap<>();
		String path = "src/searchEngine/mapNBCNewsDataFile.csv";
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while(line != null) {
			//System.out.println(line);
			String[] strs = line.split(",");
			String id = "/home/xinwen/Downloads/NBCNewsData/NBCNewsDownloadData/" + strs[0].trim();
			String url = strs[1].trim();
			System.out.println("id:" + id + " url:" + url);
			idUrlMap.put(id, url);
			line = br.readLine();
		}
	}
	public static void getOverlap() throws IOException {
		String base = "http://localhost:8983/solr/myexample/select?indent=on&q=";
		String[] input = {"Brexit", "NASDAQ", "NBA", "Snapchat", "Illegal Immigration", "Donald Trump", "Russia", "NASA"};
		for(int i = 0; i < input.length; i++) {
			HashSet<String> occur = new HashSet<String>();
			String target1 = base + input[i].trim().replace(" ", "%20") + "&wt=json";
			String target2 = base + input[i].trim().replace(" ", "%20") + "&sort=pageRankFile%20desc&wt=json";
			JSONObject response1 = getResponse(target1);
			JSONArray docs1 = response1.getJSONObject("response").getJSONArray("docs"); 
			for(int j = 0; j < docs1.length(); j++) {
				String id = docs1.getJSONObject(j).getString("id");
				System.out.println("docs1:" + id);
				occur.add(id);
			}
			int num = 0;
			JSONObject response2 = getResponse(target2);
			JSONArray docs2 = response2.getJSONObject("response").getJSONArray("docs");
			for(int j = 0; j < docs2.length(); j++) {
				String id = docs2.getJSONObject(j).getString("id");
				System.out.println("docs2:" + id);
				if (occur.contains(id)) {
					num++;
				}
			}
			System.out.println("Term:" + input[i] + " Nums:" + num);
		}
	
	}
	
	public static JSONObject getResponse(String target) throws IOException {
		URL url = new URL(target);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while(line != null) {
			sb.append(line);
			line = br.readLine();
		}
		JSONObject response1 = new JSONObject(sb.toString());
		return response1;
	}
	public static void printDir () {
		File dir = new File("");
		System.out.println(dir.getAbsolutePath());
	}

}
