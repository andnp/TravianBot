package taskstructure;

import googlesheetcontroller.SheetDriver;

import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.*;

import com.google.gdata.util.ServiceException;

public class ScanLocationTask extends TaskNode{
	WebDriver driver;
	int x, y;
	public ScanLocationTask(WebDriver driver, int x, int y){
		this.driver = driver;
		this.x = x;
		this.y = y;
	}
	public double getPriority(){
		return 1.0;
	}
	public void execute(){
		try {
			System.out.println("Scanning Location: "+x+","+y);
			URL url = new URL("http://ts2.travian.com/position_details.php?x="+x+"&y="+y);
			driver.navigate().to(url);
			List<WebElement> details_list = driver.findElements(By.id("map_details"));
			if(!details_list.isEmpty()){
				WebElement details = details_list.get(0);
				String name;
				String type;
				String pop;
				String alliance;
				String owner;
				String empty;
				if(details.findElements(By.tagName("h4")).size() == 1){
					// abandoned valley
				} else if(details.findElements(By.tagName("h4")).get(1).getText().equals("Player")){
					// This is a player
					// need x,y name type pop alliance owner
					name = driver.findElement(By.className("titleInHeader")).getText();
					type = "City";
					pop = details.findElement(By.id("village_info")).findElements(By.tagName("tr")).get(3).findElement(By.tagName("td")).getText();
					alliance = details.findElement(By.id("village_info")).findElements(By.tagName("tr")).get(1).findElement(By.tagName("td")).getText();
					owner = details.findElement(By.id("village_info")).findElements(By.tagName("tr")).get(2).findElement(By.tagName("td")).getText();
					empty = "no";
					SheetDriver.addMapElement(x + "," + y, name, type, pop, alliance, owner, empty);
				} else if(details.findElements(By.tagName("h4")).get(0).getText().equals("Bonus")){
					// This is an Unoccupied oases
					name = "Oases";
					type = "Oases";
					pop = "0";
					alliance = "";
					owner = "";
					empty = "no";
					List<WebElement> troops = driver.findElement(By.id("troop_info")).findElements(By.tagName("tr"));
					if(troops.get(0).getText().equals("none")){ empty = "yes";}
					SheetDriver.addMapElement(x + "," + y, name, type, pop, alliance, owner, empty);
				}
			}
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
	}
}
