plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.intuit.karate", "karate-apache", "0.9.5")
    implementation("com.intuit.karate", "karate-junit5", "0.9.5")
    implementation("commons-io", "commons-io", "2.7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    implementation("net.masterthought:cucumber-reporting:5.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    systemProperty("karate.options", System.getProperty("karate.options"))
    systemProperty("karate.env", System.getProperty("karate.env"))
    outputs.upToDateWhen { false }
}

sourceSets {
    test {
        resources {
            srcDir(file("src/test/kotlin"))
            exclude("**/*.kt")
        }
    }
}

val mainClass = "karate.RunAll"
tasks {
    register("fatJar", Jar::class.java) {
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to mainClass)
        }
        from(configurations.runtimeClasspath.get()
            .onEach { println("add from dependencies: ${it.name}") }
            .map { if (it.isDirectory) it else zipTree(it) })
        from(configurations.testRuntime.get()
            .onEach { println("add from test dependencies: ${it.name}") }
            .map { if (it.isDirectory) it else zipTree(it) })
        val sourcesMain = sourceSets.main.get()
        val sourcesTest = sourceSets.test.get()
        sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
        sourcesTest.allSource.forEach { println("add from tests: ${it.name}") }
        from(sourcesMain.output, sourcesTest.output)
    }
}