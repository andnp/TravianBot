package main.java.botdriver;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

import main.java.taskstructure.CheckNumberofTroopsTask;
import main.java.taskstructure.ScanLocationTask;
import main.java.taskstructure.SendTroopsTask;
import main.java.taskstructure.TaskQueue;

import com.google.gdata.util.ServiceException;

public class FarmDriver extends Thread{
	MapDriver map_driver;
	WebDriver driver;
	Map<String, String> farm_composition = new HashMap<String, String>();
	ArrayList<Map<String, String>> troop_rules = new ArrayList<Map<String,String>>();
	public FarmDriver(MapDriver mp, WebDriver dr){
		this.map_driver = mp;
		this.driver = dr;
	}
	
	private void buildRules(){
		Map<String, String> rule1 = new HashMap<String, String>();
		rule1.put("legionnaires", "3");
		Map<String, String> rule2 = new HashMap<String, String>();
		rule2.put("equites imperatoris", "2");
		troop_rules.add(rule1);
		troop_rules.add(rule2);
	}
	
	public void run(){
		buildRules();
		while(true){
			try {
				boolean clear = false;
				String coords = "";
				while(!clear){
					coords = map_driver.findNearestEmpty();
					ScanLocationTask checkLoc = new ScanLocationTask(driver, Integer.parseInt(coords.split(",")[0]), Integer.parseInt(coords.split(",")[1]), this);
					synchronized(this){
						TaskQueue.push(checkLoc);
						this.wait();
						clear = checkLoc.clear;
					}
				}
				synchronized(this){
					TaskQueue.push(new CheckNumberofTroopsTask(driver, this));
					this.wait();
				}
				Map<String, String> troop_map = SheetDriver.getTroopCount("Village 1");
				Map<String, String> composition = this.canFarm(troop_map);
				if(composition != null){
					TaskQueue.push(new SendTroopsTask(driver, coords.split(",")[0], coords.split(",")[1], composition));
					SheetDriver.updateLastFarmTime(coords);
				} else {
					Thread.sleep(600 * 1000);
				}
			} catch (IOException | ServiceException | InterruptedException e) {e.printStackTrace();}
		}
	}
	private Map<String, String> canFarm(Map<String, String> troop_map){
		boolean cansend = true;
		for(Map<String,String> composition : troop_rules){
			for(Entry<String, String> entry : composition.entrySet()){
				String troop = entry.getKey().replace(" ", "");
				int need = Integer.parseInt(entry.getValue());
				int have = Integer.parseInt(troop_map.get(troop));
				System.out.println(troop + " have: " + have + " need: " + need);
				if(have < need){
					cansend = false;
					break;
				}
			}
			if(cansend) return composition;
			cansend = true;
		}
		
		return null;
	}
}
