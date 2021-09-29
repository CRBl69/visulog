
plugins {
    `java-library`
}

dependencies {
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    testImplementation("junit:junit:4.+")
}


