package com.luoyangwei.goctldevkit.enums

/**
 * 请求模式
 * @author luoyangwei by 2023-09-22 14:10 created
 *
 */
enum class RequestMode {
    INSTANCE,

    GET,
    POST,
    PUT,
    DELETE;


    /**
     * 转换一下内容为 RequestMode
     * http.MethodPost
     * http.MethodPut
     * http.MethodDelete
     * http.MethodGet
     */
    fun toModeByAllStr(mode: String): RequestMode {
        return RequestMode.valueOf(mode.substring(11).uppercase())
    }

    fun toMode(mode: String): RequestMode {
        return RequestMode.valueOf(mode.uppercase())
    }
}
