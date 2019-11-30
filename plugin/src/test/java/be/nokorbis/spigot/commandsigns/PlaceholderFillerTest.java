package be.nokorbis.spigot.commandsigns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nokorbis.spigot.commandsigns.controller.executions.PlaceholderFiller;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class PlaceholderFillerTest {

	/*
	To test:
	- Radius
	- All
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
	public void testPlayerY() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos 7 %Player_Loc_Y% 8", "/tppos 7 2 8");
		expectedResults.put("/tppos 7 %Player_Loc_Y+9% 8", "/tppos 7 11 8");
		expectedResults.put("/tppos 7 %Player_Loc_Y-9% 8", "/tppos 7 -7 8");
		expectedResults.put("/tppos 7 %Player_Loc_Y*9% 8", "/tppos 7 18 8");
		expectedResults.put("/tppos 7 %Player_Loc_Y/9% 8", "/tppos 7 0 8");
		expectedResults.put("/tppos 7 %Player_Loc_Y/0% 8", "/tppos 7 2 8");

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
	public void testPlayerZ() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos 7 8 %Player_Loc_Z%", "/tppos 7 8 3");
		expectedResults.put("/tppos 7 8 %Player_Loc_Z+9%", "/tppos 7 8 12");
		expectedResults.put("/tppos 7 8 %Player_Loc_Z-9%", "/tppos 7 8 -6");
		expectedResults.put("/tppos 7 8 %Player_Loc_Z*9%", "/tppos 7 8 27");
		expectedResults.put("/tppos 7 8 %Player_Loc_Z/9%", "/tppos 7 8 0");
		expectedResults.put("/tppos 7 8 %Player_Loc_Z/0%", "/tppos 7 8 3");

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
	public void testPlayerWorld() {
		World abcWorld = mockWorldWithName("aBc");
		World defWorld = mockWorldWithName("DeF");

		Location playerLocation = mockLocation(abcWorld, 1, 2, 3);
		Location signLocation = mockLocation(defWorld, 4, 5, 6);

		Player player = mockPlayerWithNameAndLocation("Nokorbis", playerLocation);

		PlaceholderFiller filler = new PlaceholderFiller(player, signLocation);
		List<String> commands = filler.fillPlaceholders("/warp %pLaYeR_loc_WORLD%");

		assertEquals(1, commands.size());

		String cmd = commands.get(0);
		assertEquals("/warp aBc", cmd);
	}

	@Test
	public void testSignX() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos %Sign_Loc_X% 7 8", "/tppos 4 7 8");
		expectedResults.put("/tppos %Sign_Loc_X+9% 7 8", "/tppos 13 7 8");
		expectedResults.put("/tppos %Sign_Loc_X-9% 7 8", "/tppos -5 7 8");
		expectedResults.put("/tppos %Sign_Loc_X*9% 7 8", "/tppos 36 7 8");
		expectedResults.put("/tppos %Sign_Loc_X/9% 7 8", "/tppos 0 7 8");
		expectedResults.put("/tppos %Sign_Loc_X/0% 7 8", "/tppos 4 7 8");

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
	public void testSignY() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos 7 %Sign_Loc_Y% 8", "/tppos 7 5 8");
		expectedResults.put("/tppos 7 %Sign_Loc_Y+9% 8", "/tppos 7 14 8");
		expectedResults.put("/tppos 7 %Sign_Loc_Y-9% 8", "/tppos 7 -4 8");
		expectedResults.put("/tppos 7 %Sign_Loc_Y*9% 8", "/tppos 7 45 8");
		expectedResults.put("/tppos 7 %Sign_Loc_Y/9% 8", "/tppos 7 0 8");
		expectedResults.put("/tppos 7 %Sign_Loc_Y/0% 8", "/tppos 7 5 8");

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
	public void testSignZ() {
		Map<String, String> expectedResults = new HashMap<>();
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z%", "/tppos 7 8 6");
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z+9%", "/tppos 7 8 15");
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z-9%", "/tppos 7 8 -3");
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z*9%", "/tppos 7 8 54");
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z/9%", "/tppos 7 8 0");
		expectedResults.put("/tppos 7 8 %Sign_Loc_Z/0%", "/tppos 7 8 6");

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
	public void testSignWorld() {
		World abcWorld = mockWorldWithName("aBc");
		World defWorld = mockWorldWithName("DeF");

		Location playerLocation = mockLocation(abcWorld, 1, 2, 3);
		Location signLocation = mockLocation(defWorld, 4, 5, 6);

		Player player = mockPlayerWithNameAndLocation("Nokorbis", playerLocation);

		PlaceholderFiller filler = new PlaceholderFiller(player, signLocation);
		List<String> commands = filler.fillPlaceholders("/warp %sIgN_loc_WORLD%");

		assertEquals(1, commands.size());

		String cmd = commands.get(0);
		assertEquals("/warp DeF", cmd);
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
