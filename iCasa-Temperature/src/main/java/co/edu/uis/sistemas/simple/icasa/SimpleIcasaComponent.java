package co.edu.uis.sistemas.simple.icasa;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
//import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.temperature.*;


@Component(name="SimpleIcasaComponent")
@Instantiate
public class SimpleIcasaComponent implements DeviceListener {
	
	private Thread modifyDevicesThread;
	
	/*
	@Requires(id="lights")
	private BinaryLight[] lights;
	@Bind(id="lights")
	protected void bindLight(BinaryLight light) {
		System.out.println("SIC: A new light has been added to the platform " + light.getSerialNumber());
	}
	protected List<BinaryLight> getLights() {
		return Collections.unmodifiableList(Arrays.asList(lights));
	}
	@Unbind(id="lights")
	protected void unbindLight(BinaryLight light) {
		//System.out.println("SIC: A heater has been removed to the platform " + light.getSerialNumber());
	}
	*/
	
	@Requires(id="thermometers")
	private Thermometer[] thermometers;
	@Bind(id="thermometers")
	protected void bindThermometer(Thermometer thermometer) {
		//System.out.println("SIC: A new thermometer has been added to the platform " + thermometer.getSerialNumber());
		thermometer.addListener(this);
	}
	protected List<Thermometer> getThermometers() {
		return Collections.unmodifiableList(Arrays.asList(thermometers));
	}
	@Unbind(id="coolers")
	protected void unbindThermometer(Thermometer thermometer) {
		//System.out.println("SIC: A thermometer has been removed to the platform " + thermometer.getSerialNumber());
		thermometer.removeListener(this);
	}
	
	@Requires(id="coolers")
	private Cooler[] coolers;
	@Bind(id="coolers")
	protected void bindCooler(Cooler cooler) {
		//System.out.println("SIC: A new cooler has been added to the platform " + cooler.getSerialNumber());
		cooler.addListener(this);
	}
	protected List<Cooler> getCoolers() {
		return Collections.unmodifiableList(Arrays.asList(coolers));
	}
	@Unbind(id="coolers")
	protected void unbindCooler(Cooler cooler) {
		//System.out.println("SIC: A cooler has been removed to the platform " + cooler.getSerialNumber());
		cooler.removeListener(this);
	}
	
	@Requires(id="heaters")
	private Heater[] heaters;	
	@Bind(id="heaters")
	protected void bindHeater(Heater heater) {
		//System.out.println("SIC: A new heater has been added to the platform " + heater.getSerialNumber());
		//heater.setPowerLevel(0.3);
		heater.addListener(this);
	}
	protected List<Heater> getHeaters() {
		return Collections.unmodifiableList(Arrays.asList(heaters));
	}
	@Unbind(id="heaters")
	protected void unbindHeater(Heater heater) {
		//System.out.println("SIC: A heater has been removed to the platform " + heater.getSerialNumber());
		heater.removeListener(this);
	}
	
	@Validate
	public void start() {
		modifyDevicesThread = new Thread(new ModifyDevicesRunnable());
		modifyDevicesThread.start();
	}
	
	@Invalidate
	public void stop() throws InterruptedException {
		modifyDevicesThread.interrupt();
		modifyDevicesThread.join();
	}

	class ModifyDevicesRunnable implements Runnable {

		public void run() {
						
			boolean running = true;
			System.out.println("SIC: running ");
			
			//boolean onOff = false;
			double powerOnCooler=1.0;
			double powerOnHeater=1.0;
			
			double powerOffCooler=0.0;
			double powerOffHeater=0.0;
			
			double mxTemperature=300.0;
			double mnTemperature=290.0;
			double tConfort=295.0;
			double tEnviroment=0.0;
			
			while (running) {
				//onOff = !onOff;
				
				try {
					/*
					List<BinaryLight> lights = getLights();
					for (BinaryLight binaryLight : lights) {
						binaryLight.setPowerStatus(onOff);
					}
					*/
					List<Thermometer> thermometers = getThermometers();
					List<Cooler> coolers = getCoolers();
					List<Heater> heaters = getHeaters();
					
					for (Thermometer thermometer : thermometers) {
						
						System.out.println("\n"); 						 
						System.out.println("Device: " + thermometer.getSerialNumber());
						System.out.println("Temperature: "+thermometer.getTemperature());
						System.out.println("Zone: "+thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME));
						
						tEnviroment=thermometer.getTemperature();
												
						if(tEnviroment>mxTemperature || tEnviroment>tConfort )
						{
							System.out.println("\n"); 
							System.out.println("Cooler: On Heater: Off "); 
							//Coolers
							for(Cooler cooler : coolers)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==cooler.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME))
								{
									cooler.setPowerLevel(powerOnCooler);
									System.out.println("Device: "+cooler.getSerialNumber());
									System.out.println("Power level :"+cooler.getPowerLevel());
								}
							}
							//Heaters
							for(Heater heater : heaters)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==heater.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME))
								{
									heater.setPowerLevel(powerOffHeater);
									System.out.println("Device: "+heater.getSerialNumber());
									System.out.println("Power level: "+heater.getPowerLevel());
								}
							}
						}
						if(tEnviroment<mnTemperature || tEnviroment<tConfort )
						{
							System.out.println("\n"); 
							System.out.println("Cooler: Off Heater:On "); 
							//Coolers
							for(Cooler cooler : coolers)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==cooler.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME))
								{
									cooler.setPowerLevel(powerOffCooler);							
									System.out.println("Device: "+cooler.getSerialNumber());
									System.out.println("Power level: "+cooler.getPowerLevel());
								}
							}
							//Heaters
							for(Heater heater : heaters)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==heater.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME))
								{
									heater.setPowerLevel(powerOnHeater);
									//System.out.println("Poder de heater = "+heater.getPowerLevel());							
									System.out.println("Device: "+heater.getSerialNumber());
									System.out.println("Power level: "+heater.getPowerLevel());
								}
							}
						}
					}
					Thread.sleep(5000);					
				} catch (InterruptedException e) {
					running = false;
				}
			}			
		}		
	}


	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}

	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void devicePropertyModified(GenericDevice device, String property, Object value) {
		// 
		String id= device.getSerialNumber();
		String location= (String) device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME);
		System.out.println("\n");
		System.out.println("Dispositivo: " + id);
		System.out.println("Zona: " + location);
		//System.out.println("Modificado: " + id);
		//System.out.println("Propiedad: " + property);
		System.out.println("Valor: " + value);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
