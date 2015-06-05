package main.java.taskstructure;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.google.gdata.util.ServiceException;

import main.java.botdriver.UpgradeDriver;
import main.java.travianpage.ResourceBuildings;

public class UpgradeTask extends TaskNode{
	URL url;
	UpgradeDriver dv;
	public void execute(){
		if(url.toString().split("=").length == 2){
			synchronized(dv){
				System.out.println("Upgrading " + url.toString().split("=")[1]);
				// Upgrade building
				try {ResourceBuildings.upgradeBuilding(url);} catch (InterruptedException e) {e.printStackTrace();}
				// get new cost and update database
				Map<String, Integer> cost_map = ResourceBuildings.getUpgradeCost(url);
				Integer level = ResourceBuildings.getLevel(url);
				try {SheetDriver.updateBuildingCost(cost_map, level, "Village1", url.toString().split("=")[1]);} catch (IOException | ServiceException e) {e.printStackTrace();}
				dv.notify();
			}
		}
	}
	public UpgradeTask(URL url, UpgradeDriver dv){
		super();
		this.url = url;
		this.dv = dv;
	}
	public double getPriority(){
		return 5.0 + 2*this.age();
	}
}
