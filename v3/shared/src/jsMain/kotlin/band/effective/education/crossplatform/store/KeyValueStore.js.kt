package band.effective.education.crossplatform.store

import kotlinx.browser.localStorage

/**
 * Legacy-JS таргет. Хранилище то же самое, что и у wasm, — `localStorage`, —
 * а код другой: у `js` есть типизированные обёртки `kotlinx.browser`, у `wasmJs`
 * до них надо дотягиваться через `js("…")`.
 *
 * Это ровно то, за что платят в KMP: одинаковое поведение не значит одинаковый
 * исходник, и граница проходит по таргету, а не по «платформе» в бытовом смысле.
 */
actual fun createKeyValueStore(): KeyValueStore = LocalStorageKeyValueStore

private object LocalStorageKeyValueStore : KeyValueStore {
    override fun getString(key: String): String? = localStorage.getItem(scoped(key))
    override fun putString(key: String, value: String) = localStorage.setItem(scoped(key), value)
    override fun remove(key: String) = localStorage.removeItem(scoped(key))

    private fun scoped(key: String) = "$STORE_NAME.$key"
}
