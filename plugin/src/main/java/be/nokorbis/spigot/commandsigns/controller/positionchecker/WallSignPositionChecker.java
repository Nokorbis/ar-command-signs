package be.nokorbis.spigot.commandsigns.controller.positionchecker;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;


public class WallSignPositionChecker implements CommandBlockPositionChecker
{
	@Override
	public boolean isCommandBlockPosedOnBlock(BlockData relativeCommandBlockData, Block block, BlockFace blockFace)
	{
		return ((WallSign)relativeCommandBlockData).getFacing().equals(blockFace);
	}
}
