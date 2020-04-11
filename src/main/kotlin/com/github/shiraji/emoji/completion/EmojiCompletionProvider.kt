package com.github.shiraji.emoji.completion

import com.github.shiraji.emoji.data.EmojiDataManager
import com.github.shiraji.emoji.ext.findColonPosition
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class EmojiCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        if (parameters.editor.isOneLineMode) return
        val colonPosition = parameters.findColonPosition()
        if (colonPosition < 0) return

        EmojiDataManager.emojiList.forEach {
            result.addElement(LookupElementBuilder.create(":${it.label}: ${it.unicode ?: ""}")
                    .withIcon(it.icon)
                    .withInsertHandler { insertionContext, _ ->
                        insertionContext.document.replaceString(colonPosition, insertionContext.tailOffset, ":${it.label}: ")
                    }
            )
        }
    }
}