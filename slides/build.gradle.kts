plugins {
    kotlin("jvm")
}

val kotexVersion = "0.1-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("script-runtime"))
    implementation("io.kotex:kotex-core:$kotexVersion")
    implementation("io.kotex:kotex-beamer:$kotexVersion")
    implementation("io.kotex:kotex-bibtex:$kotexVersion")
    implementation(kotlin("reflect"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}