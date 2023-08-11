package eu.internetak.orbitaltestbot.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PingCommand : ListenerAdapter() {

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if (event.name == "ping") {
			val time = System.currentTimeMillis()
			event.reply("Ping...").queue { e ->
				e.editOriginal("Pong! ${(System.currentTimeMillis() - time) / 1000.0}s").queue()
			}
		}
	}
}