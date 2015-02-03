package taskstructure;

public class TaskQueue {
	private static TaskNode head_node = null;
	private static TaskNode last_node = null;
	
	private static boolean is_being_read = false;
	private static boolean is_being_written = false;
	
	public static int length = 0;
	
	public static void push(TaskNode node){
		while(writeBlock()){}
		is_being_written = true;
		if(head_node == null){
			head_node = node;
			last_node = node;
		}else{
			TaskNode working = head_node;
			boolean inserted = false;
			while(working.hasChild() && !inserted){
				if(working.getPriority() < node.getPriority()){
					node.setChild(working);
					node.setParent(working.getParent());
					if(working != head_node) working.getParent().setChild(node);
					working.setParent(node);
					inserted = true;
					if(working == head_node) head_node = node;
				}
				working = working.getChild();
			}
			if(!inserted){
				last_node.setChild(node);
				node.setParent(last_node);
				last_node = node;	
			}
		}
		length++;
		is_being_written = false;
		System.out.println("Adding to Queue: " + length);
	}
	public static TaskNode pop(){
		while(writeBlock()){}
		is_being_written = true;
		TaskNode ret = head_node;
		if(head_node.hasChild()) head_node.getChild().setParent(null);
		if(head_node.hasChild()) {head_node = head_node.getChild();} else {head_node = null;}
		length--;
		is_being_written = false;
		System.out.println("Taking from Queue: " + length);
		return ret;
	}
	private static boolean readBlock(){
		return is_being_written;
	}
	private static boolean writeBlock(){
		return is_being_read || is_being_written;
	}
	public static boolean isEmpty(){
		return head_node == null;
	}
}
