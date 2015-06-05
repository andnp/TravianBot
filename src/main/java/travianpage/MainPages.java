package main.java.travianpage;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import main.java.botdriver.ControlCenter;

public abstract class MainPages {
	protected static WebDriver driver;
	public static void initialize(WebDriver dr){
		driver = dr;
	}
	public static Map<String, Integer> getVillageResources(){
		ControlCenter.openResources();
		while(driver.findElements(By.id("stockBarResource1")).isEmpty()){}
		Integer wood = Integer.parseInt(driver.findElement(By.id("stockBarResource1")).findElement(By.id("l1")).getText().replaceAll("[\\D]", ""));
		Integer clay = Integer.parseInt(driver.findElement(By.id("stockBarResource2")).findElement(By.id("l2")).getText().replaceAll("[\\D]", ""));
		Integer iron = Integer.parseInt(driver.findElement(By.id("stockBarResource3")).findElement(By.id("l3")).getText().replaceAll("[\\D]", ""));
		Integer food = Integer.parseInt(driver.findElement(By.id("stockBarResource4")).findElement(By.id("l4")).getText().replaceAll("[\\D]", ""));
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("wood", wood);
		map.put("clay", clay);
		map.put("iron", iron);
		map.put("food", food);
		return map;
	}
}
