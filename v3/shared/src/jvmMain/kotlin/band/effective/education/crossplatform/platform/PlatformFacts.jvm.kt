package band.effective.education.crossplatform.platform

actual fun platformFacts(): PlatformFacts = object : PlatformFacts {
    override val targetName = "JVM desktop · " + System.getProperty("java.vm.name")
    override val storageKind = "java.io.File + java.util.Properties"
    override val hasPageAddress = false
    override val bindingKind = "JVM-байткод: actual — обычный метод класса"

    /**
     * Рефлексия здесь не потому, что так надо писать код, а потому, что это
     * единственный способ спросить у самой машины, во что превратился `actual`.
     * На Kotlin/Native такого способа нет вовсе — и это тоже факт про таргет.
     */
    override fun selfReport(): List<String> {
        val cls = Class.forName("band.effective.education.crossplatform.platform.PlatformFacts_jvmKt")
        return buildList {
            add("Class.forName(\"...PlatformFacts_jvmKt\") нашёл класс:")
            cls.declaredMethods.sortedBy { it.name }.forEach { m ->
                add("  " + m.returnType.simpleName + " " + m.name + "()")
            }
            add("java.home = " + System.getProperty("java.home"))
        }
    }
}
