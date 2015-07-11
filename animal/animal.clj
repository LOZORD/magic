(ns animal)

(def upcase clojure.string/upper-case)
(def trim   clojure.string/trim)
(def pcaps  (comp println upcase))
(def get-input #((comp upcase trim) (read-line)))

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
      (walk-tree animal-tree animal-tree)))


(defn walk-tree [animal-tree
                 [{   :q question
                      :y yes-res
                      :n no-res} :as curr-node]]
  (do
    (pcaps question)
    (let [  user-in (get-input)
            choice  (if (.startsWith user-in "Y")   (yes-res) (no-res))
            other   (if (= choice no-res)           (yes-res) (no-res))]
      ;; have we reached a leaf?
      (if (keyword? choice)
        (do
          (pcaps (str "is it a " (name choice) "?"))
          (let [  guess-resp (get-input) ]
            (if (.startsWith guess-resp "Y")
              (do
                (pcaps "haha, I got it!")
                (walk-tree animal-tree animal-tree))
              (do
                (pcaps "shoot! what animal is it?")
                (let [ new-animal (get-input)]
                  (do
                    (pcaps (str "what question can i use to distinguish a " new-animal " from a " (name choice) "?"))
                    (let [  new-question (get-input)
                            new-node {
                                        :q new-question
                                        :y (keyword new-animal)

        (walk-tree animal-tree choice))

