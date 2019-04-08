package org.suggs.fsm.behavior

enum class PseudoStateKind {
    INITIAL,
    DEEP_HISTORY,
    SHALLOW_HISTORY,
    JOIN,
    FORK,
    JUNCTION,
    CHOICE,
    ENTRY_POINT,
    EXIT_POINT,
    TERMINATE;
}
