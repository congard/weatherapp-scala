package pl.edu.agh.congard.weatherapp.backend.ext

implicit class ScopeFunExt[T](o: T) {
    def let[R](block: (it: T) => R): R =
        block(o)

    def also(block: (it: T) => Unit): T = {
        block(o)
        o
    }

    def ifNotNull(block: => Any): Unit = {
        if (o != null) {
            block
        }
    }

    def ifNotNull(block: (it: T) => Unit): T = {
        o ifNotNull { block(o) }
        o
    }
}
