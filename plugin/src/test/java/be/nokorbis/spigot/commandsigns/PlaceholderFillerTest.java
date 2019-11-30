package be.nokorbis.spigot.commandsigns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nokorbis.spigot.commandsigns.controller.executions.PlaceholderFiller;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
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
	public void testPlayerX() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos %Player_Loc_X% 7 8", "/tppos 1 7 8");
		expectedResults.put("/tppos %Player_Loc_X+9% 7 8", "/tppos 10 7 8");
		expectedResults.put("/tppos %Player_Loc_X-9% 7 8", "/tppos -8 7 8");
		expectedResults.put("/tppos %Player_Loc_X*9% 7 8", "/tppos 9 7 8");
		expectedResults.put("/tppos %Player_Loc_X/9% 7 8", "/tppos 0 7 8");
		expectedResults.put("/tppos %Player_Loc_X/0% 7 8", "/tppos 1 7 8");

		World abcWorld = mockWorldWithName("aBc");
		World defWorld = mockWorldWithName("DeF");

		Location playerLocation = mockLocation(abcWorld, 1, 2, 3);
		Location signLocation = mockLocation(defWorld, 4, 5, 6);

		Player player = mockPlayerWithNameAndLocation("Nokorbis", playerLocation);

		PlaceholderFiller filler = new PlaceholderFiller(player, signLocation);
		for (Map.Entry<String, String> entry : expectedResults.entrySet()) {
			String expected = entry.getValue();
			List<String> actual = filler.fillPlaceholders(entry.getKey());

			assertEquals(1, actual.size());
			String cmd = actual.get(0);

			assertEquals(expected, cmd);
		}
	}

	@Test
	public void testPlayerName() {
		final String command = "/warp %PlAyEr% somewhere";

		World mockedWorld = mockWorldWithName("ZZZ");
		Location playerLocation = mockLocation(mockedWorld, 0, 0, 0);
		Location signLocation = mockLocation(mockedWorld, 1, 1, 1);

		Player mockedPlayer = mockPlayerWithNameAndLocation("Noko", playerLocation);

		PlaceholderFiller filler = new PlaceholderFiller(mockedPlayer, signLocation);

		List<String> commands = filler.fillPlaceholders(command);
		assertEquals(1, commands.size());

		String cmd = commands.get(0);
		assertEquals("/warp Noko somewhere", cmd);
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
