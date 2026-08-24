@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package band.effective.education.crossplatform.platform

/**
 * Kotlin/Wasm. Общий Kotlin компилируется в модуль WebAssembly, а всё, что
 * написано через `js("...")`, становится **строкой JavaScript в таблице
 * импортов** этого модуля. Её видно глазами в собранном бандле — и это лучший
 * артефакт лекции: инструмент, описывающий сам себя.
 */
actual fun platformFacts(): PlatformFacts = object : PlatformFacts {
    override val targetName = "web (Kotlin/Wasm) · " + userAgentShort()
    override val storageKind = "window.localStorage - файловой системы нет"
    override val hasPageAddress = true
    override val bindingKind = "WebAssembly: actual - функция модуля; js(...) - импорт из JS"

    override fun selfReport(): List<String> = listOf(
        "location.href = " + currentHref(),
        "localStorage доступен: " + hasLocalStorage(),
        "WebAssembly в этом браузере: " + hasWebAssembly(),
        "Каждая js(\"...\") функция уехала в importObject бандла отдельной строкой.",
    )
}

private fun userAgentShort(): String = js("navigator.userAgent.split(' ').slice(-2).join(' ')")
private fun currentHref(): String = js("window.location.href")
private fun hasLocalStorage(): Boolean = js("typeof window.localStorage !== 'undefined'")
private fun hasWebAssembly(): Boolean = js("typeof WebAssembly !== 'undefined'")
