plugins {
    java
}

group = "me.gj"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

/* tasks.register<Copy>("deployPlugin") {
    dependsOn(tasks.jar)

    from(tasks.jar)
    into(file("C:/Users/gj/Documents/GregPlugins/26.3Server/plugins"))

    doLast {
        println("Plugin deployed to: ${destinationDir.absolutePath}")
    }
}

tasks.build {
    finalizedBy("deployPlugin")
} */
