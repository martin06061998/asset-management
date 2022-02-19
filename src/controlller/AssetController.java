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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import model.Asset;
import model.BorrowAsset;
import model.EnityFactory;

/**
 *
 * @author marti
 */
class AssetController implements I_AssetController, Serializable {

    private static final long serialVersionUID = 1225393166648534567L;
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
            controller.load();
        }
        return controller;
    }

    private static HashMap<String, Asset> seedData() {
        HashMap<String, Asset> ret = new HashMap<>();
        Asset newAsset = new Asset("A001", "Samsung projector", "White", 500l, 3.2, 10);
        Asset newAsset2 = new Asset("A002", "Macbook Pro 2016", "White", 500l, 3.2, 30);
        ret.put(newAsset.getAssetID(), newAsset);
        ret.put(newAsset2.getAssetID(), newAsset2);
        return ret;
    }

    JsonNode borrowAsset(JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        int counter = 0;
        for (JsonNode element : dataElement) {
            boolean success = borrowMng.createBorrowRequest(element);
            if (success) {
                counter++;
            }
        }
        reply.put("status", "accepted");
        reply.put("message", "successfuly add " + counter + " requests");
        if (counter > 0) {
            save();
        }
        return reply;
    }

    @Override
    public JsonNode add(JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        int counter = 0;
        for (JsonNode element : dataElement) {
            try {
                String id = element.get("id").asText();
                if (!containsAsset(id)) {
                    Asset newAsset = EnityFactory.getInstane().createAsset(element);
                    assetMap.put(newAsset.getAssetID(), newAsset);
                    counter++;
                }
            } catch (Exception ex) {

            }
        }
        reply.put("status", "accepted");
        reply.put("message", "successfuly add " + counter + " assets");
        if (counter > 0) {
            save();
        }
        return reply;
    }

    JsonNode edit(JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = this.initializeJsonArray();
        int count = 0;
        for (JsonNode element : dataElement) {
            String id = element.get("id").asText();
            if (containsAsset(id)) {
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
        reply.set("total entries", data);
        return reply;
    }

    JsonNode getAllApprovedRequests() {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = borrowMng.encapsulateAllApprovedRequests();
        reply.put("status", "accepted");
        reply.set("data", data);
        reply.put("size", data.size());
        reply.put("message", "successfully");
        return reply;
    }

    JsonNode getAllWaitingRequests() {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = borrowMng.encapsulateAllWaitingRequests();
        reply.put("status", "accepted");
        reply.set("data", data);
        reply.put("size", data.size());
        reply.put("message", "successfully");
        return reply;
    }

    void save() {
        AssetDBO.saveAssets(assetMap);
    }

    void load() {
        Serializable data = AssetDBO.loadAssets();
        if (data != null) {
            assetMap = (HashMap<String, Asset>) data;
        }
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

    private boolean containsAsset(String id) {
        return assetMap.containsKey(id);
    }

    private Asset getAssetByID(String assetID) {
        return assetMap.get(assetID);
    }

    JsonNode searchByName(String name) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data;
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

    public class BorrowManager implements Serializable {

        private static final long serialVersionUID = 1215393158348534567L;
        private HashMap<String, int[]> masterDirectory;
        private HashMap<Integer, HashMap<String, BorrowAsset>> waitingMapDirectory;
        private HashMap<Integer, HashMap<String, BorrowAsset>> approvedMapDirectory;
        private Integer[] BIDAllocationMap;
        private int allocationMapCapacity;
        private int numberOfBID;
        private static transient final int MIN_ALLOCATION_MAP_CAPACITY = 16;
        private static transient final int MAX_ALLOCATION_MAP_CAPACITY = Integer.MAX_VALUE;

        private BorrowManager() {
            masterDirectory = new HashMap<>();
            waitingMapDirectory = new HashMap<>();
            approvedMapDirectory = new HashMap<>();
            BIDAllocationMap = new Integer[MIN_ALLOCATION_MAP_CAPACITY];
            seedData();
            loadSystemFile();

        }

        private boolean createBorrowRequest(JsonNode element) {
            boolean ret = true;
            try {
                String empID = element.get("empid").asText();
                String assetID = element.get("assetid").asText();
                int quantity = Integer.parseInt(element.get("quantity").asText());
                if(!containsAsset(assetID))
                    return false;
                if (!EmpController.getInstance().isEmployeeExist(empID)) {
                    return false;
                }
                if (!this.isEmpInfoExist(empID)) {
                    initializeEmpInfo(empID);
                }
                int newBID = allocateBID(empID);
                BorrowAsset newBrr = BorrowAsset.createNewRequest(generateBIDStr(newBID), assetID, empID, quantity);
                HashMap<String, BorrowAsset> waitingdMap = this.getWaitingMap(empID);
                waitingdMap.put(newBrr.bID(), newBrr);
                updateBIDAllocateMap(newBID, empID);
                saveSystemFile();
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                ret = false;
            }

            return ret;
        }

        public void initializeEmpInfo(String empID) {
            int[] empTable = new int[2];
            int waitingTableKey = Allocate_Waiting_Borrow_Table_Key();
            int approvedTableKey = Allocate_Approved_Borrow_Table_Key();
            empTable[0] = waitingTableKey;
            empTable[1] = approvedTableKey;
            masterDirectory.put(empID, empTable);
            HashMap<String, BorrowAsset> waitingMap = new HashMap<>();
            HashMap<String, BorrowAsset> approvedMap = new HashMap<>();
            waitingMapDirectory.put(waitingTableKey, waitingMap);
            approvedMapDirectory.put(approvedTableKey, approvedMap);
        }

        private void saveSystemFile() {
            AssetDBO.saveSystemFile(this);
            AssetDBO.backupBorrowState(waitingMapDirectory, approvedMapDirectory);

        }

        private void loadSystemFile() {
            Object data = AssetDBO.loadSystemFile();
            if (data != null && data.getClass() == BorrowManager.class) {
                BorrowManager saveBorrowMng = (BorrowManager) data;
                this.masterDirectory = saveBorrowMng.masterDirectory;
                this.BIDAllocationMap = saveBorrowMng.BIDAllocationMap;
                this.allocationMapCapacity = saveBorrowMng.allocationMapCapacity;
                this.waitingMapDirectory = saveBorrowMng.waitingMapDirectory;
                this.approvedMapDirectory = saveBorrowMng.approvedMapDirectory;
                this.numberOfBID = saveBorrowMng.numberOfBID;
            }
        }

        private ArrayNode encapsulateAllApprovedRequests() {
            ArrayNode pack = initializeJsonArray();
            approvedMapDirectory.values().forEach((approvedMap) -> {
                approvedMap.values().forEach((request) -> {
                    pack.add(encapsulateApprovedRequest(request));
                });
            });
            return pack;
        }

        private ArrayNode encapsulateAllWaitingRequests() {
            ArrayNode pack = initializeJsonArray();
            waitingMapDirectory.values().forEach((waitingMap) -> {
                waitingMap.values().forEach((request) -> {
                    pack.add(encapsulateWaitingRequest(request));
                });
            });
            return pack;
        }

        private void updateBIDAllocateMap(int bid, String empID) {
            BIDAllocationMap[bid] = Integer.parseInt(empID.substring(1));
            numberOfBID++;
        }

        private void seedData() {
            String empID = "e160001";
            String assetID = "a001";
            int[] empTable = new int[2];
            int waitingTableKey = Allocate_Waiting_Borrow_Table_Key();
            int approvedTableKey = Allocate_Approved_Borrow_Table_Key();
            empTable[0] = waitingTableKey;
            empTable[1] = approvedTableKey;
            masterDirectory.put(empID, empTable);
            HashMap<String, BorrowAsset> waitingMap = new HashMap<>();
            HashMap<String, BorrowAsset> approvedMap = new HashMap<>();
            waitingMapDirectory.put(waitingTableKey, waitingMap);
            approvedMapDirectory.put(approvedTableKey, approvedMap);
            int newBID = allocateBID(empID);
            BorrowAsset newBrr = BorrowAsset.createNewRequest(generateBIDStr(newBID), assetID, empID, 5);
            waitingMapDirectory.get(waitingTableKey).put(newBrr.bID(), newBrr);
            updateBIDAllocateMap(newBID, empID);
        }

        private int Allocate_Waiting_Borrow_Table_Key() {
            int pos = waitingMapDirectory.size();
            while (waitingMapDirectory.containsKey(pos)) {
                pos++;
            }
            return pos;
        }

        private int Allocate_Approved_Borrow_Table_Key() {
            int pos = approvedMapDirectory.size();
            while (approvedMapDirectory.containsKey(pos)) {
                pos++;
            }
            return pos;
        }

        private int allocateBID(String empID) {
            ensureCapacity(numberOfBID + 1);
            int bid = numberOfBID;
            while (isBIDExist(bid)) {
                bid = (bid + 1) % allocationMapCapacity;
            }
            return bid;
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > allocationMapCapacity) {
                grow(minCapacity);
            }
        }

        private void grow(int minCapacity) {
            final int minExpand = MIN_ALLOCATION_MAP_CAPACITY;
            final int doubledOldCapacity = allocationMapCapacity << 1;
            if (doubledOldCapacity < 0) {
                hugeMap(minCapacity);
            } else {
                final int newCapacity = (doubledOldCapacity > minExpand) ? doubledOldCapacity : minExpand;
                BIDAllocationMap = Arrays.copyOf(BIDAllocationMap, newCapacity);
                allocationMapCapacity = newCapacity;
            }
        }

        private void hugeMap(int minCapacity) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            BIDAllocationMap = Arrays.copyOf(BIDAllocationMap, MAX_ALLOCATION_MAP_CAPACITY);
            allocationMapCapacity = Integer.MAX_VALUE;
        }

        private boolean isBIDExist(int bID) {
            if (bID >= allocationMapCapacity || bID < 0) {
                return false;
            }
            return BIDAllocationMap[bID] != null;
        }

        private boolean isApprovable(String bID) {
            if (!isBIDExistInBorrowMap(bID, true, false)) {
                return false;
            }
            final BorrowAsset brr = getWaitingRequest(bID);
            final int offeredQty = brr.offeredQty();
            final String assetID = brr.assetID();
            final Asset asset = getAssetByID(assetID);
            final int currentQty = asset.getCurQty();
            return offeredQty <= currentQty;
        }

        private void accept(String bID) {
            String empID = Map_BID_To_EmpID(convertBID(bID));
            final HashMap<String, BorrowAsset> waitingMap = this.getWaitingMap(empID);
            final HashMap<String, BorrowAsset> approvedMap = this.getApprovedMap(empID);
            final BorrowAsset brr = getWaitingRequest(bID);
            final int offeredQty = brr.offeredQty();
            final Asset asset = getAsset(bID);
            final int oldCurrentQty = asset.getCurQty();
            final int newCurrentQty = oldCurrentQty - offeredQty;
            asset.setCurQty(newCurrentQty);
            brr.setBorrowDateTime(new Date());
            brr.setApprovedQty(offeredQty);
            approvedMap.put(bID, brr);
            waitingMap.remove(bID);
            saveSystemFile();
            AssetController.this.save();
        }

        private Asset getAsset(String bID) {
            final BorrowAsset brr = getWaitingRequest(bID);
            final String assetID = brr.assetID();
            return getAssetByID(assetID);
        }

        private String Map_BID_To_EmpID(int bID) {
            return "e" + BIDAllocationMap[bID];
        }

        private BorrowAsset getApprovedRequest(String bID) {
            BorrowAsset ret = null;
            if (isBIDExistInBorrowMap(bID, false, true)) {
                String empID = Map_BID_To_EmpID(convertBID(bID));
                HashMap<String, BorrowAsset> approvedMap = getApprovedMap(empID);
                ret = approvedMap.get(bID);
            }
            return ret;
        }

        private BorrowAsset getWaitingRequest(String bID) {
            BorrowAsset ret = null;
            if (isBIDExistInBorrowMap(bID, true, false)) {
                String empID = Map_BID_To_EmpID(convertBID(bID));
                HashMap<String, BorrowAsset> waitingMap = getWaitingMap(empID);
                ret = waitingMap.get(bID);
            }
            return ret;
        }

        private HashMap<String, BorrowAsset> getApprovedMap(String empID) {
            final int[] empTable = masterDirectory.get(empID);
            final int pos = empTable[1];
            return approvedMapDirectory.get(pos);
        }

        private HashMap<String, BorrowAsset> getWaitingMap(String empID) {
            final int[] empTable = masterDirectory.get(empID);
            final int pos = empTable[0];
            return waitingMapDirectory.get(pos);
        }

        private boolean isBIDExistInBorrowMap(String bID, boolean checkInWaiting, boolean checkInApproved) {
            final String regex = "b\\d{3}";
            boolean exist = true;
            if (bID == null || !bID.matches(regex)) {
                return false;
            }
            if (!isBIDExist(convertBID(bID))) {
                return false;
            }
            String empID = Map_BID_To_EmpID(convertBID(bID));
            if (!isEmpInfoExist(empID)) {
                return false;
            }

            if (checkInWaiting) {
                HashMap<String, BorrowAsset> waiting = getWaitingMap(empID);
                exist = waiting.containsKey(bID);
            }
            if (checkInApproved && exist) {
                HashMap<String, BorrowAsset> approved = getApprovedMap(empID);
                exist = approved.containsKey(bID);
            }
            return exist;
        }

        private boolean isEmpInfoExist(String empID) {
            if (!EmpController.getInstance().isEmployeeExist(empID)) {
                return false;
            }
            return masterDirectory.containsKey(empID);
        }

        private JsonNode encapsulateWaitingRequest(BorrowAsset item) {
            ObjectNode pack = initializeJsonObj();
            pack.put("RID", item.rID());
            pack.put("Asset ID", item.assetID());
            pack.put("Employee ID", item.employeeID());
            pack.put("Offered Quantity", item.offeredQty());
            pack.put("Request Time", String.valueOf(item.requestDateTIme()));
            return pack;
        }

        private JsonNode encapsulateApprovedRequest(BorrowAsset item) {
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

        private String generateBIDStr(int num) {
            String newBID = "b" + String.valueOf(num);
            while (newBID.length() < 4) {
                newBID = "b0" + newBID.substring(1);
            }
            return newBID;
        }

        private int convertBID(String bid) {
            return Integer.parseInt(bid.substring(1));
        }
    }
}
