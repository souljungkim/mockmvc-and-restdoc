package com.avajjava.sample.common.type

enum StatusType {

    TEMP("00")
    , WAIT("1")
    , INSPECTING("2")
    , REJECT("3")
    , ACCEPT("4")
    , STOP("5")

    private String value

    private StatusType(String value) {
        this.value = value
    }

    String getValue() {
        return this.value
    }
}
