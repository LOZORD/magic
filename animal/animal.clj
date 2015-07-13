(ns animal)

(def upcase clojure.string/upper-case)
(def trim   clojure.string/trim)
(def pcaps  (comp println upcase))
;; FIXME fix null ptr exception on bad input
(def get-input #((comp upcase trim) (read-line)))

(declare run-game start-game create-new-tree walk-tree gather-animals
  print-animals gather-animals' print-animals exit-game)

(defn print-animals [] nil) ;; TODO

(def starter-tree
  {   :q  "are you thinking of an animal?"
      :n  :smartass
      :y  {
            :q "does it swim?"
            :n :bird
            :y :fish
          }})

(defn run-game [] (start-game starter-tree))
  ;;([filename] (throw (Exception "UNIMPLEMENTED TODO")))
  ;;TODO be able to read in list of animals and questions
  ;;([] (start-game starter-tree)))

(defn start-game [animal-tree]
  (do
      (pcaps (str "play `guess the animal`\n"
             "think of an animal and the computer will try to guess it!\n"))
      (walk-tree animal-tree animal-tree)))

(defn create-new-tree [
                       {:keys [q y n] :as old-tree}
                       old-kw
                       ;;new-tree
                       new-node]
    (if (keyword? old-tree)
      old-tree
      (cond
        (= y old-kw) {:q q, :y new-node, :n n}
        (= n old-kw) {:q q, :y y, :n new-node}
        :else {:q q, :y (create-new-tree y old-kw new-node), :n (create-new-tree n old-kw new-node)})))


(defn walk-tree [animal-tree
                 {:keys [q y n] :as curr-node}]
                 ;;[{   :q question
                 ;;     :y yes-res
                 ;;     :n no-res} :as curr-node]]
  (do
    (pcaps q)
    ;; TODO handle 'no' response to root question (should save & exit)
    (let [  user-in (get-input)
            root?   (= n :smartass)
            ;;choice  (if (.startsWith user-in "Y") y n)]
            choice (if root?
                     (case (partial .startsWith user-in) ;; FIXME
                       "Y" y
                       "N" (exit-game animal-tree)
                       "L" (print-animals animal-tree))
                     (case (.startsWith user-in) ;; FIXME
                       "Y" y
                       "N" n))]
      ;; have we reached a leaf?
      (if (keyword? choice)
        (do
          (pcaps (str "let me guess... is it a " (name choice) "?"))
          (let [  guess-resp (get-input) ]
            (if (.startsWith guess-resp "Y")
              (do
                (pcaps "haha, I got it!\n")
                (walk-tree animal-tree animal-tree))
              (do
                (pcaps "shoot! what animal is it?")
                (let [ new-animal (get-input)]
                  (do
                    (pcaps (str "what question can i use to distinguish a " new-animal " from a " (name choice) "?"))
                    ;; TODO let user distinguish what the Y/N should be for the new animal question
                    (let [  new-question (get-input)
                            new-node  {
                                        :q new-question
                                        :y (keyword new-animal)
                                        :n choice
                                      }
                            new-tree  (create-new-tree animal-tree choice new-node)]
                      (walk-tree new-tree new-tree))))))))
        (walk-tree animal-tree choice)))))

(defn gather-animals' [{:keys [y n] :as animal-tree} animal-vec]
  (if (keyword? animal-tree)
    (conj animal-vec animal-tree)
    (conj animal-vec (gather-animals' y) (gather-animals' n))))

(defn gather-animals [animal-tree]
  (sort (gather-animals' animal-tree [])))

(defn exit-game [animal-tree]
  (do
      (pcaps "writing animals to `animals.json`...")
      ;; TODO write file
      (pcaps "goodbye!")
      (System/exit 0)))
