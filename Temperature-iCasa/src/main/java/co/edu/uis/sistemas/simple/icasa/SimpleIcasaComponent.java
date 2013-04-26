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
import fr.liglab.adele.icasa.device.temperature.*;


@Component(name="SimpleIcasaComponent")
@Instantiate
public class SimpleIcasaComponent implements DeviceListener {
	
	@Requires(id="thermometers")
	private Thermometer[] thermometers;
	@Requires(id="coolers")
	private Cooler[] coolers;
	@Requires(id="heaters")
	private Heater[] heaters;
	
	
	private Thread TemperatureThread;
	
	@Bind(id="thermometers")
	protected void bindThermometer(Thermometer thermometer) {
		thermometer.addListener(this);
		
	}
	@Bind(id="coolers")
	protected void bindCooler(Cooler cooler) {
		cooler.addListener(this);
		
	}
	@Bind(id="heaters")
	protected void bindHeater(Heater heater) {
		heater.addListener(this);
		
	}
	@Unbind(id="thermometers")
	protected void unbindThermometer(Thermometer thermometer) {
		thermometer.removeListener(this);
		
	}
	@Unbind(id="coolers")
	protected void unbindCooler(Cooler cooler) {
		cooler.removeListener(this);
		
	}
	@Unbind(id="heaters")
	protected void unbindHeater(Heater heater) {
		heater.removeListener(this);
		
	}
	protected List<Thermometer> getThermometers() {
		return Collections.unmodifiableList(Arrays.asList(thermometers));
	}
	protected List<Cooler> getCoolers() {
		return Collections.unmodifiableList(Arrays.asList(coolers));
	}
	protected List<Heater> getHeaters() {
		return Collections.unmodifiableList(Arrays.asList(heaters));
	}

	
	@Validate
	public void start() {
		TemperatureThread = new Thread(new TemperatureRunnable());
		TemperatureThread.start();
	}
	
	@Invalidate
	public void stop() throws InterruptedException {
		TemperatureThread.interrupt();
		TemperatureThread.join();
	}

	
	class TemperatureRunnable implements Runnable {

		public void run() {
						
			boolean running = true;
			while (running) {
				try {
					List<Thermometer> thermometers = getThermometers();
					List<Cooler> coolers = getCoolers();
					List<Heater> heaters = getHeaters();
					for (Thermometer thermometer : thermometers) {
						System.out.println("Temperatura del term de "+thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)+" = "+thermometer.getTemperature());
						if(thermometer.getTemperature()>=300){
							for(Cooler cooler : coolers)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==cooler.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)){
								cooler.setPowerLevel(1.0/*(thermometer.getTemperature()-300)/300*/);
								System.out.println("Poder de cooler = "+cooler.getPowerLevel());}
							}
							for(Heater heater : heaters)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==heater.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)){
								heater.setPowerLevel(0.0);
								System.out.println("Poder de heater = "+heater.getPowerLevel());
								}
							}
						}
						if(thermometer.getTemperature()<=290){
							for(Cooler cooler : coolers)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==cooler.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)){
								cooler.setPowerLevel(0.0);
								System.out.println("Poder de cooler = "+cooler.getPowerLevel());
								}
							}
							for(Heater heater : heaters)
							{
								if(thermometer.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)==heater.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME)){
								heater.setPowerLevel(1.0/*(290-thermometer.getTemperature())/290*/);
								System.out.println("Poder de heater = "+heater.getPowerLevel());
								}
							}
						}
					}
					Thread.sleep(100);					
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
		String id = device.getSerialNumber();
		String location = (String) device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME);
		System.out.println("Modificado " + id + " propiedad " + property + " valor " + location);
		
		
	}

	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
