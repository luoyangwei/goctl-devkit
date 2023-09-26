package com.luoyangwei.goctldevkit.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.intellij.openapi.project.Project
import com.luoyangwei.goctldevkit.FilesService


/**
 *
 * @author luoyangwei by 2023-09-21 10:55 created
 *
 */
@Composable
fun RestfulRightWindow(project: Project, service: FilesService) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        val state = rememberScrollState()

        Column(modifier = Modifier.verticalScroll(state).fillMaxWidth()) {

            for (rfile in service.routeFiles) {
                Module(project, rfile.value.moduleName, rfile.value.modulePresentableUrl)

                for (route in rfile.value.routes) {

                    val file = rfile.value.getPsiFile()
                    if (file != null) {
                        Item(project, rfile.value, route)
                    }
                }

            }

        }

    }
}