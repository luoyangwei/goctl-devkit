package com.luoyangwei.goctldevkit

import com.goide.psi.impl.*
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.luoyangwei.goctldevkit.enums.RequestMode

private var LOG = logger<FilesService>()

/**
 *
 * @author luoyangwei by 2023-09-21 15:43 created
 *
 */
class FilesService {
    /**
     * route 文件
     */
    var routeFiles = mutableMapOf<String, VirtualRouteFile>()
    var modules = mutableMapOf<String, String>()
    private var routeFileName = "routes.go"


    fun findRouters(project: Project) {
        val gofiles = FilenameIndex.getAllFilesByExt(project, "go")
        gofiles.iterator().forEach { gofile ->
            val virtualRouteFile = VirtualRouteFile(project, gofile)

            val module = findModuleName(project, gofile)
            if (module.isNotBlank()) {
                modules[gofile.presentableUrl] = module
            }

            if (routeFileName == gofile.name) {
                virtualRouteFile.getNode()
                routeFiles[gofile.path] = virtualRouteFile
            }
        }

        moduleAddressMatching()
        LOG.info("size: " + routeFiles.size.toString())
    }

    private fun moduleAddressMatching() {
        // 去掉文件名，然后比对路径
        modules.forEach { (path, filename) ->
            val filenameIndex = path.indexOf(filename)
            val absolutePath = path.substring(0, filenameIndex - 1)

            routeFiles.forEach { (gPath, routeFile) ->
                if (gPath.indexOf(absolutePath) >= 0) {
                    routeFile.moduleName = filename
                }
            }
        }
    }

    // 通过项目目录下的 main 方法来寻找文件
    // 文件即项目名字
    private fun findModuleName(project: Project, gofile: VirtualFile): String {
        if (gofile.presentableUrl.indexOf(project.presentableUrl.toString()) >= 0) {
            val children = PsiManager.getInstance(project).findFile(gofile)?.children
            children?.forEach { item ->
                val functionDeclaration: GoFunctionDeclarationImpl? = item as? GoFunctionDeclarationImpl
                if ("main" == functionDeclaration?.name) {
                    LOG.info("filename: ${gofile.name}")
                    return gofile.name
                }
            }
        }
        return ""
    }


}