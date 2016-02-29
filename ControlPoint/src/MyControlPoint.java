package src;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;

import com.sun.net.httpserver.HttpServer;

public class MyControlPoint extends ControlPoint implements DeviceChangeListener {

	private Device projetorDevice;
	private Device audioDevice;

	private final static String PROJETOR_DEVICE_TYPY = "LIMSI PhotoTextViewer";
	private final static String AUDIO_DEVICE_TYPE = "AudioPlayer";

	private final static String LIGHT_DEVICE_NAME = "CyberGarage Light Device";

	public MyControlPoint() {
		super();
		addDeviceChangeListener(this);
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
		AudioServer ahandler = new AudioServer(commande);
		VideoServer vhandler = new VideoServer(commande);
		server.createContext("/audio", ahandler);
		server.createContext("/video", vhandler);
		server.setExecutor(null); // creates a default executor
		
		server.start();
	}

	@Override
	public void deviceAdded(Device dev) {
		System.out.println("__device__" + dev.getFriendlyName());
		if (dev.getFriendlyName().contains("AudioPlayer") && audioDevice == null)
			audioDevice = dev;
		else if (dev.getFriendlyName().contains("PhotoTextViewer") && projetorDevice == null) {
			projetorDevice = dev;
		}
	}

	@Override
	public void deviceRemoved(Device dev) {
		// TODO Auto-generated method stub

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
		// action.setArgumentValue("Command", "pause");
		action.setArgumentValue("Command", "next");
		action.setArgumentValue("Argument", url);
		if (action.postControlAction() == true) {
			ArgumentList outArgList = action.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			for (int n = 0; n < nOutArgs; n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}
		} else {
			UPnPStatus err = action.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
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
		if (action.postControlAction() == true) {
			ArgumentList outArgList = action.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			for (int n = 0; n < nOutArgs; n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}
		} else {
			UPnPStatus err = action.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
	}

	public void setVolumne(int volume) {
		if (volume < 0 || volume > 100)
			return;

		if (audioDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setVolumne(volume);
			return;
		}

		Action action = audioDevice.getAction("ExecuteCommand");
		action.setArgumentValue("ElementName", "Lecteur_Audio");
		action.setArgumentValue("setVolume", volume);
		if (action.postControlAction() == true) {
			ArgumentList outArgList = action.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			for (int n = 0; n < nOutArgs; n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}
		} else {
			UPnPStatus err = action.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
	}

	public void playProjetor(String url) {
		System.out.println("projetor device " + (projetorDevice == null));
		if (projetorDevice == null) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			playProjetor(url);
		}

		Action photoAction = projetorDevice.getAction("SetPicture");
		photoAction.setArgumentValue("Picture", url);
		if (photoAction.postControlAction() == true) {
			ArgumentList outArgList = photoAction.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			for (int n = 0; n < nOutArgs; n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}
		} else {
			UPnPStatus err = photoAction.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
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
		if (photoAction.postControlAction() == true) {
			ArgumentList outArgList = photoAction.getOutputArgumentList();
			int nOutArgs = outArgList.size();
			for (int n = 0; n < nOutArgs; n++) {
				Argument outArg = outArgList.getArgument(n);
				String name = outArg.getName();
				String value = outArg.getValue();
			}
		} else {
			UPnPStatus err = photoAction.getStatus();
			System.out.println("Error Code = " + err.getCode());
			System.out.println("Error Desc = " + err.getDescription());
		}
	}

}


//action?name=audio
