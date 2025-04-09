(ns
 stdlang-book.walkthrough-generated-test
 (:require
  [std.lang :as l]
  [scicloj.kindly.v4.kind :as kind]
  [scicloj.kindly.v4.api :as kindly]
  [clojure.test :refer [deftest is]]))


(def var2_line12 (l/script :js))


(def var4_line16 (!.js (+ 1 2)))


(deftest test5_line18 (is (= var4_line16 "1 + 2;")))


(def
 var7_line22
 (kind/hiccup
  [:div
   [:script
    (!.js
     (do
      (var m (L.map document.currentScript.parentElement))
      (m.setView [-37.84 144.95] 11)
      (-> (L.tileLayer.provider "OpenStreetMap.Mapnik") (. (addTo m)))
      (->
       [-37.9 144.8]
       L.marker
       (. (addTo m))
       (.
        (bindPopup "<i style='color:purple'>Have you been here?</i>"))
       (. (openPopup)))))]]
  {:html/deps [:leaflet], :style {:height "400px"}}))
