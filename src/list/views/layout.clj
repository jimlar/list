(ns list.views.layout
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpartial layout [& content]
            (html5
              [:head
               [:title "list"]
               (include-css "/css/list.css")
               (include-css "/css/ui-lightness/jquery-ui-1.8.16.custom.css")
               (include-js "/js/jquery-1.7.min.js")
               (include-js "/js/jquery-ui-1.8.16.custom.min.js")
               (include-js "/js/list.js")
               ]
              [:body content]))
