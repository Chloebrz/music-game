package src;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VocalServer implements HttpHandler {

	private MyControlPoint control;

	public VocalServer(MyControlPoint control) {
		this.control = control;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {

		URI uri = t.getRequestURI();
		System.out.println("URI: " + uri);

		List<NameValuePair> params = URLEncodedUtils.parse(uri, "UTF-8");
		// cover params to map
		Map<String, String> paramap = new HashMap<String, String>();
		for (NameValuePair pair : params) {
			paramap.put(pair.getName(), pair.getValue());
		}

		// -> /vocal/annoucePlayer?playerName=...
		if (uri.getPath().contains("annoucePlayer")) {

			System.out.println("Action:annoucePlayer");
			if (paramap.containsKey("playerName")) {
				control.announcePlayer(paramap.get("playerName"));
			}
		}

		String response = "This is the response";
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
