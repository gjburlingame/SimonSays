package me.gj.simonsays;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMovementListener implements Listener {
	private final SimonManager simonManager;
	private final Map<UUID, Long> lastPunished = new HashMap<>();

	public PlayerMovementListener(SimonManager simonManager) {
		this.simonManager = simonManager;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!simonManager.isSimonRunning() || simonManager.isMovementAllowed() || !event.hasChangedPosition()) return;

		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		long now = System.currentTimeMillis();
		long last = lastPunished.getOrDefault(uuid, 0L);

		if (now - last < 1000) return;

		lastPunished.put(uuid, now);

		player.damage(6.0);
		player.playSound(Sound.sound(
				Key.key("minecraft:entity.wither.ambient"),
				Sound.Source.PLAYER,
				1.0f,
				1.0f)
		);
	}
}
