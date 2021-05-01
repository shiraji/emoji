package com.github.shiraji.emoji.action

import com.github.shiraji.emoji.data.EmojiDataManager
import com.github.shiraji.emoji.service.EmojiReader
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.application.ex.ApplicationUtil.runWithCheckCanceled
import com.intellij.openapi.progress.ProgressIndicator

class EmojiPreloadingActivity : PreloadingActivity() {
    override fun preload(indicator: ProgressIndicator) {
        Notifications.Bus.notify(Notification("foo", "PluginLoaded", "EmojiPreloadingActivity", NotificationType.INFORMATION))
        EmojiDataManager.emojiList.clear()
        EmojiDataManager.emojiList.addAll(runWithCheckCanceled({ EmojiReader.loadEmoji() }, indicator))
    }
}