/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author marti
 */
public class BinaryFileHandler implements FileHandler<Serializable> {

	private final String filePath = "src\\dbo\\asset.dat";

	@Override
	public Serializable read() throws ClassNotFoundException, IOException {
		File handler = new File(filePath);
		if (!handler.exists() || !handler.canRead()) {
			throw new IOException("File not found or not readable");
		}
		String absolutePath = handler.getAbsolutePath();
		FileInputStream fis = new FileInputStream(absolutePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Serializable response = (Serializable) ois.readObject();
		ois.close();
		return response;
	}

	@Override
	public void write(Serializable data) throws IOException {
		File handler = new File(filePath);
		String absolutePath = handler.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(absolutePath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data);
		oos.close();

	}

}
