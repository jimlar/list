(ns list.views.welcome
  (:require [list.views.common :as common])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/" []
         (common/layout
           [:ul.sortable
            [:li.ui-state-default "Hej"]
            [:li.ui-state-default "Hopp"]]))
