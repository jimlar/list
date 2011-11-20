(ns list.views.welcome
  (:require [list.views.common :as common])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/" []
         (common/layout
           [:ul.sortable
            (for [i (range 10)]
              [:li.ui-state-default {:id i}(str "Hej " i)])]))
 