package com.github.shiraji.emoji.data.pattern

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFenceImpl
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile

val markdownPattern: ElementPattern<out PsiElement> = PlatformPatterns.psiElement().inside(MarkdownFile::class.java)
        .andNot(PlatformPatterns.psiElement().inside(MarkdownCodeFenceImpl::class.java))