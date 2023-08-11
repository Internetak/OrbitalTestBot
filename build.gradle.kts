plugins {
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.serialization") version "1.9.0"
}

group = "eu.internetak"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(kotlin("test"))
	implementation("net.dv8tion:JDA:5.0.0-beta.13")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0-RC")
	implementation("org.xerial:sqlite-jdbc:3.42.0.0")
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(8)
}