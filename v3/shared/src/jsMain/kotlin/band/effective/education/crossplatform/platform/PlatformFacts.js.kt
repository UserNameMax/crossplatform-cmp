package band.effective.education.crossplatform.platform

import kotlinx.browser.window

actual fun platformFacts(): PlatformFacts = object : PlatformFacts {
    override val targetName = "web (Kotlin/JS)"
    override val storageKind = "window.localStorage - файловой системы нет"
    override val hasPageAddress = true
    override val bindingKind = "JavaScript: actual - обычная функция модуля"

    override fun selfReport(): List<String> = listOf(
        "location.href = " + window.location.href,
        "Тот же браузер, что и у wasm-таргета, но другой сгенерированный код.",
    )
}
