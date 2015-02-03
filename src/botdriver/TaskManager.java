package botdriver;

import java.util.Random;

import taskstructure.TaskNode;
import taskstructure.TaskQueue;

public class TaskManager extends Thread {
	Random sleep_time = new Random();
	public void run(){
		int i = 0;
		while(true){
			if(!TaskQueue.isEmpty()){
				TaskNode node = TaskQueue.pop();
				node.execute();
				i++;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(i > 50){
				try {Thread.sleep(sleep_time.nextInt(60)*1000);} catch (InterruptedException e) {e.printStackTrace();}
				i=0;
			}
		}
	}
}
