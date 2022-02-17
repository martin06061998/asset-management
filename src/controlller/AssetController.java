/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dbo.AssetDBO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import model.Asset;
import model.BorrowAsset;
import model.EnityFactory;
import utils.StringUtilities;

/**
 *
 * @author marti
 */
class AssetController implements I_AssetController {

	private static AssetController controller = null;
	private HashMap<String, Asset> assetMap;
	private BorrowManager borrowMng;

	private AssetController() {
	}

	static AssetController getInstance() {
		if (controller == null) {
			controller = new AssetController();
			controller.borrowMng = controller.new BorrowManager();
			controller.assetMap = new HashMap<>();
			controller.assetMap.putAll(seedData());
			controller.save();
			//controller.load();
		}
		return controller;
	}

	private static HashMap<String, Asset> seedData() {
		HashMap<String, Asset> ret = new HashMap();
		Asset newAsset = new Asset("A001", "Samsung projector", "White", 500l, 3.2, 10);
		Asset newAsset2 = new Asset("A002", "Macbook Pro 2016", "White", 500l, 3.2, 30);
		ret.put(newAsset.getAssetID(), newAsset);
		ret.put(newAsset2.getAssetID(), newAsset2);
		return ret;
	}

	@Override
	public JsonNode add(JsonNode dataElement) {
		ObjectNode reply = initializeJsonObj();
		int counter = 0;
		for (JsonNode element : dataElement) {
			try {
				String id = element.get("id").asText();
				if (!assetMap.containsKey(id)) {
					Asset newAsset = EnityFactory.getInstane().createAsset(element);
					assetMap.put(newAsset.getAssetID(), newAsset);
					counter++;
				}
			} catch (Exception ex) {

			}
		}
		reply.put("status", "accepted");
		reply.put("message", "successfuly add " + counter + " assets");
		save();
		return reply;
	}

	JsonNode edit(JsonNode dataElement) {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = this.initializeJsonArray();
		int count = 0;
		for (JsonNode element : dataElement) {
			String id = element.get("id").asText();
			if (contains(id)) {
				try {
					Asset reforgedAsset = EnityFactory.getInstane().createAsset(element);
					assetMap.put(reforgedAsset.getAssetID(), reforgedAsset);
					data.add(reforgedAsset.toJsonNode());
					count++;
				} catch (Exception ex) {

				}
			}

		}
		if (count > 0) {
			save();
		}
		reply.put("status", "accepted");
		reply.put("message", "successfuly edit " + count + " assets");
		reply.set("data", data);
		return reply;
	}

	JsonNode approve(JsonNode dataElement) {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = this.initializeJsonArray();
		int count = 0;
		for (JsonNode element : dataElement) {
			String bID = BorrowAsset.Map_rID_toBID(element.asText());
			boolean isApprovable = borrowMng.isApprovable(bID);
			if (isApprovable) {
				borrowMng.accept(bID);
				data.add(element.asText());
				count++;
			}
		}
		reply.put("status", "accepted");
		reply.put("message", "successfully approve " + count + " requests");
		reply.set("data", data);
		return reply;
	}

	JsonNode getBorrowList() {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = borrowMng.encapsulateAllDoneRequests();
		reply.put("status", "accepted");
		reply.set("data", data);
		reply.put("message", "successfully");
		return reply;
	}

	JsonNode handlingRequest() {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = borrowMng.encapsulateAllHandlingRequests();
		reply.put("status", "accepted");
		reply.set("data", data);
		reply.put("message", "successfully");
		return reply;
	}

	void save() {
		AssetDBO.saveAssets(assetMap);
	}

	void load() {
		AssetDBO.loadAssets(assetMap);
	}

