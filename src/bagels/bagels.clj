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

(defn lose-state [] (pcaps "TODO"))

(defn play-round [secret guesses]
  (if (> guesses 0)
    (do
      (pcaps (str "guess #" (+ (- num-guesses secret) 1) "?"))
      (flush)
      (let [user-guess (take secret-length (trim (read-line)))
            correct? (= user-guess secret)
            result (get-result secret user-guess)
            count-pico (count (filter #(:pico? %) result))
            count-fermi (count (filter #(:fermi? %) result))
            bagels? (= 0 count-pico count-fermi)]
        (if correct?
          (do
            (pcaps "you got it!")
            (win-state))
          (do
            (if bagels?
              (pcaps "bagels")
              (do
                (repeat count-pico (pcaps "pico"))
                (repeat count-fermi (pcaps "fermi"))))
            (play-round secret (dec guesses))))))
      (lose-state)))

(defn get-result [secret user]
  (let [zipped (map vector secret user (range secret-length))
        fermi-scan (map build-fermi zipped)
        ;; pico-scan (map build-pico fermi-scan)
        non-fermis (filter (comp not :fermi?) fermi-scan)
        picos (map (fn [{ :secret s ;; TODO: refactor into separate function
                          :user   u
                          :index  n
                          :fermi? _ }]
                     (some #{u} non-fermis)))
        ;; final (pcaps "TODO zip picos and fermi-scan")
        final (map build-final fermi-scan)]
    (final)))

;;(defn bagels? [result]
;;  (not (some (fn [h]
;;               (or (:fermi? h) (:pico? h))) result))

;; more elegant way to do this? assoc?
(defn build-final [{  :secret s
                      :user   u
                      :index  n
                      :fermi? fermi? }
                   pico?]
  { :secret s
    :user   u
    :index  n
    :fermi? fermi?
    :pico?  pico?})

(defn build-fermi [[a b n]]
  { :secret a
    :user   b
    :index  n
    :fermi?  (= a b)})

(defn build-pico [{ :secret a, :user b, :index n, :fermi? fermi?}]
  { :secret a
    :user   b
    :index  n
    :fermi?  fermi?
    :pico (and false false)}) ;; TODO

(comment
(defn no-dups [] (pcaps "TODO"))

(defn pico-f [zipped]
  (map (fn [[_ u n1]]
         (some (fn [[ s _ n2]]
                  (and (= s n) (not (= n1 n2)))) zipped)) zipped))
)
