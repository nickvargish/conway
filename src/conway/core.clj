(ns conway.core
  (:gen-class)
  (:require [conway.logic :as logic]
            [conway.stringio :as stringio]
            [conway.console :as console]
            [clojure.string :as strings]))


;;
;; Testing
;;

(defn -main
  "Runs 12 gerations of R-Pentomino, then displays 98th generation."
  [& args]
  (let [r-pentomino (stringio/string-to-cells ".**\n**.\n.*.")]
    (console/print-generations r-pentomino 12)
    (console/print-nth-generation r-pentomino 98)))

(defn run-file
  [fn n]
  (let [world (-> fn (slurp) (strings/split-lines)
                  (stringio/parse-life105-data))]
    (println (str (world :description) "\n"))
    (map #(println (str (stringio/cells-to-string %) "\n"))
         (logic/generations (world :rule) (world :cells) n))))

