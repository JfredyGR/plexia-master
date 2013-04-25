package co.edu.uis.sistemas.simple;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;


@Component(name="SimpleComponent")
@Instantiate
public class SimpleComponent {

	@Validate
	public void start() {
		System.out.println("-----> My First Component");
	}
	
	@Invalidate
	public void stop() {
		System.out.println("---->   Bye Component");
	}
	
}
