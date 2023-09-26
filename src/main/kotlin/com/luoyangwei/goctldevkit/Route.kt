package com.luoyangwei.goctldevkit

import com.intellij.psi.PsiReference
import com.luoyangwei.goctldevkit.enums.RequestMode

/**
 *
 * @author luoyangwei by 2023-09-26 10:03 created
 *
 */
class Route(val mode: RequestMode, var url: String, var reference: PsiReference?)