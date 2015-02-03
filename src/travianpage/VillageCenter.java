package travianpage;

import java.net.URL;
import java.util.*;

import org.openqa.selenium.*;

public class VillageCenter extends MainPages{
	public static void upgradeBuilding(String building){
		//Finds the building, then opens the upgrade page
		List <WebElement> building_elements = driver.findElement(By.id("clickareas")).findElements(By.tagName("area"));
		for(WebElement el : building_elements){
			if(el.getAttribute("alt").contains(building)){
				el.click();
				driver.findElement(By.className("contractLink")).findElement(By.tagName("button")).click();
				break;
			}
		}
	}
	public static boolean isUpgrading(){
		if(driver.findElements(By.className("boxes-contents")).isEmpty()) return true;
		List<WebElement> elements = driver.findElement(By.className("boxes-contents")).findElements(By.tagName("li"));
		if(elements.isEmpty()){
			return false;
		} else {
			for(int i = 0; i < elements.size(); i++){
				String building_name = elements.get(i).findElement(By.className("name")).getText();
				if(building_name.contains("Crop") || building_name.contains("Wood") || building_name.contains("Clay") || building_name.contains("Iron")){
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getRemainingTime(){
		int ret = -1;
		while(driver.findElements(By.className("boxes-contents")).isEmpty()){}
		List<WebElement> buildings = driver.findElement(By.className("boxes-contents")).findElements(By.tagName("li"));
		if(buildings.size() > 1){
			List<WebElement> timers = driver.findElement(By.className("boxes-contents")).findElements(By.id("timer2"));
			if(timers.isEmpty()) return -1;
			String time = timers.get(0).getText();
			String[] sub = time.split(":");
			ret = Integer.parseInt(sub[0]) * 3600 + Integer.parseInt(sub[1]) * 60 + Integer.parseInt(sub[2]);
		} else {
			List<WebElement> timers = driver.findElement(By.className("boxes-contents")).findElements(By.id("timer1"));
			if(timers.isEmpty()) return -1;
			String time = timers.get(0).getText();
			String[] sub = time.split(":");
			ret = Integer.parseInt(sub[0]) * 3600 + Integer.parseInt(sub[1]) * 60 + Integer.parseInt(sub[2]);
		}
		
		return ret;
	}
	
	public static Map<String, Integer> getUpgradeCost(URL building){
		driver.navigate().to(building);
		while(driver.findElements(By.id("contract")).isEmpty()){}
		Integer wood;
		Integer clay;
		Integer iron;
		Integer food;
		if(driver.findElement(By.id("contract")).findElements(By.className("resources")).isEmpty()){
			wood = 0;
			clay = 0;
			iron = 0;
			food = 0;
		} else {
			wood = Integer.parseInt(driver.findElement(By.className("r1")).getText());
			clay = Integer.parseInt(driver.findElement(By.className("r2")).getText());
			iron = Integer.parseInt(driver.findElement(By.className("r3")).getText());
			food = Integer.parseInt(driver.findElement(By.className("r4")).getText());
		}
		
		Map<String, Integer> ret = new HashMap<String, Integer>();
		ret.put("wood", wood);
		ret.put("clay", clay);
		ret.put("iron", iron);
		ret.put("food", food);
		return ret;
	}
	
	public static Integer getLevel(URL building){
		driver.navigate().to(building);
		return Integer.parseInt(driver.findElement(By.id("content")).findElement(By.className("level")).getText().replaceAll("\\D+",""));
	}
}
