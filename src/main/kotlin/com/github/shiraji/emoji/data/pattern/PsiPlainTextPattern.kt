package com.github.shiraji.emoji.data.pattern

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPlainText

val psiPlainTextPattern: ElementPattern<out PsiElement> = PlatformPatterns.psiElement(PsiPlainText::class.java)