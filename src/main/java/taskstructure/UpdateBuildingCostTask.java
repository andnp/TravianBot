package main.java.taskstructure;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.google.gdata.util.ServiceException;

import main.java.travianpage.ResourceBuildings;
import main.java.travianpage.VillageCenter;

public class UpdateBuildingCostTask extends TaskNode{
	URL building_name;
	Map<String, Integer> cost_map;
	Integer level;
	public void execute(){
		System.out.println("Updating building cost");
		if(Integer.parseInt(building_name.toString().split("=")[1]) < 19 ){
			cost_map = ResourceBuildings.getUpgradeCost(building_name);
			level = ResourceBuildings.getLevel(building_name);
		} else {
			cost_map = VillageCenter.getUpgradeCost(building_name);
			level = VillageCenter.getLevel(building_name);
		}
		try {
			SheetDriver.updateBuildingCost(cost_map, level, "Village1", building_name.toString().split("=")[1]);
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
	}
	public UpdateBuildingCostTask(URL building){
		super();
		this.building_name = building;
	}
	public double getPriority(){
		return 5.0 + .5*this.age();
	}
}
