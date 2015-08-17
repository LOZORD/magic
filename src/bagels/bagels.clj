(ns bagels
    (:require
      [clojure.string :refer [trim upper-case join]]))

(def num-guesses 20)
;;(def MAX 999)
(def secret-length 3)
(def MAX
  (- (int (Math/pow 10 secret-length)) 1))
(def upcase upper-case)
(def pcaps (comp println upcase))
(def num-chars [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9])
(def zip  (partial map vector))
(def none (comp not boolean some))

(declare print-help run-game lose-state play-round win-state get-result
         build-fermi build-final zero-pad pico-fermi-print bagels?)

(defn print-help []
  (pcaps "TODO: help text"))

(defn run-game [& seed] ;; TODO: something with `seed`
  (do
    (print-help)
    (let [pre (str (rand-int (+ MAX 1))) ;; TODO refactor into separate function
          secret (zero-pad pre)]
          ;;c (count pre)
          ;;secret (if (= c secret-length)
          ;;         pre
          ;;         (concat (take (- secret-length c) ["0" "0"]) pre))]
      (play-round secret num-guesses))))

(defn lose-state [] (pcaps "YOU LOSE!"))

(defn play-round [secret guesses]
  (if (> guesses 0)
    (do
      (pcaps (str "guess #" (+ (- num-guesses guesses) 1) "?"))
      (flush)
      (let [user-guess (zero-pad (trim (read-line))) ;;(zero-pad (take secret-length (trim (read-line)))) TODO only take a certain amount
            ;;foo (pcaps (str "received: " user-guess "\tsecret " secret))
            correct? (= user-guess secret)
            result (doall (get-result secret user-guess))]
            ;;bar (doall (pcaps (doall result)))]
        (if correct?
          (do
            (pcaps "you got it!")
            (win-state))
          (do
            (if (bagels? result)
              (pcaps "bagels")
              (pico-fermi-print result))
            (play-round secret (dec guesses))))))
      (lose-state)))

(defn bagels? [result]
  (none #(or (:pico? %) (:fermi? %)) result))

(defn pico-fermi-print [result]
  (let [strings (map #(cond
                        (:pico?  %) "pico"
                        (:fermi? %) "fermi"
                        :else "") result)
        single-string (join " " strings)]
    ((comp pcaps trim) single-string)))

(defn get-result [secret user]
  (let [zipped (map vector secret user (range secret-length))
        hashed (map (fn [[s u n]]
                      { :secret s
                        :user u
                        :index n }) zipped)
        secret-eq-user #(= (:secret %) (:user %))
        fermis  (map secret-eq-user hashed)
        picos   (map (comp boolean (fn [{ s :secret
                                          n :index}]
                     (some (fn [{ u :user
                                  m :index}]
                             (and (= s u) (not= n m))) hashed))) hashed)
        result (map (fn [[h f p]]
                      (assoc h
                             :fermi? f
                             :pico?  p)) (zip hashed fermis picos))]
    result))

(defn zero-pad [s]
  (if (< (count s) secret-length)
    (case (count s)
      1 (str "00" s)
      2 (str "0" s))
    s))

(defn win-state []
  (pcaps "you won...")) ;; TODO
