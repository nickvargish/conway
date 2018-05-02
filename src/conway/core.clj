(ns conway.core
  (:gen-class)
  (:require [conway.logic :as logic]
            [conway.stringio :as stringio]
            [clojure.string :as strings]))

(defn- load-life
  "Parses the contents of _filename_ and returns a map representing the
  population described in the file."
  [filename]
  (-> filename
      (slurp)
      (strings/split-lines)
      (stringio/parse-life105-data)
      (#(merge {:rule logic/normal-rule} %))
      (#(assoc % :generations (logic/generations (:rule %) (:cells %))))))

(defn- print-world
  "Print the _g_<sup>th</sup> generation of world _w_."
  [w g]
  (let [c (nth (w :generations) g)]
    (println (format "[ generation: %d | cells: %d ]\n%s"
                     g (count c) (stringio/cells-to-string c :coordinates)))))

(defn run-file
  "Runs the life pattern specified by _filename_ for _n_ generations, printing
  each one."
  [filename n]
  (println (str filename ", " n))
  (let [world (load-life filename)]
    (println (str (world :description) "\n"))
    (dotimes [g (+ n 1)] (print-world world g))))

; (run-file "data/blinker.life" 5)

(defn -main
  "Usage: conway data-file-path number-of-generations"
  [& args]
  (let [fname (first args)
        ngen  (stringio/string->integer (last args))]
    (run-file fname ngen)
    (println "Done!")))

; (-main "data/acorn.life" "3")
