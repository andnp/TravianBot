package taskstructure;

import googlesheetcontroller.SheetDriver;

import java.io.IOException;
import java.util.Map;

import com.google.gdata.util.ServiceException;

import travianpage.ResourceBuildings;
import botdriver.ControlCenter;
import botdriver.UpgradeDriver;

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
		this.up_drive = up_drive;
	}
	public double getPriority(){
		return 5.0;
	}
}
