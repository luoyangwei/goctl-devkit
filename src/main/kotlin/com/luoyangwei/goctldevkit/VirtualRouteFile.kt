package com.luoyangwei.goctldevkit

import com.goide.psi.impl.*
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.luoyangwei.goctldevkit.enums.RequestMode

private val LOG = logger<VirtualRouteFile>()

/**
 *
 * @author luoyangwei by 2023-09-26 10:03 created
 *
 */
class VirtualRouteFile(p: Project, virtualFile: VirtualFile) {
    private val project: Project = p
    private val originVirtualFile: VirtualFile = virtualFile

    var baseUrl: String = ""
    var moduleName: String = ""
    var modulePresentableUrl: String = ""
    val routes = mutableListOf<Route>()

    private fun addRoute(route: Route) {
        routes.add(route)
    }

    fun getPsiFile(): PsiFile? {
        return PsiManager.getInstance(project).findFile(originVirtualFile)
    }

    fun getNode() {
        val children = PsiManager.getInstance(project).findFile(originVirtualFile)?.children
        children?.forEach { child ->
            val functionDeclarationImpl: GoFunctionDeclarationImpl? = child as? GoFunctionDeclarationImpl
            if (functionDeclarationImpl?.name != null) {
                val codeLineIterator = functionDeclarationImpl.children.iterator()
                codeLineIterator.forEach { line ->
                    val block: GoBlockImpl? = line as? GoBlockImpl
                    if (block != null) {
                        val statement = getSimpleStatement(block)
                        for (s in statement) {
                            getArgumentList(s.children)
                        }
                    }
                }
            }
        }
    }

    private fun getSimpleStatement(block: GoBlockImpl): MutableList<GoSimpleStatementImpl> {
        val simpleStatement = mutableListOf<GoSimpleStatementImpl>()
        val iterator = block.children.iterator()
        while (iterator.hasNext()) {
            val statement: GoSimpleStatementImpl? = iterator.next() as? GoSimpleStatementImpl
            if (statement != null) {
                simpleStatement.add(statement)
            }
        }
        return simpleStatement
    }

    private fun getArgumentList(es: Array<PsiElement>) {
        val size = es.size
        if (size <= 0) {
            return
        }

        val iterator = es.iterator()
        while (iterator.hasNext()) {
            val e = iterator.next()
            val argument: GoArgumentListImpl? = e as? GoArgumentListImpl
            if (argument == null) {
                getArgumentList(e.children)
            } else {
                getRoutes(e.children)
            }
        }
    }

    private fun getRoutes(args: Array<PsiElement>) {
        val size = args.size
        if (size <= 0) {
            return
        }

        val iterator = args.iterator()
        while (iterator.hasNext()) {
            val arg = iterator.next()
            if (arg.children.isEmpty()) {
                continue
            }

            // 可能会有使用中间件，这里需要另外解析一层
            // rest.WithMiddlewares(
            //			[]rest.Middleware{serverCtx.Decrypt},
            //			[]rest.Route{
            //				{ ... }
            //			}...,
            //		),
            //		rest.WithPrefix("/api/v1"),
            //	)
            val referenceExpression: GoReferenceExpressionImpl? = arg.children[0] as? GoReferenceExpressionImpl
            if (referenceExpression?.text == "rest.WithMiddlewares") {
                val expressionList = arg.children[1]

                val expressionIterator = expressionList.children.iterator()
                while (expressionIterator.hasNext()) {
                    val lit = expressionIterator.next()
                    val compositeLit: GoCompositeLitImpl? = lit as? GoCompositeLitImpl
                    if (compositeLit != null) {
                        val first = compositeLit.children[0]

                        if (first.text == "[]rest.Middleware") {
                            // TODO 使用的中间件
                        }

                        if (first.text == "[]rest.Route") {
                            // routes
                            forOriginStructMorphologySlice(lit.children)
                        }
                    }
                }
            }

            // 使用 WithPrefix 配置 baseUrl
            // rest.WithPrefix("/api/v1"),
            if (referenceExpression?.text == "rest.WithPrefix") {
                this.baseUrl = arg.children[1].children[0].text
                this.baseUrl = this.baseUrl.substring(1, this.baseUrl.length - 1)
                LOG.info("${referenceExpression.children.size}")
            }

            forOriginStructMorphologySlice(arg.children)
        }
    }

    private fun forOriginStructMorphologySlice(elements: Array<PsiElement>) {
        val iterator = elements.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()

            val arrayOrSliceType: GoArrayOrSliceTypeImpl? = element as? GoArrayOrSliceTypeImpl
            if (arrayOrSliceType != null) {
            }

            // 解析内容转换成 route
            val literalValueType: GoLiteralValueImpl? = element as? GoLiteralValueImpl
            literalValueToStructMorphology(literalValueType)
        }
    }

    private fun literalValueToStructMorphology(literalValueType: GoLiteralValueImpl?) {
        literalValueType?.children?.forEach { e -> addRoute(routeMapping(e)) }
    }

    /**
     * 结构体
     * {
     * 	 Method:  http.MethodPost,
     * 	 Path:    "/running/push",
     * 	 Handler: motion.MotionRunningPushHandler(serverCtx),
     * }
     */
    private fun routeMapping(element: PsiElement): Route {
        var reference: PsiReference? = null

        var method = ""
        var path = ""

        element.children[0].children[0].children.forEach { e ->

            // 属性名字跟属性值
            val key: GoKeyImpl? = e.children[0] as? GoKeyImpl
            val value: GoValueImpl? = e.children[1] as? GoValueImpl

            if (key?.text == "Method") {
                method = value?.text.toString()
            }
            if (key?.text == "Path") {
                if (value != null) {
                    val v = value.text.toString()
                    path = v.substring(1, v.length - 1)
                }
            }
            if (key?.text == "Handler") {
                if (value != null) {
                    reference = value.children[0].children[0].reference
                }
            }
        }

        return Route(
            RequestMode.INSTANCE.toModeByAllStr(method),
            path, reference
        )
    }

}