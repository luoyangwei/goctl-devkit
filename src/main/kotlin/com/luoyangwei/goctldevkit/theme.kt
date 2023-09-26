package com.luoyangwei.goctldevkit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * theme constants
 * @author luoyangwei by 2023-09-21 13:58 created
 *
 */
class ThemeConstants {
    companion object {
        val Color = ThemeColor
        val Distance = ThemeDistance
    }

}

class ThemeColor {
    companion object {
        val ColorGetMethod = Color(102, 177, 255)
        val BgColorGetMethod = Color(102, 177, 255, 20)
        val BgActiveColorGetMethod = Color(102, 177, 255, 50)

        val ColorPostMethod = Color(133, 206, 97)
        val BgColorPostMethod = Color(133, 206, 97, 20)
        val BgActiveColorPostMethod = Color(133, 206, 97, 50)

        val ColorDeleteMethod = Color(247, 137, 137)
        val BgColorDeleteMethod = Color(247, 137, 137, 20)
        val BgActiveColorDeleteMethod = Color(247, 137, 137, 50)

        val ColorPutMethod = Color(235, 181, 99)
        val BgColorPutMethod = Color(235, 181, 99, 20)
        val BgActiveColorPutMethod = Color(235, 181, 99, 50)

        val ColorMethodText = Color(255, 255, 255)

        // 深色模式
        val ColorBaseFill = Color(48, 48, 48)
        val ColorRegularText = Color(207, 211, 220)
        val ColorSecondaryText = Color(163, 166, 173)
    }
}

class ThemeDistance {
    companion object {
        val Distance1: Dp = 8.dp
        val Distance2: Dp = 16.dp
    }
}