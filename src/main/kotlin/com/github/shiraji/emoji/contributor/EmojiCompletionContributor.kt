package com.github.shiraji.emoji.contributor

import com.github.shiraji.emoji.data.EmojiData
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiPlainText
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.UIUtil
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class EmojiCompletionContributor : CompletionContributor() {

    val emojiList = mutableListOf<EmojiData>()

    init {
        val url = javaClass.getResource("/emoji/public/graphics/emojis")
        val emojiDir = File(url.toURI())
        emojiDir.list().forEach {
            val image = ImageIO.read(File(emojiDir.absolutePath + "/" + it))
            val size = if (UIUtil.isRetina()) 32 else 16
            val scaledImage = image.getScaledInstance(size, size, Image.SCALE_SMOOTH)
            emojiList.add(EmojiData(":${it.replaceAfter(".", "").replace(".", "")}:", ImageIcon(scaledImage)))
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