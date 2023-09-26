plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
    // JetBrains Compose Gradle 插件，主要用于提供 Compose multiplatfrom 需要的依赖项配置
    id("org.jetbrains.compose") version "1.5.10-beta01"
}

group = "com.luoyangwei"
version = "1.0.101-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")
    type.set("GO") // Target IDE Platform

    updateSinceUntilBuild.set(false)

    plugins.set(
        listOf(
            "org.jetbrains.plugins.go"
        )
    )
}

dependencies {
    // 添加 Compose Desktop 需要的依赖项，compose.desktop.currentOs 这个 value 便来自于上面添加的 org.jetbrains.compose 插件
    implementation(compose.desktop.currentOs)

//    implementation("com.jetbrains.intellij.go")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
