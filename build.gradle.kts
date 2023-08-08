plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
}

taboolib {
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
//    install("module-database")
    install("module-kether")
    install("module-lang")
    install("module-nms")
    install("module-nms-util")
    install("platform-bukkit")
    install("expansion-command-helper")
    options("skip-kotlin-relocate")
    classifier = null
    version = "6.0.11-31"

    description {
        dependencies {
            name("Residence").optional(true)
            name("WorldGuard").optional(true)
            name("MythicMobs").optional(true)
        }

        contributors {
            name("Mkbakaa")
        }
    }

    relocate("org.reflections.Reflections", "me.mkbaka.executableblock.org.reflections.Reflections")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
}

dependencies {
    compileOnly("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    compileOnly("javassist:javassist:3.12.1.GA")
    compileOnly("org.reflections:reflections:0.10.2")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Jar> {
    destinationDirectory = File("E:\\Servers\\1.20.1\\plugins")
}