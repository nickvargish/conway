(ns conway.logic
   (:require [clojure.set :as sets]))


;;
;; Logic
;;


(def normal-rule {:survival '(2 3) :birth '(3)})

(defn make-cell
  "Returns a map representing a cell."
  [x y] { :x x :y y })

(defn in?
  "Returns true if y exists in coll, such that y equals x."
  [coll x] (some (partial = x) coll))

(defn neighbor-cells
  "Returns a set of the cells neighboring (x, y)."
  [{:keys [x y]}]
  (set (for [dx [-1 0 1] dy [-1 0 1]
             :when (not (and (zero? dx) (zero? dy)))]
         (make-cell (+ x dx) (+ y dy)))))

(defn alive-next-gen?
  "Takes a cell and a population, returns true if that cell will be alive
  in the next generation."
  ([rule alive cell]
    (let [ncount (count (sets/intersection (neighbor-cells cell) alive))]
      (if (nil? (alive cell))
        (in? (rule :birth) ncount)
        (in? (rule :survival) ncount))))
  ([alive cell] (alive-next-gen? normal-rule alive cell)))

(defn next-generation
  "Generates the next generation of living cells from a set of cells."
  ([rule alive]
   (set (filter (partial alive-next-gen? rule alive)
                (sets/union (apply sets/union (map neighbor-cells alive))
                            alive))))
  ([alive] (next-generation normal-rule alive)))

(defn generations
  "Generates n generations starting with cells."
  ([rule cells n]
   (take n (iterate (partial next-generation rule) cells)))
  ([cells n] (generations normal-rule cells n)))

(defn nth-generation
  "Generates the nth generation starting with cells."
  ([rule cells n]
    (nth (iterate next-generation rule cells) n))
  ([cells n] (nth-generation normal-rule cells n)))


