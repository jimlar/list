(ns list.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(defpartial layout [& content]
            (html5
              [:head
               [:title "list"]
               (include-css "/css/reset.css")
               (include-css "/css/list.css")
               (include-css "/css/smoothness/jquery-ui-1.8.16.custom.css")
               (include-js "/js/jquery-1.7.min.js")
               (include-js "/js/jquery-ui-1.8.16.custom.min.js")
               (include-js "/js/list.js")
               ]
              [:body content]))
