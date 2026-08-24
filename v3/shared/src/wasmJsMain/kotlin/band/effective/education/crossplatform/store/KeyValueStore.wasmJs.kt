@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package band.effective.education.crossplatform.store

/**
 * Web: файловой системы нет, зато есть `localStorage` — тот же самый ключ→строка,
 * только живёт он в браузере и привязан к origin.
 *
 * Ограничения, о которых стоит знать заранее: около 5 МБ на origin, всё синхронно,
 * и приватный режим может отдать пустое хранилище на каждый запуск.
 */
actual fun createKeyValueStore(): KeyValueStore = LocalStorageKeyValueStore

private object LocalStorageKeyValueStore : KeyValueStore {
    override fun getString(key: String): String? = localStorageGetItem(scoped(key))
    override fun putString(key: String, value: String) = localStorageSetItem(scoped(key), value)
    override fun remove(key: String) = localStorageRemoveItem(scoped(key))

    private fun scoped(key: String) = "$STORE_NAME.$key"
}

private fun localStorageGetItem(key: String): String? = js("window.localStorage.getItem(key)")

private fun localStorageSetItem(key: String, value: String) {
    js("window.localStorage.setItem(key, value)")
}

private fun localStorageRemoveItem(key: String) {
    js("window.localStorage.removeItem(key)")
}
