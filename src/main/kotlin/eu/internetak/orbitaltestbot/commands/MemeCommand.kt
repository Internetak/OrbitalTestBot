package eu.internetak.orbitaltestbot.commands

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.HttpURLConnection
import java.net.URL

class MemeCommand : ListenerAdapter() {

	override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
		if (event.name == "meme") {
			val time = System.currentTimeMillis()

			val randomMemeUrl = "https://meme-api.com/gimme";

			val connection = URL(randomMemeUrl).openConnection() as HttpURLConnection
			val inputStream = connection.inputStream
			val response = inputStream.bufferedReader().readText()

			val json = Json.parseToJsonElement(response)


			event.replyEmbeds(
				EmbedBuilder()
					.setTitle("Random Meme")
					.setImage(json.jsonObject["url"].toString().replace("\"", ""))
					.build()
			).queue()
		}
	}
}