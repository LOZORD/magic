(ns animal)

(def upcase clojure.string/upper-case)
(def pcaps  (comp println upcase))

(defn run-game
  ([filename] (throw (Exception "UNIMPLEMENTED TODO")))
  ([] (start-game [:bird, :fish])))

(defn start-game [animal-list]
  (do
      (pcaps (str "play `guess the animal`\n"
             "think of an animal and the computer will try to guess it!\n"))
      (walk-tree animal-list)))

(defn walk-tree [animal-list])

