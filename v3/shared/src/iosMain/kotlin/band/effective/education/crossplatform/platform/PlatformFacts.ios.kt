package band.effective.education.crossplatform.platform

import platform.UIKit.UIDevice

/**
 * Kotlin/Native. Виртуальной машины нет: общий Kotlin компилируется в машинный
 * код заранее, и `actual` - это символ в бинарнике, а не метод, который кто-то
 * ищет во время работы.
 */
actual fun platformFacts(): PlatformFacts = object : PlatformFacts {
    override val targetName =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion()
    override val storageKind = "NSUserDefaults"
    override val hasPageAddress = false
    override val bindingKind = "Kotlin/Native: символ в нативном бинарнике, без ВМ"

    override fun selfReport(): List<String> = listOf(
        "Рефлексии, которая перечислила бы методы, здесь нет - и это не упущение:",
        "Kotlin/Native выбрасывает всё, до чего не дотянулись из кода, ещё при линковке.",
        "Спросить бинарник о самом себе можно снаружи: nm по .framework.",
        "device = " + UIDevice.currentDevice.model,
    )
}
