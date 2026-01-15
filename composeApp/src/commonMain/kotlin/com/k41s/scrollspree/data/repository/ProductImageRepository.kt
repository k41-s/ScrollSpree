package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.network.ProductImageApiService
import com.k41s.scrollspree.domain.model.ProductImage
import com.k41s.scrollspree.util.NetworkResult

class ProductImageRepository (
    private val apiService: ProductImageApiService
) : BaseRepository() {

    suspend fun uploadImage(
        productId: Int,
        fileBytes: ByteArray,
        fileName: String
    ): NetworkResult<ProductImage> =
        safeApiCall {
            apiService.uploadImage(
                productId,
                fileBytes,
                fileName
            ).toDomain()
        }

    suspend fun delete(imageId: Int): NetworkResult<Boolean> =
        safeApiCall {
            apiService.deleteProductImage(imageId)
            true
        }

}