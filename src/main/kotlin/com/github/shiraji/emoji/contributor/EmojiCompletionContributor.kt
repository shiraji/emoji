package com.github.shiraji.emoji.contributor

import com.github.shiraji.emoji.data.EmojiCompletionProvider
import com.github.shiraji.emoji.ext.findColonPosition
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

abstract class EmojiCompletionContributor(
    private val provider: CompletionProvider<CompletionParameters> = EmojiCompletionProvider()
) : CompletionContributor() {
    abstract val place: ElementPattern<out PsiElement>

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        if (parameters.completionType == CompletionType.BASIC && place.accepts(parameters.position)) {
            val colonPosition = parameters.findColonPosition()
            val newResult = if (colonPosition >= 0) {
                val prefix = parameters.editor.document.getText(TextRange(colonPosition, parameters.editor.caretModel.currentCaret.offset))
                result.withPrefixMatcher(prefix)
            } else {
                result
            }
            provider.addCompletionVariants(parameters, ProcessingContext(), newResult)
        }
    }
}