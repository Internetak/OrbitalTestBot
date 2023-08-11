package eu.internetak.orbitaltestbot.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.format.DateTimeFormatter

class ServerInfoCommand : ListenerAdapter() {

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if (event.name == "serverinfo") {
			val server = event.guild

			if (server == null) {
				event.reply("Server not found").queue()
				return
			}

			val builder = EmbedBuilder()
				.setTitle("Server Info")
				.setThumbnail(server.iconUrl)
				.setImage(server.bannerUrl)

			builder.addField("Server Name", server.name, true)
			if (server.owner != null) {
				builder.addField("Owner", server.owner!!.effectiveName, true)
			}
			if (server.defaultChannel != null) {
				builder.addField("Default Channel", server.defaultChannel!!.jumpUrl, true)
			}
			builder.addField(
				"Created At", server.timeCreated.format(
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
				), true
			)

			val metadata = server.retrieveMetaData().complete()

			val memberCount = metadata.approximateMembers
			val onlineCount = metadata.approximatePresences


			builder.addField("Members", "Total: $memberCount | Online: $onlineCount", true)

			builder.addField("Roles (${server.roles.size})", server.roles.map { it.name }.joinToString(", "), false)

			event.replyEmbeds(builder.build()).queue()
		}
	}
}