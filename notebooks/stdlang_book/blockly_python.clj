(ns stdlang-book.blockly-python
  (:require [std.lang :as l]))

^:kindly/hide-code
(require
  '[scicloj.kindly.v4.kind :as kind]
  '[scicloj.kindly.v4.api :as kindly])

^:kindly/hide-code
(def md
  (comp kindly/hide-code kind/md))

;; # Python and Clojure Syntax

;; ## Intro
(md "That the syntax of a Clojure program can be made to resemble Python is an obvious fact to any Clojurian. It is just a matter of the right Macros. But why doing such a thing?")

(md "This thing is for Clojurians who want to build bridges for people currently using Python. Who want to build a bridge for those people who sometimes realise that when it came to performance, Java would win over Python. Building a bridge for people who have this feeling of unease when thinking about invisible characters in Python. People who already heard about functional programming.")

(md "So let's write Python syntax where whitespace has no meaning, and let's run all this on the JVM. Let's write code to show to others. Others who are uncertain and reluctant but ready to try something new but still want familiarity. Let's show an example that everyone knows is easy to make and no-one actually makes because for the one who can make it, it is completely useless. But it matters. At least I think so.")

;; ## Code Preamble

^:kind/println
(l/script+ [:tiles-code :python])

^:kind/println
(l/script+ [:tiles-live :python]
  {:runtime :basic})

(kind/scittle
  '(.log js/console "hello"))

(kind/hiccup
  [:div
   [:script {:src "https://kloimhardt.github.io/twotiles/twotiles_core.js"}]
   [:script "var parse = scittle.core.eval_string(twotiles.parse_clj);"]
   [:script {:src "https://unpkg.com/blockly/blockly_compressed.js"}]
   [:script {:src "https://unpkg.com/blockly/msg/en.js"}]
   [:script "Blockly.defineBlocksWithJsonArray(twotiles.blocks);"]])

(defn tiles-render [code]
  [:div
   [:script (str "var xml1 = parse('" code "')")]
   [:div {:id "blocklyDiv1", :style {:height "180px"}}]
   [:script "var workspace1 = Blockly.inject('blocklyDiv1',
{'toolbox': twotiles.toolbox, 'sounds': false})"]
   [:script "const xmlDom1 = Blockly.utils.xml.textToDom(xml1)"]
   [:script "Blockly.Xml.clearWorkspaceAndLoadFromXml(xmlDom1,workspace1)"]])

(defn tiles-kw? [e]
  (and (keyword? e) (= (namespace e) "tiles")))

(defn redef-def [e]
  (if (seq? e)
    (cond
      (= (first e) 'def)
      (list 'var (first (second e))
            (list 'fn (into [] (rest (second e))) (redef-def (last e))))
      (tiles-kw? (first e))
      (redef-def (second e))
      :else
      (remove tiles-kw? (map redef-def e)))
    e))

(defn infx [e]
  (if (seq? e)
    (cond
      (#{'+} (second e))
      (list :tiles/infix
            (cons (second e)
                  (cons (first e) (rest (rest e)))))
      :else
      (map infx e))
    e))

(defmacro def.tl [name & e]
  (let [e1 (cons 'do e)
        e2 (cons 'do (interpose :tiles/slot (map infx e)))
        e3 (redef-def e2)]
    (list 'def name
          {:form         (list 'quote e1)
           :tiles-form   (list 'quote e2)
           :clojure-form (list 'quote e3)
           :tiles-hiccup (tiles-render e2)
           :python-code  (list 'l/! [:tiles-code] e3)
           :live         (list 'l/! [:tiles-live] e3)})))

;; ## The Example
;; We define a function in a syntax that resembles Python
(def.tl some-py-function

  (def (hallo x y)
    (return (x + y)))
  (hallo 1 2)

  )

;; Indeed the generated Python code is similar
^:kind/println
(:python-code some-py-function)

;; The graphical representation also looks Pythonesque
^:kind/hiccup
(:tiles-hiccup some-py-function)

;; We check that the calculation is indeed correct
(:live some-py-function)
