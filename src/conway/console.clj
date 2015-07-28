(ns conway.console
  (:require
    [conway.logic :as logic]
    [conway.stringio :as stringio]))


(defn print-generations
  "Prints n generations to the console, starting with cells."
  [cells n]
  (map  #(do (println (stringio/cells-to-string %)) (println))
        (logic/generations cells n)))


(defn print-nth-generation
  "Prints the nth generation of the population starting with cells to the console."
  [cells n]
  (println (stringio/cells-to-string (logic/nth-generation cells n))))

