;; # Walkthrough Javascript

;; Welcome to the walkthrough of std.lang and it's interaction with the javascript runtime.
;; Ideally, the reader should have at least some experience with both clojure and javascript
;; in order to get the most out of the tutorial as the library allows for seamless interop between
;; a clojure runtime and a javascript one - whether it is on the server side - node, quickjs, osascript - as well as on the browser and other embedded js environments.


;; ## Setup

;; Let us briefly explore the std.lang transpiler.

(ns stdlang-book.walkthrough-js
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
(l/script :js
  {:require [[xt.lang.base-lib :as k]]})

;; It is now possible to transpile lisp forms to code:

(!.js
  (+ 1 2 3))


;; If more than one environment is required, `l/script+` is a way to create an annex
;; that 

;; In For example, let us define the following two annexes, named `:code` and `:live`.

;; Here we define `:code` as a way to use the transpiler
;; to generate Javascript code, but not use it in any runtime.

^:kind/println
(l/script+ [:code :js]
  {:require [[xt.lang.base-lib :as k]]})

;; Here we define `:live` as a way to use the transpiler
;; go generate Javascript code, and run it in a Node.js runtime.

^:kind/println
(l/script+ [:live :js]
  {:runtime :basic
   :require [[xt.lang.base-lib :as k]]})

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
                                (display-output-js-code  code-output)
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





;; # Types - Primitives
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
  (l/! [:live] 1.54444444444444)]
 ['NaN
  (l/! [:code] NaN)
  (l/! [:live] NaN)]
 ['Infinity
  (l/! [:code] Infinity)
  (l/! [:live] Infinity)]
 ['(- Infinity)
  (l/! [:code] (- Infinity))
  (l/! [:live] (. (- Infinity)
                  (toString)))])

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

;; # Types - Additional

;; ### Regex

