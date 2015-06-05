package taskstructure;

import googlesheetcontroller.*;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gdata.util.ServiceException;

import botdriver.ControlCenter;
import botdriver.FarmDriver;

public class CheckNumberofTroopsTask extends TaskNode{
	WebDriver driver;
	FarmDriver farm_driver;
	
	public void execute(){
		ControlCenter.openResources();
		System.out.println("Checking number of troops");
		while(driver.findElements(By.id("troops")).isEmpty()){}
		Map<String, String> troop_map = new HashMap<String, String>();
		troop_map.put("Legionnaires", "0"); troop_map.put("Hero", "0"); troop_map.put("Praetorians", "0"); troop_map.put("Imperians", "0");
		troop_map.put("Equites Legati", "0"); troop_map.put("Equites Imperatoris", "0"); troop_map.put("Equites Caesaris", "0");
		troop_map.put("Battering Rams", "0"); troop_map.put("Fire Catapults", "0"); troop_map.put("Senators", "0"); troop_map.put("Settlers", "0");
		List<WebElement> troop_list = driver.findElement(By.id("troops")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		if(!troop_list.get(0).getText().equals("none")){
			for(WebElement troop : troop_list){
				String num = troop.findElement(By.className("num")).getText();
				String type = troop.findElement(By.className("un")).getText();
				
				troop_map.replace(type, num);
			}
			
			try {
				SheetDriver.updateTroopCount("Village 1", troop_map);
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
		} else {
			try {
				SheetDriver.updateTroopCount("Village 1", troop_map);
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
		}
		synchronized(farm_driver){
			farm_driver.notify();
		}
	}
	public double getPriority(){
		return 7.0 + 2*this.age();
	}
	public CheckNumberofTroopsTask(WebDriver dv, FarmDriver fmdv){
		super();
		this.driver = dv;
		this.farm_driver = fmdv;
	}
}