	@Override
	public JsonNode delete(JsonNode target) throws Exception {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isAcceptable(String key, int command) {
		if (command < 2 || command > 3000 || key == null || key.length() < 15) {
			return false;
		}
		String standardKey = key.substring(0, 8).toLowerCase() + key.substring(8, key.length());
		int privilidge = EmpController.getInstance().getPrivilege(standardKey);
		return privilidge - command >= 0;
	}

	@Override
	public JsonNode get(String key) throws Exception {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = initializeJsonArray();
		String status;
		String msg;
		if (key == null) {
			status = "not accepted";
			msg = "not found";
		} else {
			Asset asnwer = assetMap.get(key);
			if (asnwer != null) {
				JsonNode item = asnwer.toJsonNode();
				msg = "found";
				data.add(item);
			} else {
				msg = "not found";
			}
			status = "accepted";
		}
		reply.put("status", status);
		reply.put("message", msg);
		reply.set("data", data);
		return reply;
	}

	private boolean contains(String id) {
		return assetMap.containsKey(id);
	}

	private Asset getAssetByID(String assetID) {
		return assetMap.get(assetID);
	}

	JsonNode searchByName(String name) {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = initializeJsonArray();
		List<Asset> list = findAll((n) -> n.getName().contains(name));
		data = encapsulate(list);
		reply.put("status", "accepted");
		reply.put("message", "successfully");
		reply.set("data", data);
		return reply;
	}

	private List<Asset> findAll(Predicate<Asset> predicate) {
		List<Asset> ret = new ArrayList<>();
		for (Asset item : assetMap.values()) {
			if (predicate.test(item)) {
				ret.add(item);
			}
		}
		return ret;
	}

	private ArrayNode encapsulate(List<Asset> assetList) {
		ArrayNode ret = initializeJsonArray();
		assetList.forEach((item) -> {
			ret.add(item.toJsonNode());
		});
		return ret;
	}

	@Override
	public JsonNode getAll() throws Exception {
		ObjectNode reply = initializeJsonObj();
		ArrayNode data = initializeJsonArray();
		reply.put("status", "accepted");
		reply.put("message", "testing");
		reply.set("data", data);
		return reply;
	}

	private ObjectNode initializeJsonObj() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode ret = mapper.createObjectNode();
		return ret;

	}

	private ArrayNode initializeJsonArray() {
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode ret = mapper.createArrayNode();
		return ret;
	}

	private class BorrowManager {

		private final HashMap<String, ArrayList<HashMap<String, BorrowAsset>>> BIDDirectory;
		private final HashMap<Integer, int[]> BIDAllocateMap;
		private final int BLOCK_SIZE = 10;

		private BorrowManager() {
			BIDDirectory = new HashMap<>();
			BIDAllocateMap = new HashMap<>();
			seedData();

		}

		private boolean isApprovable(String bID) {
			if (!isBIDExistInHandling(bID)) {
				return false;
			}
			final BorrowAsset brr = getHandlingBorrowAsset(bID);
			final int offeredQty = brr.offeredQty();
			final String assetID = brr.assetID();
			final Asset asset = getAssetByID(assetID);
			final int currentQty = asset.getCurQty();
			return offeredQty <= currentQty;
		}

		private void accept(String bID) {
			String empID = Map_BID_To_EmpID(bID);
			final HashMap<String, BorrowAsset> handling = this.getHandlingMap(empID);
			final HashMap<String, BorrowAsset> done = this.getDoneMap(empID);
			final BorrowAsset brr = getHandlingBorrowAsset(bID);
			final int offeredQty = brr.offeredQty();
			final Asset asset = getAsset(bID);
			final int oldCurrentQty = asset.getCurQty();
			final int newCurrentQty = oldCurrentQty - offeredQty;
			asset.setCurQty(newCurrentQty);
			brr.setBorrowDateTime(new Date());
			brr.setApprovedQty(offeredQty);
			done.put(bID, brr);
			handling.remove(bID);
		}

		private Asset getAsset(String bID) {
			final BorrowAsset brr = getHandlingBorrowAsset(bID);
			final String assetID = brr.assetID();
			return getAssetByID(assetID);
		}

		private BorrowAsset getDoneBorrowAsset(String bID) {
			BorrowAsset ret = null;
			if (isBIDExistInHandling(bID)) {
				String empID = Map_BID_To_EmpID(bID);
				ret = BIDDirectory.get(empID).get(1).get(bID);
			}
			return ret;
		}

		private BorrowAsset getHandlingBorrowAsset(String bID) {
			BorrowAsset ret = null;
			if (isBIDExistInHandling(bID)) {
				String empID = Map_BID_To_EmpID(bID);
				ret = BIDDirectory.get(empID).get(0).get(bID);
			}
			return ret;
		}

