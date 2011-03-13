(ns clojure.binarytrees
  (:gen-class))

(set! *warn-on-reflection* true)

(definterface ITreeNode
  (^int item [])
  (left [])
  (right []))

(deftype TreeNode [left right ^int item]
  ITreeNode
  (^int item [this] item)
  (left [this] left)
  (right [this] right))

(defn bottom-up-tree [item depth]
  (let [int-item (int item)
        int-depth (int depth)]
    (if (zero? int-depth)
      (TreeNode. nil nil int-item)
      (TreeNode.
       (bottom-up-tree (unchecked-dec (unchecked-multiply 2 int-item))
                       (unchecked-dec int-depth))
       (bottom-up-tree (unchecked-multiply 2 int-item)
                       (unchecked-dec int-depth))
       int-item))))

(defn item-check [^TreeNode node]
  (if (nil? (.left node))
    (int (.item node))
    (unchecked-add (unchecked-add (int (.item node))
                                  (int (item-check (.left node))))
                   (int (- (item-check (.right node)))))))


(defn iterate-trees [mx mn d]
  (let [iterations (bit-shift-left 1 (int (+ mx mn (- d))))]
    (format "%d\t trees of depth %d\t check: %d" (* 2 iterations) d
            (reduce + (map (fn [i]
                             (unchecked-add (int (item-check (bottom-up-tree i d)))
                                            (int (item-check (bottom-up-tree (- i) d)))))
                           (range 1 (inc iterations)))))))

(def min-depth 4)

(defn main [max-depth]
  (let [stretch-depth (inc max-depth)]
    (let [tree (bottom-up-tree 0 stretch-depth)
          check (item-check tree)]
      (println (format "stretch tree of depth %d\t check: %d" stretch-depth check)))
    (let [long-lived-tree (bottom-up-tree 0 max-depth) ]
      (doseq [trees-nfo (map (fn [d]
                              (iterate-trees max-depth min-depth d))
			      (range min-depth stretch-depth 2)) ]
        (println trees-nfo))
      (println (format "long lived tree of depth %d\t check: %d" max-depth
                       (item-check long-lived-tree)))
      (shutdown-agents))))

(defn -main [& args]
  (let [n (condp apply args
              number? (first args)
              string? (read-string (first args))
              :else 0)
        max-depth (if (> (+ min-depth 2) n) (+ min-depth 2) n)]
    (time (main max-depth))))
