package com.github.shiraji.emoji.ext

import com.intellij.codeInsight.completion.CompletionParameters

fun CompletionParameters.findColonPosition(): Int {
    val start = this.editor.caretModel.currentCaret.offset - 1
    val end = maxOf(this.position.textRange.startOffset - 1, 0)
    val text = this.editor.document.text
    var colonPosition = -1
    loop@ for (index in start downTo end) {
        val current = text[index]
        when {
            current.isWhitespace() -> return -1
            current == ':' -> {
                colonPosition = index
                break@loop
            }
            index == end -> return -1
        }
    }
    return colonPosition
}