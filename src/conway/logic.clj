(ns conway.logic
   (:require [clojure.set :as sets]))


;;
;; Logic
;;

(defn make-cell
  "Returns a map representing a cell."
  [x y]
  { :x x :y y })

(defn neighbor-cells
  "Returns a set of the cells neighboring (x, y)."
  [{:keys [x y]}]
  (set (for [dx [-1 0 1] dy [-1 0 1]
             :when (not (and (zero? dx) (zero? dy)))]
         (make-cell (+ x dx) (+ y dy)))))

(defn alive-next-gen?
  "Takes a cell and a population, returns true if that cell will be alive in the next generation."
  [cell alive]
  (let [ncount (count (sets/intersection (neighbor-cells cell) alive))]
    (if (nil? (alive cell))
      (= 3 ncount)
      (or (= 2 ncount) (= 3 ncount)))))

(defn next-generation
  "Generates the next generation of living cells from a set of cells."
  [alive]
  (set (filter #(alive-next-gen? % alive)
               (sets/union (apply sets/union (map neighbor-cells alive)) alive))))

(defn generations
  "Generates n generations starting with cells."
  [cells n]
  (take n (iterate next-generation cells)))

(defn nth-generation
  "Generates the nth generation starting with cells."
  [cells n]
  (nth (iterate next-generation cells) n))

