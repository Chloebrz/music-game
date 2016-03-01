import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Server implements HttpHandler {
	
	private MyControlPoint control;
	
	public Server(MyControlPoint control) {
		
		this.control = control;
	}

	@Override
    public void handle(HttpExchange t) throws IOException {
    	
        String response = "This is the response";
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        
        control.turnLightOn();
    }
}