(display-output-format
 [#"^[Hh]ello d$"
  (l/! [:code] #"^[Hh]ello d$")
  (l/! [:live] #"^[Hh]ello d$")])


;; # Types - Collection

;; ### Arrays

(display-output-format
 ['[1 2 3 4]
  (l/! [:code] [1 2 3 4])
  (l/! [:live] [1 2 3 4])]
 ['["hello" ["world"]]
  (l/! [:code] ["hello" ["world"]])
  (l/! [:live] ["hello" ["world"]])])

;; ### Objects

(display-output-format
 ['{:a 1 :b 2 :c 3}
  (l/! [:code] {:a 1 :b 2 :c 3})
  (l/! [:live] {:a 1 :b 2 :c 3})]
 ['{:a {:b {:c 3}}}
  (l/! [:code] {:a {:b {:c 3}}})
  (l/! [:live] {:a {:b {:c 3}}})])

;; ### Objects - tab

(display-output-format
 ['(tab ["a" 1] ["b" 2] ["c" 3])
  (l/! [:code] (tab ["a" 1] ["b" 2] ["c" 3]))
  (l/! [:live] (tab ["a" 1] ["b" 2] ["c" 3]))])


;; # Types - Checks

;; ### Typeof

(display-output-format
 ['(typeof nil)
  (l/! [:code] (typeof nil))
  (l/! [:live] (typeof nil))]
 ['(typeof undefined)
  (l/! [:code] (typeof undefined))
  (l/! [:live] (typeof undefined))]
 ['(typeof NaN)
  (l/! [:code] (typeof NaN))
  (l/! [:live] (typeof NaN))]
 ['(typeof 1)
  (l/! [:code] (typeof 1))
  (l/! [:live] (typeof 1))]
 ['(typeof true)
  (l/! [:code] (typeof true))
  (l/! [:live] (typeof true))]
 ['(typeof "hello")
  (l/! [:code] (typeof "hello"))
  (l/! [:live] (typeof "hello"))]
 ['(typeof (Symbol "hello"))
  (l/! [:code] (typeof (Symbol "hello")))
  (l/! [:live] (typeof (Symbol "hello")))]
 ['(typeof (BigInt "0x1fffffffffffff"))
  (l/! [:code] (typeof (BigInt "0x1fffffffffffff")))
  (l/! [:live] (typeof (BigInt "0x1fffffffffffff")))]
 ['(typeof #"^[Hh]ello d$")
  (l/! [:code] (typeof #"^[Hh]ello d$"))
  (l/! [:live] (typeof #"^[Hh]ello d$"))]
 ['(typeof [1 2 3])
  (l/! [:code] (typeof [1 2 3]))
  (l/! [:live] (typeof [1 2 3]))]
 ['(typeof {:a 1})
  (l/! [:code] (typeof {:a 1}))
  (l/! [:live] (typeof {:a 1}))])

;; ### Instanceof

(display-output-format
 ['(instanceof #"^[Hh]ello d$" RegExp)
  (l/! [:code] (instanceof #"^[Hh]ello d$" RegExp))
  (l/! [:live] (instanceof #"^[Hh]ello d$" RegExp))])



;; # Operations - Assignment

;; ### Var

(display-output-format
 ['(do (var x 1)
       x)
  (l/! [:code]
    (do (var x 1)
        x))
  (l/! [:live]
    (do (var x 1)
        x))])

;; ### Const

(display-output-format
 ['(do (const x 1)
       x)
  (l/! [:code]
    (do (const x 1)
        x))
  (l/! [:live]
    (do (const x 1)
        x))])


;; ### ReAssign

(display-output-format
 ['(do (var x 1)
       (:= x 10)
       x)
  (l/! [:code]
    (do (var x 1)
        (:= x 10)
        x))
  (l/! [:live]
    (do (var x 1)
        (:= x 10)
        x))])


;; # Operations - Logic

;; ### Negation
(display-output-format
 ['(not true)
  (l/! [:code] (not true))
  (l/! [:live] (not true))]
 ['(not false)
  (l/! [:code] (not false))
  (l/! [:live] (not false))]
 ['(not 1)
  (l/! [:code] (not 1))
  (l/! [:live] (not 1))]
 ['(not 0)
  (l/! [:code] (not 0))
  (l/! [:live] (not 0))])

;; ### Or
(display-output-format
 ['(or 0 false)
  (l/! [:code] (or 0 false))
  (l/! [:live] (or 0 false))]
 ['(or 1 false)
  (l/! [:code] (or 1 false))
  (l/! [:live] (or 1 false))]
 ['(or 0 true)
  (l/! [:code] (or 0 true))
  (l/! [:live] (or 0 true))])

;; ### And
(display-output-format
 ['(and 0 false)
  (l/! [:code] (and 0 false))
  (l/! [:live] (and 0 false))]
 ['(and 1 false)
  (l/! [:code] (and 1 false))
  (l/! [:live] (and 1 false))]
 ['(and 0 true)
  (l/! [:code] (and 0 true))
  (l/! [:live] (and 0 true))])

;; ### Ternary
(display-output-format
 ['(:? true 1 2)
  (l/! [:code] (:? true 1 2))
  (l/! [:live] (:? true 1 2))]
 ['(:? (or 0 0) 1 2)
  (l/! [:code] (:? (or 0 0) 1 2))
  (l/! [:live] (:? (or 0 0) 1 2))])


;; # Operations - Math

;; ### Addition

(display-output-format
 ['(+ 1 2 3)
  (l/! [:code] (+ 1 2 3))
  (l/! [:live] (+ 1 2 3))]
 ['(+ 1 10)
  (l/! [:code] (+ 1 10))
  (l/! [:live] (+ 1 10))])

;; ### Subtraction 

(display-output-format
 ['(- 10)
  (l/! [:code] (- 10))
  (l/! [:live] (- 10))]
 ['(- 10 1.1)
  (l/! [:code] (- 10 1.1))
  (l/! [:live] (- 10 1.1))]
 ['(- 4 3 2 1.1)
  (l/! [:code] (- 4 3 2 1.1))
  (l/! [:live] (- 4 3 2 1.1))])

;; ### Multiplication

(display-output-format
 ['(* 10 20)
  (l/! [:code] (* 10 20))
  (l/! [:live] (* 10 20))]
 ['(* 4 3.3 2.2 1.1)
  (l/! [:code] (* 4 3.3 2.2 1.1))
  (l/! [:live] (* 4 3.3 2.2 1.1))])

;; ### Division

(display-output-format
 ['(/ 10)
  (l/! [:code] (/ 10))
  (l/! [:live] (/ 10))]
 ['(/ 10 20)
  (l/! [:code] (/ 10 20))
  (l/! [:live] (/ 10 20))]
 ['(/ 4 3.3 2.2 1.1)
  (l/! [:code] (/ 4 3.3 2.2 1.1))
  (l/! [:live] (/ 4 3.3 2.2 1.1))])


;; ### Pow

(display-output-format
 ['(pow 10 2)
  (l/! [:code] (pow 10 2))
  (l/! [:live] (pow 10 2))]
 ['(pow 0.5 0.2)
  (l/! [:code] (pow 0.5 0.2))
  (l/! [:live] (pow 0.5 0.2))])


;; ### Mod

(display-output-format
 ['(mod 1123 7)
  (l/! [:code] (mod 1123 7))
  (l/! [:live] (mod 1123 7))]
 ['(mod 1123 7.1)
  (l/! [:code] (mod 1123 7.1))
  (l/! [:live] (mod 1123 7.1))])



;; # Operations - Comparison

;; ### Equals

(display-output-format
 ['(== 1 1)
  (l/! [:code] (== 1 1))
  (l/! [:live] (== 1 1))]
 ['(== 1 "1")
  (l/! [:code] (== 1 "1"))
  (l/! [:live] (== 1 "1"))]
 ['(== "hello" "hello")
  (l/! [:code] (== "hello" "hello"))
  (l/! [:live] (== "hello" "hello"))])

;; ### Triple Equals

(display-output-format
 ['(=== 1 1)
  (l/! [:code] (=== 1 1))
  (l/! [:live] (=== 1 1))]
 ['(=== 1 "1")
  (l/! [:code] (=== 1 "1"))
  (l/! [:live] (=== 1 "1"))]
 ['(=== "hello" "hello")
  (l/! [:code] (=== "hello" "hello"))
  (l/! [:live] (=== "hello" "hello"))])

;; ### Not Equals

(display-output-format
 ['(not= 1 2)
  (l/! [:code] (not= 1 2))
  (l/! [:live] (not= 1 2))]
 ['(not= 1 "1")
  (l/! [:code] (not= 1 "1"))
  (l/! [:live] (not= 1 "1"))]
 ['(not= "hello" "hello")
  (l/! [:code] (not= "hello" "hello"))
  (l/! [:live] (not= "hello" "hello"))])

;; ### Less Than

(display-output-format
 ['(< 1 2)
  (l/! [:code] (< 1 2))
  (l/! [:live] (< 1 2))]
 ['(< 1 1)
  (l/! [:code] (< 1 1))
  (l/! [:live] (< 1 1))]
 ['(< 1 "2")
  (l/! [:code] (< 1 "2"))
  (l/! [:live] (< 1 "2"))])

;; ### Less Than Equals

(display-output-format
 ['(<= 1 2)
  (l/! [:code] (<= 1 2))
  (l/! [:live] (<= 1 2))]
 ['(<= 1 1)
  (l/! [:code] (<= 1 1))
  (l/! [:live] (<= 1 1))]
 ['(<= 1 "1")
  (l/! [:code] (<= 1 "1"))
  (l/! [:live] (<= 1 "1"))])

;; ### Greater Than

(display-output-format
 ['(> 3 2)
  (l/! [:code] (> 3 2))
  (l/! [:live] (> 3 2))]
 ['(> 3 3)
  (l/! [:code] (> 3 3))
  (l/! [:live] (> 3 3))]
 ['(> 3 "2")
  (l/! [:code] (> 3 "2"))
  (l/! [:live] (> 3 "2"))])

;; ### Greater Than Equals

(display-output-format
 ['(>= 3 2)
  (l/! [:code] (>= 3 2))
  (l/! [:live] (>= 3 2))]
 ['(>= 3 3)
  (l/! [:code] (>= 3 3))
  (l/! [:live] (>= 3 3))]
 ['(>= 3 "3")
  (l/! [:code] (>= 3 "3"))
  (l/! [:live] (>= 3 "3"))])

;; # Operations - Counter

;; ### Increment

(display-output-format
 ['(do (var x 1)
       (:++ x)
       x)
  (l/! [:code]
    (do (var x 1)
        (:++ x)
        x))
  (l/! [:live]
    (do (var x 1)
        (:++ x)
        x))])


;; ### Increment By

(display-output-format
 ['(do (var x 1)
       (:+= x 10)
       x)
  (l/! [:code]
    (do (var x 1)
        (:+= x 10)
        x))
  (l/! [:live]
    (do (var x 1)
        (:+= x 10)
        x))])


;; ### Decrement

(display-output-format
 ['(do (var x 5)
       (:-- x)
       x)
  (l/! [:code]
    (do (var x 5)
        (:-- x)
        x))
  (l/! [:live]
    (do (var x 5)
        (:-- x)
        x))])

;; ### Decrement By

(display-output-format
 ['(do (var x 5)
       (:-= x 50)
       x)
  (l/! [:code]
    (do (var x 5)
        (:-= x 50)
        x))
  (l/! [:live]
    (do (var x 5)
        (:-= x 50)
        x))])


;; ### Multiply By

(display-output-format
 ['(do (var x 5)
       (:*= x 50)
       x)
  (l/! [:code]
    (do (var x 5)
        (:*= x 50)
        x))
  (l/! [:live]
    (do (var x 5)
        (:*= x 50)
        x))])


;; # Operations - Bitwise

;; ### Bitwise Or

(display-output-format
 ['(b:| 7 8)
  (l/! [:code]
    (b:| 7 8))
  (l/! [:live]
    (b:| 7 8))]
 ['(b:| 7 7)
  (l/! [:code]
    (b:| 7 7))
  (l/! [:live]
    (b:| 7 7))]
 ['(b:| 7 0)
  (l/! [:code]
    (b:| 7 0))
  (l/! [:live]
    (b:| 7 0))])

;; ### Bitwise And

(display-output-format
 ['(b:& 7 8)
  (l/! [:code]
    (b:& 7 8))
  (l/! [:live]
    (b:& 7 8))]
 ['(b:& 7 7)
  (l/! [:code]
    (b:& 7 7))
  (l/! [:live]
    (b:& 7 7))]
 ['(b:& 7 0)
  (l/! [:code]
    (b:& 7 0))
  (l/! [:live]
    (b:& 7 0))])

;; ### Bitwise Xor

(display-output-format
 ['(b:xor 7 8)
  (l/! [:code]
    (b:xor 7 8))
  (l/! [:live]
    (b:xor 7 8))]
 ['(b:xor 7 7)
  (l/! [:code]
    (b:xor 7 7))
  (l/! [:live]
    (b:xor 7 7))]
 ['(b:xor 7 0)
  (l/! [:code]
    (b:xor 7 0))
  (l/! [:live]
    (b:xor 7 0))])

;; ### Bitshift Right

(display-output-format
 ['(b:>> 128 3)
  (l/! [:code]
    (b:>> 128 3))
  (l/! [:live]
    (b:>> 128 3))])


;; ### Bitshift Left

(display-output-format
 ['(b:<< 128 3)
  (l/! [:code]
    (b:<< 128 3))
  (l/! [:live]
    (b:<< 128 3))])


;; # Keywords - Functions

(display-output-format
 ['(fn [x y] (return (+ x y)))
  (l/! [:code]
    (fn [x y] (return (+ x y))))
  (l/! [:live]
    (fn [x y] (return (+ x y))))]
 ['(do (var hello (fn [x y] (return (+ x y))))
       (hello 1 2))
  (l/! [:code]
    (do (var hello (fn [x y] (return (+ x y))))
        (hello 1 2)))
  (l/! [:live]
    (do (var hello (fn [x y] (return (+ x y))))
        (hello 1 2)))])

;; # Keywords - Blocks

;; ### if block

(display-output-format
 ['(do (var arr [1 2 3 4 5])
       (var out)
       (if (< (x:len arr) 10)
         (:= out true)
         (:= out false))
       out)
  (l/! [:code]
    (do (var arr [1 2 3 4 5])
        (var out)
        (if (< (x:len arr) 10)
          (:= out true)
          (:= out false))
        out))
  (l/! [:live]
    (do (var arr [1 2 3 4 5])
        (var out)
        (if (< (x:len arr) 10)
          (:= out true)
          (:= out false))
        out))])

;; ### cond block

(display-output-format
 ['(do (var arr [1 2 3 4 5])
       (var out)
       (cond (< (x:len arr) 5)
             (:= out "1")

             (< (x:len arr) 10)
             (:= out "2")
             
             :else
             (:= out "3"))
       out)
  (l/! [:code]
    (do (var arr [1 2 3 4 5])
       (var out)
       (cond (< (x:len arr) 5)
             (:= out "1")

             (< (x:len arr) 10)
             (:= out "2")
             
             :else
             (:= out "3"))
       out))
  (l/! [:live]
    (do (var arr [1 2 3 4 5])
       (var out)
       (cond (< (x:len arr) 5)
             (:= out "1")

             (< (x:len arr) 10)
             (:= out "2")
             
             :else
             (:= out "3"))
       out))])

;; ### while block
(display-output-format
 ['(do (var x [])
       (var i 0)
       (while (< i 5)
         (x:arr-push x i)
         (:++ i))
       x)
  (l/! [:code]
    (do (var x [])
       (var i 0)
       (while (< i 5)
         (x:arr-push x i)
         (:++ i))
       x))
  (l/! [:live]
    (do (var x [])
        (var i 0)
        (while (< i 5)
          (x:arr-push x i)
          (:++ i))
        x))])

;; ### for block
(display-output-format
 ['(do (var arr [])
       (for [(var i 1) (< i 5) (:++ i)]
         (x:arr-push arr i))
       arr)
  (l/! [:code]
    (do (var arr [])
       (for [(var i 1) (< i 5) (:++ i)]
         (x:arr-push arr i))
       arr))
  (l/! [:live]
    (do (var arr [])
       (for [(var i 1) (< i 5) (:++ i)]
         (x:arr-push arr i))
       arr))])

;; ### case block
(display-output-format
 ['(do (var arr 1)
       (var out)
       (case arr
         1 (do (:= out 1)
               (break))
         2 (do (:= out 2)
               (break)))
       out)
  (l/! [:code]
    (do (var arr 1)
        (var out)
        (case arr
          1 (do (:= out 1)
                (break))
          2 (do (:= out 2)
                (break)))
        out))
  (l/! [:live]
    (do (var arr 1)
        (var out)
        (case arr
          1 (do (:= out 1)
                (break))
          2 (do (:= out 2)
                (break)))
        out))])


;; ### try/catch block
(display-output-format
 ['(do (var out "hello")
       (try
         (throw 1)
         (catch e (:= out "world")))
       out)
  (l/! [:code]
    (do (var out "hello")
       (try
         (throw 1)
         (catch e (:= out "world")))
       out))
  (l/! [:live]
    (do (var out "hello")
       (try
         (throw 1)
         (catch e (:= out "world")))
       out))])

