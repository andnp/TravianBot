package taskstructure;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import botdriver.HeroDriver;

public class CheckAdventuresTask extends TaskNode{
	WebDriver driver;
	HeroDriver hero_driver;
	
	public CheckAdventuresTask(WebDriver dv, HeroDriver hrdv){
		this.driver = dv;
		this.hero_driver = hrdv;
	}
	public void execute(){
		driver.navigate().to("http://ts2.travian.com/hero_adventure.php");
		List<WebElement> adventures = driver.findElement(By.id("adventureListForm")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		if(adventures.get(0).findElements(By.className("noData")).isEmpty()){
			adventures.get(0).findElement(By.className("gotoAdventure")).click();
			driver.findElement(By.id("start")).click();
		}
	}
	public double getPriority(){
		return 2.0;
	}
}
