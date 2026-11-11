package me.gj.simonsays;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SimonManager {
	private final SimonSays plugin;

	public SimonManager(SimonSays plugin) {
		this.plugin = plugin;
	}

	private BukkitTask simonTask;

	private boolean simonRunning = false;
	private boolean movementAllowed = true;

	public boolean isSimonRunning() { return simonRunning; }
	public boolean isMovementAllowed() { return movementAllowed; }

	public void startSimon() {
		if (simonRunning) return;

		simonRunning = true;
		movementAllowed = true;

		Bukkit.broadcast(Component.text("Simon says GO!", NamedTextColor.GREEN));

		simonTask = new BukkitRunnable() {
			@Override
			public void run() {
				movementAllowed = !movementAllowed;

				Bukkit.broadcast(Component.text(
						movementAllowed ? "Simon says GO!" : "Simon says STOP!",
						movementAllowed ? NamedTextColor.GREEN : NamedTextColor.RED
				));
			}
		}.runTaskTimer(plugin, 600L, 600L);
	}

	public void stopSimon() {
		if (!simonRunning) return;

		simonRunning = false;
		movementAllowed = true;

		if (simonTask != null) {
			simonTask.cancel();
			simonTask = null;
		}
	}
}
