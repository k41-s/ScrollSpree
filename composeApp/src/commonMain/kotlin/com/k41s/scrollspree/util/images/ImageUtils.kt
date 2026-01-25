package com.k41s.scrollspree.util.images

fun detectMimeType(bytes: ByteArray): String {
    if (bytes.size < 4) return "image/jpeg"

    return when {
        // PNG: 89 50 4E 47
        bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() -> "image/png"

        // JPEG: FF D8 FF
        bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() -> "image/jpeg"

        // GIF: 47 49 46 38
        bytes[0] == 0x47.toByte() && bytes[1] == 0x49.toByte() -> "image/gif"

        // WEBP: RIFF (bytes 0-3) and WEBP (bytes 8-11)
        bytes.size > 12 &&
                bytes[0] == 'R'.code.toByte() && bytes[8] == 'W'.code.toByte() && bytes[11] == 'P'.code.toByte() -> "image/webp"

        else -> "image/jpeg"
    }
}