package eu.internetak.orbitaltestbot.commands

import eu.internetak.orbitaltestbot.Database
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class UserInfoCommand(private val database: Database) : ListenerAdapter() {

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if (event.name == "userinfo") {
			val userOption = event.getOption("user")

			val user = userOption?.asUser ?: event.user
			val member = userOption?.asMember ?: event.member

			val dbUser = database.getUser(user.id)

			val builder = EmbedBuilder()
				.setTitle("User Info")
				.setThumbnail(user.effectiveAvatarUrl)

			builder.addField("Username", user.effectiveName, true)
			builder.addField("User ID", user.id, true)
			if (dbUser == null) {
				builder.addField(
					"Registered At", user.timeCreated.format(
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
					), true
				)
			} else {
				builder.addField(
					"Registered At",
					Instant.ofEpochSecond(dbUser.registered)
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime().format(
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
						),
					true
				)
			}
			if (member != null) {
				builder.addField("Roles (${member.roles.size})", member.roles.map { it.name }.joinToString(", "), false)
				if (dbUser != null && dbUser.present) {
					builder.addField(
						"Joined At",
						Instant.ofEpochSecond(dbUser.joined)
							.atZone(ZoneId.systemDefault())
							.toLocalDateTime().format(
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
							),
						true
					)
				}

				builder.setColor(member.color)
			}

			event.replyEmbeds(builder.build()).queue()
		}
	}
}