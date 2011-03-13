(ns clojure.binarytrees-me
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

(defn bottom-up-tree [^long item  ^long depth]
  (if (== 0  depth)
      (TreeNode. nil nil item)
      (TreeNode.
       (bottom-up-tree (dec (* 2 item))
                       (dec depth))
       (bottom-up-tree (* 2 item)
                       (* depth))
       item)))

(defn ^long  item-check [^TreeNode node]
  (if (identical? nil (.left node))
    (.item node)
    (+ (+ (.item node)
          (item-check (.left node)))
       (- (item-check (.right node))))))

(defn iterate-trees [mx mn d]
  (let [iterations (bit-shift-left 1 (int (+ mx mn (- d))))]
    (format "%d\t trees of depth %d\t check: %d" (* 2 iterations) d
            (reduce + (map (fn [i]
                             (unchecked-add (item-check (bottom-up-tree i d))
                                (item-check (bottom-up-tree (- i) d))))
                           (range 1 (inc iterations)))))))

(defn main [^long max-depth]
  (let [stretch-depth (inc max-depth)]
    (println "stretch tree of depth " stretch-depth  "\t"
             "check: " (item-check (bottom-up-tree 0 stretch-depth)))
 
    (doseq [trees-nfo (map (fn [d]
                             (iterate-trees max-depth min-depth d))
                           (range min-depth stretch-depth 2)) ]
    (println trees-nfo))
    (println "long lived tree of depth " max-depth "\t"
             " check: " (item-check (bottom-up-tree 0 max-depth) ))))

(defn -main [& args]
   (let [n (condp apply args
              number? (first args)
              string? (read-string (first args))
              :else 0)
        max-depth (if (> (+ min-depth 2) n) (+ min-depth 2) n)]
    (time (main max-depth))))
