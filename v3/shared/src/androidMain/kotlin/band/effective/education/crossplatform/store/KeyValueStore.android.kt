package band.effective.education.crossplatform.store

import android.content.Context

/**
 * Android: `SharedPreferences`, но добраться до них можно только через [Context].
 *
 * Вот она, цена платформенного кода в одну строку: у desktop и web ничего
 * подобного нет — там хранилище доступно откуда угодно, а здесь его надо
 * сначала откуда-то получить. Поэтому точка входа Android обязана положить
 * контекст в [AndroidStoreContext] до первого обращения к хранилищу.
 */
actual fun createKeyValueStore(): KeyValueStore {
    val context = AndroidStoreContext.value
        ?: error("AndroidStoreContext.value не заполнен: вызовите AndroidStoreContext.install(this) в MainActivity до setContent")
    return SharedPreferencesKeyValueStore(
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE),
    )
}

/** Единственное место, где Android-точка входа отдаёт общему коду свой контекст. */
object AndroidStoreContext {
    internal var value: Context? = null
        private set

    fun install(context: Context) {
        value = context.applicationContext
    }
}

private class SharedPreferencesKeyValueStore(
    private val prefs: android.content.SharedPreferences,
) : KeyValueStore {
    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
