plugins {
    java
}

group = "me.gj"
version = "1.1.0"

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

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

val localDeploy = file("local.deploy.gradle")

if (localDeploy.exists()) {
    apply(from = localDeploy)
}
