package org.suggs.fsm.behavior.builders

interface Builder<T> {

    fun build(): T
}