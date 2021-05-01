import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

buildscript {
    repositories {
        jcenter()
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("jacoco")
    id("org.jetbrains.intellij") version "0.4.8"
}

group = "com.github.shiraji"
version = System.getProperty("VERSION") ?: "0.0.1"

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    maxHeapSize = "3g"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}

jacoco {
    toolVersion = "0.8.2"
}

val jacocoTestReport by tasks.existing(JacocoReport::class) {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

repositories {
    mavenCentral()
    jcenter()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "IU-2020.1"

    setPlugins(
            // https://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/plugin_compatibility.html
//            "Kotlin",
//            "Pythonid:2018.1.181.5087.50", // https://plugins.jetbrains.com/plugin/631-python
//            "org.jetbrains.plugins.ruby:2018.1.20180515", // https://plugins.jetbrains.com/plugin/1293-ruby
//            "yaml",
//            "org.jetbrains.plugins.go:181.5087.39.204", // https://plugins.jetbrains.com/plugin/9568-go
//            "com.jetbrains.php:181.5087.11", // https://plugins.jetbrains.com/plugin/6610-php
//            "JavaScriptLanguage",
            "markdown"
//            "Groovy",
//            "org.intellij.scala:2018.1.4", // https://plugins.jetbrains.com/plugin/1347-scala
//            "org.rust.lang:0.2.0.2107-181", // https://plugins.jetbrains.com/plugin/8182-rust
//            "CSS",
//            "java-i18n",
//            "properties",
//            "coverage"
    )
    updateSinceUntilBuild = false
}

val patchPluginXml: PatchPluginXmlTask by tasks
patchPluginXml {
    changeNotes(project.file("LATEST.txt").readText())
}

val publishPlugin: PublishTask by tasks
publishPlugin {
    token(System.getenv("HUB_TOKEN"))
    channels(System.getProperty("CHANNELS") ?: "beta")
}

dependencies {
    val kotlinVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("io.mockk:mockk:1.8.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

configurations {
    create("ktlint")

    dependencies {
        add("ktlint", "com.github.shyiko:ktlint:0.30.0")
    }
}

tasks.register("ktlintCheck", JavaExec::class) {
    description = "Check Kotlin code style."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("src/**/*.kt")
}

tasks.register("ktlintFormat", JavaExec::class) {
    description = "Fix Kotlin code style deviations."
    classpath = configurations["ktlint"]
    main = "com.github.shyiko.ktlint.Main"
    args("-F", "src/**/*.kt")
}

tasks.register("resolveDependencies") {
    doLast {
        project.rootProject.allprojects.forEach {subProject ->
            subProject.buildscript.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
            subProject.configurations.forEach {configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
        }
    }
}

tasks.register("downloadEmoji") {
    doFirst {
        val file = File("${project.rootDir.absolutePath}/src/main/resources/emoji.json")
        if (!file.exists()) {
            val bytes = URL("https://api.github.com/emojis").readBytes()
            file.writeBytes(bytes)
        }
        // Waiting for response from GitHub
        // They don't open a license of using emoji images.
//        val map = JsonSlurper().parse(file) as Map<*, *>
//        map.forEach { item ->
//            val url = URL(item.value.toString())
//            val icon = File("src/main/resources/icons/${item.key}.png")
//            if (!icon.exists()) {
//                icon.writeBytes(url.readBytes())
//            }
//        }
    }
}

val compileKotlin by tasks.existing {
    dependsOn("downloadEmoji")
}

tasks.register("deleteResourceJson") {
    delete("${project.rootDir.absolutePath}/src/main/resources/emoji.json")
}

val clean by tasks.existing {
    dependsOn("deleteResourceJson")
}

tasks.register("verifyEmojiFile") {
    doFirst {
        val file = File("${project.rootDir.absolutePath}/src/main/resources/emoji.json")
        check(file.exists())

        // This must matches with code inside project.
        // TODO: make it common variables
        val emojiKeyRegex = "^[a-z0-9_+\\\\-]+\$"
        val map = groovy.json.JsonSlurper().parse(file) as Map<*, *>

        val result = map.filterKeys { item ->
            !item.toString().matches(Regex(emojiKeyRegex))
        }
        check(result.isEmpty()) {
            println("Invalid key found. '${result.keys.joinToString()}'")
            println("Make sure all emoji keys match '$emojiKeyRegex'")
        }
    }
}

val verifyPlugin by tasks.existing {
    dependsOn("verifyEmojiFile")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
