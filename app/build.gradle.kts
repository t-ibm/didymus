import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("com.github.node-gradle.node") version "3.5.1"
}

repositories {
    mavenCentral()
}

node.apply {
    download.set(true)
}

tasks {
    val clean by registering(Delete::class) {
        delete(project.layout.buildDirectory)
    }

    val createDiagram by registering(NpxTask::class) {
        val destinationDir = project.layout.buildDirectory.dir("images")
        group = "npm"
        description = "Creates the 'origin.svg' file from the 'origin.mmd' template."
        command.set("@mermaid-js/mermaid-cli")
        args.set(
            listOf(
                "mmdc", "--backgroundColor=transparent",
                "--input", project.layout.projectDirectory.file("src/doc/templates/origin.mmd").toString(),
                "--output", destinationDir.get().file("origin.svg").toString()
            )
        )
        doFirst {
            project.mkdir(destinationDir)
        }
    }
    project.rootProject.defaultTasks(clean.get(), createDiagram.get())
}