(ns stdlang-book.blockly-python
  (:refer-clojure :exclude [do])
  (:require [std.lang :as l]))

^:kindly/hide-code
(require
  '[scicloj.kindly.v4.kind :as kind]
  '[scicloj.kindly.v4.api :as kindly])

^:kindly/hide-code
(def md
  (comp kindly/hide-code kind/md))

;; # Resembling Python Syntax

;; ## Intro

(md "It is of course known to Clojurians that, using Macros, one can morph Clojure's syntax to more and more resemble Python syntax. The only thing that remains after such a syntax-morphing with Macros ist the position of the parentheses - tabs, space and newline can exactly be chosen as in Python.")

(md "So, it is not the point of not knowing about this morphing possibility. The point is rather the followig attitude: 'Why would I want this for my programs? I left all this behind me. Why would I go back?'")

(md "The answer to those questions is: You do not want to go back. You want to stick to Clojure syntax. You do not need all this morphing business. But this is not about You. It is not about improving Your value in the job market. On the contrary, it is about giving away your competitive edge.")

(md "It is about building a bridge for people who are currenty doing data anaysis in Python. Buildig a bridge for people who sometimes realize that when it comes to performance and having the choice between Python and Java, the decision will always be in favor of Java, at least in an industry or business context. Building a bridge for people who have this feeling of unease when thinking about those invisible characters that in Python are meaning parts of the syntax.")

(md "So let's write Python syntax where whitespace has no meaning, and let's run all this on the JVM. Let's write code to show to others. Others who are uncertain and reluctant but ready to give something new a try but want familiarity. Let's show an example that everyone knows is easy to make and noone actually makes because for the one who can make it, it is completely useless. But it matters. At least I think so.")

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

(def.tl some-py-function

  (def (hallo x y)
    (return (x + y)))
  (hallo 1 2)

  )

^:kind/println
(:python-code some-py-function)

^:kind/hiccup
(:tiles-hiccup some-py-function)

(:live some-py-function)
