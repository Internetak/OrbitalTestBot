package eu.internetak.orbitaltestbot

import eu.internetak.orbitaltestbot.commands.MemeCommand
import eu.internetak.orbitaltestbot.commands.PingCommand
import eu.internetak.orbitaltestbot.commands.ServerInfoCommand
import eu.internetak.orbitaltestbot.commands.UserInfoCommand
import eu.internetak.orbitaltestbot.listeners.UserJoinListener
import eu.internetak.orbitaltestbot.listeners.UserQuitListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.util.*

fun main(args: Array<String>) {

	val jdaBuilder =
		JDABuilder.createDefault("MTEzODkxMzQxODMzMDUwOTQwNA.GLQTk3.XdTU5F0BdK4yheroxejRXz-un-e0fvzz_0YTTU")
	val database = Database()

	jdaBuilder.addEventListeners(
		MemeCommand(),
		PingCommand(),
		ServerInfoCommand(),
		UserInfoCommand(database),
		UserJoinListener(database),
		UserQuitListener(database)
	)

	jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)

	jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ONLINE)

	val jda = jdaBuilder.build()

	jda.updateCommands().addCommands(
		Commands.slash("meme", "Shows a random meme"),
		Commands.slash("ping", "Ping"),
		Commands.slash("serverinfo", "Shows server info"),
		Commands.slash("userinfo", "Shows user info")
			.addOption(OptionType.USER, "user", "The user to get info about")
	).queue()

	println("Type 'exit' to exit")

	val scanner = Scanner(System.`in`)

	while (scanner.hasNextLine()) {
		val line = scanner.nextLine()
		if (line == "exit") {
			println("Stopping...")
			database.close()
			jda.shutdown()
			println("Bye!")
			scanner.close()
			break
		}
	}

}