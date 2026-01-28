package com.k41s.scrollspree.util

import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ShareManager : KoinComponent {
    private val context: Context by inject()

    actual fun shareProduct(name: String, price: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out $name for only $price on ScrollSpree!"
            )
            type = "text/plain"
        }

        val shareIntent =
            Intent.createChooser(sendIntent, "Share via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}