plugins {
    `kotlin-dsl`
    `maven-publish`
    java
}

var minestomVersion = "aebf72de90"
var blockStatesVersion = "0b09e438c6"

group = "fr.bretzel.minestom.utils"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    //Minestom
    implementation("com.github.Minestom:Minestom:$minestomVersion")

    //ALS Lib
    implementation("com.github.ALS-Project:Minestom-States:$blockStatesVersion")

    //Compression
    implementation("com.github.luben:zstd-jni:1.4.5-1");
}
