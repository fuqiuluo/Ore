plugins {
    java
    kotlin("jvm")
}

group = "moe.ore.pay"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    // testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation(project(":Ore-Core"))
    implementation(project(":Ore-Base"))
    implementation(project(":Ore-Api"))
}
