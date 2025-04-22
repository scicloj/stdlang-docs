;; # Walkthrough 👣

;; Let us briefly explore the std.lang transpiler. 

;; ## Setup

^:kindly/hide-code
(ns stdlang-book.walkthrough
  (:require [std.lang :as l]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]))

;;
;; First lets see what the script
;;


^:kind/hidden
(l/script :js
  {:runtime :basic})



(l/script+ [:code :js])



(l/script+ [:code :js])

;; ## Basic examples

(!.js
  (+ 1 2))

(l/! [:code]
  (+ 1 2 3))

(kindly/check = "1 + 2;")

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

