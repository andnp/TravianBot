package botdriver;

import googlesheetcontroller.SheetDriver;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import taskstructure.SendTroopsTask;
import taskstructure.TaskQueue;

import com.google.gdata.util.ServiceException;

public class FarmDriver extends Thread{
	MapDriver map_driver;
	WebDriver driver;
	public FarmDriver(MapDriver mp, WebDriver dr){
		this.map_driver = mp;
		this.driver = dr;
	}
	
	public void run(){
		try {
			String coords = map_driver.findNearestEmpty();
			TaskQueue.push(new SendTroopsTask(driver, coords.split(",")[0], coords.split(",")[1]));
			SheetDriver.updateLastFarmTime(coords);
		} catch (IOException | ServiceException e) {e.printStackTrace();}
	}
}
