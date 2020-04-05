package com.github.shiraji.emoji.contributor

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPlainText

class PsiPlainTextCompletionContributor : EmojiCompletionContributor() {
    override val place: ElementPattern<out PsiElement> = PlatformPatterns.psiElement(PsiPlainText::class.java)
}