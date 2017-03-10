package mahjong_game;

import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JMenu;

public class DClock extends Thread
{
	boolean keepRunning = true;
	GregorianCalendar startTime = null;
	JFrame theFrame = null;
	long theDiff, theHours, theMinutes, theSeconds;
	String theResult = "";
	String theGameNumberTitle = "";
	JMenu theMenu = null;
	
	public DClock(JFrame frame){
		theFrame = frame;
		theGameNumberTitle = frame.getTitle();
	}
	
	public DClock(JMenu menu){
		theMenu = menu;
	}
	
	
	public void run()
	{
		try
		{
			startTime = new GregorianCalendar();
			
			while (keepRunning)
			{
				
				//System.out.printf("%tT\n", now);
				theDiff = ((new GregorianCalendar().getTimeInMillis() - startTime.getTimeInMillis()));
				theHours = ((theDiff / 1000) / 3600);
				theMinutes = ((theDiff / 1000) /60);
				theSeconds = ((theDiff / 1000) % 60);
				theResult = (theHours > 0) ? String.format("%02d:%02d:%02d", theHours, theMinutes, theSeconds)
						: String.format("%02d:%02d", theMinutes, theSeconds);
				//theFrame.setTitle(theGameNumberTitle + "    " +  theResult);
				theMenu.setText(theResult);
				Thread.sleep(1000);
			}
			//theFrame.setTitle("");
		}
		catch (InterruptedException ie)
		{
			System.err.println("DClock.run: " + ie);
		}
	}


	public static void main(String[] args)
	{
		//DClock	clock = new DClock();
		//clock.start();
		new DClock(new JFrame()).start();
	}
}
