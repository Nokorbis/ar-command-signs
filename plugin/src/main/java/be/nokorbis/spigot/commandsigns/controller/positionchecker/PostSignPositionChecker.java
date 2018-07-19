package be.nokorbis.spigot.commandsigns.controller.positionchecker;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;


public class PostSignPositionChecker implements CommandBlockPositionChecker
{
	@Override
	public boolean isCommandBlockPosedOnBlock(BlockData relativeCommandBlockData, Block block, BlockFace blockFace)
	{
		return BlockFace.UP.equals(blockFace);
	}
}
