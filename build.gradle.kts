plugins {
    `kotlin-dsl`
    `maven-publish`
    java
}

var blockStatesVersion = "0b09e438c6"

group = "fr.bretzel.minestom.utils"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("dev.hollowcube:minestom-ce:74ca1041f3")

    //Minestom
    implementation("dev.hollowcube:minestom-ce:74ca1041f3")

    //ALS Lib
    implementation("com.github.ALS-Project:Minestom-States:$blockStatesVersion")

    //Compression
    implementation("com.github.luben:zstd-jni:1.5.4-2");
}
