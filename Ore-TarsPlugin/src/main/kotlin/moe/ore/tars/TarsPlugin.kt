package moe.ore.tars

import moe.ore.tars.util.FileUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

//import org.jetbrains.kotlin.konan.file.File

class TarsPlugin : Plugin<Project> {
    override fun apply(project: Project) {


        project.afterEvaluate { thisProject ->
            // 配置项目文件 （构建前执行发生）
            // 构建前给任务注入监听

            // classes任务 任务完成后生成class文件在本地
            // 接下来gradle会执行jar任务封装进临时jar内
            thisProject.getTasksByName("classes", false).forEach { task ->
                // 必须是last
                // first时还没有生成文件
                task.doLast {
                    println("start build tars")
                    val buildDir = thisProject.buildDir.absolutePath
                    println("buildDir : $buildDir")
                    if(FileUtil.has(buildDir + File.separator + "classes")) {
                        FileUtil.traverseFile(buildDir + File.separator + "classes") { _, classFile ->
                            if(classFile.name.endsWith(".class")) {
                                TarsTransform(classFile).transform()
                            }
                        }
                        println("finish build tars file")
                    } else {
                        println("not have classes dir, not to build tars")
                    }
                }
            }
        }
    }
}