;; # Walkthrough 👣

;; Let us briefly explore the std.lang transpiler. 

;; ## Setup

(ns stdlang-book.walkthrough
  (:require [std.lang :as l]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]
            [charred.api :as charred]
            [clojure.string :as str]))

;; Stdlang can be used in different ways:
;; - generate code for different languages
;; - run the code in different runtimes of those languages

;; To specify a way to use it, we use `l/script`.

;; For example, let us define the following two ways,
;; named `:code` and `:node`.

;; Here we define `:code` as a way to use the transpiler
;; to generate Javascript code, but not use it in any runtime.

^:kind/println ; just so the return value of this call is displayed nicely
(l/script+ [:code :js])

;; Here we define `:node` as a way to use the transpiler
;; go generate Javascript code, and run it in a Node.js runtime.

^:kind/println
(l/script+ [:node :js]
           {:runtime :basic})

;; Let us now use these two ways for basic arithmetic.

[;; No runtime, just generating code:
 (l/! [:code] (+ 1 2))
 ;; Generating, running in Node.js:
 (l/! [:node] (+ 1 2))]

^:kindly/hide-code
(defn show-js-code [code]
  (kind/md
   (format "```js\n%s\n```" code)))

^:kindly/hide-code
(defn show-clj-code [code]
  (kind/md
   (format "```clj\n%s\n```" code)))

^:kindly/hide-code
(defmacro show-code-and-node [& forms]
  (with-meta
    {:column-names [:form :code :node]
     :row-vectors (->> forms
                       (mapv (fn [form]
                               `[(show-clj-code (pr-str (quote ~form)))
                                 (show-js-code (l/! [:code] ~form))
                                 (l/! [:node] ~form)])))}
    {:kind/table true
     :kindly/hide-code true}))

;; ## Language
;; Let us see how to generate some common Javascript idioms.
;; We will follow examples from the
;; [Learn X in Y minutes](https://learnxinyminutes.com/) tutorial
;; [Where X=Javascript](https://learnxinyminutes.com/javascript).

;; ### Numbers, Strings, and Operators

;; Number types:

(show-code-and-node
 3
 1.5)

;; Basic arithmetic:

(show-code-and-node
 (+ 1 1)
 (+ 0.1 0.2)
 (- 8 1)
 (* 10 2)
 (/ 35 5)
 (/ 5 2))

;; ## Data visualization with Javascript
(kind/hiccup
 [:div
  [:script
   (l/! [:code]
        (do (var m (L.map document.currentScript.parentElement))
            (m.setView [-37.84 144.95]
                       11)
            (-> (L.tileLayer.provider "OpenStreetMap.Mapnik")
                (. (addTo m)))
            (-> [-37.9 144.8]
                L.marker
                (. (addTo m))
                (. (bindPopup "<i style='color:purple'>Have you been here?</i>"))
                (. (openPopup)))))]]
 {:html/deps [:leaflet]
  :style {:height "400px"}})

