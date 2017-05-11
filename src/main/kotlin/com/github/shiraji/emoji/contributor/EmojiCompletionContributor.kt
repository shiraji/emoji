package com.github.shiraji.emoji.contributor

import com.github.shiraji.emoji.data.EmojiData
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.IconLoader
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiPlainText
import com.intellij.util.ProcessingContext
import java.io.File

class EmojiCompletionContributor : CompletionContributor() {

    val emojiList = mutableListOf<EmojiData>()

    init {
        File(javaClass.getResource("/icons").toURI()).list()
                .filterNot { it.contains("@2") }
                .forEach {
                    val icon = IconLoader.getIcon("/icons/$it")
                    emojiList.add(EmojiData(it.replaceAfter(".", "").replace(".", ""), icon))
                }

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiPlainText::class.java), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                if (parameters.editor.isOneLineMode) return
                val message = parameters.editor.document.charsSequence.toString()
                val colonPosition = message.lastIndexOf(":", parameters.offset)
                if (colonPosition < 0) return
                val spacePosition = message.lastIndexOf(" ", parameters.offset)
                if (spacePosition > colonPosition) return
                emojiList.forEach {
                    result.addElement(LookupElementBuilder.create(it.emojiText, ":${it.emojiText}: ")
                            .withIcon(it.icon)
                            .withInsertHandler { insertionContext, lookupElement ->
                                val startOffset = insertionContext.startOffset
                                val document = insertionContext.document
                                if (startOffset > 0 && document.charsSequence[startOffset - 1] == ':') {
                                    document.deleteString(startOffset - 1, startOffset)
                                }
                            }
                    )
                }
            }
        })
    }
}