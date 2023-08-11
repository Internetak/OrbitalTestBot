package eu.internetak.orbitaltestbot.listeners

import eu.internetak.orbitaltestbot.Database
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class UserJoinListener(private val database: Database) : ListenerAdapter() {

	override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
		val user = event.user
		val member = event.member

		database.addUser(
			user.id,
			user.name,
			user.timeCreated.toEpochSecond(),
			member.timeJoined.toEpochSecond()
		)
	}
}