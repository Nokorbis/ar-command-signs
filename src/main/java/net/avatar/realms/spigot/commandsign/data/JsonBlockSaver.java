package net.avatar.realms.spigot.commandsign.data;

import java.io.*;
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
	private static final String CHARSET = "UTF-8";
	
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

		OutputStreamWriter osWriter = null;
		try {
			OutputStream os = new FileOutputStream(saveFile);
			osWriter = new OutputStreamWriter(os, CHARSET);
			osWriter.write(json);
		} catch (IOException e) {
			CommandSign.getPlugin().getLogger().warning("Was not able to save json file");
		}
		finally {
			if (osWriter != null) {
				try {
					osWriter.close();
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
		
		InputStreamReader reader = null;
		try {
			InputStream is = new FileInputStream(saveFile);
			try {
				reader = new InputStreamReader(is, CHARSET);
			}
			catch (UnsupportedEncodingException e) {
			}
			BufferedReader br = new BufferedReader(reader);
			Gson gson = builder.create();
			
			Collection<CommandBlockData> data = gson.fromJson(br, dataType);
			
			List<CommandBlock> blocks = new LinkedList<CommandBlock>();
			
			if (data == null) {
				return null;
			}
			for (CommandBlockData da : data) {
				CommandBlock b = CommandBlockData.transform(da);
				if (b != null) {
					blocks.add(b);
				}
			}
			
			return blocks;
		} catch (FileNotFoundException e) {
			CommandSign.getPlugin().getLogger().severe("Was not able to read json file !");
		}
		
		
		return null;
	}

}
