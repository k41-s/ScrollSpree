package com.k41s.scrollspree.util

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ShareManager() {
    fun shareProduct(name: String, price: Double)
}