;; # Base Lib - For 

;; ### for:array

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

;; ### for:object

(display-output-format
 ['(do
     (var out := [])
     (var obj := {:a 1 :b 2})
     (k/for:object [[k v] obj]
       (x:arr-push out [k v]))
     out)
  (l/! [:code]
    (do
     (var out := [])
     (var obj := {:a 1 :b 2})
     (k/for:object [[k v] obj]
       (x:arr-push out [k v]))
     out))
  (l/! [:live]
    (do
     (var out := [])
     (var obj := {:a 1 :b 2})
     (k/for:object [[k v] obj]
       (x:arr-push out [k v]))
     out))])


;; ### for:index

(display-output-format
 ['(do
     (var out := [])
     (k/for:index [i [0 10 2]]
       (x:arr-push out i))
     out)
  (l/! [:code]
    (do
     (var out := [])
     (k/for:index [i [0 10 2]]
       (x:arr-push out i))
     out))
  (l/! [:live]
    (do
     (var out := [])
     (k/for:index [i [0 10 2]]
       (x:arr-push out i))
     out))])


;; ### for:return

(display-output-format
 ['(do
    (var out)
    (var success (fn [cb]
                   (cb nil "OK")))
    (k/for:return [[ret err] (success (x:callback))]
      {:success (:= out ret)
       :error   (:= out err)})
    out)
  (l/! [:code]
    (do
    (var out)
    (var success (fn [cb]
                   (cb nil "OK")))
    (k/for:return [[ret err] (success (x:callback))]
      {:success (:= out ret)
       :error   (:= out err)})
    out))
  (l/! [:live]
    (do
    (var out)
    (var success (fn [cb]
                   (cb nil "OK")))
    (k/for:return [[ret err] (success (x:callback))]
      {:success (:= out ret)
       :error   (:= out err)})
    out))])


