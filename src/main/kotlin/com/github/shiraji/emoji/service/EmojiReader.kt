package com.github.shiraji.emoji.service

import com.github.shiraji.emoji.data.EmojiData
import com.google.gson.Gson
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.util.IconUtil
import com.intellij.util.ui.JBUI
import javax.swing.JLabel

object EmojiReader {
    fun loadEmoji(): List<EmojiData> {
        Notifications.Bus.notify(Notification("foo", "PluginLoaded", "Load emoji", NotificationType.INFORMATION))
        javaClass.getResourceAsStream("/emoji.json").use { inputStream ->
            val text = inputStream.bufferedReader().readText()
            return Gson().fromJson(text, Map::class.java).map {
                val url = it.value.toString()
                // e.g. https://github.githubassets.com/images/icons/emoji/unicode/1f1e6-1f1eb.png?v8
                val hex = url.substringAfterLast("/").substringBefore(".")
                try {
                    val codePoints = hex.split("-").map { code -> Integer.decode("0x$code") }.toIntArray()
                    val unicode = String(codePoints, 0, codePoints.size)
                    val icon = if (codePoints.size == 1) IconUtil.textToIcon(unicode, JLabel(), JBUI.scale(11f)) else null
                    EmojiData(label = it.key.toString(), url = url, icon = icon, unicode = unicode)
                } catch (e: NumberFormatException) {
                    // Emoji without unicode URL. e.g. GitHub original emoji
                    EmojiData(label = it.key.toString(), url = url, icon = null, unicode = null)
                }
            }
        }
    }
}