/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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

	public List<String> readText() throws Exception {
		List<String> data = (List<String>) manager.handlerList.get("TextFileHandler").read();
		return data;
	}

	public void writeText(List<String> data) throws IOException {
		manager.handlerList.get("TextFileHandler").write(data);
	}

	public <T extends Serializable> void writeBinary(T data) throws IOException {
		manager.handlerList.get("BinaryFileHandler").write(data);
	}

	public Serializable readBinary() throws Exception {
		System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());
		Serializable response = (Serializable) manager.handlerList.get("BinaryFileHandler").read();
		return response;
	}
}
