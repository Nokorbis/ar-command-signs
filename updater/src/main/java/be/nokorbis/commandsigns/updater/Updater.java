package be.nokorbis.commandsigns.updater;

import be.nokorbis.commandsigns.updater.data.v16x.CommandBlockGsonDeserializer;
import be.nokorbis.commandsigns.updater.data.v20x.CommandBlockGsonSerializer;
import be.nokorbis.commandsigns.updater.models.CommandBlock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.nio.charset.StandardCharsets.UTF_8;


public class Updater {

	private static final String EXTENSION = ".json";

	public static void main(String[] args) {
		File baseDir = getWorkDir();
		if (baseDir == null) { return; }

		File[] files = baseDir.listFiles((dir, name) -> name.endsWith(EXTENSION));
		if (files == null || files.length == 0) {
			System.out.println("- No files to save and convert");
			return;
		}

		if (!saveOldData(baseDir, files)) {
			return;
		}

		File execFolder = new File(baseDir, "executions");
		if (!execFolder.exists()) { execFolder.mkdirs(); }
		File confFolder = new File(baseDir, "configurations");
		if (!confFolder.exists()) { confFolder.mkdirs(); }

		GsonBuilder b1 = new GsonBuilder();
		b1.registerTypeAdapter(CommandBlock.class, new CommandBlockGsonDeserializer());
		Gson gson1 = b1.create();

		GsonBuilder b2 = new GsonBuilder();
		b2.registerTypeAdapter(CommandBlock.class, new CommandBlockGsonSerializer());
		b2.setPrettyPrinting();
		Gson gson2 = b2.create();

		try {
			for (File file : files) {
				CommandBlock commandBlock;

				File outputFile = new File(confFolder, file.getName());
				if (outputFile.exists()) {
					outputFile.createNewFile();
				}

				try (InputStream is = new FileInputStream(file);
					 InputStreamReader reader = new InputStreamReader(is, UTF_8)) {

					commandBlock = gson1.fromJson(reader, CommandBlock.class);
				}

				try (OutputStream os = new FileOutputStream(outputFile);
					 OutputStreamWriter writer = new OutputStreamWriter(os, UTF_8)) {

					String json = gson2.toJson(commandBlock);
					writer.write(json);
				}
				file.delete();
			}

			System.out.println("+ All files have been converted");
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static boolean saveOldData(File baseDir, File[] files) {
		File oldFolder = new File(baseDir, "old");
		System.out.println("+ Copying data to avoid any loss (into 'old' folder)");
		if (!oldFolder.exists()) {
			if (oldFolder.mkdirs()) {
				System.out.println("+ 'old' folder created");
			}
			else {
				System.out.println("- Was not able to create 'old' folder");
				return false;
			}
		}

		for (File dataFile : files) {
			try {
				File copiedFile = new File(oldFolder, dataFile.getName());
				if (!copiedFile.exists()) {
					copiedFile.createNewFile();
				}
				Files.copy(dataFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e) {
				System.out.println("- Was not able to copy file to save it");
				e.printStackTrace();
				return false;
			}
		}

		System.out.println("+ Filed copied");
		return true;
	}

	private static File getWorkDir() {
		File pluginsFolder = new File("plugins");
		if (!pluginsFolder.exists()) {
			System.out.println("- Plugins folder not found. Make sure you are in the right folder");
			return null;
		}
		File pluginFolder = new File(pluginsFolder, "CommandSigns");
		if (!pluginFolder.exists()) {
			System.out.println("- CommandSigns folder not found. Make sure you are in the right folder");
			return null;
		}
		File blocksFolder = new File(pluginFolder, "CommandBlocks");
		if (!blocksFolder.exists()) {
			System.out.println("- CommandBlocks folder not found. Make sure you are in the right folder");
			return null;
		}
		return blocksFolder;
	}

}
