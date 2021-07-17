package moe.ore.tars

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import moe.ore.util.MD5
import org.gradle.api.Project

class TarsTransform : Transform() {
    lateinit var project : Project

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        if (transformInvocation == null) return

        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider
        inputs.forEach {  input ->
            input.directoryInputs.forEach { dir ->
                CompileTars().compileDir(dir.file.absolutePath)
                val dest = outputProvider.getContentLocation(dir.name, dir.contentTypes, dir.scopes, Format.DIRECTORY)
                FileUtils.copyFile(dir.file, dest)
            }
            input.jarInputs.forEach { jar ->
                var name = jar.name
                val md5 = MD5.toMD5(jar.file.absolutePath)
                if(name.endsWith(".jar")) {
                    name = name.substring(0, name.length - 4)
                }
                val dest = outputProvider.getContentLocation(name + md5, jar.contentTypes, jar.scopes, Format.JAR)
                FileUtils.copyFile(jar.file, dest)
            }
        }

    }

    /**
     * task名称
     */
    override fun getName() = "tars_transform"

    /**
     * 设置处理的文件类型
     */
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    /**
     * 作用范围
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    /**
     * 是否支持增量编译
     */
    override fun isIncremental(): Boolean = false

    companion object {
        @JvmStatic
        fun transform() : TarsTransform {
            return TarsTransform()
        }
    }
}