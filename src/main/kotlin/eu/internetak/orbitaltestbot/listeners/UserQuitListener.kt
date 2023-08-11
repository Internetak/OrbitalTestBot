package eu.internetak.orbitaltestbot.listeners

import eu.internetak.orbitaltestbot.Database
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class UserQuitListener(private val database: Database) : ListenerAdapter() {

	override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
		database.removeUser(event.user.id)
	}
}