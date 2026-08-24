import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "band.effective.education.crossplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "band.effective.education.crossplatform"
            packageVersion = "1.0.0"
        }
    }
}
// Снимок стенда без окна — страховка к паре и прогон демо руками.
tasks.register<JavaExec>("renderCheck") {
    group = "verification"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("band.effective.education.crossplatform.RenderCheckKt")
}
