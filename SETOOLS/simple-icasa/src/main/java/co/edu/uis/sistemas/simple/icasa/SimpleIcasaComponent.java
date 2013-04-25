package co.edu.uis.sistemas.simple.icasa;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import fr.liglab.adele.icasa.device.light.BinaryLight;


@Component(name="SimpleIcasaComponent")
@Instantiate
public class SimpleIcasaComponent {
	
	@Requires(id="lights")
	private BinaryLight[] lights;
	
	private Thread modifyLightsThread;
	
	@Bind(id="lights")
	protected void bindLight(BinaryLight light) {
		System.out.println("A new light has been added to the platform " + light.getSerialNumber());
	}

	protected List<BinaryLight> getLights() {
		return Collections.unmodifiableList(Arrays.asList(lights));
	}

	
	@Validate
	public void start() {
		modifyLightsThread = new Thread(new ModifyLigthsRunnable());
		modifyLightsThread.start();
	}
	
	@Invalidate
	public void stop() throws InterruptedException {
		modifyLightsThread.interrupt();
		modifyLightsThread.join();
	}

	
	class ModifyLigthsRunnable implements Runnable {

		public void run() {
						
			boolean running = true;
			
			boolean onOff = false;
			while (running) {
				onOff = !onOff;
				try {
					List<BinaryLight> lights = getLights();
					for (BinaryLight binaryLight : lights) {
						binaryLight.setPowerStatus(onOff);
					}
					Thread.sleep(1000);					
				} catch (InterruptedException e) {
					running = false;
				}
			}
			
		}
		
	}
	
}
