package pageRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GraphGenerator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = "/home/xinwen/Downloads/NBCNewsData/mapNBCNewsDataFile.csv";
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		HashMap<String, String> urlFileMap = new HashMap<>();
		HashMap<String, String> fileUrlMap = new HashMap<>();
		while(line != null) {
			//System.out.println(line);
			String[] strs = line.split(",");
			String fileName = strs[0].trim();
			String url = strs[1].trim();
			urlFileMap.put(url, fileName);
			fileUrlMap.put(fileName, url);
			line = br.readLine();
		}
		PrintWriter out = new PrintWriter("/home/xinwen/Downloads/edgeList.txt", "UTF-8");
		String dirPath = "/home/xinwen/Downloads/NBCNewsData/NBCNewsDownloadData";
		File dir = new File(dirPath);
		HashSet<String> edges = new HashSet<>();
		for(File cur : dir.listFiles()) {
			Document doc = Jsoup.parse(cur, "UTF-8", fileUrlMap.get(cur.getName()));
			System.out.println("here");
			Elements links = doc.select("a[href]");
			for(Element link : links) {
				String url = link.attr("abs:href").trim();
				System.out.println(url);
				if (urlFileMap.containsKey(url)) {
					String temp = cur.getName() + " " + urlFileMap.get(url);
					edges.add(temp);
				}
			}
		}
		for(String edge : edges) {
			out.println(edge);
		}
		out.flush();
		out.close();
	}

}
