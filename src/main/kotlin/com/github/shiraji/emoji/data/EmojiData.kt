package com.github.shiraji.emoji.data

import javax.swing.Icon

data class EmojiData(
    val label: String,
    val url: String,
    val unicode: String?,
    val icon: Icon?
)