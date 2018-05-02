(ns conway.logic)

;;
;; Logic
;;

(def normal-rule {:survival #{2 3} :birth #{3}})

(defn make-cell
  "Returns a map representing a cell at _x_, _y_."
  [x y] { :x x :y y })

(defn- neighbor-cells
  "Returns a seq of the cells neighboring (x, y)."
  [{:keys [x y]}]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not (and (zero? dx) (zero? dy)))]
    (make-cell (+ x dx) (+ y dy))))

(defn- alive-next-generation?
  "Returns cell if cell will be alive next generation, otherwise nil."
  [population nfreqs rule cell]
  (if (population cell)
    (when ((:survival rule) (nfreqs cell)) cell)
    (when ((:birth rule) (nfreqs cell)) cell)))

(defn next-generation
  "Return the set of cells that will be alive next generation."
  ([rule cells]
    (let [nf (frequencies (mapcat neighbor-cells cells))]
      (set (keep (partial alive-next-generation? cells nf rule) (keys nf)))))
  ([cells] (next-generation normal-rule cells)))

(defn generations
  "Create an iterator for a populations's successive generations."
  ([rule cells]
    (iterate (partial next-generation rule) cells))
  ([cells]
    (generations normal-rule cells)))
