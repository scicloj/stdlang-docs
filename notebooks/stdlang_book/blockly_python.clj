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
;; This section can be skipped in a first read, the main goal of this article is the example in the next section.

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

(defn tiles-render [divid code]
  [:div
   [:div {:id divid :style {:height "180px"}}]
   [:script (str "Blockly.Xml.clearWorkspaceAndLoadFromXml(Blockly.utils.xml.textToDom(parse('" code "')),
Blockly.inject('" divid "',
{'toolbox': twotiles.toolbox,
 'sounds': false,
 'scrollbars': false,
 'trashcan': false}));")]
   [:p]])

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
           :tiles-hiccup (tiles-render (str name) e2)
           :python-code  (list 'l/! [:tiles-code] e3)
           :live         (list 'l/! [:tiles-live] e3)})))

;; ## The Main Example

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

;; ## Step by Step Guide

;; In Clojure we can define and call a basic function like this:

(do
  (def hello (fn [x y]
               (+ x y)))
  (hello 1 2))

;; With `std-lang` for Python we can do the same in a similar manner

(l/! [:tiles-live]
  (do
    (var hallo (fn [x y]
                 (return (+ x y))))
    (hallo 1 2)))

;; The Python code for the same example looks like this:
^{:kind/println true :kindly/hide-code true}
(:python-code some-py-function)

;; We want to transform the `std-lang` code to make it look more like Python. As we disregard the prefix/infix issue for the moment, the immediate aim is the following form:

^:kindly/hide-code
(quote
  (def (hallo x y)
    (return (+ x y))))

;; To keep the above chosen form executable, it needs to be transformed back into a valid `std-lang` syntax

(defn to-std-lang [e]
  (cond
    (= (first e) 'def)
    (list 'var (first (second e))
          (list 'fn (into [] (rest (second e))) (last e)))
    :else
    e))

(to-std-lang
  '(def (hallo x y)
     (return (+ x y))))

;; To make it more obvious that the new form indeed resembles Python, we'd like to get rid of the parentheses somehow.

;; This can be done using the [Blockly](https://developers.google.com/blockly/guides/get-started/what-is-blockly) framework, which takes an XML-string and displays it in a graphical way.

^:kind/hiccup
[:div
 [:script "var xml10 = '<xml><block id=\"funs-h-3-inp-10-10\" type=\"funs-h-3-inp\" x=10 y=10><field name=\"kopf\">hallo</field><value name=\"args-2\"><block id=\"num2-funs-h-3-inp-10-10\" type=\"num\"><field name=\"nummer\">1</field></block></value><value name=\"args-3\"><block id=\"num3-funs-h-3-inp-10-10\" type=\"num\"><field name=\"nummer\">2</field></block></value></block></xml>'"]
 [:div {:id "blocklyDiv10", :style {:height "80px"}}]
 [:script "var workspace10 = Blockly.inject('blocklyDiv10',
{'toolbox': twotiles.toolbox, 'sounds': false})"]
 [:script "const xmlDom10 = Blockly.utils.xml.textToDom(xml10)"]
 [:script "Blockly.Xml.clearWorkspaceAndLoadFromXml(xmlDom10,workspace10)"]
 [:p]]

;; We use the small [twotiles](https://kloimhardt.github.io/twotiles/twotiles_core.js)-library to parse Clojure data structures and turn them into XML

^:kind/hiccup
[:div
 [:script "var twotiles_parse = scittle.core.eval_string(twotiles.parse_clj);"]
 [:script "var xml11 = twotiles_parse('(hallo 1 2)')"]
 [:textarea {:id "blocklyText11" :style {:width "100%"}}]
 [:script "document.getElementById(\"blocklyText11\").innerHTML = xml11"]]

;; Putting `Blockly` and `twotiles` together leads do a graphical representation of our code that now without the parentheses is a further step towards Python resemblance.

^:kind/hiccup
(tiles-render "blocklyDiv12"
              '(def (hallo x y)
                 (return (:tiles/infix (+ x y)))))

;;
;; As you can see, I sneaked the infix feature of `twotiles` into the above example.

;; Of course, to get back to an executable `std-lang` syntax, this infix instruction has to be removed together with the already shown transformation of `def` to `(var ... (fn ...))`. All this is done by the `def.tl` macro used in the preceding section that demonstrates the main example which has also been the main goal of this article.
