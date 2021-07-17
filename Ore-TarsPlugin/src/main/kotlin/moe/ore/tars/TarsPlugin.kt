package moe.ore.tars

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TarsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("testTarsPlugin").apply {
            doFirst {
                println("plugin可以运行。")
            }
        }

        val ex = project.extensions.getByType(AppExtension::class.java)
        val tars = TarsTransform.transform()
        tars.project = project
        ex.registerTransform(tars)
    }
}