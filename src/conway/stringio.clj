(ns conway.stringio
  (:require
    [clojure.string :as strings]
    [conway.logic :as logic]))


;;;
;;; Input
;;;

;; Utility

(defn string->integer
  [s] (when-let [d (re-find #"\d+" s)] (Integer. d)))

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
  ([s xo yo]
   (let [rows (strings/split-lines s)
         rs   (count rows)]
     (set (flatten (map (fn [r y] (parse-life105-row r xo (+ y yo)))
                        rows (range rs))))))
  ([s] (parse-life105-block s 0 0)))

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
  [s] (parse-life105-block s))

(defn parse-rule-string
  "Returns a map of rules described in survival/birth format."
  [rule]
  (let [r (map (fn [s1] (map (fn [s2] (string->integer s2))
                             (strings/split s1 #"")))
               (strings/split rule #"/"))]
    {:survival (sort (first r)), :birth (sort (last r))}))


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
