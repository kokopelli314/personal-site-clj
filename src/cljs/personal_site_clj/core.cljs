(ns personal-site-clj.core
	(:require [quil.core :as q :include-macros true]))

(enable-console-print!)
(extend-type js/HTMLCollection
	ISeqable
	(-seq [array] (array-seq array 0)))

; Binary Tree goodness
; thanks to https://eddmann.com/posts/binary-search-trees-in-clojure/
; and https://eddmann.com/posts/avl-trees-in-clojure/
(defrecord Node [el left right])

(defn tree-min [{:keys [el left]}]
	(if left
		(recur left)
		el))

(defn tree-max [{:keys [el right]}]
	(if right
		(recur right)
		el))

(defn tree-insert [{:keys [el left right] :as tree} value]
	(cond
		(nil? tree) (Node. value nil nil)
		(< value el) (Node. el (tree-insert left value) right)
		(> value el) (Node. el left (tree-insert right value))
		:else tree))

(defn tree-remove [{:keys [el left right] :as tree} value]
	(cond
		(nil? tree) nil
		(< value el) (Node. el (tree-remove left value) right)
		(> value el) (Node. el left (tree-remove right value))
		(nil? left) right
		(nil? right) left
		:else (let [min-value (tree-min right)]
			(Node. min-value left (tree-remove right min-value)))))

(defn tree-contains? [{:keys [el left right] :as tree} value]
	(cond
		(nil? tree) false
		(< value el) (recur left value)
		(> value el) (recur right value)
		:else true))

(defn tree-count [{:keys [left right] :as tree}]
	(if tree
		(+ 1 (tree-count left) (tree-count right))
		0))

(defn tree-height
	([tree] (tree-height tree 0))
	([tree count]
		(if tree
			(max (tree-height (:left tree) (inc count))
					 (tree-height (:right tree) (inc count)))
			count)))

(def to-tree #(reduce tree-insert nil %))

(defn to-list [{:keys [el left right] :as tree}]
	(when tree
		`(~@(to-list left) ~el ~@(to-list right))))

(defn factor [{:keys [left right]}]
	(- (tree-height left) (tree-height right)))

(defn rotate-left [{:keys [el left right] :as tree}]
	(if right
		(->Node (:el right) (->Node el left (:left right)) (:right right))
		tree))

(defn rotate-right [{:keys [el left right] :as tree}]
	(if left
		(->Node (:el left) (:left left) (->Node el (:right left) right))
		tree))

(defn is-left-case? [tree]
	(< (factor tree) -1))
(defn is-left-right-case? [tree]
	(and (is-left-case? tree) (> (factor (:right tree)) 0)))
(defn is-right-case? [tree]
	(> (factor tree) 1))
(defn is-right-left-case? [tree]
	(and (is-right-case? tree) (< (factor (:left tree)) 0)))

(defn tree-balance [{:keys [el left right] :as tree}]
	(cond
		(is-right-left-case? tree) (rotate-right (->Node el (rotate-left left) right))
		(is-left-right-case? tree) (rotate-left (->Node el left (rotate-right right)))
		(is-right-case? tree) (rotate-right tree)
		(is-left-case? tree) (rotate-left tree)
		:else tree))

(def avl-insert (comp tree-balance tree-insert))
(def avl-remove (comp tree-balance tree-remove))
(def seq->avl (partial reduce avl-insert nil))


; Event handlers
(defn trigger-when-clicked [class-name f]
	(let [elements (.getElementsByClassName js/document class-name)]
		(doall (map #(.addEventListener % "click" f) elements))))

(defn toggle-menu [event]
	(let [hamburger (js/document.getElementById "hamburger")
				menu (js/document.getElementById "menu")]
		(.toggle (.-classList menu) "hidden")
		(.toggle (.-classList hamburger) "is-active")))
(defn close-menu [event]
	(let [hamburger (js/document.getElementById "hamburger")
				menu (js/document.getElementById "menu")]
		(.add (.-classList menu) "hidden")
		(.remove (.-classList hamburger) "is-active")))

(trigger-when-clicked "hamburger" toggle-menu)
(trigger-when-clicked "menu-link" close-menu)

; Animations & drawings
(defn setup []
	(q/frame-rate 1)
	(q/background (q/color 0 0 0 1)))

(defn draw []
	(q/stroke (q/random 255))
	(q/stroke-weight (q/random 10))
	(q/fill (q/random 255))
	
	(let [diam (q/random 100)
				x		 (q/random (q/width))
				y		 (q/random (q/height))]
		(q/ellipse x y diam diam)))

(q/defsketch personal-site-clj
	:host "test-canvas"
	:settings #(q/smooth 2)
	:setup setup
	:draw draw
	:size [(- (.-innerWidth js/window) 25) 500])




(defn render [] nil)
; (defn render []
; 	(set! (.-innerHTML (js/document.getElementById "replace-this"))
; 				"TEXT HERE"))
