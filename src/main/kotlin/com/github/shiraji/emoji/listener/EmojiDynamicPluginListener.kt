package com.github.shiraji.emoji.listener

import com.github.shiraji.emoji.data.EmojiDataManager
import com.github.shiraji.emoji.service.EmojiReader
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor

class EmojiDynamicPluginListener : DynamicPluginListener {

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "com.github.shiraji.emoji" && EmojiDataManager.emojiList.isEmpty()) {
            EmojiDataManager.emojiList.addAll(EmojiReader.loadEmoji())
        }
    }
}