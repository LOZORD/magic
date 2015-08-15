(ns main
  (:require [clojure.java.io :as io]
            [clojure.string :refer [trim upper-case]]))

(declare run-game)

(def src-dir
  (io/file "./src/"))

(def src-contents
  (vec (.listFiles src-dir)))

(def sub-dirs
  (filter #(.isDirectory %) src-contents))

(def dir-names
  (map #(.getName %) sub-dirs))

(def pcaps
  (comp println upper-case))

(def indexed-game-list
  (let [indices (map inc (range (count dir-names)))]
    (map vec indices dir-names)))

(defn print-games [igl]
  (let [game-strs (map (fn [gnum gname]
                    (format "%d:\t%s" gnum gname)) igl)
        print-out (apply str (interpose "\n" game-strs))]
    (pcaps print-out)))

(defn main [& args]
  (do
    (pcaps "welcome to magic\nplease enter a number for a game below\n")
    (print-games indexed-game-list)
    (let [unum  (dec ((comp read-string trim) (read-line))) ;; FIXME
          uname (nth dir-names unum)
          usym  (symbol uname)]
      (if-not (nil? uname)
        (pcaps "you are dumb")
        (do
            (load (str uname "/" uname))
            (in-ns (symbol uname))
            (run-game))))))
