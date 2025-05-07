(ns stdlang-book.blockly-python
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

^:kind/println
(l/script :python
  {:require [[xt.lang.base-lib :as k]
             [xt.lang.base-iter :as it]]})

;; It is now possible to transpile lisp forms to code:

(!.py
  (+ 1 2 3))

;; If more than one environment is required, `l/script+` is a way to create an annex
;; that 

;; In For example, let us define the following two annexes, named `:code` and `:live`.

;; Here we define `:code` as a way to use the transpiler
;; to generate Python code, but not use it in any runtime.

^:kind/println
(l/script+ [:code :python]
  {:require [[xt.lang.base-lib :as k]
             [xt.lang.base-iter :as it]]})

;; Here we define `:live` as a way to use the transpiler
;; go generate Python code, and run it in a Node.js runtime.

^:kind/println
(l/script+ [:live :python]
  {:runtime :basic
   :require [[xt.lang.base-lib :as k]
             [xt.lang.base-iter :as it]]})

;; Let us now use these two ways for basic arithmetic.

[ ;; No runtime, just generating code:
 (l/! [:code] (+ 1 2))
 ;; Generating, running in Node.js:
 (l/! [:live] (+ 1 2))]

[(l/! [:code] (do (var hello (fn [x y] (return (+ x y))))
                  (hello 1 2)))
 (l/! [:live] (do (var hello (fn [x y] (return (+ x y))))
                  (hello 1 2)))]

(l/! [:code] (def hu 5))
(l/! [:code] (var hu [] 5))

(def a '(do (def (hello x y) (return (:tiles/infix (+ x y))))
            (hello 1 2)))

(defn redef-def [e]
  (if (list? e)
    (cond
      (= (first e) 'def)
      (list 'var (first (second e))
            (list 'fn (into [] (rest (second e)))
                  (redef-def (last e))))
      (and (keyword? (first e)) (= (namespace (first e)) "tiles"))
      (redef-def (second e))
      :else
      (map redef-def e))
    e))

(defmacro ! [o e]
  (list 'l/! o (redef-def e)))

(namespace :tiles/infix)
(keyword? :tiles/infix)
(redef-def '(:tiles/infix (+ 1 7)))

(! [:code] (do (def (hello x y) (return (:tiles/infix (+ x y))))
               (hello 1 2)))
(! [:live] (do (def (hello x y) (return (:tiles/infix (+ x y))))
               (hello 1 2)))

(l/! [:live]  (var hello (fn [x y] (return (+ x y)))))

(l/! [:live] (do (var hello (fn [x y] (return (+ x y))))
                 (hello 1 2)))

(l/! [:live] (hello 3 4))