;; ### for:try

(display-output-format
 ['(do
     (var out := nil)
     (k/for:try [[ret err] (do:> (x:err "hello"))]
       {:success (:= out ret)
        :error   (:= out err)})
     out)
  (l/! [:code]
    (do
     (var out := nil)
     (k/for:try [[ret err] (do:> (x:err "hello"))]
       {:success (:= out ret)
        :error   (:= out err)})
     out))
  (l/! [:live]
    (do
     (var out := nil)
     (k/for:try [[ret err] (do:> (x:err "hello"))]
       {:success (:= out ret)
        :error   (:= out err)})
     out))])


;; ### for:async

(display-output-format
 ['(do
     (var out := nil)
     (k/for:async [[ret err] (+ 1 2 3)]
       {:success (:= out ret)
        :error (:= out err)}))
  (l/! [:code]
    (do
      (var out := nil)
      (k/for:async [[ret err] (+ 1 2 3)]
        {:success (:= out ret)
         :error (:= out err)})))
  (l/! [:live]
    (do
      (var out := nil)
      (k/for:async [[ret err] (+ 1 2 3)]
        {:success (:= out ret)
         :error (:= out err)})))])


;; # Base Lib - Util


;; ### invoke

(display-output-format
 ['(k/invoke k/add 1 2)
  (l/! [:code]
    (k/invoke k/add 1 2))
  (l/! [:live]
    (k/invoke k/add 1 2))])

