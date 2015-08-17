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
(def zip (partial map vector))

(declare print-help run-game lose-state play-round win-state get-result
         build-fermi build-final zero-pad pico-fermi-print)

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
            foo (pcaps (str "received: " user-guess "\tsecret " secret))
            correct? (= user-guess secret)
            result (doall (get-result secret user-guess))
            bar (pcaps (doall result))
            count-pico (count (filter #(:pico? %) result))
            count-fermi (count (filter #(:fermi? %) result))
            bagels? (= 0 count-pico count-fermi)]
            ;;bagels? (comp not some #(or (:pico? %) (:fermi? %)) result)]
        (if correct?
          (do
            (pcaps "you got it!")
            (win-state))
          (do
            (if bagels?
              (pcaps "bagels")
              (pico-fermi-print result))
            (play-round secret (dec guesses))))))
      (lose-state)))

(defn pico-fermi-print [result]
  (let [strings (map #(cond
                        (:pico?  %) "pico"
                        (:fermi? %) "fermi"
                        :else "") result)
        single-string (join " " strings)]
    (comp pcaps trim single-string)))

(defn get-result [secret user]
  (let [zipped (map vector secret user (range secret-length))
        hashed (map (fn [[s u n]]
                      { :secret s
                        :user u
                        :index n }) zipped)
        secret-eq-user #(= (:secret %) (:user %))
        fermis  (map secret-eq-user hashed)
        picos   (map (comp boolean (fn [{ u :user
                                          n :index}]
                     (some (fn [{ s :secret
                                  m :index}]
                             (and (= u s) (not= n m))) hashed))) hashed)
        result (map (fn [[h f p]]
                      (assoc h
                             :fermi? f
                             :pico?  p)) (zip hashed fermis picos))]
    result))

(defn get-result' [secret user]
  (let [zipped (map vector secret user (range secret-length))
        pz (pcaps zipped)
        hashed (map (fn [s u n]
                      { :secret s
                        :user   u
                        :index  n }) zipped)
        ph (pcaps hashed)
        fermi-scan (map build-fermi zipped)
        non-fermis (filter (comp not :fermi?) fermi-scan)
        picos (map (fn [{ s :secret ;; TODO: refactor into separate function
                          u :user
                          n :index
                          _ :fermi? }]
                     (some #{u} non-fermis)) fermi-scan) ;; TODO is this the correct collection?
        ;; final (pcaps "TODO zip picos and fermi-scan")
        _fermis_ (map #(= (:secret %) (:user %)) hashed)
        pf (pcaps _fermis_)
        _picos_ (map (fn [{ u :user
                            n :index}]
                       (some (fn [{ s :secret
                                    n' :index}]
                               (and (= u s) (not= n n'))) hashed)) hashed)
        pp (pcaps _picos_)
        final (map (partial apply build-final) (map vector fermi-scan picos))
        _final_ (map (fn [h f p] (assoc h :fermi? f :pico? p)) (map vector hashed _fermis_ _picos_))]
    ;;final))
    _final_))

;; more elegant way to do this? assoc?
(defn build-final [{ s  :secret
                     u :user
                     n :index
                     fermi? :fermi? }
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

(defn zero-pad [s]
  (if (< (count s) secret-length)
    (case (count s)
      1 (str "00" s)
      2 (str "0" s))
    s))

(defn win-state []
  (pcaps "you won...")) ;; TODO
