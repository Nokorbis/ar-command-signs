package net.avatar.realms.spigot.commandsign.data;

import java.io.File;
import java.util.Collection;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class JsonBlockSaver implements IBlockSaver{
	
	private static final String FILENAME = "commandblocks.json";
	
	private File saveFile;
	
	public JsonBlockSaver (File folder) throws Exception {
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		saveFile = new File(saveFile, FILENAME);
		if (!saveFile.exists()) {
			saveFile.createNewFile();
		}
	}

	@Override
	public void save(Collection<CommandBlock> commandBlocks) {
		if (saveFile == null) {
			CommandSign.getPlugin().getLogger().severe("commandblocks.json is null !");
			return;
		}
		
	}

	@Override
	public Collection<CommandBlock> load() {
		if (saveFile == null) {
			CommandSign.getPlugin().getLogger().severe("commandblocks.json is null !");
			return null;
		}
		
		
		return null;
	}

}
