package me.gj.simonsays;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimonManager {
	private final SimonSays plugin;
	private final List<BukkitTask> countdownTasks = new ArrayList<>();

	public SimonManager(SimonSays plugin) {
		this.plugin = plugin;
	}

	private BukkitTask simonTask;

	private boolean simonRunning = false;
	private boolean movementAllowed = true;

	private void scheduleNextPhase() {
		long duration = ThreadLocalRandom.current().nextLong(300L, 400L);
		countdownTasks.clear();

		for (int seconds = 3; seconds > 0; seconds--) {
			final int remaining = seconds;

			BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
				Bukkit.broadcast(Component.text(
						"Switching in " + remaining + "...",
						NamedTextColor.YELLOW
				));

				playSoundToEveryone(Key.key("minecraft:ui.button.click"));

			}, duration - remaining * 20L);

			countdownTasks.add(task);
		}

		simonTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
			movementAllowed = !movementAllowed;
			announcePhase();
			scheduleNextPhase();
		}, duration);
	}

	private void playSoundToEveryone(Key key) {
		Sound sound = Sound.sound(
				key,
				Sound.Source.PLAYER,
				1.0f,
				1.0f
		);

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(sound);
		}
	}

	private void announcePhase() {
		Bukkit.broadcast(Component.text(
				movementAllowed ? "Simon says GO!" : "Simon says STOP!",
				movementAllowed ? NamedTextColor.GREEN : NamedTextColor.RED
		));

		playSoundToEveryone(Key.key(
				movementAllowed ? "minecraft:block.note_block.chime" : "minecraft:entity.warden.hurt"
		));
	}

	public void startSimon() {
		if (simonRunning) return;

		simonRunning = true;
		movementAllowed = true;

		announcePhase();
		scheduleNextPhase();
	}

	public void stopSimon() {
		if (!simonRunning) return;

		simonRunning = false;
		movementAllowed = true;

		if (simonTask != null) {
			simonTask.cancel();
			simonTask = null;
		}

		playSoundToEveryone(Key.key("minecraft:entity.allay.ambient_with_item"));
		Bukkit.broadcast(Component.text("Simon says has ended!", NamedTextColor.YELLOW));

		countdownTasks.forEach(BukkitTask::cancel);
		countdownTasks.clear();
	}

	public boolean isSimonRunning() { return simonRunning; }
	public boolean isMovementAllowed() { return movementAllowed; }
}
