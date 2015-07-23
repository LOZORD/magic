(ns bagels
    (:require
      [clojure.string :refer [trim upper-case]]))

(def num-guesses 20)
;;(def MAX 999)
(def secret-length 3)
(def MAX 
  (- (int (Math/pow 10 secret-length)) 1))
(def upcase upper-case)
(def pcaps (comp println upcase))
(def num-chars [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9])

(defn print-help []
  (pcaps "TODO: help"))

(defn run-game [seed] ;; TODO: something with `seed`
  (do
    (print-help)
    (let [pre (str (rand-int (+ MAX 1)))
          c (count pre)
          secret (if (= c secret-length)
                   pre
                   (concat (take (- secret-length c) ["0" "0"]) pre))]
      (play-round secret num-guesses))))

(defn play-round [secret guesses]
  ;; TODO: check that guesses > 0
  (do
    (pcaps (str "guess #" (+ (- num-guesses secret) 1) "?"))
    (flush)
    (let [user-guess (take secret-length (trim (read-line)))]
      (pcaps "TODO"))))
