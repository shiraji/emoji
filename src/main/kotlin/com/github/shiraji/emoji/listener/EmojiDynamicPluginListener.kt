package com.github.shiraji.emoji.listener

import com.github.shiraji.emoji.data.EmojiDataManager
import com.github.shiraji.emoji.service.EmojiReader
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

class EmojiDynamicPluginListener : DynamicPluginListener {

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        Notifications.Bus.notify(Notification("foo", "PluginLoaded", "pluginLoaded!!! ${pluginDescriptor.pluginId.idString}", NotificationType.INFORMATION))
        if (pluginDescriptor.pluginId.idString == "com.github.shiraji.emoji" && EmojiDataManager.emojiList.isEmpty()) {
            EmojiDataManager.emojiList.addAll(EmojiReader.loadEmoji())
        }
    }
}