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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            //controller.assetMap.putAll(seedData());
            //controller.save();
            controller.load();
        }
        return controller;
    }

    /*private static HashMap<String, Asset> seedData() {
        HashMap<String, Asset> ret = new HashMap<>();
        Asset newAsset = new Asset("A001", "Samsung projector", "White", 500, 3.2, 10, 5);
        Asset newAsset2 = new Asset("A002", "Macbook Pro 2016", "Sliver", 1000, 2.2, 5, 2);
        ret.put(newAsset.getAssetID(), newAsset);
        ret.put(newAsset2.getAssetID(), newAsset2);
        return ret;
    }*/

    JsonNode borrowAsset(String empID, JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        int counter = 0;
        for (JsonNode element : dataElement) {
            try {
                boolean success = borrowMng.createBorrowRequest(empID, element);
                if (success) {
                    counter++;
                }
            } catch (Exception ex) {
                Logger.getLogger(AssetController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reply.put("status", "accepted");
        reply.put("message", "successfuly add " + counter + " requests");
        if (counter > 0) {
            borrowMng.save();
        }
        return reply;
    }

    JsonNode cancelRequest(String empID, JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        int counter = 0;
        for (JsonNode element : dataElement) {
            try {
                final int bID = borrowMng.Map_BIDStr_To_Number(element.get("id").asText());
                boolean success = borrowMng.cancelRequest(empID, bID);
                if (success) {
                    counter++;
                }
            } catch (Exception ex) {
                Logger.getLogger(AssetController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reply.put("status", "accepted");
        reply.put("message", "successfuly cancel " + counter + " requests");
        if (counter > 0) {
            borrowMng.save();
        }
        return reply;
    }

    @Override
    public JsonNode add(JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = this.initializeJsonArray();
        int count = 0;
        int success = 0;
        for (JsonNode element : dataElement) {
            ObjectNode entry = (ObjectNode) element;
            ObjectNode report = initializeJsonObj();
            String status;
            String id = allocateAssetID();
            entry.put("id", id);
            try {
                Asset newAsset = EnityFactory.getInstane().createAsset(entry);
                assetMap.put(newAsset.getAssetID(), newAsset);
                success++;
                status = "success";
            } catch (IllegalArgumentException ex) {
                status = "fail due to " + ex.getMessage();
            }

            count++;
            report.put("Number", count);
            report.put("status", status);
            data.add(report);
        }
        reply.put("status", "accepted");
        reply.set("data", data);
        reply.put("message", "successfuly add " + success + " assets");
        if (success > 0) {
            save();
        }
        return reply;
    }

    String allocateAssetID() {
        int size = assetMap.size();
        String newID = generateAssetIDStr(size);
        while (containsAsset(newID)) {
            newID = generateAssetIDStr(++size);
        }
        return newID;
    }

    String generateAssetIDStr(int number) {
        String newBID = "a" + String.valueOf(number);
        while (newBID.length() < 4) {
            newBID = "a0" + newBID.substring(1);
        }
        return newBID;
    }

    JsonNode edit(JsonNode dataElement) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = this.initializeJsonArray();
        int count = 0;
        int success = 0;
        for (JsonNode element : dataElement) {
            ObjectNode report = initializeJsonObj();
            String status;
            String id = element.get("id").asText();
            if (containsAsset(id)) {
                try {
                    Asset remakeAsset = EnityFactory.getInstane().reforgeAsset(element);
                    assetMap.put(remakeAsset.getAssetID(), remakeAsset);
                    success++;
                    status = "success";
                } catch (IllegalArgumentException ex) {
                    status = "fail due to " + ex.getMessage();
                }
            } else {
                status = "fail due to not exist id";
            }
            count++;
            report.put("Number", count);
            report.put("status", status);
            data.add(report);
        }
        if (success > 0) {
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
            int bID = borrowMng.Map_BIDStr_To_Number(element.asText());
            boolean isApprovable = borrowMng.isApprovable(bID);
            if (isApprovable) {
                borrowMng.accept(bID);
                data.add(element.asText());
                count++;
            }
        }
        if (count > 0) {
            borrowMng.save();
            save();
        }
        reply.put("status", "accepted");
        reply.put("message", "successfully approve " + count + " requests");
        reply.set("total entries", data);
        return reply;
    }

    JsonNode getAllApprovedRequests(int privilege, String empID) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = borrowMng.encapsulateAllApprovedRequests(privilege, empID);
        reply.put("status", "accepted");
        reply.set("data", data);
        reply.put("size", data.size());
        reply.put("message", "successfully");
        return reply;
    }

    JsonNode getAllWaitingRequests(int privilege, String empID) {
        ObjectNode reply = initializeJsonObj();
        ArrayNode data = borrowMng.encapsulateAllWaitingRequests(privilege, empID);
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
        HashMap<String, Asset> data = AssetDBO.loadAssets();
        if (data != null) {
            assetMap = data;
        }
    }

    @Override
    public JsonNode delete(JsonNode target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAcceptable(String key, int command) {
        if (command < 0 || command > 3000 || key == null || key.length() < 15) {
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
        list.sort((a1, a2) -> a2.getName().compareTo(a1.getName()));
        data = encapsulate(list);
        reply.put("status", "accepted");
        reply.put("message", "successfully");
        reply.set("data", data);
        return reply;
    }

    private List<Asset> findAll(Predicate<Asset> predicate) {
        List<Asset> ret = new ArrayList<>();
        assetMap.values().stream().filter((item) -> (predicate.test(item))).forEachOrdered((item) -> {
            ret.add(item);
        });
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

    private class BorrowManager implements Serializable {

        private static final long serialVersionUID = 1215393158348534567L;
        private HashMap<String, int[]> masterDirectory;
        private transient HashMap<Integer, HashMap<Integer, BorrowAsset>> waitingMapDirectory;
        private transient HashMap<Integer, HashMap<Integer, BorrowAsset>> approvedMapDirectory;
        private HashMap<Integer, String> BIDAllocationMap;
        private int numOfRequest; // number of waiting request
        private final static int MAX_NUMBER_OF_REQUESTS = 1000;

        private BorrowManager() {
            masterDirectory = new HashMap<>();
            waitingMapDirectory = new HashMap<>();
            approvedMapDirectory = new HashMap<>();
            BIDAllocationMap = new HashMap<>();
            //seedData();
            //save();
            load();
        }

        private void save() {
            AssetDBO.saveBorrowState(waitingMapDirectory, approvedMapDirectory);
            AssetDBO.saveSystemFile(this);
        }

        private void load() {
            Serializable data = AssetDBO.loadSystemFile();
            if (data != null && data.getClass() == BorrowManager.class) {
                BorrowManager savedBrrManager = (BorrowManager) data;
                if (savedBrrManager.BIDAllocationMap != null && savedBrrManager.masterDirectory != null) {
                    if (AssetDBO.loadBorrowState(waitingMapDirectory, approvedMapDirectory)) {
                        this.masterDirectory = savedBrrManager.masterDirectory;
                        this.BIDAllocationMap = savedBrrManager.BIDAllocationMap;
                        this.numOfRequest = savedBrrManager.numOfRequest;
                    }
                }
            }
        }

        public int Map_BIDStr_To_Number(String bID) {
            final String regex = "[rb]\\d{3}";
            if (bID == null || !bID.matches(regex)) {
                return -1;
            }
            int ret = Integer.parseInt(bID.substring(1));
            if (bID.charAt(0) == 'b') {
                ret += 1000;
            }
            return ret;
        }

        public String getBIDStr(int bID) {
            String ret;
            String prefix;
            int num = bID;
            if (num >= 1000) {
                num -= 1000;
                prefix = "b";
            } else {
                prefix = "r";
            }
            ret = prefix + String.valueOf(num);
            while (ret.length() < 4) {
                ret = prefix + "0" + ret.substring(1);
            }
            return ret;
        }

        private boolean createBorrowRequest(String empID, JsonNode element) throws Exception {
            boolean ret = true;
            try {
                if (numOfRequest == MAX_NUMBER_OF_REQUESTS) {
                    return false;
                }
                String assetID = element.get("assetid").asText();
                int quantity = Integer.parseInt(element.get("quantity").asText());
                if (!containsAsset(assetID)) {
                    return false;
                }
                if (!EmpController.getInstance().isEmployeeExist(empID)) {
                    return false;
                }
                ensureSystemStructure(empID, true, false);
                int newBID = allocateBID();
                BorrowAsset newBrr = BorrowAsset.createNewRequest(newBID, assetID, empID, quantity);
                HashMap<Integer, BorrowAsset> waitingdMap = this.getWaitingMap(empID);
                waitingdMap.put(newBrr.bID(), newBrr);
                BIDAllocationMap.put(newBID, empID);
                numOfRequest++;
            } catch (NumberFormatException | IllegalStateException e) {
                //System.out.println(e.getMessage());
                ret = false;
            }

            return ret;
        }

        private boolean cancelRequest(String empID, int bID) {
            if (!empHasBID(empID, bID, true, false)) {
                return false;
            }
            HashMap<Integer,BorrowAsset> waitingMap = getWaitingMap(empID);
            waitingMap.remove(bID);
            BIDAllocationMap.remove(bID);
            numOfRequest--;
            clearEmpInfo(empID, true, true);
            return true;
        }

        private boolean empHasBID(String empID, int bID, boolean checkWaitingMap, boolean checkApprovedMap) {
            boolean exist = true;
            if (!isBIDExist(bID)) {
                return false;
            }
            if (!isEmpInfoExist(empID)) {
                return false;
            }

            if (checkWaitingMap) {
                HashMap<Integer, BorrowAsset> waitingMap = getWaitingMap(empID);
                if (waitingMap == null) {
                    exist = false;
                } else {
                    exist = waitingMap.containsKey(bID);
                }
            }
            if (checkApprovedMap && exist) {
                HashMap<Integer, BorrowAsset> approvedMap = getApprovedMap(empID);
                if (approvedMap == null) {
                    exist = false;
                } else {
                    exist = approvedMap.containsKey(bID);
                }
            }
            return exist;
        }

        private void ensureSystemStructure(String empID, boolean createWaitingMap, boolean createApprovedMap) {
            if (!isEmpInfoExist(empID)) {
                masterDirectory.put(empID, new int[]{-1, -1});
            }
            final int[] empTable = masterDirectory.get(empID);
            if (createWaitingMap && empTable[0] == -1) {
                final int waitingTableKey = Allocate_Waiting_Borrow_Table_Key();
                empTable[0] = waitingTableKey;
                HashMap<Integer, BorrowAsset> waitingMap = new HashMap<>();
                waitingMapDirectory.put(waitingTableKey, waitingMap);

            }
            if (createApprovedMap && empTable[1] == -1) {
                final int approvedTableKey = Allocate_Approved_Borrow_Table_Key();
                empTable[1] = approvedTableKey;
                HashMap<Integer, BorrowAsset> approvedMap = new HashMap<>();
                approvedMapDirectory.put(approvedTableKey, approvedMap);
            }

        }

        private ArrayNode encapsulateAllApprovedRequests(int privilege, String empID) {
            ArrayNode pack = initializeJsonArray();
            if (privilege == 3000) {
                approvedMapDirectory.values().forEach((approvedMap) -> {
                    approvedMap.values().forEach((request) -> {
                        pack.add(encapsulateApprovedRequest(request));
                    });
                });
            }
            else{
                HashMap<Integer, BorrowAsset> approvedMap = this.getApprovedMap(empID);
                approvedMap.values().forEach((request) -> {
                    pack.add(this.encapsulateApprovedRequest(request));
                });
            }
            return pack;
        }

        private ArrayNode encapsulateAllWaitingRequests(int privilege, String empID) {
            ArrayNode pack = initializeJsonArray();
            ensureSystemStructure(empID, true, false);
            if (privilege == 3000) {
                waitingMapDirectory.values().forEach((waitingMap) -> {
                    waitingMap.values().forEach((request) -> {
                        pack.add(encapsulateWaitingRequest(request));
                    });
                });
            } else {
                HashMap<Integer, BorrowAsset> waitingMap = this.getWaitingMap(empID);
                waitingMap.values().forEach((request) -> {
                    pack.add(encapsulateWaitingRequest(request));
                });
            }
            return pack;
        }

        /*private void seedData() {
            int newBID = 0;
            String assetID;
            Date requestTime;
            String empID;
            int qty = 1;
            //Request
            newBID = 1;
            assetID = "a001";
            requestTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 23, 13, 17, 56).getTime();
            empID = "e140449";
            ensureSystemStructure(empID, true, false);
            BorrowAsset newBrr = BorrowAsset.createNewRequest(newBID, assetID, empID, qty, requestTime);
            HashMap<Integer, BorrowAsset> waitingdMap = this.getWaitingMap(empID);
            waitingdMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 2;
            assetID = "a002";
            requestTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 24, 12, 18, 56).getTime();
            empID = "e160001";
            ensureSystemStructure(empID, true, false);
            newBrr = BorrowAsset.createNewRequest(newBID, assetID, empID, qty, requestTime);
            waitingdMap = this.getWaitingMap(empID);
            waitingdMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 3;
            assetID = "a001";
            requestTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 23, 11, 19, 56).getTime();
            empID = "e160798";
            ensureSystemStructure(empID, true, false);
            newBrr = BorrowAsset.createNewRequest(newBID, assetID, empID, qty, requestTime);
            waitingdMap = this.getWaitingMap(empID);
            waitingdMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 7;
            assetID = "a002";
            requestTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 24, 10, 10, 56).getTime();
            empID = "e160240";
            ensureSystemStructure(empID, true, false);
            newBrr = BorrowAsset.createNewRequest(newBID, assetID, empID, qty, requestTime);
            waitingdMap = this.getWaitingMap(empID);
            waitingdMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);
            numOfRequest = 4;

            //Borrow
            newBID = 1001;
            assetID = "a001";
            Date borrowTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 23, 15, 13, 46).getTime();
            empID = "e160001";
            ensureSystemStructure(empID, false, true);
            newBrr = BorrowAsset.createApprovedRequest(newBID, assetID, empID, qty, borrowTime);
            HashMap<Integer, BorrowAsset> approvedMap = this.getApprovedMap(empID);
            approvedMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 1002;
            assetID = "a001";
            qty = 2;
            borrowTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 25, 16, 14, 56).getTime();
            empID = "e160001";
            ensureSystemStructure(empID, false, true);
            newBrr = BorrowAsset.createApprovedRequest(newBID, assetID, empID, qty, borrowTime);
            approvedMap = this.getApprovedMap(empID);
            approvedMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 1003;
            assetID = "a002";
            qty = 3;
            borrowTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 15, 17, 15, 52).getTime();
            empID = "e160798";
            ensureSystemStructure(empID, false, true);
            newBrr = BorrowAsset.createApprovedRequest(newBID, assetID, empID, qty, borrowTime);
            approvedMap = this.getApprovedMap(empID);
            approvedMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

            newBID = 1007;
            assetID = "a001";
            qty = 2;
            borrowTime = new GregorianCalendar(2021, GregorianCalendar.DECEMBER, 26, 12, 16, 53).getTime();
            empID = "e160240";
            ensureSystemStructure(empID, false, true);
            newBrr = BorrowAsset.createApprovedRequest(newBID, assetID, empID, qty, borrowTime);
            approvedMap = this.getApprovedMap(empID);
            approvedMap.put(newBrr.bID(), newBrr);
            BIDAllocationMap.put(newBID, empID);

        }*/

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

        private int allocateBID() {
            int bid = numOfRequest;
            if (numOfRequest == MAX_NUMBER_OF_REQUESTS) {
                throw new IllegalStateException("Reach maximum of request");
            }
            while (isBIDExist(bid)) {
                bid = (bid + 1) % MAX_NUMBER_OF_REQUESTS;
            }
            return bid;
        }

        private boolean isBIDExist(int bID) {
            if (bID < 0 || bID > 1999) {
                return false;
            }
            return BIDAllocationMap.containsKey(bID);
        }

        private boolean isApprovable(int bID) {
            final int numOfApproved = BIDAllocationMap.size() - numOfRequest;
            if (numOfApproved == MAX_NUMBER_OF_REQUESTS) {
                return false;
            }
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

        private void accept(int bID) {
            String empID = Map_BID_To_EmpID(bID);
            ensureSystemStructure(empID, true, true);
            final HashMap<Integer, BorrowAsset> waitingMap = this.getWaitingMap(empID);
            final HashMap<Integer, BorrowAsset> approvedMap = this.getApprovedMap(empID);
            final BorrowAsset brr = getWaitingRequest(bID);
            final int offeredQty = brr.offeredQty();
            final Asset asset = getAsset(bID);
            final int oldCurrentQty = asset.getCurQty();
            final int newCurrentQty = oldCurrentQty - offeredQty;
            asset.setCurQty(newCurrentQty);
            brr.setBorrowDateTime(new Date());
            brr.setApprovedQty(offeredQty);
            final int numOfApproved = BIDAllocationMap.size() - numOfRequest;
            int newBID = numOfApproved + MAX_NUMBER_OF_REQUESTS;
            while (BIDAllocationMap.containsKey(newBID)) {
                newBID = (newBID + 1) % MAX_NUMBER_OF_REQUESTS + MAX_NUMBER_OF_REQUESTS;
            }
            brr.setBID(newBID);
            approvedMap.put(newBID, brr);
            waitingMap.remove(bID);
            BIDAllocationMap.remove(bID);
            BIDAllocationMap.put(newBID, empID);
            numOfRequest--;
            clearEmpInfo(empID, true, false);
        }

        private void clearEmpInfo(String empID, boolean checkWaitingDirectory, boolean checkApprovedDirectory) {
            final int[] empTable = masterDirectory.get(empID);
            if (checkWaitingDirectory && empTable[0] != -1) {
                final int size = this.getWaitingMap(empID).size();
                if (size == 0) {
                    this.waitingMapDirectory.remove(empTable[0]);
                    empTable[0] = -1;
                }
            }
            if (checkApprovedDirectory && empTable[1] != -1) {
                final int size = this.getApprovedMap(empID).size();
                if (size == 0) {
                    this.approvedMapDirectory.remove(empTable[1]);
                    empTable[1] = -1;
                }
            }
            if (empTable[0] == -1 && empTable[1] == -1) {
                masterDirectory.remove(empID);
            }
        }

        private Asset getAsset(int bID) {
            final BorrowAsset brr = getWaitingRequest(bID);
            final String assetID = brr.assetID();
            return getAssetByID(assetID);
        }

        private String Map_BID_To_EmpID(int bID) {
            return BIDAllocationMap.get(bID);
        }

        private BorrowAsset getApprovedRequest(int bID) {
            BorrowAsset ret = null;
            if (isBIDExistInBorrowMap(bID, false, true)) {
                String empID = Map_BID_To_EmpID(bID);
                HashMap<Integer, BorrowAsset> approvedMap = getApprovedMap(empID);
                ret = approvedMap.get(bID);
            }
            return ret;
        }

        private BorrowAsset getWaitingRequest(int bID) {
            BorrowAsset ret;
            String empID = Map_BID_To_EmpID(bID);
            HashMap<Integer, BorrowAsset> waitingMap = getWaitingMap(empID);
            ret = waitingMap.get(bID);
            return ret;
        }

        private HashMap<Integer, BorrowAsset> getApprovedMap(String empID) {
            final int[] empTable = masterDirectory.get(empID);
            final int pos = empTable[1];
            return approvedMapDirectory.get(pos);
        }

        private HashMap<Integer, BorrowAsset> getWaitingMap(String empID) {
            final int[] empTable = masterDirectory.get(empID);
            final int pos = empTable[0];
            return waitingMapDirectory.get(pos);
        }

        private boolean isBIDExistInBorrowMap(int bID, boolean checkInWaiting, boolean checkInApproved) {
            boolean exist = true;
            if (!isBIDExist(bID)) {
                return false;
            }

            String empID = Map_BID_To_EmpID(bID);
            if (!isEmpInfoExist(empID)) {
                return false;
            }

            if (checkInWaiting) {
                HashMap<Integer, BorrowAsset> waiting = getWaitingMap(empID);
                if (waiting == null) {
                    exist = false;
                } else {
                    exist = waiting.containsKey(bID);
                }

            }
            if (checkInApproved && exist) {
                HashMap<Integer, BorrowAsset> approved = getApprovedMap(empID);
                if (approved == null) {
                    exist = false;
                } else {
                    exist = approved.containsKey(bID);
                }

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
            Asset asset = getAsset(item.bID());
            pack.put("RID", getBIDStr(item.bID()));
            pack.put("Asset ID", item.assetID());
            pack.put("Employee ID", item.employeeID());
            pack.put("Offered Qty", item.offeredQty());
            pack.put("Current Qty", asset.getCurQty());
            pack.put("Request Time", String.valueOf(item.requestDateTIme()));
            return pack;
        }

        private JsonNode encapsulateApprovedRequest(BorrowAsset item) {
            ObjectNode pack = initializeJsonObj();
            pack.put("BID", getBIDStr(item.bID()));
            pack.put("Asset ID", item.assetID());
            pack.put("Employee ID", item.employeeID());
            pack.put("Quantity", item.offeredQty());
            pack.put("Borrow Time", String.valueOf(item.getBorrowDateTime()));
            return pack;
        }

    }
}
