package com.github.shiraji.emoji.folding

import com.github.shiraji.emoji.data.pattern.markdownPattern
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

class MarkdownTextFoldingBuilder : EmojiFoldingBuilder() {
    override val place: ElementPattern<out PsiElement> = markdownPattern
}