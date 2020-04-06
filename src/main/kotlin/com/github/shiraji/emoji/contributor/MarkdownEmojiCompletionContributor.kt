package com.github.shiraji.emoji.contributor

import com.intellij.patterns.PlatformPatterns
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile

class MarkdownEmojiCompletionContributor : EmojiCompletionContributor() {
    override val place = PlatformPatterns.psiElement().inside(MarkdownFile::class.java)
            .andNot(PlatformPatterns.psiElement().inside(MarkdownCodeFenceImpl::class.java))
}
