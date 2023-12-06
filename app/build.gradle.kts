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

    addRule("Pattern: createDiagram<ID>") {
        val taskName = this
        val id = taskName.replace("createDiagram", "").replaceFirstChar { it.lowercase() }
        register<NpxTask>(taskName) {
            val destinationDir = project.layout.buildDirectory.dir("images")
            group = "npm"
            description = "Creates the '$id.svg' file from the '$id.mmd' template."
            command.set("@mermaid-js/mermaid-cli")
            args.set(
                    listOf(
                            "mmdc", "--backgroundColor=transparent",
                            "--input", project.layout.projectDirectory.file("src/doc/templates/$id.mmd").toString(),
                            "--output", destinationDir.get().file("$id.svg").toString()
                    )
            )
            doFirst {
                project.mkdir(destinationDir)
            }
        }
    }
    project.rootProject.defaultTasks(clean.get().name, "createDiagramOrigin", "createDiagramCreateDefaultEdge", "createDiagramCreateEdgeDefault", "createDiagramMicroservicesBuildingBlocks", "elkStack", "elkStackFluent")
}
