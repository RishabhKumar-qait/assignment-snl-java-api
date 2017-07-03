package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BoardTest {
	Board board;

	@BeforeTest
	public void loadboard() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		board = new Board();
		// assertThat(board.getUUID().toString().length()).isEqualTo(0);
	}

	@Test
	public void register_player_should_return_list_of_registered_player()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		board = new Board();

		assertThat(board.registerPlayer("user 1").length()).isEqualTo(1);
		assertThat(board.registerPlayer("user 2").length()).isEqualTo(2);
		assertThat(board.registerPlayer("user 3").length()).isEqualTo(3);
		assertThat(board.registerPlayer("user 4").length()).isEqualTo(4);
		// assertThat(board.registerPlayer("user 5")).isEqualTo(null);

	}

	@Test
	public void delete_player_using_uuid() throws NoUserWithSuchUUIDException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, IOException {
		board = new Board();
		assertThat(board.registerPlayer("user 1").length()).isEqualTo(1);
		assertThat(board.registerPlayer("user 2").length()).isEqualTo(2);
		assertThat(board.registerPlayer("user 3").length()).isEqualTo(3);
		assertThat(board.registerPlayer("user 4").length()).isEqualTo(4);
		UUID uuid = null;
		JSONArray playerArray = new JSONArray();
		playerArray = board.getData().getJSONArray("players");

		for (int i = 0; i < playerArray.length(); i++) {
			JSONObject singlePlayer = playerArray.getJSONObject(i);
			if (singlePlayer.get("name").equals("user 2")) {

				uuid = (UUID) singlePlayer.get("uuid");
				System.out.println(uuid);
			}

		}
		System.out.println(playerArray.toString());
		board.deletePlayer(uuid);
		System.out.println(playerArray.toString());
		// board.deletePlayer(UUID.randomUUID());
		assertThat(board.getData().length()).isEqualTo(3);
		System.out.println(board.getData().length());

	}

	@Test
	public void rollDice_using_uuid() throws InvalidTurnException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {
		board = new Board();
		board.registerPlayer("user 1");
		board.registerPlayer("user 2");
		board.registerPlayer("user 3");
		board.registerPlayer("user 4");
		int w = 1;
		UUID uuid = null;
		JSONArray player = new JSONArray();
		player = board.getData().getJSONArray("players");
		int c = player.length();
		JSONObject objPlayer;

		while (c != 0) {
			for (int i = 0; i < player.length(); i++) {
				player = board.getData().getJSONArray("players");
				objPlayer = player.getJSONObject(i);

				int position = (int) objPlayer.get("position");
				String name = (String) objPlayer.get("name");
				UUID uuid1 = (UUID) objPlayer.get("uuid");

				objPlayer = player.getJSONObject(i);

				JSONObject obj = board.rollDice(uuid1);

				uuid = (UUID) objPlayer.get("uuid");
				// System.out.println(objPlayer);

				String m = (String) obj.get("message");
				String pname = (String) obj.get("playerName");
				int dice = (int) obj.get("dice");
				System.out.println(pname + " with dice " + dice + " " + m);

				if (position == 100) {
					assertThat(position).isEqualTo(100);
					System.out.println(w + " winner is " + name);
					w++;
					board.deletePlayer(uuid1);

					c--;
				}

			}

		}

	}

	@Test
	public void game_in_progress_exception()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {

		board = new Board();
		board.registerPlayer("user 1");
		board.registerPlayer("user 2");
		board.registerPlayer("user 3");
		// board.registerPlayer("user 4");
		int w = 1;
		UUID uuid = null;
		JSONArray player = new JSONArray();
		player = board.getData().getJSONArray("players");
		int c = player.length();
		JSONObject objPlayer;

		while (c != 0) {
			for (int i = 0; i < player.length(); i++) {
				player = board.getData().getJSONArray("players");
				objPlayer = player.getJSONObject(i);

				UUID uuid1 = (UUID) objPlayer.get("uuid");

				objPlayer = player.getJSONObject(i);

				JSONObject obj = board.rollDice(uuid1);

				board.registerPlayer("user 5").equals("false");

			}

		}

	}
		@Test
	public void ladder_steps() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		board = new Board();
		board.registerPlayer("User 1");
		UUID uuid = null;
		JSONArray player = new JSONArray();
		player = board.getData().getJSONArray("players");
		int c = player.length();
		JSONObject objPlayer;

		for (int i = 0; i < player.length(); i++) {
			player = board.getData().getJSONArray("players");
			objPlayer = player.getJSONObject(i);
			uuid = (UUID) objPlayer.get("uuid");

			JSONObject obj = board.rollDice(uuid);

			int dice = (int) obj.get("dice");
			System.out.println("dice is="+dice);

			int position = (int) objPlayer.get("position");
			if (obj.get("dice").equals(1)) {
				assertThat(position).isEqualTo(7);
			}
			if (obj.get("dice").equals(2)) {
				assertThat(position).isEqualTo(24);
			}
			if (obj.get("dice").equals(3)) {
				assertThat(position).isEqualTo(8);
			}
			if (obj.get("dice").equals(4)) {
				assertThat(position).isEqualTo(9);
			}
			if (obj.get("dice").equals(5)) {
				assertThat(position).isEqualTo(10);
			}
			if (obj.get("dice").equals(6)) {
				assertThat(position).isEqualTo(12);
			}

		}
	}
}
