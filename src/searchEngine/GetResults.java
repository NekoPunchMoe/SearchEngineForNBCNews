package searchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class GetResults
 */
@WebServlet("/getresults")
public class GetResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String, String> idUrlMap = new HashMap<String, String>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public GetResults() throws IOException {
        super();
        String path = "/home/xinwen/workspace/csci572hw4/WebContent/mapNBCNewsDataFile.csv";
        //String path = getServletContext().getRealPath(relativeWebPath);
		File file = new File(path);
		BufferedReader br1 = new BufferedReader(new FileReader(file));
		String line1 = br1.readLine();
		while(line1 != null) {
			//System.out.println(line);
			String[] strs = line1.split(",");
			String id = "/home/xinwen/Downloads/NBCNewsData/NBCNewsDownloadData/" + strs[0].trim();
			String url = strs[1].trim();
			idUrlMap.put(id, url);
			line1 = br1.readLine();
		}
		br1.close();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.print("Hello World!");
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String line = br.readLine();
		while(line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		JSONObject str = new JSONObject(sb.toString());
		//JSONArray result = getResult(str);
		JSONArray result = getResult(str);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}
	
	private JSONArray getResult(JSONObject str) throws IOException {
		JSONArray result = new JSONArray();
		String target = "http://localhost:8983/solr/myexample/select?indent=on&q=" + ((String)str.get("term")).trim().replace(" ", "%20");
		String type = (String)str.get("type");
		if (!type.trim().equals("default")) {
			target += "&sort=pageRankFile%20desc";
		}
		target += "&wt=json";
		URL url = new URL(target);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		if (connection.getResponseCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String temp = br.readLine();
			while(temp != null) {
				sb.append(temp);
				temp = br.readLine();
			}
			JSONObject response = new JSONObject(sb.toString());
			JSONArray docs = response.getJSONObject("response").getJSONArray("docs");
			for(int i = 0; i < docs.length(); i++) {
				JSONObject cur = (JSONObject)docs.get(i);
				JSONObject newCur = new JSONObject();
				newCur.put("title", !cur.has("title")? "No title." : (String)cur.getJSONArray("title").get(0));
				newCur.put("id", (String)cur.get("id"));
				newCur.put("description", !cur.has("description") ? "No description." : (String)cur.getJSONArray("description").get(0));
				newCur.put("url", idUrlMap.get((String)cur.get("id")));
				result.put(newCur);
			}
		}
		return result;
	}

}
