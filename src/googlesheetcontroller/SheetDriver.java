package googlesheetcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class SheetDriver {
	
	private static SpreadsheetEntry spreadsheet;
	private static SpreadsheetService service = new SpreadsheetService("travian");
	
	public static String getBuildingById(String building_name, String village) throws IOException, ServiceException{
		String ret = "";
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingLevels")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("buildingname").contains(building_name) && row.getCustomElements().getValue("village").contains(village)){
				ret = row.getCustomElements().getValue("location");
			}
		}
		return ret;
	}
	
	public static Map<String,String> getTroopCount(String village) throws IOException, ServiceException{
		Map<String, String> ret = new HashMap<String, String>();
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Troops")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").contains(village)){
				for(String tag : row.getCustomElements().getTags()){
					if(!tag.equals("village"));
					ret.put(tag, row.getCustomElements().getValue(tag));
				}
			}
		}
		return ret;
	}
	
	public static void updateTroopCount(String village, Map<String, String> troop_map) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Troops")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").contains(village)){
				for(Entry<String, String> entry : troop_map.entrySet()){
					row.getCustomElements().setValueLocal(entry.getKey().replace(" ", ""), entry.getValue());
				}
				row.update();
			}
		}
	}
	
	public static void updateLastFarmTime(String coords) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Map")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("coords").contains(coords)){
				row.getCustomElements().setValueLocal("lastfarmed", System.currentTimeMillis() + "");
				row.update();
			}
		}
	}
	
	public static List<String> getEmptyCoordsList(boolean to_farm) throws IOException, ServiceException{
		long now = System.currentTimeMillis();
		List<String> ret = new ArrayList<String>();
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Map")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			//System.out.println(row.getCustomElements().getTags());
			if(row.getCustomElements().getValue("empty").contains("yes") && (now - Long.parseLong(row.getCustomElements().getValue("lastfarmed"))) > (60 * 60000)){
				ret.add(row.getCustomElements().getValue("coords"));
			}
		}
		return ret;
	}
	
	public static void addMapElement(String coords, String name, String type, String pop, String all, String own, String empty, long time) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Map")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		boolean updated = false;
		for(ListEntry row : listFeed.getEntries()){
			//System.out.println(row.getCustomElements().getTags());
			if(row.getCustomElements().getValue("coords").contains(coords)){
				row.getCustomElements().setValueLocal("coords", coords);
				row.getCustomElements().setValueLocal("name", name);
				row.getCustomElements().setValueLocal("type", type);
				row.getCustomElements().setValueLocal("population", pop);
				row.getCustomElements().setValueLocal("alliance", all);
				row.getCustomElements().setValueLocal("owner", own);
				row.getCustomElements().setValueLocal("empty", empty);
				row.getCustomElements().setValueLocal("lastfarmed", time + "");
				row.update();
				updated = true;
			}
		}
		if(!updated){
			ListEntry row = new ListEntry();
			row.getCustomElements().setValueLocal("coords", coords);
			row.getCustomElements().setValueLocal("name", name);
			row.getCustomElements().setValueLocal("type", type);
			row.getCustomElements().setValueLocal("population", pop);
			row.getCustomElements().setValueLocal("alliance", all);
			row.getCustomElements().setValueLocal("owner", own);
			row.getCustomElements().setValueLocal("empty", empty);
			row = service.insert(listFeedUrl, row);
			row.update();
		}
	}
	
	public static void loadSpreadsheet() throws IOException, ServiceException{
		service.setUserCredentials("soundguyandyp@gmail.com", "andnp972965");
		URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		String spreadsheet_name = "Travian Database";
		int spreadsheet_location = 0;
		for(int i = 0; i < spreadsheets.size(); i++){
			if(spreadsheets.get(i).getTitle().getPlainText().equals(spreadsheet_name)){
				spreadsheet_location = i;
			}
		}
		SpreadsheetEntry sheet = spreadsheets.get(spreadsheet_location);
		spreadsheet = sheet;
	}
	
	public static int getTargetLevel(String building) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingController")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			//System.out.println(row.getCustomElements().getTags());
			if(row.getCustomElements().getValue("buildingname").contains(building)){
				return Integer.parseInt(row.getCustomElements().getValue("level"));
			}
		}
		if(building.equals("Cropland") || building.equals("Iron Mine") || building.equals("Clay Pit") || building.equals("Woodcutter")){
			return getTargetLevel("Resources");
		} else {
			return -1;
		}
	}
	
	public static void updateResources(Map<String, Integer> res_map, String village) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Resources")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").equals(village)){
				row.getCustomElements().setValueLocal("wood", res_map.get("wood").toString());
				row.getCustomElements().setValueLocal("clay", res_map.get("clay").toString());
				row.getCustomElements().setValueLocal("iron", res_map.get("iron").toString());
				row.getCustomElements().setValueLocal("food", res_map.get("food").toString());
				row.update();
			}
		}
	}
	
	public static void updateBuildingCost(Map<String, Integer> res_map, Integer level, String village, String location) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingLevels")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").equals(village) && row.getCustomElements().getValue("location").equals(location)){
				row.getCustomElements().setValueLocal("wood", res_map.get("wood").toString());
				row.getCustomElements().setValueLocal("clay", res_map.get("clay").toString());
				row.getCustomElements().setValueLocal("iron", res_map.get("iron").toString());
				row.getCustomElements().setValueLocal("food", res_map.get("food").toString());
				row.getCustomElements().setValueLocal("level", level.toString());
				row.update();
			}
		}
	}
	
	public static void setupBuildingCost(Map<Integer, String> loc_map, String village) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingLevels")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		boolean update = true;
		for(Entry<Integer, String> entry : loc_map.entrySet()){
			innerloop: for(ListEntry row : listFeed.getEntries()){
				if(row.getCustomElements().getValue("village").equals(village) && row.getCustomElements().getValue("location").equals(entry.getKey().toString())){
					update = false;
					break innerloop;
				}
			}
			if(update){
				ListEntry row = new ListEntry();
				row.getCustomElements().setValueLocal("village", village);
				row.getCustomElements().setValueLocal("location", entry.getKey().toString());
				row.getCustomElements().setValueLocal("buildingname", entry.getValue());
				row = service.insert(listFeedUrl, row);
			} else {
				update = true;
			}
		}
	}
	
	public static List<String> getBuildings(String village, boolean res, boolean vc, boolean for_upgrade) throws IOException, ServiceException{
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingLevels")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		List<String> ret = new ArrayList<String>();
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").equals(village)){
				String loc = row.getCustomElements().getValue("location");
				if(res && Integer.parseInt(loc) < 19){
					if((for_upgrade && Integer.parseInt(row.getCustomElements().getValue("level")) < getTargetLevel(row.getCustomElements().getValue("buildingname"))) || !for_upgrade){
						ret.add(loc);
					} 
				}
				if(vc && Integer.parseInt(loc) >= 19){
					if((for_upgrade && Integer.parseInt(row.getCustomElements().getValue("level")) < getTargetLevel(row.getCustomElements().getValue("buildingname"))) || !for_upgrade){
						ret.add(loc);
					} 
				}
			}
		}
		return ret;
	}
	public static Map<String, Integer> getBuildingCost(String village, String loc) throws IOException, ServiceException{
		Map<String, Integer> cost_map = new HashMap<String, Integer>();
		
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("BuildingLevels")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").equals(village) && row.getCustomElements().getValue("location").equals(loc)){
				cost_map.put("wood", Integer.parseInt(row.getCustomElements().getValue("wood")));
				cost_map.put("clay", Integer.parseInt(row.getCustomElements().getValue("clay")));
				cost_map.put("iron", Integer.parseInt(row.getCustomElements().getValue("iron")));
				cost_map.put("food", Integer.parseInt(row.getCustomElements().getValue("food")));
			}
		}
		return cost_map;
	}
	
	public static Map<String, Integer> getVillageResources(String village) throws IOException, ServiceException{
Map<String, Integer> cost_map = new HashMap<String, Integer>();
		
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry table = null;
		for(WorksheetEntry worksheet : worksheets){
			if(worksheet.getTitle().getPlainText().equals("Resources")){
				table = worksheet;
				break;
			}
		}
		URL listFeedUrl = table.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		for(ListEntry row : listFeed.getEntries()){
			if(row.getCustomElements().getValue("village").equals(village)){
				cost_map.put("wood", Integer.parseInt(row.getCustomElements().getValue("wood")));
				cost_map.put("clay", Integer.parseInt(row.getCustomElements().getValue("clay")));
				cost_map.put("iron", Integer.parseInt(row.getCustomElements().getValue("iron")));
				cost_map.put("food", Integer.parseInt(row.getCustomElements().getValue("food")));
			}
		}
		return cost_map;
	}
}
