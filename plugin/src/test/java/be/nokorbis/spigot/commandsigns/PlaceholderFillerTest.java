package be.nokorbis.spigot.commandsigns;

import java.util.List;

import be.nokorbis.spigot.commandsigns.controller.executions.PlaceholderFiller;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class PlaceholderFillerTest {

	/*
	To test:
	- Radius
	- All
	- Player
	- Player world
	- Player X
	- Player Y
	- Player Z
	- Sign world
	- Sign X
	- Sign Y
	- Sign Z
	 */

	@Test
	public void testPlayerName() {
		String command = "/warp %PlAyEr% somewhere";

		World mockedWorld = mockWorldWithName("ZZZ");
		Location playerLocation = mockLocation(mockedWorld, 0, 0, 0);
		Location signLocation = mockLocation(mockedWorld, 1, 1, 1);

		Player mockedPlayer = mockPlayerWithNameAndLocation("Noko", playerLocation);

		PlaceholderFiller filler = new PlaceholderFiller(mockedPlayer, signLocation);

		List<String> commands = filler.fillPlaceholders(command);
		Assert.assertEquals(1, commands.size());

		String cmd = commands.get(0);
		Assert.assertEquals("/warp Noko somewhere", cmd);
	}

	private World mockWorldWithName(final String name) {
		World mockedWorld = mock(World.class);
		when(mockedWorld.getName()).thenReturn(name);
		return mockedWorld;
	}

	private Player mockPlayerWithNameAndLocation (final String name, final Location playerLocation) {
		Player mockedPlayer = mock(Player.class);
		when(mockedPlayer.getName()).thenReturn(name);
		when(mockedPlayer.getLocation()).thenReturn(playerLocation);
		return mockedPlayer;
	}

	private Location mockLocation(final World world, final int x, final int y, final int z) {
		Location location = mock(Location.class);

		when(location.getWorld()).thenReturn(world);
		when(location.getBlockX()).thenReturn(x);
		when(location.getBlockY()).thenReturn(y);
		when(location.getBlockZ()).thenReturn(z);

		return location;
	}
}
