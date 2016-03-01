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

public class AudioServer implements HttpHandler {

	private MyControlPoint control;

	public AudioServer(MyControlPoint control) {
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

		// -> /audio/playMusic?musicurl=...
		if (uri.getPath().contains("playMusic")) {

			System.out.println("Action:playMusic");
			if (paramap.containsKey("musicurl")) {
				control.playMusic(paramap.get("musicurl"));
			}
		}

		// -> /audio/pauseMusic
		else if (uri.getPath().contains("pauseMusic")) {

			System.out.println("Action:pauseMusic");
			control.pauseMusic();
		}
		
		// -> /audio/setVolume?value=(0-100)
		else if (uri.getPath().contains("setVolume")) {

			System.out.println("Action:setVolume");
			if (paramap.containsKey("value")) {
				System.out.println("value:" + paramap.get("value"));
				control.setVolume(Integer.parseInt(paramap.get("value")));
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
