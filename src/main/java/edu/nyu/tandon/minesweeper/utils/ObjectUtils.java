package edu.nyu.tandon.minesweeper.utils;

import java.io.*;

public class ObjectUtils {

	public static byte[] toByteStream(Object object) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			System.err.println("Exception occurred while converting converting object to byte array: " + e.getMessage());
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static Object toObject(byte[] byteArr) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
		     ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Exception occurred while converting byte array to Object: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
