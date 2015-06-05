package botdriver;

import java.io.IOException;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import travianpage.*;
import googlesheetcontroller.*;

import com.google.gdata.util.ServiceException;

public class ControlCenter {
	static WebDriver driver = new FirefoxDriver();
	static UpgradeDriver upgrade_driver = new UpgradeDriver(driver);
	static TaskManager task_manager = new TaskManager();
	static MapDriver map_driver = new MapDriver(driver);
	static FarmDriver farm_driver = new FarmDriver(map_driver, driver);
	static HeroDriver hero_driver = new HeroDriver(driver);
	
	public static void main(String[] args) throws InterruptedException, IOException, ServiceException{
		Login.login(driver, "andnp", "972965");
		MainPages.initialize(driver);
		SheetDriver.loadSpreadsheet();
		upgrade_driver.start();
		task_manager.start();
		map_driver.start();
		farm_driver.start();
		hero_driver.start();
	}
	
	public static void openHero(){
		openResources();
		driver.findElement(By.id("heroImageButton")).click();
	}
	public static void openVillageCenter(){
		WebElement nav_bar = driver.findElement(By.className("villageBuildings")).findElement(By.tagName("a"));
		nav_bar.click();
	}
	public static void openResources(){
		while(driver.findElements(By.className("villageResources")).isEmpty()){}
		WebElement nav_bar = driver.findElement(By.className("villageResources")).findElement(By.tagName("a"));
		nav_bar.click();
	}
	public static void openMap(){
		WebElement nav_bar = driver.findElement(By.className("map")).findElement(By.tagName("a"));
		nav_bar.click();
	}
	public static void openReports(){
		WebElement nav_bar = driver.findElement(By.className("reports")).findElement(By.tagName("a"));
		nav_bar.click();
	}
	public static void openMessages(){
		WebElement nav_bar = driver.findElement(By.className("messages")).findElement(By.tagName("a"));
		nav_bar.click();
	}
}
