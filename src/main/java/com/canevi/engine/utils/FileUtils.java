package com.canevi.engine.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {
	public static String loadAsString(String path) {
		StringBuilder result = new StringBuilder();
		
		ClassLoader classLoader = FileUtils.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(path)) {
            assert is != null;
            try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }
        } catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return result.toString();
	}

	public static byte[] loadImageFile(String fileName) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		ClassLoader classLoader = FileUtils.class.getClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(fileName)) {
			// Start with a reasonable size or use a minimum
            assert is != null;
            int bufferSize = Math.min(1024, is.available());
			byte[] buffer = new byte[bufferSize];
			int bytesRead;

			while ((bytesRead = is.read(buffer)) != -1) {
				stream.write(buffer, 0, bytesRead);

				// Dynamically adjust buffer size based on remaining bytes
				bufferSize = Math.min(1024, is.available());
				if (bufferSize > 0) {
					buffer = new byte[bufferSize];
				} else {
					break; // No more bytes to read
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return stream.toByteArray();
	}
}
