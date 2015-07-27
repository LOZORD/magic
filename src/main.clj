(ns main
  (:require [clojure.java.io :as io]
            [clojure.string :refer [trim upper-case]]))

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

(defn main [& args]
  (do
    (pcaps "welcome to magic\nplease enter a number for a game below\n")
    (map (fn [[gname gnum]]
              (pcaps (str gnum ":\t" gname)))
         (vec dir-names
              (map inc (range (count dir-names)))))
    (let [unum  (dec ((comp read-string trim) read-line))
          uname (nth dir-names unum)
          usym  (symbol uname)]
      (if-not (nil? uname)
        (pcaps "you are dumb")
        (do
            (load (str uname "/" uname))
            (in-ns (symbol uname))
            (run-game))))))
