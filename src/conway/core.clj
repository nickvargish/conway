(ns conway.core
  (:gen-class)
  (:require [clojure.set :as sets]
            [clojure.string :as strings]))




;;
;; Logic
;;

(defn neighbor-cells [{:keys [x y]}]
  (set (for [dx [-1 0 1] dy [-1 0 1]
             :when (not (and (zero? dx) (zero? dy)))]
         { :x (+ x dx) :y (+ y dy) })))

(defn alive-next-gen? [cell alive]
  (let [ncount (count (sets/intersection (neighbor-cells cell) alive))]
    (if (nil? (alive cell))
      (= 3 ncount)
      (or (= 2 ncount) (= 3 ncount)))))

(defn next-generation [alive]
  "Generates the next generation of living cells from a set of cells."
  (set (filter #(alive-next-gen? % alive)
    (sets/union (apply sets/union (map neighbor-cells alive)) alive))))

(defn generations [cells n]
  "Generates n generations starting with cells."
  (take n (iterate next-generation cells)))

(defn make-cell [x y]
  "Returns a map representing a cell."
  { :x x :y y })

;;
;; Input
;;

(defn string-to-cells [in]
  (set (remove nil? (flatten
    (let [lines (strings/split-lines in)]
      (for [y (range (count lines))]
        (let [l (get lines y)]
          (for [x (range (count l))]
            (let [c (get l x)]
              (if (not (or (= c \space) (= c \.)))
                (make-cell x y) ))))))))))
            
;;
;; Output
;;

(defn get-dimensions [cells]
  (let [xs (map #(% :x) cells) ys (map #(% :y) cells)]
    {:x-min (apply min xs)  :x-max (apply max xs)
     :y-min (apply min ys)  :y-max (apply max ys) }))

(defn cells-to-string [cells]
  (let [dims (get-dimensions cells)]
    (strings/join \newline
      (for [row (range(dims :y-min) (+ (dims :y-max) 1))]
        (apply str (for [col (range(dims :x-min) (+ (dims :x-max) 1))]
          (if (nil? (cells { :x col :y row })) \. \*)))))))

;;
;; Testing
;;



;(def blinker (string-to-cells "***"))

;(keep #(do (println (cells-to-string %)) (println)) (generations r-pentomino 12))

(defn -main
  "Runs 12 gerations of R-Pentomino, then displays 98th generation."
  [& args]
  (let [r-pentomino-string ".**\n**.\n.*."
        r-pentomino (string-to-cells r-pentomino-string)
        r-pentomino-forever (iterate next-generation r-pentomino)]
    (do
      (keep #(do (println (cells-to-string %)) (println))
            (generations r-pentomino 12))

      (println (cells-to-string (nth r-pentomino-forever 98)))
      )))
