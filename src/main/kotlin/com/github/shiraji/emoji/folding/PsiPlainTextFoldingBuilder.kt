package com.github.shiraji.emoji.folding

import com.github.shiraji.emoji.data.pattern.psiPlainTextPattern
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

class PsiPlainTextFoldingBuilder : EmojiFoldingBuilder() {
    override val place: ElementPattern<out PsiElement> = psiPlainTextPattern
}