package eu.internetak.orbitaltestbot

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class Database {

	private var database: Connection? = null
	private val cache = mutableMapOf<String, User>()

	init {
		reconnect()
	}

	private fun reconnect() {
		if (database == null || database!!.isClosed) {
			cache.clear()
			File("data").mkdir()
			database = DriverManager.getConnection("jdbc:sqlite:data/database.db")

			val execute = database!!.createStatement().execute(
				"CREATE TABLE IF NOT EXISTS `users` (`id` TEXT, " +
						"`username` TEXT, " +
						"`registered` INTEGER, " +
						"`joined` INTEGER, " +
						"`present` BOOLEAN)"
			)
		}
	}

	fun addUser(id: String, username: String, registered: Long, joined: Long) {
		reconnect()

		if (getUser(id) == null) {
			database!!.prepareStatement("INSERT INTO `users` (`id`, `username`, `registered`, `joined`, `present`) VALUES (?, ?, ?, ?, ?)")
				.use {
					it.setString(1, id)
					it.setString(2, username)
					it.setLong(3, registered)
					it.setLong(4, joined)
					it.setBoolean(5, true)
					it.executeUpdate()
				}
		} else {
			database!!.prepareStatement("UPDATE `users` SET `present` = ? WHERE `id` = ?").use {
				it.setBoolean(1, true)
				it.setString(2, id)
				it.executeUpdate()
			}
		}

		cache.remove(id)
	}

	fun removeUser(id: String) {
		reconnect()
		database!!.prepareStatement("UPDATE `users` SET `present` = ? WHERE `id` = ?").use {
			it.setBoolean(1, false)
			it.setString(2, id)
			it.executeUpdate()
		}

		cache.remove(id)
	}

	data class User(val id: String, val username: String, val registered: Long, val joined: Long, val present: Boolean)

	fun getUser(id: String): User? {
		if (cache.containsKey(id)) {
			return cache[id]
		}
		reconnect()
		database!!.prepareStatement("SELECT * FROM `users` WHERE `id` = ?").use { stmt ->
			stmt.setString(1, id)
			stmt.executeQuery().use { result ->
				if (result.next()) {
					val user = User(
						id,
						result.getString("username"),
						result.getLong("registered"),
						result.getLong("joined"),
						result.getBoolean("present")
					)
					cache[id] = user
					return user
				}
			}
		}
		return null
	}

	fun close() {
		database!!.close()
	}
}