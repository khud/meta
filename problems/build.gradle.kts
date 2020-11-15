plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":tsg"))
    implementation(kotlin("script-runtime"))
}