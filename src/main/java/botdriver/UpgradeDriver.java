package main.java.botdriver;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.google.gdata.util.ServiceException;

import main.java.taskstructure.CheckResourcesTask;
import main.java.taskstructure.CheckTimeUntilUpgradeTask;
import main.java.taskstructure.TaskQueue;
import main.java.taskstructure.UpdateBuildingCostTask;
import main.java.taskstructure.UpdateExistingBuildingsTask;
import main.java.taskstructure.UpgradeTask;

public class UpgradeDriver extends Thread{
	WebDriver driver;
	Map<String, Double> res_val = new HashMap<String, Double>();

	public int sleep = 0;
	public boolean upgrade_res = false;
	public boolean upgrade_vc = false;
	
	public UpgradeDriver(WebDriver driver){
		// On creation, build the weighted resource value map
		res_val.put("wood", 1.0);
		res_val.put("food", .5);
		res_val.put("iron", 1.0);
		res_val.put("clay", 1.5);
		this.driver = driver;
	}
	
	public void run(){
		synchronized(this){
			TaskQueue.push(new UpdateExistingBuildingsTask(driver, this, "Village1"));// Check the google spreadsheet, and update it accordingly
			try {this.wait();} catch (InterruptedException e1) {e1.printStackTrace();}
		}
		this.updateBuildingCost();// Update the cost to upgrade for every building
		while(true){
			sleep = 0; // reset variables for new loop iteration
			upgrade_res = false;
			upgrade_vc = false;
			synchronized(this){
				TaskQueue.push(new CheckTimeUntilUpgradeTask(driver, this, "Village1"));
				try {this.wait();} catch (InterruptedException e2) {e2.printStackTrace();}
			}
			System.out.println("Upgrade driver sleeping for " + sleep + " seconds");
			try {Thread.sleep(sleep * 1000);} catch (InterruptedException e) {}
			synchronized(this){
				TaskQueue.push(new CheckResourcesTask(this));
				try {this.wait();} catch (InterruptedException e1) {e1.printStackTrace();}
			}
			try {
				String loc = this.getCheapestUpgrade();
				if(!loc.isEmpty()){
					synchronized(this){
						TaskQueue.push(new UpgradeTask(new URL("http://ts2.travian.com/build.php?id=" + loc), this));
						this.wait();
					}
				} else {Thread.sleep(300 * 1000);}
			} catch (IOException | ServiceException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateBuildingCost(){
		List<String> building_locations = null;
		try {
			building_locations = SheetDriver.getBuildings("Village1", true, true, false);
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
		for(String loc : building_locations){
			try {
				TaskQueue.push(new UpdateBuildingCostTask(new URL("http://ts2.travian.com/build.php?id=" + loc)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Double getTotalCost(String loc) throws IOException, ServiceException{
		Map<String, Integer> cost_map = SheetDriver.getBuildingCost("Village1", loc);
		Double cost = cost_map.get("wood") * res_val.get("wood") + cost_map.get("clay") * res_val.get("clay") + cost_map.get("iron") * res_val.get("iron") + cost_map.get("food") * res_val.get("food");
		return cost;
	}
	
	private boolean canAffordUpgrade(String loc) throws IOException, ServiceException{
		Map<String, Integer> cost_map = SheetDriver.getBuildingCost("Village1", loc);
		Map<String, Integer> res_map = SheetDriver.getVillageResources("Village 1");
		
		return (res_map.get("wood") > cost_map.get("wood")) && (res_map.get("food") > cost_map.get("food")) && (res_map.get("clay") > cost_map.get("clay")) && (res_map.get("iron") > cost_map.get("iron"));
	}
	
	private String getCheapestUpgrade() throws IOException, ServiceException{
		List<String> building_locations = null;
		Double cur_cost = 0.0;
		Double low_cost = Double.POSITIVE_INFINITY;
		String low_location = "";
		building_locations = SheetDriver.getBuildings("Village1", upgrade_res, upgrade_vc, true);
		for(String loc : building_locations){
			if(this.canAffordUpgrade(loc)){
				cur_cost = getTotalCost(loc);
				if(cur_cost < low_cost){
					low_cost = cur_cost;
					low_location = loc;
				}
			}
		}
		return low_location;
	}
}
