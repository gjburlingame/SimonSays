package me.gj.simonsays;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class SimonSays extends JavaPlugin {
	private SimonManager simonManager;

	@Override
	public void onEnable() {
		simonManager = new SimonManager(this);

		getLifecycleManager().registerEventHandler(
				LifecycleEvents.COMMANDS, event -> event.registrar().register(
						Commands.literal("simon")
								.then(Commands.literal("start")
										.executes(_ -> {
											simonManager.startSimon();

											return 1;
										}))
								.then(Commands.literal("stop")
										.executes(_ -> {
											simonManager.stopSimon();

											return 1;
										}))
								.build()
				)
		);

		getServer().getPluginManager().registerEvents(new PlayerMovementListener(simonManager), this);
	}

	@Override
	public void onDisable() {
		simonManager.stopSimon();
	}
}