		private String Map_BID_To_EmpID(String bID) {
			final int length = bID.length();
			final int offset = Integer.parseInt(bID.substring(1, length - 1));
			int pair[] = BIDAllocateMap.get(offset);
			if (pair == null) {
				return null;
			}
			return "e" + String.valueOf(pair[BLOCK_SIZE]);
		}

		private boolean isEmpIDExist(String empID) {
			if (empID == null) {
				return false;
			}
			return BIDDirectory.containsKey(empID);
		}

		private BorrowAsset getDoneRequest(String bID) {
			BorrowAsset ret = null;
			if (isBIDExistInHandling(bID)) {
				String empID = Map_BID_To_EmpID(bID);
				HashMap<String, BorrowAsset> doneMap = BIDDirectory.get(empID).get(1);
				ret = doneMap.get(bID);
			}
			return ret;
		}

		private BorrowAsset getHandlingRequest(String bID) {
			BorrowAsset ret = null;
			if (isBIDExistInHandling(bID)) {
				String empID = Map_BID_To_EmpID(bID);
				HashMap<String, BorrowAsset> handlingMap = BIDDirectory.get(empID).get(0);
				ret = handlingMap.get(bID);
			}
			return ret;
		}

		private HashMap<String, BorrowAsset> getDoneMap(String empID) {
			HashMap<String, BorrowAsset> done = null;
			if (containsEmp(empID)) {
				done = BIDDirectory.get(empID).get(1);
			}
			return done;
		}

		private HashMap<String, BorrowAsset> getHandlingMap(String empID) {
			HashMap<String, BorrowAsset> handling = null;
			if (containsEmp(empID)) {
				handling = BIDDirectory.get(empID).get(0);
			}
			return handling;
		}

		private boolean containsEmp(String empID) {
			String regex = "[eE]\\d{6}";
			if (empID == null || !empID.matches(regex)) {
				return false;
			}
			return BIDDirectory.containsKey(empID);

		}

		private boolean isBIDExistInDone(String bID) {
			String regex = "[bB]\\d{3}";
			if (bID == null || !bID.matches(regex)) {
				return false;
			}
			String empID = Map_BID_To_EmpID(bID);
			if (empID == null) {
				return false;
			}
			HashMap<String, BorrowAsset> done = BIDDirectory.get(empID).get(1);
			return done.containsKey(bID);
		}

		private boolean isBIDExistInHandling(String bID) {
			String regex = "[bB]\\d{3}";
			if (bID == null || !bID.matches(regex)) {
				return false;
			}
			String empID = Map_BID_To_EmpID(bID);
			if (empID == null) {
				return false;
			}
			HashMap<String, BorrowAsset> handling = BIDDirectory.get(empID).get(0);
			return handling.containsKey(bID);
		}

		private ArrayNode encapsulateAllHandlingRequests() {
			ArrayNode pack = initializeJsonArray();
			getAllHandlingRequest().forEach((brr) -> {
				pack.add(encapsulateHandlingRequest(brr));
			});
			return pack;
		}

		private JsonNode encapsulateHandlingRequest(BorrowAsset item) {
			ObjectNode pack = initializeJsonObj();
			pack.put("RID", item.rID());
			pack.put("Asset ID", item.assetID());
			pack.put("Employee ID", item.employeeID());
			pack.put("Offered Quantity", item.offeredQty());
			pack.put("Request Time", String.valueOf(item.requestDateTIme()));
			return pack;
		}

		private List<BorrowAsset> getAllHandlingRequest() {
			List<BorrowAsset> handling = new ArrayList<>();
			BIDDirectory.values().stream().map((BITTable) -> BITTable.get(0)).forEachOrdered((handlingMap) -> {
				handling.addAll(handlingMap.values());
			});
			return handling;
		}

		private ArrayNode encapsulateAllDoneRequests() {
			ArrayNode pack = initializeJsonArray();
			getAllDoneRequest().forEach((brr) -> {
				pack.add(encapsulateDoneRequest(brr));
			});
			return pack;
		}

