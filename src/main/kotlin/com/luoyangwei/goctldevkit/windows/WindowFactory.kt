package com.luoyangwei.goctldevkit.windows

import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.luoyangwei.goctldevkit.FilesService

/**
 *
 * @author luoyangwei by 2023-09-21 11:01 created
 *
 */

private val LOG = logger<WindowFactory>()

class WindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        LOG.info("${toolWindow.contentManager.contents.size}")

        // 文件服务
        val service = FilesService()

        // 找到项目里所有的 router.go 文件
        service.findRouters(project)

        toolWindow.contentManager.addContent(
            toolWindow.contentManager.factory.createContent(ComposePanel().apply {
                setContent {
                    RestfulRightWindow(project, service)
                }
            }, String(), true)
        )
    }

}