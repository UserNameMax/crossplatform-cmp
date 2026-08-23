package band.effective.education.crossplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform