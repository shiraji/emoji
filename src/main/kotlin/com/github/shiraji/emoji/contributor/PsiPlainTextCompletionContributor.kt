package com.github.shiraji.emoji.contributor

import com.github.shiraji.emoji.data.pattern.psiPlainTextPattern
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

class PsiPlainTextCompletionContributor : EmojiCompletionContributor() {
    override val place: ElementPattern<out PsiElement> = psiPlainTextPattern
}