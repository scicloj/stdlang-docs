;; # Introduction

;; Welcome to the walkthrough of std.lang and it's interaction with the javascript runtime.
;; Ideally, the reader should have at least some experience with both clojure and javascript
;; in order to get the most out of the tutorial as the library allows for seamless interop between
;; a clojure runtime and a javascript one - whether it is on the server side - node, quickjs, osascript - as well as on the browser and other embedded js environments.


;; ## Setup

;; Let us briefly explore the std.lang transpiler.

(ns stdlang-book.walkthrough
  (:require [std.lang :as l]))

;; std.lang can be used in different ways:
;; - generate code for different languages
;; - run the code in different runtimes of those languages

^:kindly/hide-code
(require 
 '[scicloj.kindly.v4.kind :as kind]
 '[scicloj.kindly.v4.api :as kindly])

;; To specify a way to use it, we use `l/script`. This will create a runtime
;; for evaluation.

;; In For example, let us define the following two ways,
;; named `:code` and `:live`.

;; Here we define `:code` as a way to use the transpiler
;; to generate Javascript code, but not use it in any runtime.

^:kind/println ; just so the return value of this call is displayed nicely
(l/script+ [:code :js]
  {:require [[xt.lang.base-lib :as k]]})

;; Here we define `:live` as a way to use the transpiler
;; go generate Javascript code, and run it in a Node.js runtime.

^:kind/println
(l/script+ [:live :js]
  {:runtime :basic
   :require [[xt.lang.base-lib :as k]]})

(l/script+ [:live :js]
  {:runtime :basic
   :require [[xt.lang.base-lib :as k]]})

(l/! [:live]
  (k/abs 1))

;; Let us now use these two ways for basic arithmetic.

[ ;; No runtime, just generating code:
 (l/! [:code] (+ 1 2))
 ;; Generating, running in Node.js:
 (l/! [:live] (+ 1 2))]

^:kindly/hide-code
(defn display-output-js-code [code]
  (kind/md
   (format "```js\n%s\n```" code)))

^:kindly/hide-code
(defn display-output-clj-code [code]
  (kind/md
   (format "```clj\n%s\n```" code)))

^:kindly/hide-code
(defn display-output-format [& forms]
  (with-meta
    {:column-names [:form
                    :code
                    :live]
     :row-vectors (->> forms
                       (mapv (fn [[form code-output rt-output]]
                               [(display-output-clj-code (pr-str form))
                                (display-output-js-code  (pr-str code-output))
                                (display-output-clj-code (pr-str rt-output))])))}
    {:kind/table true
     :kindly/hide-code true}))

^:kindly/hide-code
(defmacro display-output-table [& forms]
  
  (with-meta
    {:column-names [:form
                    :code
                    :live]
     :row-vectors (->> forms
                       (mapv (fn [form]
                               `[(display-output-clj-code (pr-str (quote ~form)))
                                 (display-output-js-code  (pr-str (l/! [:code] ~form)))
                                 (pr-str (l/! [:live] ~form))])))}
    {:kind/table true
     :kindly/hide-code true}))





;; # Primitives
;;
;; The seven primitive data types in JavaScript are string, number, bigint, boolean, undefined, symbol, and null.
;; We work with examples from: https://www.w3docs.com/learn-javascript/methods-of-primitives.html
;;
;; From the Javascript Runtime perspective, primitives are extremely important to understand for designing fast programs. They offer the following traits:
;;
;; 1. Immutability: Once a primitive value is created, it cannot be altered. For instance, when you create a string, you cannot change its individual characters. Any operation that seems to change a primitive actually creates a new primitive. Example:
;; 2. Memory Efficiency: Primitives are stored directly in the stack memory where the variable is located. This direct storage makes access to primitive values faster and more memory-efficient than objects. Example:
;; 3. Simple and Fast: Primitives are straightforward in their representation, making them simpler and faster to process compared to objects. They don't have the overhead of object properties and methods. Example:


;; ### Null

(display-output-format
 ['nil
  (l/! [:code] nil)
  (l/! [:live] nil)])

;; ### Undefined

(display-output-format
 ['undefined
  (l/! [:code] undefined)
  (l/! [:live] undefined)])

;; ### Boolean

(display-output-format
 ['true
  (l/! [:code] true)
  (l/! [:live] true)]
 ['false
  (l/! [:code] false)
  (l/! [:live] false)])

;; ### Strings

(display-output-format
 ['"3"
  (l/! [:code] "3")
  (l/! [:live] "3")]
 ["Hello World"
  (l/! [:code] "Hello World")
  (l/! [:live] "Hello World")])


;; ### Numbers

(display-output-format
 ['3
  (l/! [:code] 3)
  (l/! [:live] 3)]
 ['1.5
  (l/! [:code] 1.5)
  (l/! [:live] 1.5)]
 ['1.54444444444444
  (l/! [:code] 1.54444444444444)
  (l/! [:live] 1.54444444444444)])

;; ### Bigint

(display-output-format
 ['(. (BigInt "0x1fffffffffffff")
      (toString))
  (l/! [:code] (. (BigInt "0x1fffffffffffff")
                  (toString)))
  (l/! [:live] (. (BigInt "0x1fffffffffffff")
                  (toString)))])

;; ### Symbol

(display-output-format
 ['(. (Symbol "hello")
      (toString))
  (l/! [:code] (. (Symbol "hello")
                  (toString)))
  (l/! [:live] (. (Symbol "hello")
                  (toString)))])

;; ### Regex

(display-output-format
 [#"^[Hh]ello d$"
  (l/! [:code] #"^[Hh]ello d$")
  (l/! [:live] #"^[Hh]ello d$")])


;; # Operations - Math

;; ### Addition

(display-output-format
 ['(+ 1 2 3)
  (l/! [:code] (+ 1 2 3))
  (l/! [:live] (+ 1 2 3))]
 ['(+ 1 10)
  (l/! [:code] (+ 1 10))
  (l/! [:live] (+ 1 10))])




;; # 




(display-output-format
 ['(do (var out := [])
       (k/for:array [e [1 2 3 4]]
         (if (> e 3)
           (break))
         (x:arr-push out e))
       out)
  (l/! [:code]
    (do (var out := [])
        (k/for:array [e [1 2 3 4]]
          (if (> e 3)
            (break))
          (x:arr-push out e))
        out))
  (l/! [:live]
    (do (var out := [])
        (k/for:array [e [1 2 3 4]]
          (if (> e 3)
            (break))
          (x:arr-push out e))
        out))])




;; Basic arithmetic:

(display-output-table
  (+ 1 1)
  (+ 0.1 0.2)
  (- 8 1)
  (* 10 2)
  (/ 35 5)
  (/ 5 2))


^:kindly/hide-code
(comment

  
  ^:kind/println
  (l/script :js
    {:runtime :basic
     :require [[xt.lang.base-lib :as k]]})

  (!.js
    ((fn []
       (return (Math.pow 2 3)))))

  ;; `l/script` with the `{:runtime :basic}` option will setup a node runtime and also provide an evaluation macro - `!.js`. Forms within the macro will transpile into javascript code evaluated in the node runtime and the results returned back to the Clojure runtime:

  (!.js
    [(+ 1 2 3 4)
     (Math.pow 4 4)
     (typeof [1 2 3 4])])

  (kindly/check [10 256 "object"])
  
  
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

  )
