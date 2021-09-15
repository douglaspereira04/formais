package model.io;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Class to read JSON configuration files from the disk
 * 
 * @author douglas
 */
public class FileUtils {

	private static Gson gson = new Gson();
	private static JFileChooser fileChooser = new JFileChooser();

	public static String[] getFileList(String path) {
		String[] listOfNames;
		File[] listOfFiles;
		File folder;

		folder = new File(path);
		listOfFiles = folder.listFiles();
		listOfNames = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			listOfNames[i] = listOfFiles[i].getName();
		}

		return listOfNames;
	}

	public static <T> void saveToFile(T object, String path) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		
		if(object.getClass() == String.class) {
			writer.write((String)object);
		}else {
			JsonElement result = gson.toJsonTree(object, object.getClass());

			if (result.isJsonArray()) {
				JSONArray json = new JSONArray(result.toString());
				writer.write(json.toString(4));
			} else {
				JSONObject json = new JSONObject(result.toString());
				writer.write(json.toString(4));
			}
		}

		writer.close();
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	public static <T> T loadFromFile(String path, Class<T> classOfT)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonElement jsonElement;

		jsonElement = JsonParser.parseReader(new FileReader(path));
		return gson.fromJson(jsonElement, classOfT);

	}

	public static <T> T loadFromFile(InputStream in, Class<T> classOfT) throws Exception {
		Scanner scanner = new Scanner(in);
		try {
			scanner.useDelimiter("\\A");
			String jsonString = scanner.hasNext() ? scanner.next() : "";
			JsonElement jsonElement;

			jsonElement = JsonParser.parseString(jsonString);
			scanner.close();
			return gson.fromJson(jsonElement, classOfT);
		} catch (JsonParseException e) {
			throw new Exception("Error loading file json parse");
		} catch (IllegalStateException e) {
			throw new Exception("Error loading file illegal");
		} catch (Exception e) {
			throw new Exception("Error");
		} finally {

		}

	}

	/**
	 * Selects export file
	 * 
	 * @param defaultFileName default file name
	 * @param extension file extension
	 * @return {@link File} selected file
	 */
	public static File selectExportFile(String defaultFileName, Component parent, String extension) {
		fileChooser.setSelectedFile(new File(defaultFileName));
		int option = fileChooser.showSaveDialog(parent);

		if (option == JFileChooser.APPROVE_OPTION) {

			String filePath = fileChooser.getSelectedFile().getAbsolutePath();

			if (!filePath.endsWith(extension))
				filePath += extension;

			File file = new File(filePath);
			if (file.exists()) {
				option = JOptionPane.showConfirmDialog(fileChooser, "File already exists. Overwrite?", "Overwrite",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION)
					file = selectExportFile(defaultFileName, parent, extension);

			}
			return file;

		}

		return null;
	}


	/**
	 * Selects export file with json extension
	 * 
	 * @param defaultFileName default file name
	 * @return {@link File} selected file
	 */
	public static File selectExportFile(String defaultFileName, Component parent) {
		return selectExportFile(defaultFileName, parent, ".json");
	}

	/**
	 * Selects import file
	 * 
	 * @param defaultFileName default file name
	 * @return {@link File} selected file
	 */
	public static File selectImportFile(String defaultFileName, Component parent) {
		fileChooser.setSelectedFile(new File(defaultFileName));
		int option = fileChooser.showOpenDialog(parent);

		if (option == JFileChooser.APPROVE_OPTION) {

			String filePath = fileChooser.getSelectedFile().getAbsolutePath();

			if (!filePath.endsWith(".json"))
				filePath += ".json";

			File file = new File(filePath);
			if (!file.exists()) {
				JOptionPane.showMessageDialog(fileChooser, "Not a valid file.");
				file = selectImportFile(defaultFileName, parent);
			}
			return file;

		}

		return null;
	}
}
