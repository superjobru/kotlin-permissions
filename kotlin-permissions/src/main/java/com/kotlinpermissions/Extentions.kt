package com.kotlinpermissions

inline fun <E : Any, T : Collection<E>> T?.withNotNullNorEmpty(func: T.() -> Unit) {
    if (this != null && this.isNotEmpty()) {
        with(this) { func() }
    }
}

inline fun <E : Any, T : Collection<E>> T?.whenNotNullNorEmpty(func: (T) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}

inline fun <T> T?.notNull(f: (T) -> Unit) {
    if (this != null) {
        f(this)
    }
}

inline fun <T : Any, R> T?.ifNotNullOrElse(ifNotNullPath: (T) -> R, elsePath: () -> R) = let { if (it == null) elsePath() else ifNotNullPath(it) }