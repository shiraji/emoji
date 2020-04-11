package com.github.shiraji.emoji.contributor

import com.github.shiraji.emoji.data.pattern.markdownPattern

class MarkdownEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = markdownPattern
}
