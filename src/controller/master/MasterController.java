package controller.master;


public class MasterController 
{
	private static MasterController instance = null;
	
	private MasterController()
	{
		
	}
	
	public static MasterController getInstance()
	{
		if (instance == null)
		{
			instance = new MasterController();
		}
		return instance;
	}

}
