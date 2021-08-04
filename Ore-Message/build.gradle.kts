plugins {
    kotlin("jvm")
    java
}

group = "moe.ore.msg"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":Ore-Core"))
    implementation(project(":Ore-Base"))
    implementation(project(":Ore-Api"))
}
