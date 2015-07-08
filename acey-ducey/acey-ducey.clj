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

(def pcaps (partial println (clojure.string/upper-case)))

(def help-str
  (str "acey-ducey is played in the following manner\nthe dealer (computer) "
        "deals two cards face up\nyou have have an option to bet or not bet "
        "depending on whether or not you feel the card will have a value "
        "between the first two.\nif you do not want to bet, input a '0'\n"))

(defn get-card-pair []
  (let [fst (rand-int suit-size)
        snd (rand-int suit-size)]
        [fst snd])) ;; this is broken, need (min, max)

(defn game-loop [curr-cash fst-ind snd-ind]
  (let
    [
      fst (card-map fst-ind),
      snd (card-map snd-ind)
    ]
   (do ;;TODO first check for positive curr-cash
    (pcaps (str "you now have $" curr-cash "\n"))
    (pcaps (str "here are your next two cards\n\t" (name fst) "\n\t" (name snd)))
    (pcaps "what is your bet?")
    (flush)
    (let
      [
          user-bet read-line,
          thd-ind  (rand-int suit-size),
          thd-card (card-map thd-ind)
      ]
    (if (< fst-ind thd-ind snd-ind)
      (pcaps "win")
      (pcaps "lose"))))))

(defn main (game-loop init-cash))
