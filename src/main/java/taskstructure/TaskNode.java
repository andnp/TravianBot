package main.java.taskstructure;

public abstract class TaskNode {
	private TaskNode parent = null;
	private TaskNode child = null;
	private long time_created;
	public double priority;
	
	public TaskNode(){
		time_created = System.currentTimeMillis();
	}
	public long age(){ // return age of task in seconds
		return (System.currentTimeMillis() - time_created) / 1000;
	}
	
	public TaskNode getParent(){
		return parent;
	}
	public TaskNode getChild(){
		return child;
	}
	public void setParent(TaskNode n){
		parent = n;
	}
	public void setChild(TaskNode n){
		child = n;
	}
	public boolean hasChild(){
		return child != null;
	}
	public boolean hasParent(){
		return parent != null;
	}
	
	public abstract void execute();
	public abstract double getPriority();
}
