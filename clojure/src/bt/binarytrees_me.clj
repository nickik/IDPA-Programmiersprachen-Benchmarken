(ns bt.binarytrees-me
  (:gen-class))

(set! *warn-on-reflection* true)

(def min-depth (long 4))

(definterface ITreeNode
  (^long item [])
  (left [])
  (right []))

(deftype TreeNode [left right ^long item]
  ITreeNode
  (^long item [this] item)
  (left [this] left)
  (right [this] right))

(defn ^TreeNode  bottom-up-tree [^long item ^long depth]
  (if (zero? depth)
    (TreeNode. nil nil item)
    (TreeNode.
     (bottom-up-tree (unchecked-dec (unchecked-multiply 2 item))
                     (unchecked-dec depth))
     (bottom-up-tree (unchecked-multiply 2 item)
                     (unchecked-dec depth))
     item)))

(defn item-check [^TreeNode node]
  (if (nil? (.left node))
    (.item node)
    (- (+ (.item node)
          (item-check (.left node)))
       (item-check (.right node)))))

(defn iterate-trees [^long mx ^long  mn ^long d]
  (let [iterations (bit-shift-left 1 (+ mx mn (- d)))]
    (format "%d\t trees of depth %d\t check: %d"
            (unchecked-multiply 2 iterations)
            d
            (reduce unchecked-add
                    (map (fn [i]
                             (unchecked-add
                                (item-check (bottom-up-tree i d))
                                (item-check (bottom-up-tree (- i) d))))
                           (range 1 (unchecked-int iterations)))))))

(defn main [^long max-depth]
  (let [stretch-depth (inc max-depth)]
    (println "stretch tree of depth " stretch-depth  "\t"
             "check: " (item-check (bottom-up-tree 0 stretch-depth)))
    (let [long-lived-tree (bottom-up-tree 0 stretch-depth)]
      (doseq [trees-nfo (map (fn [d]
                              (iterate-trees max-depth min-depth d))
			      (range min-depth stretch-depth 2)) ]
        (println trees-nfo))
      (println "long lived tree of depth " max-depth "\t"
               " check: " (item-check long-lived-tree)))))

(defn -main [& args]
  (println "Binarytree-me")
  (let [n (read-string (first args))
        max-depth (long (if (> (+ min-depth 2) n) (+ min-depth 2) n))]
    (time (main max-depth))))