;; ### apply

(display-output-format
 ['(k/apply (fn:> [a b] (+ a b))
            [1 2])
  (l/! [:code]
    (k/apply (fn:> [a b] (+ a b))
             [1 2]))
  (l/! [:live]
    (k/apply (fn:> [a b] (+ a b))
             [1 2]))])

;; ### eval

(display-output-format
 ['(k/eval "1+2")
  (l/! [:code]
    (k/eval "1+2"))
  (l/! [:live]
    (k/eval "1+2"))]
 ['(k/apply k/eval ["1+2"])
  (l/! [:code]
    (k/apply k/eval ["1+2"]))
  (l/! [:live]
    (k/apply k/eval ["1+2"]))])

;; # Base Lib - Math

;; ### eq

(display-output-format
 ['[(k/eq 2 2) (k/eq 2 1)]
  (l/! [:code]
    [(k/eq 2 2) (k/eq 2 1)])
  (l/! [:live]
    [(k/eq 2 2) (k/eq 2 1)])]
 ['(k/apply k/eq [1 1])
  (l/! [:code]
    (k/apply k/eq [1 1]))
  (l/! [:live]
    (k/apply k/eq [1 1]))])

;; ### neq

(display-output-format
 ['[(k/neq 2 2) (k/neq 2 1)]
  (l/! [:code]
    [(k/neq 2 2) (k/neq 2 1)])
  (l/! [:live]
    [(k/neq 2 2) (k/neq 2 1)])]
 ['(k/apply k/neq [1 1])
  (l/! [:code]
    (k/apply k/neq [1 1]))
  (l/! [:live]
    (k/apply k/neq [1 1]))])

;; ### add

(display-output-format
 ['(k/add 1 2)
  (l/! [:code]
    (k/add 1 2))
  (l/! [:live]
    (k/add 1 2))]
 ['(k/apply k/add [1 2])
  (l/! [:code]
    (k/apply k/add [1 2]))
  (l/! [:live]
    (k/apply k/add [1 3]))])

;; ### sub 

