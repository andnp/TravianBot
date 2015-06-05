package main.java.taskstructure;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.googlesheetcontroller.SheetDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gdata.util.ServiceException;

import main.java.botdriver.ControlCenter;

public class SendTroopsTask extends TaskNode{
	WebDriver driver;
	String x, y;
	Map<String, String> troop_map;
	public SendTroopsTask(WebDriver dv, String x, String y, Map<String, String> trm){
		super();
		this.driver = dv;
		this.x = x;
		this.y = y;
		this.troop_map = trm;
	}
	
	public void execute(){
		ControlCenter.openVillageCenter();
		try {
			System.out.println("Sending troops to: " + x + "," + y);
			String loc = SheetDriver.getBuildingById("Rally", "Village1");
			driver.navigate().to("ts2.travian.com/build.php?tt=2&id=" + loc);
			while(driver.findElements(By.id("troops")).isEmpty()){}
			WebElement troops = driver.findElement(By.id("troops"));
			List<WebElement> lines = troops.findElements(By.tagName("tr"));
			for(Entry<String, String> entry : troop_map.entrySet()){
				switch(entry.getKey()){
				case "legionnaires":
					lines.get(0).findElements(By.tagName("td")).get(0).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "equites legati":
					lines.get(0).findElements(By.tagName("td")).get(1).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "battering rams":
					lines.get(0).findElements(By.tagName("td")).get(2).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "senators":
					lines.get(0).findElements(By.tagName("td")).get(3).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "praetorians":
					lines.get(1).findElements(By.tagName("td")).get(0).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "equites imperatoris":
					lines.get(1).findElements(By.tagName("td")).get(1).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "fire catapults":
					lines.get(1).findElements(By.tagName("td")).get(2).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "settlers":
					lines.get(1).findElements(By.tagName("td")).get(3).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "imperians":
					lines.get(2).findElements(By.tagName("td")).get(0).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "equites caesaris":
					lines.get(2).findElements(By.tagName("td")).get(1).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				case "hero":
					lines.get(2).findElements(By.tagName("td")).get(3).findElement(By.tagName("input")).sendKeys(entry.getValue());
					break;
				}
			}
			// Enter coordinates
			driver.findElement(By.id("xCoordInput")).clear();
			((JavascriptExecutor)driver).executeScript("document.getElementById('xCoordInput').setAttribute('value', '"+x+"')");
			driver.findElement(By.id("yCoordInput")).clear();
			((JavascriptExecutor)driver).executeScript("document.getElementById('yCoordInput').setAttribute('value', '"+y+"')");
			// Accept attack
			driver.findElement(By.id("btn_ok")).click();
			// Confirm send
			driver.findElement(By.id("btn_ok")).click();
		} catch (IOException | ServiceException e) {e.printStackTrace();}
	}
	
	public double getPriority(){
		return 10.0 + 2*this.age();
	}
}
