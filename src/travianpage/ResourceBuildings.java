package travianpage;

import java.net.URL;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ResourceBuildings extends MainPages{
	private static Map<String,String> resource_mappings = new HashMap<String, String>();
	private static boolean res_map_init = false;
	
	public static boolean isUpgrading(){
		if(driver.findElements(By.className("boxes-contents")).isEmpty()) return true;
		List<WebElement> elements = driver.findElement(By.className("boxes-contents")).findElements(By.tagName("li"));
		if(elements.isEmpty()){
			return false;
		} else {
			for(int i = 0; i < elements.size(); i++){
				String building_name = elements.get(i).findElement(By.className("name")).getText();
				if(building_name.contains("Crop") || building_name.contains("Wood") || building_name.contains("Clay") || building_name.contains("Iron")){
					return true;
				}
			}
		}
		return false;
	}
	
	public static void upgradeBuilding(URL building) throws InterruptedException{
		driver.navigate().to(building);
		driver.findElement(By.className("contractLink")).findElement(By.tagName("button")).click();
	}
	
	public static void upgradeLowestBuilding(String building) throws InterruptedException{
		if(!res_map_init){initResMap();}
		String res = " ";
		if(!building.equals("all")){res = resource_mappings.get(building);}
		
		int lowest_level = 21;
		WebElement lowest_level_building = null;
		
		List <WebElement> building_elements = driver.findElement(By.id("rx")).findElements(By.tagName("area"));
		for(WebElement el : building_elements){
			if(el.getAttribute("alt").contains(res)){
				int level = getLevel(el);
				if(level < lowest_level){
					lowest_level = level;
					lowest_level_building = el;
				}
			}
		}
		if(lowest_level_building != null){
			lowest_level_building.click();
			boolean retry = true;
			while(retry){
				List<WebElement> button = driver.findElements(By.className("contractLink"));
				if(!button.isEmpty()){
					List<WebElement> enough_res = button.get(0).findElements(By.className("none"));
					if(enough_res.isEmpty()){
						button.get(0).findElement(By.tagName("button")).click();
					}
					retry = false;
				}
			}
		}
	}
	
	public static int getRemainingTime(){
		List<WebElement> timers = driver.findElement(By.className("boxes-contents")).findElements(By.id("timer1"));
		if(timers.isEmpty()) return -1;
		String time = timers.get(0).getText();
		String[] sub = time.split(":");
		int ret = Integer.parseInt(sub[0]) * 3600 + Integer.parseInt(sub[1]) * 60 + Integer.parseInt(sub[2]);
		
		return ret;
	}
	
	private static int getLevel(WebElement el){
		String s = el.getAttribute("alt");
		return Integer.parseInt(s.replaceAll("[\\D]", ""));
	}
	
	private static void initResMap(){
		resource_mappings.put("food", "Cropland");
		resource_mappings.put("iron", "Iron Mine");
		resource_mappings.put("clay", "Clay Pit");
		resource_mappings.put("wood", "Woodcutter");
	}
	
	public static Map<String, Integer> getUpgradeCost(URL building){
		driver.navigate().to(building);
		while(driver.findElements(By.className("r1")).isEmpty()){}
		Integer wood = Integer.parseInt(driver.findElement(By.className("r1")).getText());
		Integer clay = Integer.parseInt(driver.findElement(By.className("r2")).getText());
		Integer iron = Integer.parseInt(driver.findElement(By.className("r3")).getText());
		Integer food = Integer.parseInt(driver.findElement(By.className("r4")).getText());
		
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
