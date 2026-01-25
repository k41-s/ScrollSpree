package com.k41s.scrollspree.util

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ShareManager actual constructor() {
    actual fun shareProduct(name: String, price: Double) {
        val message = "Check out $name for only $$price on ScrollSpree!"

        val activityViewController = UIActivityViewController(
            activityItems = listOf(message),
            applicationActivities = null
        )

        val rootViewController =
            UIApplication.sharedApplication.keyWindow?.rootViewController

        activityViewController.popoverPresentationController?.apply {
            sourceView = rootViewController?.view
        }

        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null
        )
    }
}