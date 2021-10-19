
plugins {
    `java-library`
    `jacoco`
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    testImplementation("junit:junit:4.+")

}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