		private List<BorrowAsset> getAllDoneRequest() {
			List<BorrowAsset> done = new ArrayList<>();
			BIDDirectory.values().stream().map((BITTable) -> BITTable.get(1)).forEachOrdered((doneMap) -> {
				done.addAll(doneMap.values());
			});
			return done;
		}

		private JsonNode encapsulateDoneRequest(BorrowAsset item) {
			ObjectNode pack = initializeJsonObj();
			pack.put("BID", item.bID());
			pack.put("Asset ID", item.assetID());
			pack.put("Employee ID", item.employeeID());
			pack.put("Offered Quantity", item.offeredQty());
			pack.put("Approved Quantity", item.approvedQty());
			pack.put("Request Time", String.valueOf(item.requestDateTIme()));
			pack.put("Approved Time", String.valueOf(item.getBorrowDateTime()));
			return pack;
		}

		private void seedData() {
			String empID = "e160001";
			String assetID = "a001";
			int offeredQty = 5;
			String bID = allocate(empID);
			BorrowAsset brr = BorrowAsset.createNewRequest(bID, assetID, empID, offeredQty);
			if (isEmpIDExist(empID)) {
				HashMap<String, BorrowAsset> handling = getHandlingMap(empID);
				handling.put(bID, brr);

			} else {
				HashMap<String, BorrowAsset> handling = new HashMap<>();
				HashMap<String, BorrowAsset> done = new HashMap<>();
				ArrayList<HashMap<String, BorrowAsset>> BIDTable = new ArrayList<>(2);
				BIDTable.add(0, handling);
				BIDTable.add(1, done);
				BIDDirectory.put(empID, BIDTable);
				handling.put(bID, brr);
			}
			updateBIDAllocateMap(bID, empID);
		}

		private void updateBIDAllocateMap(String bID, String empID) {
			int offset = getOffset(bID);
			int[] pair = null;
			if (BIDAllocateMap.containsKey(offset)) {
				pair = BIDAllocateMap.get(offset);
				pair[1]++;
			} else {
				pair = new int[BLOCK_SIZE + 1];
				pair[BLOCK_SIZE] = Integer.parseInt(empID.substring(1));
				BIDAllocateMap.put(offset, pair);
			}
			final int length = bID.length();
			int pos = Integer.parseInt(bID.substring(length - 1));
			pair[pos] = 1;
		}

		private String allocate(String empID) {
			String newBID = null;
			if (containsEmp(empID)) {
				HashMap<String, BorrowAsset> handling = getHandlingMap(empID);
				for (String bID : handling.keySet()) {
					int offset = getOffset(bID);
					newBID = allocateBID(offset);
					if (newBID != null) {
						break;
					}

				}
				if (newBID == null) {
					int offset = allocateBIDOffset();
					newBID = allocateBID(offset);
				}
			} else {
				int offset = allocateBIDOffset();
				newBID = allocateBID(offset);
			}

			return newBID;
		}

		private String allocateBID(int offset) {
			String newBID = null;
			if (BIDAllocateMap.containsKey(offset)) {
				int[] pair = BIDAllocateMap.get(offset);
				int relative = -1;
				for (int i = 0; i < BLOCK_SIZE; i++) {
					if (pair[i] == 0) {
						relative = i;
						break;
					}
				}
				if (relative != -1) {
					newBID = nomarlizeBID("b" + String.valueOf(offset + relative));
				}
			} else {
				newBID = nomarlizeBID("b" + String.valueOf(offset));
			}
			return newBID;
		}

		private int allocateBIDOffset() {
			int numOfBlock = BIDAllocateMap.size();
			while (BIDAllocateMap.containsKey(numOfBlock)) {
				numOfBlock++;
			}
			return numOfBlock * BLOCK_SIZE;
		}

		private String nomarlizeBID(String bID) {
			String newBID = StringUtilities.toLowerCasse(bID);
			while (newBID.length() < 4) {
				newBID = "b0" + newBID.substring(1);
			}
			return newBID;
		}

		private int getOffset(String bID) {
			final int length = bID.length();
			return Integer.parseInt(bID.substring(1, length - 1));
		}
	}
}
