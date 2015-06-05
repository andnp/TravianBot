package main.java.taskstructure;

import main.java.googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.util.Map;

import com.google.gdata.util.ServiceException;

import main.java.travianpage.ResourceBuildings;
import main.java.botdriver.ControlCenter;
import main.java.botdriver.UpgradeDriver;

public class CheckResourcesTask extends TaskNode {
	UpgradeDriver up_drive;
	@Override
	public void execute(){
		System.out.println("Checking Resources");
		synchronized(up_drive){
			ControlCenter.openResources();
			Map<String, Integer> res_map = ResourceBuildings.getVillageResources();
			
			try {
				SheetDriver.updateResources(res_map, "Village 1");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			up_drive.notify();
		}

	}

	public CheckResourcesTask(UpgradeDriver up_drive){
		super();
		this.up_drive = up_drive;
	}
	public double getPriority(){
		return 5.0 + 2*this.age();
	}
}
