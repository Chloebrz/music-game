package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoServer implements HttpHandler {

	private MyControlPoint control;

	public VideoServer(MyControlPoint control) {

		this.control = control;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		
		// -> /video -> requestBody:(html content)
		URI uri = t.getRequestURI();
		System.out.println(uri.toString());

		int i;
		StringBuilder sb = new StringBuilder();
		InputStream in = t.getRequestBody();
		InputStreamReader reader = new InputStreamReader(in);
		while ((i = reader.read()) != -1) {
			sb.append((char) i);
		}
		//System.out.println("the body:" + sb.toString());
		in.close();
		String htmlbody = sb.toString();

		String response = "This is the response";
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();

		control.displayHtml(htmlbody);;
	}
}