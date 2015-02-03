package botdriver;

import googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import taskstructure.CheckNumberofTroopsTask;
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
			synchronized(this){
			TaskQueue.push(new CheckNumberofTroopsTask(driver, this));
			this.wait();
			}
			Map<String, String> troop_map = SheetDriver.getTroopCount("Village 1");
			if(Integer.parseInt(troop_map.get("legionnaire")) >= 3){
				TaskQueue.push(new SendTroopsTask(driver, coords.split(",")[0], coords.split(",")[1]));
				SheetDriver.updateLastFarmTime(coords);
			}
		} catch (IOException | ServiceException | InterruptedException e) {e.printStackTrace();}
	}
}
