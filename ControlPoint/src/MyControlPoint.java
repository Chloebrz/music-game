package src;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;

import com.sun.net.httpserver.HttpServer;

public class MyControlPoint extends ControlPoint implements
		DeviceChangeListener {

	private Device projetorDevice;
	private Device audioDevice;

	private final static String PROJETOR_DEVICE_NAME = "LIMSI PhotoTextViewer";
	private final static String AUDIO_DEVICE_NAME = "LIMSI AudioPlayer";

	public MyControlPoint() {

		super();
		addDeviceChangeListener(this);
		start();
		search("upnp:rootdevice");
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
		AudioServer ahandler = new AudioServer(commande);
		VideoServer vhandler = new VideoServer(commande);
		server.createContext("/audio", ahandler);
		server.createContext("/video", vhandler);
		server.setExecutor(null);

		server.start();
	}

	@Override
	public void deviceAdded(Device dev) {
		
		String name = dev.getFriendlyName();
		System.out.println("Device added: " + name);

		if (name.equals(AUDIO_DEVICE_NAME) && audioDevice == null) audioDevice = dev;
		
		else if (name.equals(PROJETOR_DEVICE_NAME) && projetorDevice == null) projetorDevice = dev;
	}

	@Override
	public void deviceRemoved(Device dev) {
		
		String name = dev.getFriendlyName();
		System.out.println("Device removed: " + name);

		if (name.equals(AUDIO_DEVICE_NAME)) audioDevice = null;
		
		else if (name.equals(PROJETOR_DEVICE_NAME)) projetorDevice = null;
	}
	
	public void setPostControl(Action action) {
		
		if (action.postControlAction() == true) {
			
			ArgumentList outArgList = action.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			
			for (int n = 0; n < nOutArgs; n++) {
				
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
				System.out.println("Name: " + name + " & value: " + value);
			}
		} else {
			
			UPnPStatus err = action.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
	}

	public void playMusic(String url) {

		if (audioDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			playMusic(url);
			return;
		}

		Action action = audioDevice.getAction("ExecuteCommand");
		action.setArgumentValue("ElementName", "Lecteur_Audio");
		action.setArgumentValue("Command", "next");
		action.setArgumentValue("Argument", url);
		setPostControl(action);
	}

	public void pauseMusic() {
		
		if (audioDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pauseMusic();
			return;
		}

		Action action = audioDevice.getAction("ExecuteCommand");
		action.setArgumentValue("ElementName", "Lecteur_Audio");
		action.setArgumentValue("Command", "pause");
		setPostControl(action);
	}

	public void setVolume(int volume) {

		if (volume < 0 || volume > 100)
			return;

		if (audioDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setVolume(volume);
			return;
		}

		Action action = audioDevice.getAction("ExecuteCommand");
		action.setArgumentValue("ElementName", "Lecteur_Audio");
		action.setArgumentValue("Command", "setVolume");
		action.setArgumentValue("Argument", volume);
		setPostControl(action);
	}

	public void playProjetor(String url) {

		System.out.println("projetor device " + (projetorDevice == null));

		if (projetorDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			playProjetor(url);
		}

		Action photoAction = projetorDevice.getAction("SetPicture");
		photoAction.setArgumentValue("Picture", url);

		setPostControl(photoAction);
	}

	public void displayHtml(String url) {

		System.out.println("projetor device " + (projetorDevice == null));

		if (projetorDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			displayHtml(url);
		}

		Action photoAction = projetorDevice.getAction("SetText");
		photoAction.setArgumentValue("Text", url);
		
		setPostControl(photoAction);
	}
}

