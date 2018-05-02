(ns conway.stringio
  (:require
    [clojure.string :as strings]
    [conway.logic :as logic]))

;;;
;;; Input
;;;

;; Utility

(defn string->integer
  "Converts a string containing decimal characters into an integer,
  otherwise returns nil."
  [s] (when-let [d (re-find #"-?\d+" s)] (Integer. d)))

(defn parse-rule-string
  "Returns a map of rules described in survival/birth format."
  [rule]
  (let [r (map (fn [s1] (map (fn [s2] (string->integer s2))
                             (strings/split s1 #"")))
               (strings/split rule #"/"))]
    {:survival (set (first r)) :birth (set (last r))}))

;; Life 1.05 (*.lif, *.life)

(defn parse-life105-row
  "Takes a string pattern s, x and y offsets xo and yo, returns a seq of
  the living cells."
  [s xo yo]
  (let [cl (seq s)]
    (remove nil? (map (fn [c x] (when (not (or (= c \space) (= c \.)))
                                  (logic/make-cell (+ x xo) yo)))
                      cl (range (count cl))))))

(defn parse-life105-block
  "Takes a block of text containing pattern and position lines, returns
  a seq of the living cells."
  [block]
  (let [pstr (last (re-find #"^#P (.*)" (first block)))
        pl (when pstr (strings/split pstr #" "))
        xo (if pstr (string->integer (first pl)) 0)
        yo (if pstr (string->integer (last pl)) 0)
        rows (if pstr (rest block) block)]
    (map (fn [r y] (parse-life105-row r xo (+ y yo)))
         rows (range (count rows)))))

(defn partition-life105-blocks
  "Takes rows of pattern strings, partitions by position lines."
  [rows]
  (let [bcount (atom 0)]
    (partition-by #(do (when (re-find #"^#P" %) (reset! bcount (inc @bcount)))
                       @bcount) rows)))

(defn parse-life105-data
  "Parses the lines of a Life 1.05 file into a map containing :cells, :rule,
  and :description values."
  [lines]
  (let [rule (->> lines
                  (keep (partial re-find #"^#R (\d+/\d+)"))
                  (first)
                  (last)
                  (#(if (nil? %) logic/normal-rule (parse-rule-string %))))
        cells (->> lines
                   (keep (partial re-find #"^([.*]|#P).*"))
                   (map first)
                   (partition-life105-blocks)
                   (map parse-life105-block)
                   (flatten)
                   (set))
        desc (->> lines
                  (keep (partial re-find #"^#D (.*)"))
                  (map last)
                  (strings/join \newline))]
    {:cells cells :rule rule :description desc}))

;; Life 1.06 (*.lif, *.life)

(defn parse-life106-block
  "Takes a string containing Life 1.06 data and returns a seq of the
  living cells."
  [s]
  (set (let [rows (strings/split-lines s)]
         (map #(let [cs (strings/split % #" ")]
                (logic/make-cell (string->integer (first cs))
                                 (string->integer (last cs)))) rows))))

;;;
;;; Output
;;;

(defn get-dimensions
  "Returns the minimum and maximum x and y values for a population of cells."
  [cells]
  (let [xs (map #(% :x) cells) ys (map #(% :y) cells)]
    {:x-min (apply min xs) :x-max (apply max xs)
     :y-min (apply min ys) :y-max (apply max ys)}))

(defn render-cells
  "Returns a block of text representing a population of cells"
  ([cells dims]
   (strings/join
     \newline
     (for [row (range (dims :y-min) (+ (dims :y-max) 1))]
       (apply str
              (for [col (range (dims :x-min) (+ (dims :x-max) 1))]
                (if (nil? (cells {:x col :y row})) \. \*))))))
  ([cells]
   (render-cells cells (get-dimensions cells))))

(defn cells-to-string
  "Returns a string representation of a population of cells with optional
  decorations."
  [cells & opts]
  (let [dims (get-dimensions cells)]
    (strings/join
      \newline
      (remove nil? (list (when (some #{:coordinates} opts)
                           (str "v (" (dims :x-min) "," (dims :y-min) ")"))
                         (render-cells cells dims))))))
