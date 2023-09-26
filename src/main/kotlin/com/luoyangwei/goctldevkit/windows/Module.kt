package com.luoyangwei.goctldevkit.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intellij.openapi.project.Project
import com.luoyangwei.goctldevkit.ThemeConstants

/**
 *
 * @author luoyangwei by 2023-09-21 13:49 created
 *
 */

@Composable
fun Module(project: Project, name: String) {
    return Column(Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().background(ThemeConstants.Color.ColorBaseFill)) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    name,
                    color = ThemeConstants.Color.ColorRegularText,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    "~/Document/workspace/account",
                    color = ThemeConstants.Color.ColorSecondaryText,
                    style = TextStyle(fontSize = 10.sp),

                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