(display-output-format
 ['(k/sub 1 2)
  (l/! [:code]
    (k/sub 1 2))
  (l/! [:live]
    (k/sub 1 2))]
 ['(k/apply k/sub [1 2])
  (l/! [:code]
    (k/apply k/sub [1 2]))
  (l/! [:live]
    (k/apply k/sub [1 3]))])

;; ### mul

(display-output-format
 ['(k/mul 10 10)
  (l/! [:code]
    (k/mul 10 10))
  (l/! [:live]
    (k/mul 10 10))]
 ['(k/apply k/mul [1 2])
  (l/! [:code]
    (k/apply k/mul [1 2]))
  (l/! [:live]
    (k/apply k/mul [1 3]))])

;; ### div

(display-output-format
 ['(k/div 10 2)
  (l/! [:code]
    (k/div 10 2))
  (l/! [:live]
    (k/div 10 2))]
 )

;; ### greater than

(display-output-format
 ['[(k/gt 2 2) (k/gt 2 1)]
  (l/! [:code]
    [(k/gt 2 2) (k/gt 2 1)])
  (l/! [:live]
    [(k/gt 2 2) (k/gt 2 1)])]
 ['(k/arr-sort [1 2 3 4 5 6] k/identity k/gt)
  (l/! [:code]
    (k/arr-sort [1 2 3 4 5 6] k/identity k/gt))
  (l/! [:live]
    (k/arr-sort [1 2 3 4 5 6] k/identity k/gt))])

;; ### less than

(display-output-format
 ['[(k/lt 2 2)  (k/lt 1 2)]
  (l/! [:code]
    [(k/lt 2 2)  (k/lt 1 2)])
  (l/! [:live]
    [(k/lt 2 2)  (k/lt 1 2)])]
 ['(k/arr-sort [6 5 4 3 2 1] k/identity k/lt)
  (l/! [:code]
    (k/arr-sort [6 5 4 3 2 1] k/identity k/lt))
  (l/! [:live]
    (k/arr-sort [6 5 4 3 2 1] k/identity k/lt))])

;; ### greater than equals

(display-output-format
 ['[(k/gte 2 2) (k/gte 2 1)]
  (l/! [:code]
    [(k/gte 2 2) (k/gte 2 1)])
  (l/! [:live]
    [(k/gte 2 2) (k/gte 2 1)])]
 ['(k/arr-sort [1 2 3 4 5 6] k/identity k/gte)
  (l/! [:code]
    (k/arr-sort [1 2 3 4 5 6] k/identity k/gte))
  (l/! [:live]
    (k/arr-sort [1 2 3 4 5 6] k/identity k/gte))])


;; ### less than equals

(display-output-format
 ['[(k/lte 2 2)  (k/lte 1 2)]
  (l/! [:code]
    [(k/lte 2 2)  (k/lte 1 2)])
  (l/! [:live]
    [(k/lte 2 2)  (k/lte 1 2)])]
 ['(k/arr-sort [6 5 4 3 2 1] k/identity k/lte)
  (l/! [:code]
    (k/arr-sort [6 5 4 3 2 1] k/identity k/lte))
  (l/! [:live]
    (k/arr-sort [6 5 4 3 2 1] k/identity k/lte))])

;; ### neg

