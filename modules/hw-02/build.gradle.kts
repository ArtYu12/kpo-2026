plugins {
    application
    checkstyle
    java
    jacoco
    id("org.springframework.boot") version "4.1.1"
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "pos.PosApplication"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    standardInput = System.`in`
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName = "pos.jar"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:4.1.1")

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.1.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.15"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required = true
        xml.required = true
        csv.required = false
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.60".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

val sunChecks = configurations.detachedConfiguration(
    dependencies.create("com.puppycrawl.tools:checkstyle:14.1.0")
).apply {
    isTransitive = false
}

checkstyle {
    toolVersion = "14.1.0"
    config = resources.text.fromFile(
        layout.buildDirectory.file("checkstyle/sun_checks.xml").get().asFile
    )
    isShowViolations = true
    isIgnoreFailures = false
    maxWarnings = 0
}

val checkstyleConfig = layout.buildDirectory.file("checkstyle/sun_checks.xml")

val prepareCheckstyleConfig = tasks.register("prepareCheckstyleConfig") {
    inputs.files(sunChecks)
    outputs.file(checkstyleConfig)
    doLast {
        val standardConfig = resources.text
            .fromArchiveEntry(sunChecks, "sun_checks.xml")
            .asString()
        val projectConfig = standardConfig
            // Комментарии пишем через //, поэтому Javadoc-правила выключены.
            .replace(
                Regex("<module name=\"(Missing|Invalid)?Javadoc\\w*\"\\s*/>"),
                ""
            )
            // Параметры конструктора называются так же, как поля.
            .replace(
                "<module name=\"HiddenField\"/>",
                """<module name="HiddenField">
                    <property name="ignoreConstructorParameter"
                              value="true"/>
                </module>"""
            )
            // Числа в тестах описывают ожидаемое поведение.
            .replace(
                "<module name=\"Checker\">",
                """<module name="Checker">
                    <module name="SuppressionSingleFilter">
                        <property name="checks" value="MagicNumber"/>
                        <property name="files" value="[/\\]src[/\\]test[/\\]"/>
                    </module>"""
            )
        checkstyleConfig.get().asFile.apply {
            parentFile.mkdirs()
            writeText(projectConfig)
        }
    }
}

tasks.withType<Checkstyle>().configureEach {
    dependsOn(prepareCheckstyleConfig)
    reports {
        html.required = true
        xml.required = false
    }
}
