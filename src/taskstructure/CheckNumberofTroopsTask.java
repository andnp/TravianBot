package taskstructure;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import botdriver.ControlCenter;

public class CheckNumberofTroopsTask extends TaskNode{
	WebDriver driver;
	
	public void execute(){
		ControlCenter.openResources();
		while(driver.findElements(By.id("troops")).isEmpty()){}
		Map<String, String> troop_map = new HashMap<String, String>();
		List<WebElement> troop_list = driver.findElement(By.id("troops")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		
		for(WebElement troop : troop_list){
			String num = troop.findElement(By.className("num")).getText();
			String type = troop.findElement(By.className("un")).getText();
			
			troop_map.put(type, num);
		}
		
		//TODO: Make function in SheetDriver that takes a map, then adds to a google sheet.
	}
	public double getPriority(){
		return 7.0;
	}
	public CheckNumberofTroopsTask(WebDriver dv){
		this.driver = dv;
	}
}
