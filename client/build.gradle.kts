description = "client"

plugins {
    application
    id("org.openjfx.javafxplugin") version ("0.0.9")
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClassName = "pl.agh.StockExchangeUiApplication"
}

dependencies {
    compile(project(":core"))
}
