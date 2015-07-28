(ns conway.stringio
  (:require
    [clojure.string :as strings]
    [conway.logic :as logic]))


;;
;; Input
;;

(defn string-to-cells
  "Returns the set of cells that corresponds to the input string."
  [in]
  (set (remove nil? (flatten
                      (let [lines (strings/split-lines in)]
                        (for [y (range (count lines))]
                          (let [l (get lines y)]
                            (for [x (range (count l))]
                              (let [c (get l x)]
                                (if (not (or (= c \space) (= c \.)))
                                  (logic/make-cell x y) ))))))))))

;;
;; Output
;;

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
