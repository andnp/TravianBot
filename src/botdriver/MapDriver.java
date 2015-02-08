package botdriver;

import java.io.IOException;
import java.util.List;

import googlesheetcontroller.SheetDriver;

import org.openqa.selenium.WebDriver;

import com.google.gdata.util.ServiceException;

import taskstructure.ScanLocationTask;
import taskstructure.TaskQueue;

public class MapDriver extends Thread{
	String my_coords = "122,-58";
	WebDriver driver;
	public void run(){
		int radius = 1;
		int x, y;
		/*TODO
		 * create map task
		 * scan map with increasing radius
		 */
		x = Integer.parseInt(my_coords.split(",")[0]);
		y = Integer.parseInt(my_coords.split(",")[1]);
		while(true){
			for(int i = x - radius; i < x + radius; i++){
				TaskQueue.push(new ScanLocationTask(driver, i, y + radius));
				TaskQueue.push(new ScanLocationTask(driver, i, y - radius));
				try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
			}
			for(int j = y - radius; j < y + radius; j++){
				TaskQueue.push(new ScanLocationTask(driver, x - radius, j));
				TaskQueue.push(new ScanLocationTask(driver, x + radius, j));
				try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
			}
			try {Thread.sleep(600000);} catch (InterruptedException e) {e.printStackTrace();}
			radius++;
			if(radius > 50) radius = 1;
		}
	}
	public String findNearestEmpty() throws IOException, ServiceException{
		List<String> coords = SheetDriver.getEmptyCoordsList(true);
		double shortest = Double.POSITIVE_INFINITY;
		String nearest_coords = "";
		for(String coord : coords){
			if(getDistance(Integer.parseInt(coord.split(",")[0]), Integer.parseInt(my_coords.split(",")[0]), Integer.parseInt(coord.split(",")[1]), Integer.parseInt(my_coords.split(",")[1])) < shortest ){
				shortest = getDistance(Integer.parseInt(coord.split(",")[0]), Integer.parseInt(my_coords.split(",")[0]), Integer.parseInt(coord.split(",")[1]), Integer.parseInt(my_coords.split(",")[1]));
				nearest_coords = coord;
			}
		}
		return nearest_coords;
	}
	public MapDriver(WebDriver driver){
		this.driver = driver;
	}
	public double getDistance(int x1, int x2, int y1, int y2){
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 -y2, 2));
	}
}
