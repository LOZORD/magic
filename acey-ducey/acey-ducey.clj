(ns acey-ducey)
(def init-cash 100)
(def card-map [
  :2
  :3
  :4
  :5
  :6
  :7
  :8
  :9
  :Jack
  :Queen
  :King
  :Ace
])
(def suit-size (count card-map))
(def trim (partial clojure.string/trim))
(def upcase (partial clojure.string/upper-case))
(def pcaps (comp println upcase))
(def get-ind #(rand-int suit-size))
(declare run-game start-game game-loop continue-round lose-state)

(def help-str
  (str "acey-ducey is played in the following manner\nthe dealer (computer) "
        "deals two cards face up\nyou have have an option to bet or not bet "
        "depending on whether or not you feel the card will have a value "
        "between the first two.\nif you do not want to bet, input a '0'\n"))

(defn get-card-pair []
  (let [a   (get-ind)
        b   (get-ind)
        fst (min a b)
        snd (max a b)]
    (if (or (= fst snd) (= (- snd fst) 1))
      (get-card-pair)
      [fst snd])))

(defn start-game []
  (game-loop init-cash))

(defn game-loop [curr-cash]
  (if (< 0 curr-cash)
    (apply continue-round (flatten [curr-cash (get-card-pair)]))
    (lose-state)))

(defn lose-state []
  (do
    (pcaps "sorry friend, but you blew your wad")
    (pcaps "try again? ('yes' or 'no')")
    (if (= ((comp trim upcase) (read-line)) "YES")
      (start-game)
      (do
        (pcaps "ok, hope you had fun")
        (System/exit 0)))))

(defn continue-round [curr-cash fst-ind snd-ind]
  (let
    [
      fst (card-map fst-ind),
      snd (card-map snd-ind)
    ]
   (do
    (pcaps (str "you now have $" curr-cash "\n"))
    (pcaps (str "here are your next two cards\n\t" (name fst) "\n\t" (name snd)))
    (pcaps "what is your bet?")
    (flush)
    (let
      [
       user-bet (min (read-string (read-line)) curr-cash),
       thd-ind  (rand-int suit-size),
       thd (card-map thd-ind)
       ]
      (do
        (pcaps (str "you bet " user-bet))
        (if (< 0 user-bet)
          (do
            (pcaps (name thd))
            (if (< fst-ind thd-ind snd-ind)
              (do
                (pcaps "you win!")
                (game-loop (+ curr-cash user-bet)))
              (do
                (pcaps "sorry, you lose")
                (game-loop (- curr-cash user-bet)))))
          (do
            (pcaps "chicken!")
            (game-loop curr-cash))))))))

(defn run-game []
  (do
    (pcaps help-str)
    (start-game)))