(display-output-format
 ['[(k/neg 1) (k/neg 0) (k/neg -1)]
  (l/! [:code]
    [(k/neg 1) (k/neg 0) (k/neg -1)])
  (l/! [:live]
    [(k/neg 1) (k/neg 0) (k/neg -1)])]
 ['(k/arr-map [1 2 3 4 5] k/neg)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/neg))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/neg))])

;; ### inc

(display-output-format
 ['(k/inc 1)
  (l/! [:code]
    (k/inc 1))
  (l/! [:live]
    (k/inc 1))]
 ['(k/arr-map [1 2 3 4 5] k/inc)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/inc))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/inc))])

;; ### dec

(display-output-format
 ['(k/dec 1)
  (l/! [:code]
    (k/dec 1))
  (l/! [:live]
    (k/dec 1))]
 ['(k/arr-map [1 2 3 4 5] k/dec)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/dec))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/dec))])

;; ### zero?

(display-output-format
 ['[(k/zero? 1)
    (k/zero? 0)]
  (l/! [:code]
    [(k/zero? 1)
     (k/zero? 0)])
  (l/! [:live]
    [(k/zero? 1)
     (k/zero? 0)])]
 ['(k/arr-map [0 2 0 4 5] k/zero?)
  (l/! [:code]
    (k/arr-map [0 2 0 4 5] k/zero?))
  (l/! [:live]
    (k/arr-map [0 2 0 4 5] k/zero?))])

;; ### pos?

(display-output-format
 ['[(k/pos? 1)
    (k/pos? 0)]
  (l/! [:code]
    [(k/pos? 1)
     (k/pos? 0)])
  (l/! [:live]
    [(k/pos? 1)
     (k/pos? 0)])]
 ['(k/arr-map [0 2 0 4 5] k/pos?)
  (l/! [:code]
    (k/arr-map [0 2 0 4 5] k/pos?))
  (l/! [:live]
    (k/arr-map [0 2 0 4 5] k/pos?))])

;; ### neg?

(display-output-format
 ['[(k/neg? -1)
    (k/neg? 0)]
  (l/! [:code]
    [(k/neg? -1)
     (k/neg? 0)])
  (l/! [:live]
    [(k/neg? -1)
     (k/neg? 0)])]
 ['(k/arr-map [0 -2 0 -4 5] k/neg?)
  (l/! [:code]
    (k/arr-map [0 -2 0 -4 5] k/neg?))
  (l/! [:live]
    (k/arr-map [0 -2 0 -4 5] k/neg?))])

;; ### even?

(display-output-format
 ['[(k/even? 2)
    (k/even? 1)]
  (l/! [:code]
    [(k/even? 2)
     (k/even? 1)])
  (l/! [:live]
    [(k/even? 2)
     (k/even? 1)])]
 ['(k/arr-map [1 2 3 4 5] k/even?)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/even?))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/even?))])

;; ### odd?

(display-output-format
 ['[(k/odd? 2)
    (k/odd? 1)]
  (l/! [:code]
    [(k/odd? 2)
     (k/odd? 1)])
  (l/! [:live]
    [(k/odd? 2)
     (k/odd? 1)])]
 ['(k/arr-map [1 2 3 4 5] k/odd?)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/odd?))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/odd?))])

;; ### abs

(display-output-format
 ['[(k/abs -1) (k/abs 1)]
  (l/! [:code]
    [(k/abs -1) (k/abs 1)])
  (l/! [:live]
    [(k/abs -1) (k/abs 1)])]
 ['(k/arr-map [0 -2 0 -4 5] k/abs)
  (l/! [:code]
    (k/arr-map [0 -2 0 -4 5] k/abs))
  (l/! [:live]
    (k/arr-map [0 -2 0 -4 5] k/abs))])

;; ### max

(display-output-format
 ['(k/max 1 2 3 2)
  (l/! [:code]
    (k/max 1 2 3 2))
  (l/! [:live]
    (k/max 1 2 3 2))]
 ['(k/apply k/max [1 2 3 2])
  (l/! [:code]
    (k/apply k/max [1 2 3 2]))
  (l/! [:live]
    (k/apply k/max [1 2 3 2]))])

;; ### min

(display-output-format
 ['(k/min 1 2 3 2)
  (l/! [:code]
    (k/min 1 2 3 2))
  (l/! [:live]
    (k/min 1 2 3 2))]
 ['(k/apply k/min [1 2 3 2])
  (l/! [:code]
    (k/apply k/min [1 2 3 2]))
  (l/! [:live]
    (k/apply k/min [1 2 3 2]))])

;; ### ceil

(display-output-format
 ['[(k/ceil -1.1) (k/ceil 1.1)]
  (l/! [:code]
    [(k/ceil -1.1) (k/ceil 1.1)])
  (l/! [:live]
    [(k/ceil -1.1) (k/ceil 1.1)])]
 ['(k/arr-map [0 -2.1 0 -4.1 5] k/ceil)
  (l/! [:code]
    (k/arr-map [0 -2.1 0 -4.1 5] k/ceil))
  (l/! [:live]
    (k/arr-map [0 -2.1 0 -4.1 5] k/ceil))])

;; ### floor

(display-output-format
 ['[(k/floor -1.1) (k/floor 1.1)]
  (l/! [:code]
    [(k/floor -1.1) (k/floor 1.1)])
  (l/! [:live]
    [(k/floor -1.1) (k/floor 1.1)])]
 ['(k/arr-map [0 -2.1 0 -4.1 5] k/floor)
  (l/! [:code]
    (k/arr-map [0 -2.1 0 -4.1 5] k/floor))
  (l/! [:live]
    (k/arr-map [0 -2.1 0 -4.1 5] k/floor))])

;; ### sqrt

(display-output-format
 ['[(k/sqrt -1) (k/sqrt 1)]
  (l/! [:code]
    [(k/sqrt -1) (k/sqrt 1)])
  (l/! [:live]
    [(k/sqrt -1) (k/sqrt 1)])]
 ['(k/arr-map [0 -2 0 -4 5] k/sqrt)
  (l/! [:code]
    (k/arr-map [0 -2 0 -4 5] k/sqrt))
  (l/! [:live]
    (k/arr-map [0 -2 0 -4 5] k/sqrt))])

;; ### sin

(display-output-format
 ['[(k/sin (/ 3.14159 4))
    (k/sin (/ 3.14159 6))]
  (l/! [:code]
    [(k/sin (/ 3.14159 4))
     (k/sin (/ 3.14159 6))])
  (l/! [:live]
    [(k/sin (/ 3.14159 4))
     (k/sin (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/sin)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/sin))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/sin))])

;; ### cos

(display-output-format
 ['[(k/cos (/ 3.14159 4))
    (k/cos (/ 3.14159 6))]
  (l/! [:code]
    [(k/cos (/ 3.14159 4))
     (k/cos (/ 3.14159 6))])
  (l/! [:live]
    [(k/cos (/ 3.14159 4))
     (k/cos (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/cos)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/cos))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/cos))])

;; ### tan

(display-output-format
 ['[(k/tan (/ 3.14159 4))
    (k/tan (/ 3.14159 6))]
  (l/! [:code]
    [(k/tan (/ 3.14159 4))
     (k/tan (/ 3.14159 6))])
  (l/! [:live]
    [(k/tan (/ 3.14159 4))
     (k/tan (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/tan)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/tan))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/tan))])

