package com.canevi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
	public static String loadAsString(String path) {
		var classLoader = FileUtils.class.getClassLoader();
		try (var is = classLoader.getResourceAsStream(path)) {
			if (is == null) throw new IOException("Resource not found: " + path);
			return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public static byte[] loadImageFile(String fileName) {
		var classLoader = FileUtils.class.getClassLoader();
		try (var is = classLoader.getResourceAsStream(fileName)) {
			if (is == null) throw new IOException("Resource not found: " + fileName);
			return is.readAllBytes();
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
