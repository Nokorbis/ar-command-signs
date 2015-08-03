package net.avatar.realms.spigot.commandsign.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class JsonBlockSaver implements IBlockSaver{
	
	private static final String FILENAME = "commandblocks.json";
	
	private File saveFile;
	
	private GsonBuilder builder;
	
	private Type dataType = new TypeToken<List<CommandBlockData>>(){}.getType();
	
	public JsonBlockSaver (File folder) throws Exception {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		saveFile = new File(folder, FILENAME);
		if (!saveFile.exists()) {
			saveFile.createNewFile();
		}
		
		builder = new GsonBuilder().setPrettyPrinting();
	}

	@Override
	public void save(Collection<CommandBlock> commandBlocks) {
		if (saveFile == null) {
			CommandSign.getPlugin().getLogger().severe("commandblocks.json is null !");
			return;
		}
		
		List<CommandBlockData> datas = new LinkedList<CommandBlockData>();
		for (CommandBlock cmd : commandBlocks) {
			datas.add(CommandBlockData.transform(cmd));
		}
		
		Gson gson = builder.create();
		String json = gson.toJson(datas);
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(saveFile);
			writer.write(json);
		} catch (IOException e) {
			CommandSign.getPlugin().getLogger().warning("Was not able to save json file");
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public Collection<CommandBlock> load() {
		if (saveFile == null) {
			CommandSign.getPlugin().getLogger().severe("commandblocks.json is null !");
			return null;
		}
		
		FileReader reader = null;
		try {
			reader = new FileReader(saveFile);
			BufferedReader br = new BufferedReader(reader);
			Gson gson = builder.create();
			
			Collection<CommandBlockData> data = gson.fromJson(br, dataType);
			
			List<CommandBlock> blocks = new LinkedList<CommandBlock>();
			
			if (data == null) {
				return null;
			}
			for (CommandBlockData da : data) {
				blocks.add(CommandBlockData.transform(da));
			}
			
			return blocks;
		} catch (FileNotFoundException e) {
			CommandSign.getPlugin().getLogger().severe("Was not able to read json file !");
		}
		
		
		return null;
	}

}
