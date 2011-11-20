(ns list.views.welcome
  (:require [list.views.common :as common]
            [list.models.lists :as lists])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/" []
         (common/layout
           [:ul.sortable
            (for [item (lists/all)]
              [:li.ui-state-default {:id item}(str "Hej " item)])]))
 