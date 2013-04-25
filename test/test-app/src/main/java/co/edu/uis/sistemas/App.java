package co.edu.uis.sistemas;

import org.slf4j.*;

/**
 * Hello Everybody!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello Everybody!" );
		 Logger logger = LoggerFactory.getLogger(App.class);
		logger.info( "Hello Everybody!" );
    }
}
