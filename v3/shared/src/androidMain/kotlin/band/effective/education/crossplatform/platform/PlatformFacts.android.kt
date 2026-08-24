package band.effective.education.crossplatform.platform

import android.os.Build

actual fun platformFacts(): PlatformFacts = object : PlatformFacts {
    override val targetName = "Android " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")"
    override val storageKind = "SharedPreferences - и до них нужен Context"
    override val hasPageAddress = false
    override val bindingKind = "DEX-байткод: actual - обычный метод класса"

    override fun selfReport(): List<String> = listOf(
        "Build.SUPPORTED_ABIS = " + Build.SUPPORTED_ABIS.joinToString(),
        "Build.FINGERPRINT = " + Build.FINGERPRINT,
        "ART исполняет байткод, как и JVM-таргет, но собран он отдельно.",
    )
}
