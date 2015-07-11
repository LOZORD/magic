# magic
Many of [Ahl's Games](http://www.atariarchives.org/basicgames/) In Clojure

This project is a way for me (and hopefully others) to learn Clojure.

## HOW TO PLAY

First make sure you have [Leiningen](http://leiningen.org/).

An easy way to do this (on Mac, using `bash` and `brew`) is:
```bash
lein repl || (brew install leiningen && lein repl)
```

Then enter this line in the repl
```clojure
;; GAME is the name of game you want to play
(do (load "GAME/GAME") (ns GAME) (run-game))
```
