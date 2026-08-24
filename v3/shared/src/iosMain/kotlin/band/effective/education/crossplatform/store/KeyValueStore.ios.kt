package band.effective.education.crossplatform.store

import platform.Foundation.NSUserDefaults

/**
 * iOS: `NSUserDefaults` — системный ключ→строка, ровно та же роль, что у
 * `SharedPreferences` на Android.
 *
 * В курсе iOS — бонус, но `actual` здесь обязателен: без него не соберётся
 * **весь** проект, а не только iOS-таргет. Это и есть разница с рантайм-мостом:
 * пропущенная реализация — ошибка компиляции.
 */
actual fun createKeyValueStore(): KeyValueStore = UserDefaultsKeyValueStore

private object UserDefaultsKeyValueStore : KeyValueStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String): String? = defaults.stringForKey(scoped(key))
    override fun putString(key: String, value: String) = defaults.setObject(value, scoped(key))
    override fun remove(key: String) = defaults.removeObjectForKey(scoped(key))

    private fun scoped(key: String) = "$STORE_NAME.$key"
}
