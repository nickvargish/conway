(ns conway.stringio
  (:require
    [clojure.string :as strings]
    [conway.logic :as logic]))


;;;
;;; Input
;;;

;; Utility

(defn string->integer
  [s] (when-let [d (re-find #"-?\d+" s)] (Integer. d)))


(defn parse-rule-string
  "Returns a map of rules described in survival/birth format."
  [rule]
  (let [r (map (fn [s1] (map (fn [s2] (string->integer s2))
                             (strings/split s1 #"")))
               (strings/split rule #"/"))]
    {:survival (sort (first r)), :birth (sort (last r))}))
;;
;; Life 1.05 (*.lif, *.life)
;;

(defn parse-life105-row
  [s xo yo]
  (let [cl (seq s)
        cs (count cl)]
    (remove nil? (map (fn [c x] (when (not (or (= c \space) (= c \.)))
                                  (logic/make-cell (+ x xo) yo)))
                      cl (range cs)))))

(defn parse-life105-block
  [block]
  (let [pstr (last (re-find #"^#P (.*)" (first block)))
        pl   (when pstr (strings/split pstr #" "))
        xo   (if pstr (string->integer (first pl)) 0)
        yo   (if pstr (string->integer (last pl)) 0)
        rows (if pstr (rest block) block)
        rs   (count rows)]
    (map (fn [r y] (parse-life105-row r xo (+ y yo)))
                        rows (range rs))))

(defn partition-life105-blocks
  [rows]
  (let [bcount (atom 0)]
    (partition-by #(do
                    (when (re-find #"^#P" %)
                      (reset! bcount (inc @bcount)))
                    @bcount) rows)))

(defn parse-life105-data
  [lines]
  (let [rstr  (last (first (keep (partial re-find #"^#R (\d+/\d+)") lines)))
        rule  (if (nil? rstr) logic/normal-rule (parse-rule-string rstr))
        cstrs (map first (keep (partial re-find #"^([.*]|#P).*") lines))
        cells (->> cstrs (partition-life105-blocks) (map parse-life105-block) (flatten) (set))
        desc  (strings/join \newline (map last (keep (partial re-find #"^#D (.*)") lines)))]
    {:cells cells :rule rule :description desc}))

;;
;; Life 1.06 (*.lif, *.life)
;;

(defn parse-life106-block
  [s]
  (set (let [rows (strings/split-lines s)]
         (map #(let [cs (strings/split % #" ")]
                (logic/make-cell (string->integer (first cs))
                                 (string->integer (last cs)))) rows))))

;;
;; Misc formats
;;

(defn string-to-cells
  "Returns the set of cells that corresponds to the input string."
  [s] (parse-life105-block (strings/split-lines s)))

;;;
;;; Output
;;;

(defn get-dimensions
  "Returns the minimum and maximum x and y values for a population of cells."
  [cells]
  (let [xs (map #(% :x) cells) ys (map #(% :y) cells)]
    {:x-min (apply min xs)  :x-max (apply max xs)
     :y-min (apply min ys)  :y-max (apply max ys) }))

(defn cells-to-string
  "Returns a string representation of a population of cells."
  [cells]
  (let [dims (get-dimensions cells)]
    (strings/join \newline
                  (for [row (range(dims :y-min) (+ (dims :y-max) 1))]
                       (apply str (for [col (range(dims :x-min) (+ (dims :x-max) 1))]
                                       (if (nil? (cells { :x col :y row })) \. \*)))))))
