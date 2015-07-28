(ns conway.core
  (:gen-class)
  (:require [conway.logic :as logic]
            [conway.stringio :as stringio]
            [conway.console :as console]))


;;
;; Testing
;;

(defn -main
  "Runs 12 gerations of R-Pentomino, then displays 98th generation."
  [& args]
  (let [r-pentomino (stringio/string-to-cells ".**\n**.\n.*.")]
    (console/print-generations r-pentomino 12)
    (console/print-nth-generation r-pentomino 98)))
