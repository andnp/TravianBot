package taskstructure;

import org.openqa.selenium.WebDriver;

import travianpage.ResourceBuildings;
import travianpage.VillageCenter;
import botdriver.ControlCenter;
import botdriver.UpgradeDriver;

public class CheckTimeUntilUpgradeTask extends TaskNode {
	WebDriver driver;
	String village;
	UpgradeDriver up_drive;
	@Override
	public void execute() {
		System.out.println("Checking time until upgrade");
		ControlCenter.openResources();
		int rb = ResourceBuildings.getRemainingTime();
		int vc = VillageCenter.getRemainingTime();
		int sleep = Math.min(rb, vc);
		if(!VillageCenter.isUpgrading() || !ResourceBuildings.isUpgrading()) sleep = 0;
		up_drive.sleep = sleep;
		up_drive.upgrade_res = !ResourceBuildings.isUpgrading();
		up_drive.upgrade_vc = !VillageCenter.isUpgrading();
		synchronized(up_drive){
			up_drive.notify();
		}
	}
	
	public CheckTimeUntilUpgradeTask(WebDriver driver, UpgradeDriver up_drive, String village){
		this.driver = driver;
		this.village = village;
		this.up_drive = up_drive;
	}
	public double getPriority(){
		return 5.0;
	}

}
