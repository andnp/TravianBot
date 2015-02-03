package travianpage;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Login {
	static WebDriver driver;
	
	public static void login(WebDriver dr, String account, String pass){
		driver = dr;
		
		loadLoginPage();
		WebElement account_field = getAccountField();
		WebElement pass_field = getPasswordField();
		
		account_field.sendKeys(account);
		pass_field.sendKeys(pass);
		
		pass_field.submit();
	}
	
	private static void loadLoginPage(){
		driver.get("http://ts2.travian.com");
	}
	
	private static WebElement getAccountField(){
		WebElement output = driver.findElement(By.className("account"));
		output = output.findElements(By.tagName("td")).get(1).findElement(By.tagName("input"));
		return output;
	}
	
	private static WebElement getPasswordField(){
		WebElement output = driver.findElement(By.className("pass"));
		output = output.findElements(By.tagName("td")).get(1).findElement(By.tagName("input"));
		return output;
	}
}
