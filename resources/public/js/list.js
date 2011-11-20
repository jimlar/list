
$(function() {
    $( ".sortable" ).sortable({
        placeholder: "ui-state-highlight",
        update: function(event, ui) {
            console.log("Sorting changed" + $(this).sortable("toArray"));
        }
    });
    $( ".sortable" ).disableSelection();
});