;; ### asin

(display-output-format
 ['[(k/asin 0.5)
    (k/asin 0.8)]
  (l/! [:code]
    [(k/asin 0.5)
     (k/asin 0.8)])
  (l/! [:live]
    [(k/asin 0.5)
     (k/asin 0.8)])]
 ['(k/arr-map [1 2 3 4 5] k/asin)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/asin))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/asin))])

;; ### acos

(display-output-format
 ['[(k/acos 0.5)
    (k/acos 0.8)]
  (l/! [:code]
    [(k/acos 0.5)
     (k/acos 0.8)])
  (l/! [:live]
    [(k/acos 0.5)
     (k/acos 0.8)])]
 ['(k/arr-map [1 2 3 4 5] k/acos)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/acos))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/acos))])


;; ### atan

(display-output-format
 ['[(k/atan 0.5)
    (k/atan 0.8)]
  (l/! [:code]
    [(k/atan 0.5)
     (k/atan 0.8)])
  (l/! [:live]
    [(k/atan 0.5)
     (k/atan 0.8)])]
 ['(k/arr-map [1 2 3 4 5] k/atan)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/atan))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/atan))])


;; ### sinh

(display-output-format
 ['[(k/sinh (/ 3.14159 4))
    (k/sinh (/ 3.14159 6))]
  (l/! [:code]
    [(k/sinh (/ 3.14159 4))
     (k/sinh (/ 3.14159 6))])
  (l/! [:live]
    [(k/sinh (/ 3.14159 4))
     (k/sinh (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/sinh)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/sinh))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/sinh))])

;; ### cosh

(display-output-format
 ['[(k/cosh (/ 3.14159 4))
    (k/cosh (/ 3.14159 6))]
  (l/! [:code]
    [(k/cosh (/ 3.14159 4))
     (k/cosh (/ 3.14159 6))])
  (l/! [:live]
    [(k/cosh (/ 3.14159 4))
     (k/cosh (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/cosh)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/cosh))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/cosh))])


;; ### tanh

(display-output-format
 ['[(k/tanh (/ 3.14159 4))
    (k/tanh (/ 3.14159 6))]
  (l/! [:code]
    [(k/tanh (/ 3.14159 4))
     (k/tanh (/ 3.14159 6))])
  (l/! [:live]
    [(k/tanh (/ 3.14159 4))
     (k/tanh (/ 3.14159 6))])]
 ['(k/arr-map [1 2 3 4 5] k/tanh)
  (l/! [:code]
    (k/arr-map [1 2 3 4 5] k/tanh))
  (l/! [:live]
    (k/arr-map [1 2 3 4 5] k/tanh))])



;; ### gt-string

(display-output-format
 ['[(k/gt-string "a" "b")
    (k/gt-string "A" "a")]
  (l/! [:code]
    [(k/gt-string "a" "b")
     (k/gt-string "A" "a")])
  (l/! [:live]
    [(k/gt-string "a" "b")
     (k/gt-string "A" "a")])])

;; ### lt-string

(display-output-format
 ['[(k/lt-string "a" "b")
    (k/lt-string "A" "a")]
  (l/! [:code]
    [(k/lt-string "a" "b")
     (k/lt-string "A" "a")])
  (l/! [:live]
    [(k/lt-string "a" "b")
     (k/lt-string "A" "a")])])

(comment
  ['(k/arr-foldl [1 2 3 4 5 6] k/sub 0)
   (l/! [:code]
     (k/arr-foldl [1 2 3 4 5 6] k/sub 0))
   (l/! [:live]
     (k/arr-foldl [1 2 3 4 5 6] k/sub 0))]
  ['(k/arr-foldl [1 2 3 4 5 6] k/mul 1)
   (l/! [:code]
     (k/arr-foldl [1 2 3 4 5 6] k/mul 1))
   (l/! [:live]
     (k/arr-foldl [1 2 3 4 5 6] k/mul 1))]
  ['(k/arr-foldl [1 2 3 4 5 6] k/div 1)
   (l/! [:code]
     (k/arr-foldl [1 2 3 4 5 6] k/div 1))
   (l/! [:live]
     (k/arr-foldl [1 2 3 4 5 6] k/div 1))])




^:kindly/hide-code
(comment

  
;; Basic arithmetic:

  (display-output-table
    (+ 1 1)
    (+ 0.1 0.2)
    (- 8 1)
    (* 10 2)
    (/ 35 5)
    (/ 5 2))
 
  
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
