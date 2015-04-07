package main;


import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import controller.master.MasterController;


public class PokemonMAIN implements ServletContextListener
{
	protected static Logger logger = Logger.getLogger("pokemon");

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		Thread controllerThread = new Thread(new Runnable() 
		{ 
			@Override
			public void run()
			{	
				startControllers();
			}

		});
		
		controllerThread.start();
	}
	
	private void startControllers()
	{
		logger.info("\n*********** STARTING POKEMON - PSEUDO MAIN");
		
		
		MasterController masterController = MasterController.getInstance();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		
	}
}
