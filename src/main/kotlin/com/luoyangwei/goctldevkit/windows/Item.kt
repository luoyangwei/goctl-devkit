package com.luoyangwei.goctldevkit.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.luoyangwei.goctldevkit.Route
import com.luoyangwei.goctldevkit.ThemeConstants
import com.luoyangwei.goctldevkit.enums.RequestMode

const val POST = "POST"
const val GET = "GET"
const val PUT = "PUT"
const val DEL = "DEL"

/**
 *
 * @author luoyangwei by 2023-09-21 11:49 created
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Item(project: Project, route: Route) {
    var active by remember { mutableStateOf(false) }

    val modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
        active = true
    }.onPointerEvent(PointerEventType.Exit) {
        active = false
    }.clickable {
        val reference = route.reference
        if (reference != null) {
            val resolveElement = reference.resolve()

            if (resolveElement != null) {
                val containingFile = resolveElement.containingFile
                val virtualFile = containingFile.virtualFile

                OpenFileDescriptor(project, virtualFile, resolveElement.textOffset).navigate(true)
            }
        }
    }

    when (route.mode) {
        RequestMode.GET -> {
            ModeGetBox(modifier, route.url, active)
        }

        RequestMode.POST -> {
            ModePostBox(modifier, route.url, active)
        }

        RequestMode.DELETE -> {
            ModeDeleteBox(modifier, route.url, active)
        }

        RequestMode.PUT -> {
            ModePutBox(modifier, route.url, active)
        }

        RequestMode.INSTANCE -> {}
    }
}

@Composable
fun ModeGetBox(modifier: Modifier, url: String, active: Boolean) {
    return Row(
        modifier.then(
            Modifier.background(
                color =
                if (!active) ThemeConstants.Color.BgColorGetMethod else ThemeConstants.Color.BgActiveColorGetMethod
            )
                .padding(8.dp).fillMaxWidth()
        )
    ) {
        ModeText(GET, ThemeConstants.Color.ColorGetMethod)
        ModeUrl(url)
    }
}

@Composable
fun ModePostBox(modifier: Modifier, url: String, active: Boolean) {
    return Row(
        modifier.then(
            Modifier.background(
                color =
                if (!active) ThemeConstants.Color.BgColorPostMethod else ThemeConstants.Color.BgActiveColorPostMethod
            )
                .padding(8.dp).fillMaxWidth()
        )
    ) {
        ModeText(POST, ThemeConstants.Color.ColorPostMethod)
        ModeUrl(url)
    }
}

@Composable
fun ModeDeleteBox(modifier: Modifier, url: String, active: Boolean) {
    return Row(
        modifier.then(
            Modifier.background(
                color =
                if (!active) ThemeConstants.Color.BgColorDeleteMethod else ThemeConstants.Color.BgActiveColorDeleteMethod
            )
                .padding(8.dp).fillMaxWidth()
        )
    ) {
        ModeText(DEL, ThemeConstants.Color.ColorDeleteMethod)
        ModeUrl(url)
    }
}

@Composable
fun ModePutBox(modifier: Modifier, url: String, active: Boolean) {
    return Row(
        modifier.then(
            Modifier.background(
                color =
                if (!active) ThemeConstants.Color.BgColorPutMethod else ThemeConstants.Color.BgActiveColorPutMethod
            )
                .padding(8.dp).fillMaxWidth()
        )
    ) {
        ModeText(PUT, ThemeConstants.Color.ColorPutMethod)
        ModeUrl(url)
    }
}

@Composable
fun ModeText(text: String, color: Color) {
    return Text(text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.width(60.dp))
}

@Composable
fun ModeUrl(text: String) {
    return Text(text, fontSize = 14.sp, color = ThemeConstants.Color.ColorSecondaryText)
}