package com.github.shiraji.emoji.folding

import com.github.shiraji.emoji.data.EmojiDataManager
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

abstract class EmojiFoldingBuilder : FoldingBuilderEx() {

    abstract val place: ElementPattern<out PsiElement>

    // This should match with 'emojiKeyRegex' in build.gradle.kts
    private val regex = Regex(":([a-z0-9_+\\\\-]+):")

    override fun getPlaceholderText(node: ASTNode, range: TextRange): String? {
        val label = range.substring(node.text).replace(":", "")
        val unicode = EmojiDataManager.emojiList.firstOrNull { it.label == label }?.unicode ?: return null
        return " $unicode "
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val list = mutableListOf<FoldingDescriptor>()
        regex.findAll(root.text).forEach { matchResult ->
            if (matchResult.groups.size >= 2 && EmojiDataManager.emojiList.firstOrNull { it.label == matchResult.groups[1]?.value } != null) {
                val element = root.findElementAt(TextRange(matchResult.range.first, matchResult.range.last + 1)) ?: return@forEach
                if (place.accepts(element)) {
                    list.add(FoldingDescriptor(root, TextRange(matchResult.range.first, matchResult.range.last + 1)))
                }
            }
        }
        return list.toTypedArray()
    }

    private fun PsiElement.findElementAt(range: TextRange): PsiElement? {
        var element = findElementAt(range.startOffset) ?: return null
        while (!element.textRange.contains(range)) {
            if (element == this) return null
            element = element.parent
        }
        return element
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return null
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}