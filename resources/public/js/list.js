
var LIST = window.LIST || {};

LIST.ajax = (function(){
  function signalSuccess() {
    $("ol").animate({opacity: 0.1}, 100, function() {
        $("ol").animate({opacity: 1}, 100);
    });
  }

  return {
    init: function() {
    },

    saveSortOrder: function(order) {
        $.post("items", {"order": "" + order}, function(data, textstatus) {
            signalSuccess();
        });
    }
  };

}());

LIST.ajax.init();


$(function() {
    $( ".sortable" ).sortable({
        placeholder: "ui-state-highlight",
        update: function(event, ui) {
            var order = $(this).sortable("toArray");
            console.log("Sorting changed" + order);

            LIST.ajax.saveSortOrder(order);

        }
    });
    $( ".sortable" ).disableSelection();

    $("input[type='text']").first().focus();
});
