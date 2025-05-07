(ns stdlang-book.blockly-python
  (:require [std.lang :as l]))

^:kindly/hide-code
(require
 '[scicloj.kindly.v4.kind :as kind]
 '[scicloj.kindly.v4.api :as kindly])

^:kind/println
(l/script+ [:code :python]
  {:require [[xt.lang.base-lib :as k]
             [xt.lang.base-iter :as it]]})

^:kind/println
(l/script+ [:live :python]
  {:runtime :basic
   :require [[xt.lang.base-lib :as k]
             [xt.lang.base-iter :as it]]})

(defn tiles-kw? [e]
  (and (keyword? e) (= (namespace e) "tiles")))

(defn redef-def [e]
  (if (list? e)
    (cond
      (= (first e) 'def)
      (list 'var (first (second e))
            (list 'fn (into [] (rest (second e))) (redef-def (last e))))
      (tiles-kw? (first e)) (redef-def (second e))
      :else (remove tiles-kw? (map redef-def e))) e))

(defmacro ! [o e] (list 'l/! o (redef-def e)))

^:kind/println
(! [:code] (do (def (hello x y) (return (:tiles/infix (+ x y))))
               :tiles/slot
               (hello 1 2)))

^:kindly/hide-code
(kind/scittle
  '(.log js/console "hello"))

^:kindly/hide-code
(kind/hiccup
  [:div
   [:script {:src "https://kloimhardt.github.io/twotiles/twotiles_core.js"}]
   [:script "var parse = scittle.core.eval_string(twotiles.parse_clj);"]
   [:script {:src "https://unpkg.com/blockly/blockly_compressed.js"}]
   [:script {:src "https://unpkg.com/blockly/msg/en.js"}]
   [:script "Blockly.defineBlocksWithJsonArray(twotiles.blocks);"]])

^:kindly/hide-code
(defn tiles-render [code]
  [:div
   [:script (str "var xml1 = parse('" code "')")]
   [:div {:id "blocklyDiv1", :style {:height "180px"}}]
   [:script "var workspace1 = Blockly.inject('blocklyDiv1',
{'toolbox': twotiles.toolbox, 'sounds': false})"]
   [:script "const xmlDom1 = Blockly.utils.xml.textToDom(xml1)"]
   [:script "Blockly.Xml.clearWorkspaceAndLoadFromXml(xmlDom1,workspace1)"]])

^:kind/hiccup
(tiles-render '(do (def (hello x y) (return (:tiles/infix (+ x y))))
                   :tiles/slot
                   (hello 1 2)))

(! [:live] (do (def (hello x y) (return (:tiles/infix (+ x y))))
               :tiles/slot
               (hello 1 2)))
