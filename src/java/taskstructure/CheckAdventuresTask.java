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
		super();
		this.driver = dv;
		this.hero_driver = hrdv;
	}
	public void execute(){
		System.out.println("Checking for Adventures");
		driver.navigate().to("http://ts2.travian.com/hero_adventure.php");
		List<WebElement> adventures = driver.findElement(By.id("adventureListForm")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		if(adventures.get(0).findElements(By.className("noData")).isEmpty()){
			System.out.println("Sending hero on adventure");
			adventures.get(0).findElement(By.className("gotoAdventure")).click();
			if(!driver.findElements(By.id("start")).isEmpty()){
				driver.findElement(By.id("start")).click();
			}
		}
	}
	public double getPriority(){
		return 2.0 + .1*this.age();
	}
}
