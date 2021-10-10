
plugins {
    `java-library`
}

dependencies {
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.8.1.202007141445-r")
    testImplementation("junit:junit:4.+")
}


