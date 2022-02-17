/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author marti
 */
final public class FileHandlerManager {

	private HashMap<String, FileHandler> handlerList;
	private static FileHandlerManager manager;

	private FileHandlerManager() {
	}

	public static FileHandlerManager getInstance() {
		if (Objects.isNull(manager)) {
			manager = new FileHandlerManager();
			manager.handlerList = new HashMap<>();
			manager.handlerList.put("BinaryFileHandler", new BinaryFileHandler());
		}
		return manager;
	}

	public <T extends Serializable> void writeBinary(T data,String filePath) throws IOException {
		manager.handlerList.get("BinaryFileHandler").write(data,filePath);
	}

	public Serializable readBinary(String filePath) throws Exception {
		Serializable response = (Serializable) manager.handlerList.get("BinaryFileHandler").read(filePath);
		return response;
	}
}
