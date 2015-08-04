(ns conway.core
  (:gen-class)
  (:require [conway.logic :as logic]
            [conway.stringio :as stringio]
            [conway.console :as console]
            [clojure.string :as strings]))


;;
;; Testing
;;


(defn run-file
  [fn n]
  (let [world (-> fn (slurp) (strings/split-lines)
                  (stringio/parse-life105-data))]
    (println (str (world :description) "\n"))
    (map #(println (str "[ gen: " %2 " | cells: " (count %1) " ]\n"
                        (stringio/cells-to-string %1)))
         (logic/generations (world :rule) (world :cells) n)
         (range 1 (+ n 1)))))

(defn -main
  "Usage: conway data-file-path number-of-generations"
  [& args]
  (let [fname (first args)
        ngen  (stringio/string->integer (last args))]
    (println (str "filename=" fname "  generations=" ngen))
    (run-file fname ngen)
    (println "Done!")))

