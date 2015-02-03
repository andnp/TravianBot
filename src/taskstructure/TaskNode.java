package taskstructure;

public abstract class TaskNode {
	private TaskNode parent = null;
	private TaskNode child = null;
	public double priority;
	
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
	
	public abstract void execute();
	public abstract double getPriority();
}
