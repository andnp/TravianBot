package main.java.taskstructure;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gdata.util.ServiceException;

import main.java.botdriver.ControlCenter;
import main.java.botdriver.UpgradeDriver;

public class UpdateExistingBuildingsTask extends TaskNode {
	WebDriver driver;
	String village;
	UpgradeDriver up_drive;
	@Override
	public void execute() {
		synchronized(up_drive){
			System.out.println("Updating existing buildings in village " + village);
			ControlCenter.openResources();
			List<WebElement> buildings = driver.findElement(By.id("rx")).findElements(By.tagName("area"));
			Map<Integer, String> loc_map = new HashMap<Integer, String>();
			for(WebElement building : buildings){
				String name = building.getAttribute("alt").split(" ")[0];
				if(building.getAttribute("href").contains("id")){
					Integer loc = Integer.parseInt(building.getAttribute("href").split("=")[1]);
					
					loc_map.put(loc, name);
				}
			}
			ControlCenter.openVillageCenter();
			while(driver.findElements(By.id("clickareas")).isEmpty()){}
			buildings = driver.findElement(By.id("clickareas")).findElements(By.tagName("area"));
			for(WebElement building : buildings){
				String name = building.getAttribute("alt").split(" ")[0];
				if(!name.equals("Building")){
					if(building.getAttribute("href").contains("id")){
						Integer loc = Integer.parseInt(building.getAttribute("href").split("=")[1]);
						
						loc_map.put(loc, name);
					}
				}
			}
			try {
				SheetDriver.setupBuildingCost(loc_map, village);
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
			up_drive.notifyAll();
		}
	}
	public UpdateExistingBuildingsTask(WebDriver driver, UpgradeDriver up_drive, String village){
		super();
		this.driver = driver;
		this.village = village;
		this.up_drive = up_drive;
	}
	public double getPriority(){
		return 5.0 + .5*this.age();
	}
}
