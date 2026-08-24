package band.effective.education.crossplatform.store

import java.io.File
import java.util.Properties

/**
 * Desktop: обычный файл в домашней папке пользователя.
 *
 * Файловая система здесь есть, и это первое, чем desktop отличается от web:
 * там её нет вовсе, и хранилище приходится строить на другом механизме.
 */
actual fun createKeyValueStore(): KeyValueStore = FileKeyValueStore(
    File(File(System.getProperty("user.home"), ".$STORE_NAME"), "$STORE_NAME.properties"),
)

private class FileKeyValueStore(private val file: File) : KeyValueStore {

    private val properties = Properties().apply {
        if (file.isFile) file.inputStream().use { load(it) }
    }

    override fun getString(key: String): String? = properties.getProperty(key)

    override fun putString(key: String, value: String) {
        properties.setProperty(key, value)
        flush()
    }

    override fun remove(key: String) {
        properties.remove(key)
        flush()
    }

    private fun flush() {
        file.parentFile?.mkdirs()
        file.outputStream().use { properties.store(it, STORE_NAME) }
    }
}
