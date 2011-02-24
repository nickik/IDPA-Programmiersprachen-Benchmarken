(ns binarytrees_me
  (:gen-class))

(defn TreeNode [left right item]
  {:left left
   :right right
   :item item})

(defn bottom-up-tree [item depth]
  (if (zero? depth)
      (TreeNode nil nil item)
      (TreeNode
       (bottom-up-tree (dec (* 2 item))
                       (dec depth))
       (bottom-up-tree (* 2 item)
                       (dec depth))
       item)))

(defn item-check [node]
  (if (nil? (:left node))
    (:item node)
    (+ (:item node)
       (item-check (:left node))
       (- (item-check (:right node))))))

(defn iterate-trees [mx mn d]
  (let [iterations (bit-shift-left 1 (int (+ mx mn (- d))))]
    (format "%d\t trees of depth %d\t check: %d" (* 2 iterations) d
            (reduce + (map (fn [i]
                             (+ (item-check (bottom-up-tree i d))
                                (item-check (bottom-up-tree (- i) d))))
                           (range 1 (inc iterations)))))))

(def min-depth 4)

(defn main [max-depth]
  (let [stretch-depth (inc max-depth)]
    (let [tree (bottom-up-tree 0 stretch-depth)]
      (println (format "stretch tree of depth %d\t check: %d" stretch-depth (item-check tree))))
    (let [long-lived-tree (bottom-up-tree 0 max-depth)]
      (doseq [trees-nfo (map (fn [d]
                                (iterate-trees max-depth min-depth d))
			      (range min-depth stretch-depth 2)) ]
        (println trees-nfo))
      (println (format "long lived tree of depth %d\t check: %d" max-depth (item-check long-lived-tree)))
      (shutdown-agents))))

(defn -main [& args]
  (let [n (if (first args) (Integer/parseInt (first args)) 0)
        max-depth (if (> (+ min-depth 2) n) (+ min-depth 2) n)]
    (main max-depth)))
