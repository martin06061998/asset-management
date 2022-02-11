/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbo;

import java.io.IOException;

/**
 *
 * @author marti
 */
interface FileHandler<T> {
	T read() throws Exception;
	void write(T data) throws IOException;
}
