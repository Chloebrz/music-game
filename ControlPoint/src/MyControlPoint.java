import java.io.IOException;
import java.net.InetSocketAddress;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;

import com.sun.net.httpserver.HttpServer;

public class MyControlPoint extends ControlPoint implements
		DeviceChangeListener {

	private final static String METEO_DEVICE_NAME = "BrouzesCaroff Meteo Device";
	private final static String LIGHT_DEVICE_NAME = "CyberGarage Light Device";

	public MyControlPoint() {

		addDeviceChangeListener(this);

		start();
		search("upnp:rootdevice");
	}

	public void deviceAdded(Device dev) {

		String name = dev.getFriendlyName();
		System.out.println("Device added: " + name);

		if (name.equals(METEO_DEVICE_NAME))
			this.getTemp("Pointe-Noire");
		
//		if (name.equals(LIGHT_DEVICE_NAME))
//			this.turnLightOn();
	}

	public void deviceRemoved(Device dev) {

		String name = dev.getFriendlyName();
		System.out.println("Device removed: " + name);
	}
	
	public void turnLightOn() {
		
		Device dev = getDevice(LIGHT_DEVICE_NAME);

		if (dev == null)
			return;

		Action setPowerAct = dev.getAction("SetPower");
		setPowerAct.setArgumentValue("Power", "1");
		setPowerAct.postControlAction();
	}

	public void getTemp(String lieu) {

		Device dev = getDevice(METEO_DEVICE_NAME);

		if (dev == null)
			return;

		Action getTempAct = dev.getAction("GetTemperature");
		getTempAct.setArgumentValue("Lieu", lieu);
		getTempAct.postControlAction();
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
