import java.io.IOException;
import java.net.InetSocketAddress;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;

import com.sun.net.httpserver.HttpServer;

public class MyControlPoint extends ControlPoint  {

	private final static String LIGHT_DEVICE_NAME = "CyberGarage Light Device";

	public MyControlPoint() {

		start();
		search("upnp:rootdevice");
	}

	public void turnLightOn() {
		
		Device dev = getDevice(LIGHT_DEVICE_NAME);

		if (dev == null)
			return;

		Action setPowerAct = dev.getAction("SetPower");
		setPowerAct.setArgumentValue("Power", "1");
		setPowerAct.postControlAction();
	}

	public static void main(String[] args) throws IOException {

		final MyControlPoint commande = new MyControlPoint();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				commande.start();
			}
		}).start();
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        Server handler = new Server(commande);
        server.createContext("/test", handler);
        server.setExecutor(null); // creates a default executor
        server.start();
	}
}

