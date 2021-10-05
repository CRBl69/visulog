
plugins {
    java
    application
}

application.mainClass.set("up.visulog.cli.CLILauncher")

dependencies {
    implementation(project(":analyzer"))
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    implementation("com.beust:jcommander:1.78")
    implementation("com.sparkjava:spark-core:2.9.3")
    testImplementation("junit:junit:4.+")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.2")

}


