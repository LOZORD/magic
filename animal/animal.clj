(ns animal)

(def upcase clojure.string/upper-case)
(def trim   clojure.string/trim)
(def pcaps  (comp println upcase))

;; XXX assume leaf nodes all at the same level
(def starter-tree
  ({  :q "are you thinking of an animal?"
      :n #(do (pcaps "goodbye") (System/exit 0))
      :y  {
            :q "does it swim?"
            :n :bird
            :y :fish
          }}))

(defn run-game
  ([filename] (throw (Exception "UNIMPLEMENTED TODO")))
  ([] (start-game starter-tree)))

(defn start-game [animal-tree]
  (do
      (pcaps (str "play `guess the animal`\n"
             "think of an animal and the computer will try to guess it!\n"))
      (walk-tree animal-tree)))


(defn walk-tree [animal-tree]
  (let [  question  (animal-tree :q)
          yes-res   (animal-tree :y)
          no-res    (animal-tree :n)]
    (do
      (pcaps question)
        (let [  user-in ((comp upcase trim) (read-line))
                choice  (if (= (user-in 0) "y") (yes-res) (no-res))
                other   (if (= choice no-res)   (yes-res) (no-res))]
          ;; have we reached a leaf?
          (if (keyword? choice)
            (do
              (pcaps (str "is it a " (name choice) "?"))) ;; TODO
            (walk-tree choice))))))


(comment
(defn walk-tree [animal-tree]
  ;; did we reach a leaf?
  (if (keyword? animal-tree)
    ;; yes! we're at a leaf!
    (guess-animal animal-tree)
    ;; nope, continue walking
    (let [ question (animal-tree :q)
          yes-res  (animal-tree :y)
          no-res   (animal-tree :n)]
      (do
        (pcaps question)
        (let [user-in ((comp upcase trim) (read-line))]
          (if (= (user-in 0) "y")
            (walk-tree yes-res)
            (walk-tree no-res)))))))
)
