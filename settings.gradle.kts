rootProject.name = "stock-exchange"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version (kotlinVersion)
    }
}

include(":core")
include(":client")

